package com.blogspot.android_czy_java.apps.mgr.main.courses

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import javax.inject.Inject

class CoursesFragmentPresenter @Inject constructor(private val coursesDao: CoursesDao) {

    fun getCoursesLiveData() = coursesDao.getCoursesLiveData()

}