import os
import re
from flask import Flask, request, jsonify, render_template
import requests
import json

REALM_NAME = 'Construc-sw-2023-1'
SERVER_URL = 'https://keycloak:8080/auth'
CLIENT_ID = 'oauth'
CLIENT_SECRET = 'fsQ4jucS5s7bz4VohrDw7SBRRevlHVbG'
TOKEN_URL = 'http://keycloak:8080/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token'
USERS_URL = "http://keycloak:8080/auth/admin/realms/Construc-sw-2023-1/users"

app = Flask(__name__)

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/hello', methods=['GET'])
def hello():
    return "Hello World!"

@app.route('/login', methods=['POST'])
def generate_token():
    url = TOKEN_URL
    payload = request.form.to_dict()
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    response = requests.post(url, headers=headers, data=payload)
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request Structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid username or password'}), 401
    return jsonify(response.json()), 200


#this has some problems, the response doesnt works propperly
@app.route('/users', methods=['POST'])
def create_user():
    token = request.headers.get('Authorization')
    url = USERS_URL
    headers = {'Content-Type': 'application/json', 'Authorization': token}
    email = request.get_json().get('email')
    # python não tankou o regex
    #
    #     if not re.match(r"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:
    # [\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\
    # x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*
    # [a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?
    # [0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\
    # [\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    # , email):
    if not email or not re.match(r"[^@]+@[^@]+\.[^@]+", email):
       return jsonify({'error_code': 'OA-400','error_description': 'Bad Request: Missing email or badly formated email'}), 400
    payload = request.get_json()
    response = requests.post(url, headers=headers, json=payload)
    #response_json = response.json()
    # userId = response.headers['Location'].split('/')[-1]
    #print(response_json)
    if response.status_code == 409:
        return jsonify({'error_code':'OA-409','error_description' : 'Conflict: This username or email already exists'}), 409
    return jsonify({'success': True, 'user': response.headers}), 201

@app.route('/users', methods=['GET'])
def get_users():
    token = request.headers.get('Authorization')
    url = USERS_URL
    headers = {'Authorization': token}
    response = requests.get(url, headers=headers)
    return jsonify(response.json()), 200

# for this to work propperly the POST method must be working
@app.route('/users/<user_id>', methods=['GET'])
def get_user(user_id):
    token = request.headers.get('Authorization')
    url = USERS_URL + user_id
    headers = {'Authorization': token}
    response = requests.get(url, headers=headers)
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-400','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return jsonify(response.json()), 200

# for this to work propperly the POST method must be working and a postman test request still needs to be created
@app.route('/users/<user_id>', methods=['PUT'])
def put_user(user_id):
    token = request.headers.get('Authorization')
    url = USERS_URL + user_id
    headers = {'Authorization': token}
    payload = request.get_json()
    response = requests.get(url, headers=headers, json=payload)
    
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return jsonify({'success': True}), 200
    
# for this to work propperly the POST method must be working and a postman test request still needs to be created
@app.route('/users/<user_id>', methods=['PATCH'])
def patch_user(user_id):
    token = request.headers.get('Authorization')
    url = USERS_URL + user_id
    headers = {'Authorization': token}
    payload = request.get_json()
    response = requests.get(url, headers=headers, json=payload)
    
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return jsonify({'success': True}), 200

# for this to work propperly the POST method must be working and a postman test request still needs to be created
@app.route('/users/<user_id>', methods=['DELETE'])
def delete_user(user_id):
    token = request.headers.get('Authorization')
    url = USERS_URL + user_id
    headers = {'Authorization': token}
    response = requests.delete(url, headers=headers)
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return jsonify({'success': True}), 200


if __name__ == '__main__':
    port = int(os.environ.get('PORT', 8085))
    app.run(host='0.0.0.0', port=8085, debug=True)



