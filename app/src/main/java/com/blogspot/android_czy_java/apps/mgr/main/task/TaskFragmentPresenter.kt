package com.blogspot.android_czy_java.apps.mgr.main.task

import androidx.lifecycle.MutableLiveData
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskWithCommentsModel
import javax.inject.Inject

class TaskFragmentPresenter @Inject constructor(private val coursesDao: CoursesDao) {

    fun getTaskLiveData(taskId: String) =
        coursesDao.getCourseTaskLiveData(taskId)

}
