package com.blogspot.android_czy_java.apps.mgr.main.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageWithAuthorModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.fragment_chat.view.chat
import javax.inject.Inject

class ChatFragment : Fragment(), ChatAdapter.ChatAdapterCallback {

    @Inject
    lateinit var presenter: ChatPresenter

    lateinit var layout: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        arguments?.getString(KEY_COURSE_ID)?.let {
            presenter.init(it)
            presenter.messagesLiveData.observe(this, Observer { prepareLayout(view.chat, it) })
        }
        layout = view.chat
        view.button_send.setOnClickListener { tryToSentMessage(view.new_message.text?.toString()) }
        return view
    }

    private fun tryToSentMessage(message: String?) {
        if (message != null) presenter.tryToSendMessage(message)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun prepareLayout(chat: RecyclerView, messages: List<MessageWithAuthorModel>) {
        chat.apply {
            adapter = ChatAdapter(messages, this@ChatFragment)
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        }
    }

    override fun upvoteMessage(message: MessageModel) {
        if(!presenter.upvoteMessage(message)) {
            Snackbar.make(chat, chat.context.getString(R.string.msg_points_exceeded), Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {

        private const val KEY_COURSE_ID = "course_id"

        fun getInstance(courseId: String): ChatFragment {
            val fragment = ChatFragment()
            val arguments = Bundle()
            arguments.putString(KEY_COURSE_ID, courseId)
            fragment.arguments = arguments
            return fragment
        }
    }

}