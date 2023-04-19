from collections import OrderedDict
import json
import os
import re
from flask import Flask, Response, request, jsonify, render_template
import requests
import jwt
import time
from jwt import PyJWKClient

# Environment variables to be used for keycloak authentication
CLIENT_ID = "oauth"
CLIENT_SECRET = "Wi5qctJ5jdHMpilcacB5lZtrPifypjYb"
refresh_token = ""
token_expiration_time = time.time()

# URLs to be used inside docker
DOCKER_PUBLIC_KEY_URL = 'http://keycloak:8080/auth/realms/Construc-sw-2023-1/protocol/openid-connect/certs'
DOCKER_SERVER_URL = 'https://keycloak:8080/auth'
DOCKER_TOKEN_URL = 'http://keycloak:8080/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token'
DOCKER_USERS_URL = 'http://keycloak:8080/auth/admin/realms/Construc-sw-2023-1/users/'

# URLs to be used in localhost testing
SERVER_URL = 'https://localhost:8090/auth'
TOKEN_URL = 'http://localhost:8090/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token'
USERS_URL = 'http://localhost:8090/auth/admin/realms/Construc-sw-2023-1/users/'


# Creating the flask app
app = Flask(__name__)

def refresh_token(refresh_token):
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    data = {
        'grant_type': 'refresh_token',
        'client_id': CLIENT_ID,
        'client_secret': CLIENT_SECRET,
        'refresh_token': refresh_token
    }
    response = requests.post(DOCKER_TOKEN_URL, headers=headers, data=data)

    if response.status_code == 200:
        refresh_token = response.json()['refresh_token']
        access_token = response.json()['access_token']
        decoded_token = jwt.decode(access_token, verify=False, algorithms=["RS256"])
        token_expiration_time = decoded_token["exp"]
        return response.json()['access_token']
    else:
        return jsonify({'error_code': 'OA-500', 'error_description' : 'Internal Server Error: Something went wrong while trying to refresh your token, request a new one'}), 500

# This function is used to get the user info to be returned as a full user
def get_full_user(user_id):

    # Requesting the user info
    token = request.headers.get('Authorization')

    current_time = time.time()
    if current_time > token_expiration_time:
        token = refresh_token(refresh_token)

    request_url = DOCKER_USERS_URL + user_id
    headers = {'Authorization': token}
    response = requests.get(request_url, headers=headers)

    # Exception handling
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-400','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return jsonify(response.json()), 200

# This function is used to get the user info to be returned as a resumed user
def get_resumed_user(user_id):

    # Requesting the user info
    token = request.headers.get('Authorization')

    current_time = time.time()
    if current_time > token_expiration_time:
        token = refresh_token(refresh_token)

    request_url = DOCKER_USERS_URL + user_id
    headers = {'Authorization': token}
    response = requests.get(request_url, headers=headers)

    # Getting the user fields to be returned as a resumed user
    user_id = response.json()['id']
    username = response.json()['username']
    first_name = response.json()['firstName']
    last_name = response.json()['lastName']
    email = response.json()['email']
    enabled = response.json()['enabled']

    # Formating the user as an ordered dict to return the info in a easy to read format 
    user = OrderedDict(
        id=user_id,
        username=username,
        first_name=first_name,
        last_name=last_name,
        email=email,
        enabled=enabled
    )

    # Exception handling
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-400','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        refresh_token = request.headers.get('Refresh-Token')

        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return Response(json.dumps(user), status=200, mimetype='application/json')

# home endpoint to render the index.html file and make it easy to tell if the server is running properly
@app.route('/')
def home():
    return render_template('index.html')

# hello endpoint to test if the server is running properly, will be removed in the future
@app.route('/hello', methods=['GET'])
def hello():
    return "Hello World!"

