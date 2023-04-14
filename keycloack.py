from flask import Flask, request
import requests
import json

REALM_NAME = 'Construc-sw-2023-1'
SERVER_URL = 'https://localhost:8090/auth'
CLIENT_ID = 'oauth'
CLIENT_SECRET = 'fsQ4jucS5s7bz4VohrDw7SBRRevlHVbG'
TOKEN_URL = 'http://localhost:8090/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token'

app = Flask(__name__)

@app.route('/hello', methods=['GET'])
def hello():
    return "Hello World!"

@app.route('/login', methods=['POST'])
def generate_token():
    url = "http://localhost:8090/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token"
    payload = request.form.to_dict()
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    response = requests.post(url, headers=headers, data=payload)
    json_data = json.loads(response.text)
    return(json_data['access_token'])

@app.route('/users', methods=['POST'])
def create_user():
    token = request.headers.get('Authorization')
    url = "http://localhost:8090/auth/admin/realms/Construc-sw-2023-1/users"
    headers = {'Content-Type': 'application/json', 'Authorization': token}
    payload = request.get_json()
    print(payload)
    response = requests.post(url, headers=headers, json=payload)
    print(response.text)
    return response.text



app.run(port=8082, debug=True)



