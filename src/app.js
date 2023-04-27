const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const { authenticate } = require('./services/keycloakService');

const app = express();

app.use(cors({ origin: process.env.CORS_ORIGIN }));

app.use(bodyParser.json());

app.post('/login', async (req, res, next) => {
    const username = req.body.username;
    const password = req.body.password;

    try {
        const accessToken = await authenticate(username, password);
        res.send({ accessToken });
    }
    catch (error) {
        console.error(error);
        res.status(401).send('Failed to authenticate');
    }
});

app.get('/', (req, res) => {
    res.send('Hello, World!');
})

module.exports = app;