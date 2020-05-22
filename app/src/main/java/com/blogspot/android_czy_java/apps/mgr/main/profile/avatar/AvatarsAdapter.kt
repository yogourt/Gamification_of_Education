package com.blogspot.android_czy_java.apps.mgr.main.profile.avatar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.android_czy_java.apps.mgr.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_avatar.view.*

class AvatarsAdapter(private val callback: AvatarAdapterCallback) :
    RecyclerView.Adapter<AvatarsAdapter.AvatarViewHolder>() {

    companion object {
        const val NUM_OF_AVATARS = 6
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        return AvatarViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_avatar, parent, false)
        )
    }

    override fun getItemCount() = NUM_OF_AVATARS

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.apply {
            val resource = itemView.resources.getIdentifier(
                "avatar${position}",
                "drawable",
                itemView.context.packageName
            )
            avatar.apply {
                Glide.with(this).load(resource).into(this)
                this.setOnClickListener {
                    callback.onAvatarClick("avatar${position}")
                }
            }
        }
    }

    inner class AvatarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.avatar
    }

    interface AvatarAdapterCallback {
        fun onAvatarClick(id: String)
    }
}