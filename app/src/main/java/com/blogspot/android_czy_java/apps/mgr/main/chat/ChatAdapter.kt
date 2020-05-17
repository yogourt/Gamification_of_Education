package com.blogspot.android_czy_java.apps.mgr.main.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageWithAuthorModel
import kotlinx.android.synthetic.main.item_chat_message.view.*

class ChatAdapter(
    private val messages: List<MessageWithAuthorModel>,
    private val callback: ChatAdapterCallback
) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat_message,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.apply {
            val messageData = messages[position].message
            message.text = messageData.message
            points.text = messageData.points.toString()
            thumb.setOnClickListener { callback.upvoteMessage(messageData.firebaseId) }

        }
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val message: TextView = itemView.message
        val points: TextView = itemView.points
        val thumb: ImageView = itemView.thumb

    }

    interface ChatAdapterCallback {
        fun upvoteMessage(messageId: String)
    }
}
