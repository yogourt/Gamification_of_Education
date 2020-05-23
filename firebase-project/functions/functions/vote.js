const admin = require('firebase-admin')
const firestore = require('firebase-admin').firestore
const COLLECTION_USERS = require('../const/firestore-ref').COLLECTION_USERS

admin.initializeApp()

exports.vote = function vote(collectionPath, documentPath, authorId, upvote) {
  let operation = getOperation(upvote)
  const documentToVote = admin.firestore().collection(collectionPath).doc(documentPath)
  documentToVote.update({ points: operation })
  if (upvote) {
    addPointsForAuthor(authorId)
  }
}

function addPointsForAuthor(authorId) {
    admin.firestore().collection(COLLECTION_USERS).doc(authorId).update({ points: increment })
  }

const increment = firestore.FieldValue.increment(1)
const decrement = firestore.FieldValue.increment(-1)

function getOperation(upvote) {
  if (upvote) {
    return increment
  } else {
    return decrement
  }
}