const functions = require('firebase-functions');
const COLLECTION_TASK_COMMENTS = require('../../const/firestore-ref').COLLECTION_TASK_COMMENTS
const vote = require('../vote').vote

exports.voteForComment = functions.https.onCall((data, context) => {
    let upvote = data.upvote
    let commentId = data.commentId

    vote(COLLECTION_TASK_COMMENTS, commentId, upvote)
  });