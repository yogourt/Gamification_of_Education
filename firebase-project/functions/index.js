
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.voteForComment = require('./functions/task/vote').voteForComment
exports.voteForMessage = require('./functions/chat/vote').voteForMessage