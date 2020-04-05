package com.blogspot.android_czy_java.apps.mgr.main.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.courses.CoursesFragment
import com.blogspot.android_czy_java.apps.mgr.main.leaderboards.LeaderboardsFragment
import com.blogspot.android_czy_java.apps.mgr.main.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.fragment_navigation.view.*

class BottomNavigationFragment : Fragment() {

    private val indexToIdMap = mapOf(
        Pair(R.id.menu_profile, 0),
        Pair(R.id.menu_courses, 1),
        Pair(R.id.menu_leaderboards, 2)
    )

    private val coursesFragment = CoursesFragment()
    private val profileFragment = ProfileFragment()
    private val leaderboardsFragment = LeaderboardsFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_navigation, container, false)

        prepareBottomNavigation(view.navigation)
        addBackStackChangeListener()

        if (fragmentManager?.findFragmentById(R.id.fragment_container) == null) {
            addMainFragment()
        }
        return view
    }

    private fun prepareBottomNavigation(navigation: BottomNavigationView) {
        navigation.setOnNavigationItemSelectedListener {
            getCurrentFragment(it.itemId)?.let {
                replaceFragment(it)
            }
            true
        }
    }

    private fun addMainFragment() {
        fragmentManager?.beginTransaction()?.add(
            R.id.fragment_container,
            CoursesFragment()
        )?.addToBackStack(null)?.commit()
    }

    private fun getCurrentFragment(itemId: Int): Fragment? {
        return when (itemId) {
            R.id.menu_courses -> coursesFragment
            R.id.menu_leaderboards -> leaderboardsFragment
            R.id.menu_profile -> profileFragment
            else -> null
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            fragment
        )?.addToBackStack(null)?.commit()
    }

    private fun addBackStackChangeListener() {
        fragmentManager?.addOnBackStackChangedListener {
            fragmentManager?.findFragmentById(R.id.fragment_container)?.let { selectItem(it) }
        }
    }

    private fun selectItem(fragment: Fragment) {
        when (fragment) {
            is ProfileFragment -> selectItem(R.id.menu_profile)
            is CoursesFragment -> selectItem(R.id.menu_courses)
            is LeaderboardsFragment -> selectItem(R.id.menu_leaderboards)
        }
    }

    private fun selectItem(itemId: Int) {
        indexToIdMap[id]?.let { navigation.menu.getItem(it).isChecked = true; }
    }

}