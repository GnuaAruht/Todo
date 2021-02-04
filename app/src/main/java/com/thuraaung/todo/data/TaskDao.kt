package com.thuraaung.todo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Query("select * from task order by data desc")
    suspend fun getAllTasks() : List<Task>

    @Query("update task set isCompleted = :isCompleted where id = :taskId")
    suspend fun completeTask(taskId : Int,isCompleted : Boolean)

    @Query("delete from task where isCompleted = 1")
    suspend fun clearCompletedTask()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task : Task)

    @Query("update task set isCompleted = :isCompleted where id = :taskId ")
    suspend fun updateComplete(taskId : String,isCompleted: Boolean)

}