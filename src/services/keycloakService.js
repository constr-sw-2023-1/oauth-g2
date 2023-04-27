const fetch = require('node-fetch');

async function authenticate(username, password) {
  const response = await fetch(`${process.env.KEYCLOAK_BASE_URL}/protocol/openid-connect/token`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams({
      grant_type: 'password',
      client_id: process.env.KEYCLOAK_CLIENT_ID,
      client_secret: process.env.KEYCLOAK_CLIENT_SECRET,
      username: username,
      password: password
    })
  });

  if (!response.ok) {
    throw new Error('Failed to authenticate with Keycloak');
  }

  const json = await response.json();
  return json.access_token;
}

module.exports = {
  authenticate
};