package com.blogspot.android_czy_java.apps.mgr.main.classroom_api

import android.content.SharedPreferences
import com.blogspot.android_czy_java.apps.mgr.main.PreferencesKeys
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.CourseModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskModel
import com.google.api.services.classroom.Classroom
import com.google.api.services.classroom.model.Course
import com.google.api.services.classroom.model.CourseWork
import javax.inject.Inject


class FetchCoursesDataUseCase @Inject constructor(private val coursesDao: CoursesDao,
                                                  private val prefs: SharedPreferences) {

    fun execute() {
        Thread(Runnable {

            val accessToken = prefs.getString(PreferencesKeys.KEY_ACCESS_TOKEN, null)

            if(accessToken != null) {
                val service = ClassroomService.getInstance(accessToken)
                val response = service.Courses().list().execute()
                val courses = response.courses

                saveFetchTime()
                courses?.insertCourses(service)
            }

        }).start()
    }

    private fun saveFetchTime() {
        prefs.edit().putLong(PreferencesKeys.KEY_LAST_CLASSROOM_API_FETCH, System.currentTimeMillis()).apply()
    }

    private fun List<Course>.insertCourses(service: Classroom) {
        for (course in this) {
            Thread(Runnable {
                coursesDao.insertCourse(CourseModel(course.id, course.name, 0))
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
                        submissions.any { !(it["state"]?.equals("CREATED") ?: true) }
                    )
                )
        }
        coursesDao.insertTasks(tasks)
    }

}