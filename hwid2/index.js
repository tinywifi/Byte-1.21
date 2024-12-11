const fs = require('fs');
const path = require('path');
const express = require('express');
const bodyParser = require('body-parser');
const crypto = require('crypto');
const { v4: uuidv4 } = require('uuid');

const DATABASE_FILE = path.join(__dirname, 'users.json');

let database = {};

const loadDatabase = () => {
    try {
        if (fs.existsSync(DATABASE_FILE)) {
            const data = fs.readFileSync(DATABASE_FILE, 'utf8');
            database = JSON.parse(data);
        } else {
            console.error('Database file not found! Using an empty database.');
            database = {};
        }
    } catch (error) {
        console.error('Failed to load the database:', error);
        throw new Error('Failed to load the database.');
    }
};

const hashString = (input) => {
    return crypto.createHash('sha256').update(input, 'utf8').digest('hex');
};

const activeChallenges = new Map();

const generateChallenge = (username) => {
    const challenge = uuidv4();
    activeChallenges.set(username, challenge);
    return challenge;
};

const getChallenge = (username) => {
    return activeChallenges.get(username);
};

const removeChallenge = (username) => {
    activeChallenges.delete(username);
};

loadDatabase();

const app = express();
app.use(bodyParser.json());

app.post('/initiate', (req, res) => {
    const { username, hwid } = req.body;

    if (!username || !hwid) {
        console.log("INVALID REQUEST");
        return res.send('INVALID_REQUEST');
    }

    const userRecord = Object.values(database).find(
        (record) => record.username === username
    );

    if (!userRecord || userRecord.hwid !== hwid) {
        console.log(`User: ${username} has an invalid HWID!`);
        return res.send('INVALID_HWID');
    }

    const challenge = generateChallenge(username);
    res.send(challenge);
    console.log(`User: ${username} send challenge!`);
});

app.post('/validate', (req, res) => {
    const { username, response } = req.body;

    if (!username || !response) {
        return res.send('INVALID_REQUEST');
    }

    const challenge = getChallenge(username);

    if (!challenge) {
        console.log(`User: ${username} Challenge not active!`);
        return res.send('NO_ACTIVE_CHALLENGE');
    }

    const userRecord = Object.values(database).find(
        (record) => record.username === username
    );

    if (!userRecord) {
        console.log(`User: ${username} is not found!`);
        return res.send('USER_NOT_FOUND');
    }

    const expectedResponse = hashString(challenge + userRecord.hwid);

    if (expectedResponse !== response) {
        console.log(`User: ${username} failed to authenticate!`);
        return res.send('AUTHENTICATION_FAILED');
    }

    removeChallenge(username);
    console.log(`User: ${username} authenticated!`);
    res.send('AUTHENTICATION_SUCCESS');
});


const PORT = 42078;
app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server is running on port ${PORT}`);
});