# login endpoint to get the token from the keycloak server
@app.route('/login', methods=['POST'])
def generate_token():
    url = DOCKER_TOKEN_URL

    # Formating the payload to be sent on the correct format and sending the request
    payload = request.form.to_dict()
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    response = requests.post(url, headers=headers, data=payload)

    # Exception handling
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request Structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid username or password'}), 401
    
    jkws_client = PyJWKClient(DOCKER_PUBLIC_KEY_URL)
    signing_key = jkws_client.get_signing_key_from_jwt(response.json()['access_token'])
    decoded_jwt = jwt.decode(response.json()['access_token'], signing_key.key, algorithms=["RS256"])
    token_expiration_time = decoded_jwt["exp"]
    refresh_token = response.json()['refresh_token']

    return jsonify(response.json()), 200

# users endpoint to create a new user
@app.route('/users', methods=['POST'])
def create_user():
    new_user = {}
    
    # Getting the token from the request header
    token = request.headers.get('Authorization')
    request_url = DOCKER_USERS_URL

    current_time = time.time()
    if current_time > token_expiration_time:
        token = refresh_token(refresh_token)

    headers = {'Content-Type': 'application/json', 'Authorization': token}

    # Formating the payload and retrieving the email to be used in the exception handling
    payload = request.get_json()
    email = payload.get('email')

    # Exception handling
    if not email or not re.match(r"[^@]+@[^@]+\.[^@]+", email):
       return jsonify({'error_code': 'OA-400','error_description': 'Bad Request: Missing email or badly formated email'}), 400
    response = requests.post(request_url, headers=headers, json=payload)
    if response.status_code == 409:
        return jsonify({'error_code':'OA-409','error_description' : 'Conflict: This username or email already exists'}), 409
    
    # Returning the new user info as a json object
    if response.status_code == 201:
        user_info = response.headers
        new_user = {
            'location': user_info['location'].split('/')[-1]
        }
    return get_resumed_user(new_user['location']), 201

# users endpoint to get the full user info
@app.route('/users', methods=['GET'])
def get_users():
    token = request.headers.get('Authorization')
    url = DOCKER_USERS_URL

    current_time = time.time()
    if current_time > token_expiration_time:
        token = refresh_token(refresh_token)

    headers = {'Authorization': token}
    response = requests.get(url, headers=headers)
    return jsonify(response.json()), 200

# users endpoint to get the resumed user info, this enpoint uses the get_resumed_user function documented above
@app.route('/users/<user_id>', methods=['GET'])
def use_get_user(user_id):
    return get_resumed_user(user_id)

# users endpoint to update the user info
@app.route('/users/<user_id>', methods=['PUT'])
def put_user(user_id):

    # Request
    token = request.headers.get('Authorization')
    url = DOCKER_USERS_URL + user_id

    current_time = time.time()
    if current_time > token_expiration_time:
        token = refresh_token(refresh_token)

    headers = {'Content-Type': 'application/json', 'Authorization': token}
    
    # Formating the payload sent to our api to be sent to the keycloak server on the correct format
    payload = request.get_json()
    response = requests.put(url, headers=headers, json=payload)

    # Exception handling
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return get_resumed_user(user_id), 200

# users endpoint to update the user password
@app.route('/users/<user_id>', methods=['PATCH'])
def patch_user(user_id):

    # Request
    token = request.headers.get('Authorization')
    url = DOCKER_USERS_URL + user_id

    current_time = time.time()
    if current_time > token_expiration_time:
        token = refresh_token(refresh_token)

    # Formating the payload sent to our api to be sent to the keycloak server on the correct format
    headers = {'Content-Type': 'application/json','Authorization': token}
    payload = request.get_json()
    response = requests.patch(url, headers=headers, json=payload)
    
    # Exception handling
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return get_resumed_user(user_id), 200

# users endpoint to delete the user
@app.route('/users/<user_id>', methods=['DELETE'])
def delete_user(user_id):

    # Request
    token = request.headers.get('Authorization')
    url = DOCKER_USERS_URL + user_id

    current_time = time.time()
    if current_time > token_expiration_time:
        token = refresh_token(refresh_token)

    headers = {'Authorization': token}
    response = requests.delete(url, headers=headers)

    # Exception handling
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return jsonify({'success': True}), 200


# main function configured to run the app on the port 8085, debug mode on
# and on the host needed to be able to access the app from outside the container using docker
if __name__ == '__main__':
    port = int(os.environ.get('PORT', 8085))
    app.run(host='0.0.0.0', port=8085, debug=True)
