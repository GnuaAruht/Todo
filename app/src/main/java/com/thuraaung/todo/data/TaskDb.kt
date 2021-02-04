package com.thuraaung.todo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.thuraaung.todo.data.Task
import com.thuraaung.todo.data.TaskDao
import java.util.*

@Database(entities = [Task::class],version = 1)
@TypeConverters(Converters::class)
abstract class TaskDb : RoomDatabase() {
    abstract fun taskDao() : TaskDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}