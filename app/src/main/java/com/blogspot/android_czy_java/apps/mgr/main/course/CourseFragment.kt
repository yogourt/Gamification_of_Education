package com.blogspot.android_czy_java.apps.mgr.main.course

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_course.view.*
import javax.inject.Inject

class CourseFragment : Fragment() {

    @Inject
    lateinit var presenter: CourseFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_course, container, false)

        presenter.getCourseTasks().observe(this, Observer { prepareLayout(view.list_tasks, it) })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        arguments?.getString(KEY_COURSE_ID)?.let {
            presenter.courseId = it
        }
    }

    private fun prepareLayout(tasksRV: RecyclerView, taskList: List<TaskModel>) {

        tasksRV.apply {
            adapter = TasksAdaper(taskList)
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        }
    }


    companion object {

        private const val KEY_COURSE_ID = "course_id"

        fun getInstance(id: String): CourseFragment {
            val fragment = CourseFragment()
            val arguments = Bundle()
            arguments.putString(KEY_COURSE_ID, id)
            fragment.arguments = arguments
            return fragment
        }
    }

}