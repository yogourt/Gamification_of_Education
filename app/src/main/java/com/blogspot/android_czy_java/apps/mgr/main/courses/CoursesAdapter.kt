package com.blogspot.android_czy_java.apps.mgr.main.courses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.db.model.CourseModel
import kotlinx.android.synthetic.main.item_course.view.*

class CoursesAdapter(private val callback: CoursesAdapterCallback, private val coursesList: List<CourseModel>) :
    RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false))
    }

    override fun getItemCount() = coursesList.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.title.text = coursesList[position].title
        holder.itemView.setOnClickListener {
            callback.openCourse(coursesList[position].id)
        }
    }


    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.title

    }

    interface CoursesAdapterCallback {
        fun openCourse(id: String)
    }

}
