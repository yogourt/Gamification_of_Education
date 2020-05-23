package com.blogspot.android_czy_java.apps.mgr.main.task

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskCommentWithAuthorModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskWithCommentsModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.item_course_task_expanded.*
import kotlinx.android.synthetic.main.item_task_comment.view.*
import kotlinx.android.synthetic.main.item_task_comment_new.*
import java.util.*
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
            presenter.init(taskId)
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

            view.prepareComments(comments)
        }
    }

    private fun View.prepareComments(comments: List<TaskCommentWithAuthorModel>) {
        presenter.sortCommentsByPoints(comments)
        this.let {
            task_comments.removeAllViews()
            for (comment in comments) {
                task_comments.addView(prepareComment(comment))
            }
        }
    }

    private fun prepareComment(comment: TaskCommentWithAuthorModel): View {
        val commentLayout =
            layoutInflater.inflate(R.layout.item_task_comment, task_comments, false)
        var msg = comment.taskComment.message
        if (comment.taskComment.link != null) {
            msg = "<a href=\"${comment.taskComment.link}\">$msg</a>"
        }
        commentLayout.apply {
            message.text = Html.fromHtml(msg)
            message.movementMethod = LinkMovementMethod.getInstance()
            points.text = comment.taskComment.points.toString()
            upvote_button.setOnClickListener {
                tryToVote(comment.taskComment.id, true)
            }
            downvote_button.setOnClickListener {
                tryToVote(comment.taskComment.id, false)
            }
        }
        return commentLayout
    }

    private fun View.tryToVote(commentId: String, upvote: Boolean) {
        if(!presenter.tryToVote(commentId, upvote)) {
            showErrorSnackbar(this, context.getString(R.string.msg_points_exceeded))
        }
    }

    private fun tryToSendComment(view: View) {
        view.let {
            val comment = new_comment.text.toString()
            val link = new_comment_link.text.toString()

            if (comment.isBlank()) {
                return showErrorSnackbar(it, getString(R.string.comment_empty))
            }
            presenter.tryToSendComment(comment, link).subscribe({}, { error ->
                if (error != null) {
                    showErrorSnackbar(it, error.message ?: "")
                }
            })
        }
    }

    private fun showErrorSnackbar(view: View, message: String = "") =
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