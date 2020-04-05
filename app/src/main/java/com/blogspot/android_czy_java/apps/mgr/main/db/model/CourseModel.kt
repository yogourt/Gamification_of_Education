package com.blogspot.android_czy_java.apps.mgr.main.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course")
class CourseModel(
    @PrimaryKey
    val id: String,
    val title: String
)