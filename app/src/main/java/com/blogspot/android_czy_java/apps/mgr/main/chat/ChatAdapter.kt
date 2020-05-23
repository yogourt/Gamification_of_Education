package com.blogspot.android_czy_java.apps.mgr.main.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageWithAuthorModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.item_chat_message.view.*
import kotlinx.android.synthetic.main.item_chat_message.view.points

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
            val authorData = messages[position].author
            message.text = messageData.message
            points.text = messageData.points.toString()

            if (!(position > 0 && messages[position - 1].message.authorId == authorData.id)) {
                nickname.text = authorData.nickname
                photo.let { photoIV ->
                    val resource = getPhotoRes(authorData.photo, photoIV.context)
                    Glide.with(photoIV).load(resource).into(photoIV)
                }
            }
            thumb.setOnClickListener { callback.upvoteMessage(messageData) }
        }
    }

    private fun getPhotoRes(photo: String?, context: Context) =
        context.resources.getIdentifier(photo ?: "ic_profile", "drawable", context.packageName)

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val message: TextView = itemView.message
        val points: TextView = itemView.points
        val thumb: ImageView = itemView.thumb
        val nickname: TextView = itemView.nickname
        val photo: ImageView = itemView.author_icon

    }

    interface ChatAdapterCallback {
        fun upvoteMessage(message: MessageModel)
    }
}
