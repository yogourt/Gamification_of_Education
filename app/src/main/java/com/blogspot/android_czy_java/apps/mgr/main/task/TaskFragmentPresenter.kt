package com.blogspot.android_czy_java.apps.mgr.main.task

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskCommentWithAuthorModel
import com.blogspot.android_czy_java.apps.mgr.main.task.usecase.ObserveFirestoreComments
import com.blogspot.android_czy_java.apps.mgr.main.task.usecase.SendComment
import com.blogspot.android_czy_java.apps.mgr.main.task.usecase.Vote
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class TaskFragmentPresenter @Inject constructor(
    private val coursesDao: CoursesDao,
    private val sendComment: SendComment,
    private val vote: Vote,
    private val observeFirestoreComments: ObserveFirestoreComments
) {

    var taskId = ""

    fun init(taskId: String) {
        this.taskId = taskId
        observeFirestoreComments.execute(taskId)
    }
    fun getTaskLiveData() =
        coursesDao.getCourseTaskLiveData(taskId)


    fun tryToSendComment(message: String, link: String?): Single<Boolean> {
        return sendComment.execute(message, if(link.isNullOrBlank()) null else link, taskId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun tryToVote(commentId: String, upvote: Boolean) = vote.execute(commentId, upvote)

    fun sortCommentsByPoints(comments: List<TaskCommentWithAuthorModel>) {
        Collections.sort(comments) { o1, o2 -> (o2.taskComment.points - o1.taskComment.points).toInt() }
    }

}
