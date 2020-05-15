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
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.item_course_task_expanded.*
import kotlinx.android.synthetic.main.item_course_task_expanded.view.*
import kotlinx.android.synthetic.main.item_task_comment.view.*
import kotlinx.android.synthetic.main.item_task_comment_new.*
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
            presenter.taskId = taskId
            presenter.getTaskLiveData().observe(this, Observer { prepareLayout(it, view) })
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun prepareLayout(taskWithComments: TaskWithCommentsModel, view: View) {
        val task = taskWithComments.task
        val comments = taskWithComments.comments
        view.let {
            task_title.text = task.title
            task_desc.text = task.description
            submitted.isChecked = task.completed
            button_send.setOnClickListener { tryToSendComment(it) }

            task_comments.removeAllViews()
            for(comment in comments) {
                val commentLayout = layoutInflater.inflate(R.layout.item_task_comment, task_comments, false)
                commentLayout.message.text = comment.taskComment.message
                commentLayout.points.text = comment.taskComment.points.toString()
                task_comments.addView(commentLayout)
            }
        }
    }

    private fun tryToSendComment(view: View) {
        view.let {
            val comment = new_comment.text.toString()
            val link = new_comment_link.text.toString()

            if (comment.isBlank()) {
                return showSnackbarSendImpossible(it, getString(R.string.comment_empty))
            }
            presenter.tryToSendComment(comment, link).subscribe ( {},  { error ->
                if (error != null) {
                    showSnackbarSendImpossible(it, error.message ?: "")
                }
            })
        }
    }

    private fun showSnackbarSendImpossible(view: View, message: String) =
        Snackbar.make(view, getString(R.string.sth_wrong, message), Snackbar.LENGTH_LONG).show()

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