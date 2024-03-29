const functions = require('firebase-functions');
const COLLECTION_MESSAGES = require('../../const/firestore-ref').COLLECTION_MESSAGES
const vote = require('../vote').vote

exports.voteForMessage = functions.https.onCall((data, context) => {
    let upvote = data.upvote
    let messageId = data.messageId
    let authorId = data.authorId

    vote(COLLECTION_MESSAGES, messageId, authorId, upvote)
    });