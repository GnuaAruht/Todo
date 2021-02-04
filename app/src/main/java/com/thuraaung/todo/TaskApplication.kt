package com.thuraaung.todo

import android.app.Application
import com.thuraaung.todo.data.TaskRepository
import com.thuraaung.todo.di.Injector

class TaskApplication : Application() {

    val taskRepository : TaskRepository
        get() = Injector.provideTaskRepository(this)

}