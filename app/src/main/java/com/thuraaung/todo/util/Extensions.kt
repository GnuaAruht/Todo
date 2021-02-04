package com.thuraaung.todo.util

import android.graphics.Paint
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thuraaung.todo.TaskApplication

fun AppCompatActivity.getViewModelFactory() : ViewModelFactory {
    val repository = (applicationContext as TaskApplication).taskRepository
    return ViewModelFactory(application,repository,this)
}


fun TextView.showStrikeThrough(show: Boolean) {
    paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}