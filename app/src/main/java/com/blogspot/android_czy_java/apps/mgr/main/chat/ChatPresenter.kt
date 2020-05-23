package com.blogspot.android_czy_java.apps.mgr.main.chat

import androidx.lifecycle.LiveData
import com.blogspot.android_czy_java.apps.mgr.main.chat.usecase.MessageWithCourseId
import com.blogspot.android_czy_java.apps.mgr.main.chat.usecase.ObserveFirestoreMessages
import com.blogspot.android_czy_java.apps.mgr.main.chat.usecase.SendChatMessage
import com.blogspot.android_czy_java.apps.mgr.main.chat.usecase.Vote
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageWithAuthorModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ChatPresenter @Inject constructor(
    private val coursesDao: CoursesDao,
    private val sendMessage: SendChatMessage,
    private val observeFirestoreMessages: ObserveFirestoreMessages,
    private val vote: Vote
) {

    lateinit var messagesLiveData: LiveData<List<MessageWithAuthorModel>>
    private lateinit var courseId: String

    fun init(courseId: String) {
        this.courseId = courseId
        messagesLiveData = coursesDao.getCourseChatLiveData(courseId)
        observeFirestoreMessages.execute(courseId)
    }

    fun tryToSendMessage(message: String?) {
        if (!message.isNullOrBlank()) {
            sendMessage.execute(MessageWithCourseId(message, courseId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    addPointForChatMessage()
                }
        }
    }

    private fun addPointForChatMessage() {
        Thread {
            val points = coursesDao.getPointsValue(courseId).plus(1)
            coursesDao.setPoints(courseId, points)
        }.start()
    }

    fun upvoteMessage(messageId: String) = vote.execute(messageId)


}