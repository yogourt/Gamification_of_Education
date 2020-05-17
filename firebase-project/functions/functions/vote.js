const admin = require('firebase-admin')
const firestore = require('firebase-admin').firestore

admin.initializeApp()

  exports.vote = function vote(collectionPath, documentPath, upvote) {
    let operation = getOperation(upvote)
    admin.firestore().collection(collectionPath).doc(documentPath).update({ points : operation })
  }

  function getOperation(upvote) {
    if(upvote) {
        return firestore.FieldValue.increment(1)
    } else {
        return firestore.FieldValue.increment(-1)
    }
  }