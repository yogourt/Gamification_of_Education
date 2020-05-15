package com.blogspot.android_czy_java.apps.mgr.main.task

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.task.usecase.SendComment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TaskFragmentPresenter @Inject constructor(
    private val coursesDao: CoursesDao,
    private val sendComment: SendComment
) {

    var taskId = ""
    fun getTaskLiveData() =
        coursesDao.getCourseTaskLiveData(taskId)


    fun tryToSendComment(message: String, link: String?): Single<Boolean> {
        return sendComment.execute(message, if(link.isNullOrBlank()) null else link, taskId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
