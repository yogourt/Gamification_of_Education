package com.blogspot.android_czy_java.apps.mgr.main.classroom_api

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.CourseModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskModel
import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.CourseWork
import javax.inject.Inject


class FetchCoursesDataOperation @Inject constructor(private val coursesDao: CoursesDao) {

    fun execute() {
        Thread(Runnable {

            val service = ClassroomService.getInstance()
            val response = service.Courses().list().execute()
            val courses = response.courses

            courses?.insertCourses(service)

        }).start()
    }

    private fun List<Course>.insertCourses(service: Classroom) {
        for (course in this) {
            Thread(Runnable {
                coursesDao.insertCourse(CourseModel(course.id, course.name))
                val response = service.courses().courseWork().list(course.id).execute()
                val courseWorks = response.courseWork

                courseWorks?.insertTasks(service)
            }).start()
        }
    }

    private fun List<CourseWork>.insertTasks(service: Classroom) {
        val tasks = ArrayList<TaskModel>()

        for (work in this) {

            val response =
                service.courses().courseWork().studentSubmissions().list(work.courseId, work.id).execute()
            val submissions = response.studentSubmissions

            if (work.workType.equals("Assignment", true))
                tasks.add(
                    TaskModel(
                        work.id,
                        work.courseId,
                        work.title,
                        work.description,
                        submissions != null
                    )
                )
        }
        coursesDao.insertTasks(tasks)
    }

}