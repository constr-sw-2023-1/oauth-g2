import os
import re
from flask import Flask, request, jsonify, render_template, redirect
from flasgger import Swagger
from flask_swagger_ui import get_swaggerui_blueprint
import requests

D_SERVER_URL = 'https://keycloak:8080/auth'
D_TOKEN_URL = 'http://keycloak:8080/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token'
D_USERS_URL = "http://keycloak:8080/auth/admin/realms/Construc-sw-2023-1/users/"
SERVER_URL = 'https://localhost:8090/auth'
TOKEN_URL = 'http://localhost:8090/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token'
USERS_URL = "http://localhost:8090/auth/admin/realms/Construc-sw-2023-1/users/"

app = Flask(__name__)
swagger = Swagger(app)

SWAGGER_URL = '/api/docs'  # URL for exposing Swagger UI (without trailing '/')
API_URL = 'http://petstore.swagger.io/v2/swagger.json'  # Our API url (can of course be a local resource)

# Call factory function to create our blueprint
swaggerui_blueprint = get_swaggerui_blueprint(
    SWAGGER_URL,  # Swagger UI static files will be mapped to '{SWAGGER_URL}/dist/'
    API_URL,
    config={  # Swagger UI config overrides
        'app_name': "Test application"
    },
    # oauth_config={  # OAuth config. See https://github.com/swagger-api/swagger-ui#oauth2-configuration .
    #    'clientId': "your-client-id",
    #    'clientSecret': "your-client-secret-if-required",
    #    'realm': "your-realms",
    #    'appName': "your-app-name",
    #    'scopeSeparator': " ",
    #    'additionalQueryStringParams': {'test': "hello"}
    # }
)

app.register_blueprint(swaggerui_blueprint)


@app.route('/docs')
def docs():
    return swagger.ui()

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
        return jsonify({'error_code': 'OA-400','error_description': 'Bad Request: Request Structure error'}), 400
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
    print(email)
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
    response = requests.post(url, headers=headers, json=payload)
    if response.status_code == 409:
        return jsonify({'error_code':'OA-409','error_description' : 'Conflict: This username or email already exists'}), 409
    if response.status_code == 201:
        user_info = response.headers
        new_user = {
            'location': user_info['location'].split('/')[-1]
        }
    print(new_user)
    return redirect(D_USERS_URL + new_user['location'], code=302)

@app.route('/users', methods=['GET'])
def get_users():
    token = request.headers.get('Authorization')
    url = D_USERS_URL
    headers = {'Authorization': token}
    response = requests.get(url, headers=headers)
    return jsonify(response.json()), 200

@app.route('/users/<user_id>', methods=['GET'])
def get_user(user_id):
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
    return jsonify({'success': True}), 200
    
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
    return jsonify({'success': True}), 200

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
