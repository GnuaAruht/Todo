package com.thuraaung.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task")
data class Task(
        @PrimaryKey
        val id : String = UUID.randomUUID().toString(),
        val name : String,
        val data : Date = Date(),
        val isCompleted : Int = 0) {

        val isActive : Boolean
                get() = isCompleted != 1
}