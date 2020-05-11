package com.blogspot.android_czy_java.apps.mgr.main.task

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskWithCommentsModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.item_course_task_expanded.*
import kotlinx.android.synthetic.main.item_course_task_expanded.view.*
import javax.inject.Inject

class TaskFragment : Fragment() {

    @Inject
    lateinit var presenter: TaskFragmentPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_course_task_expanded, container, false)

        val taskId = arguments?.getString(KEY_TASK_ID)
        if (taskId != null) {
            presenter.getTaskLiveData(taskId).observe(this, Observer { prepareLayout(it, view) })
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun prepareLayout(taskWithComments: TaskWithCommentsModel, view: View) {
        val task = taskWithComments.task
        task_title.text = task.title
        view.task_desc.text = task.description
        submitted.isChecked = task.completed
    }

    companion object {

        const val KEY_TASK_ID = "task_id"

        fun getInstance(id: String): TaskFragment {
            val fragment = TaskFragment()
            val arguments = Bundle()
            arguments.putString(KEY_TASK_ID, id)
            fragment.arguments = arguments
            return fragment
        }
    }

}