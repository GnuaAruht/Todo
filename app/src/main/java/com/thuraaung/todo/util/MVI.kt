package com.thuraaung.todo.util

import com.thuraaung.todo.data.Task
import com.thuraaung.todo.data.TasksFilterType


sealed class MainEvent {
    object LoadTasks : MainEvent()
    data class AddNewTask(val taskName : String) : MainEvent()
    object RefreshTasks : MainEvent()
    data class FilterTask(val filterType : TasksFilterType) : MainEvent()
    data class CompletedTask(val taskId : String, val isCompleted : Boolean) : MainEvent()
}

data class MainState(val status : LoadStatus, val tasks : List<Task>)

sealed class LoadStatus {

    object NotLoad : LoadStatus()
    object Loading : LoadStatus()
    object Loaded : LoadStatus()
}

sealed class MainEffect {
    data class ShowToast(val message : String) : MainEffect()
}