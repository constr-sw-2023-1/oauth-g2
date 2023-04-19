from collections import OrderedDict
import json
import os
import re
from flask import Flask, Response, request, jsonify, render_template
import requests

D_SERVER_URL = 'https://keycloak:8080/auth'
D_TOKEN_URL = 'http://keycloak:8080/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token'
D_USERS_URL = "http://keycloak:8080/auth/admin/realms/Construc-sw-2023-1/users/"
SERVER_URL = 'https://localhost:8090/auth'
TOKEN_URL = 'http://localhost:8090/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token'
USERS_URL = "http://localhost:8090/auth/admin/realms/Construc-sw-2023-1/users/"

app = Flask(__name__)

def get_full_user(user_id):
    token = request.headers.get('Authorization')
    url = D_USERS_URL + user_id
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

def get_resumed_user(user_id):
    token = request.headers.get('Authorization')
    url = D_USERS_URL + user_id
    headers = {'Authorization': token}
    response = requests.get(url, headers=headers)
    id = response.json()['id']
    username = response.json()['username']
    first_name = response.json()['firstName']
    last_name = response.json()['lastName']
    email = response.json()['email']
    enabled = response.json()['enabled']
    user = OrderedDict(
        id=id,
        username=username,
        first_name=first_name,
        last_name=last_name,
        email=email,
        enabled=enabled
    )
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-400','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return Response(json.dumps(user), status=200, mimetype='application/json')

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/hello', methods=['GET'])
def hello():
    return "Hello World!"

@app.route('/login', methods=['POST'])
def generate_token():
    url = D_TOKEN_URL
    payload = request.form.to_dict()
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    response = requests.post(url, headers=headers, data=payload)
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request Structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid username or password'}), 401
    return jsonify(response.json()), 200

@app.route('/users', methods=['POST'])
def create_user():
    new_user = {}
    token = request.headers.get('Authorization')
    url = D_USERS_URL
    headers = {'Content-Type': 'application/json', 'Authorization': token}
    payload = request.get_json()
    email = payload.get('email')
    if not email or not re.match(r"[^@]+@[^@]+\.[^@]+", email):
       return jsonify({'error_code': 'OA-400','error_description': 'Bad Request: Missing email or badly formated email'}), 400
    response = requests.post(url, headers=headers, json=payload)
    if response.status_code == 409:
        return jsonify({'error_code':'OA-409','error_description' : 'Conflict: This username or email already exists'}), 409
    if response.status_code == 201:
        user_info = response.headers
        new_user = {
            'location': user_info['location'].split('/')[-1]
        }
    return get_resumed_user(new_user['location']), 201

@app.route('/users', methods=['GET'])
def get_users():
    token = request.headers.get('Authorization')
    url = D_USERS_URL
    headers = {'Authorization': token}
    response = requests.get(url, headers=headers)
    return jsonify(response.json()), 200

@app.route('/users/<user_id>', methods=['GET'])
def use_get_user(user_id):
    return get_resumed_user(user_id)

@app.route('/users/<user_id>', methods=['PUT'])
def put_user(user_id):
    token = request.headers.get('Authorization')
    url = D_USERS_URL + user_id
    headers = {'Content-Type': 'application/json', 'Authorization': token}
    payload = request.get_json()
    response = requests.put(url, headers=headers, json=payload)
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return get_resumed_user(user_id), 200

@app.route('/users/<user_id>', methods=['PATCH'])
def patch_user(user_id):
    token = request.headers.get('Authorization')
    url = D_USERS_URL + user_id
    headers = {'Content-Type': 'application/json','Authorization': token}
    payload = request.get_json()
    response = requests.patch(url, headers=headers, json=payload)
    
    if response.status_code == 400:
        return jsonify({'error_code': 'OA-404','error_description': 'Bad Request: Request structure error'}), 400
    if response.status_code == 401:
        return jsonify({'error_code': 'OA-401','error_description': 'Unauthorized: Invalid Token or username/password'}), 401
    if response.status_code == 403:
        return jsonify({'error_code': 'OA-403','error_description': 'Forbidden: Missing the necessary roles or privilages'}), 403
    if response.status_code == 404:
        return jsonify({'error_code': 'OA-404','error_description': 'Not Found: User not Found'}), 404
    return get_resumed_user(user_id), 200

@app.route('/users/<user_id>', methods=['DELETE'])
def delete_user(user_id):
    token = request.headers.get('Authorization')
    url = D_USERS_URL + user_id
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
