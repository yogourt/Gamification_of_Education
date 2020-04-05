package com.blogspot.android_czy_java.apps.mgr.main.classroom_api

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CourseDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.CourseModel
import javax.inject.Inject


class FetchCoursesDataOperation @Inject constructor(private val courseDao: CourseDao) {

    fun execute() {
        Thread(Runnable {

            val service = ClassroomService.getInstance()
            val response = service.Courses().list().execute()
            val courses = response.courses

            if(courses != null) {
                for (course in courses) {
                    courseDao.insertCourse(CourseModel(course.id, course.name))
                }
            }

        }).start()
    }

}