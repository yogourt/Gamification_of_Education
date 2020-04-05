package com.blogspot.android_czy_java.apps.mgr.main.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskModel
import kotlinx.android.synthetic.main.item_course_task.view.*

class TasksAdaper(private val taskList: List<TaskModel>) : RecyclerView.Adapter<TasksAdaper.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_course_task, parent, false))
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.apply {
            val task = taskList[position]
            title.text = task.title
            description.text = task.description
        }
    }


    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.task_title
        val description: TextView = itemView.task_desc

    }

}
