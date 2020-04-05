package com.blogspot.android_czy_java.apps.mgr.main.courses

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
import com.blogspot.android_czy_java.apps.mgr.main.course.CourseFragment
import com.blogspot.android_czy_java.apps.mgr.main.db.model.CourseModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_courses.view.*
import javax.inject.Inject

class CoursesFragment : Fragment(), CoursesAdapter.CoursesAdapterCallback {

    @Inject
    lateinit var presenter: CoursesFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_courses, container, false)

        presenter.getCoursesLiveData().observe(this, Observer { prepareCourseList(view.list_courses, it) })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }


    private fun prepareCourseList(coursesRV: RecyclerView, coursesList: List<CourseModel>) {

        coursesRV.apply {
            adapter = CoursesAdapter(this@CoursesFragment, coursesList)
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        }

    }

    override fun openCourse(id: String) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            CourseFragment.getInstance(id)
        )?.addToBackStack(null)?.commit()
    }

}