package com.thuraaung.todo.data

import com.thuraaung.todo.TaskDb

class TaskRepository(private val taskDb: TaskDb) {

    suspend fun getAllTasks() : DataResult<List<Task>> {
        return DataResult.Success(taskDb.taskDao().getAllTasks())
    }

    suspend fun addNewTask(task : Task) {
        taskDb.taskDao().insertTask(task)
    }

    suspend fun updateCompleted(taskId : String,isCompleted : Boolean) {
        taskDb.taskDao().updateComplete(taskId,isCompleted)
    }

}