package com.thuraaung.todo.di

import android.content.Context
import androidx.room.Room
import com.thuraaung.todo.TaskDb
import com.thuraaung.todo.data.TaskRepository

object Injector {

    private fun provideTaskDatabase(context: Context) : TaskDb {
        return Room.databaseBuilder(context.applicationContext,
            TaskDb::class.java,"task.db")
            .build()
    }

    fun provideTaskRepository(context: Context) : TaskRepository {
        return TaskRepository(provideTaskDatabase(context))
    }

}