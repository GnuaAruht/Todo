package com.thuraaung.todo.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thuraaung.mvi.AacMviViewModel
import com.thuraaung.todo.util.LoadStatus
import com.thuraaung.todo.util.MainEffect
import com.thuraaung.todo.util.MainEvent
import com.thuraaung.todo.util.MainState
import com.thuraaung.todo.data.DataResult
import com.thuraaung.todo.data.Task
import com.thuraaung.todo.data.TasksFilterType
import com.thuraaung.todo.data.TaskRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TASK_FILTER_SAVED_KEY = "TASKS_FILTER_KEY"

class MainViewModel(app : Application,
                    private val repository: TaskRepository,
                    private val savedStateHandle: SavedStateHandle) : AacMviViewModel<MainState, MainEffect, MainEvent>(app) {

    private var _currentFilterType : TasksFilterType
        get() = savedStateHandle.get(TASK_FILTER_SAVED_KEY) ?: TasksFilterType.ALL
        set(value) = savedStateHandle.set(TASK_FILTER_SAVED_KEY,value)

    private var _tasksTitle = MutableLiveData(_currentFilterType.title)
    val tasksTitle : LiveData<String> = _tasksTitle

    init {
        viewState = MainState(status = LoadStatus.NotLoad,tasks = emptyList())
    }

    override fun process(viewEvent: MainEvent) {
        super.process(viewEvent)
        when(viewEvent) {
            MainEvent.LoadTasks -> loadTasks()
            MainEvent.RefreshTasks -> refreshTasks()
            is MainEvent.AddNewTask -> addNewTask(viewEvent.taskName)
            is MainEvent.FilterTask -> updateFilterType(viewEvent.filterType)
            is MainEvent.CompletedTask -> completeTask(viewEvent.taskId,viewEvent.isCompleted)
        }
    }

    private fun completeTask(taskId : String,isComplete : Boolean) {
        viewModelScope.launch {
            repository.updateCompleted(taskId,isComplete)
            viewState = viewState.copy(status = LoadStatus.NotLoad)
        }
    }

    private fun updateFilterType(tasksFilterType: TasksFilterType) {
        if (_currentFilterType != tasksFilterType) {
            _currentFilterType = tasksFilterType
            _tasksTitle.value = tasksFilterType.title
            viewState = viewState.copy(status = LoadStatus.NotLoad)
        }
    }

    private fun filterTask(tasks : List<Task>) : List<Task> {

        val tasksToShow = mutableListOf<Task>()
        for(task in tasks) {
            when(_currentFilterType) {
                TasksFilterType.ALL -> tasksToShow.add(task)
                TasksFilterType.ACTIVE -> if(task.isActive) tasksToShow.add(task)
                TasksFilterType.COMPLETE -> if (task.isCompleted == 1) tasksToShow.add(task)
            }
        }

        if (tasksToShow.isEmpty()) {
            viewEffect = MainEffect.ShowToast("No task found")
        }

        return tasksToShow
    }

    private fun addNewTask(name : String) {

        viewModelScope.launch {
            val task = Task(name = name)
            repository.addNewTask(task)
            viewState = viewState.copy(status = LoadStatus.NotLoad)
        }
    }

    private fun refreshTasks() {
        viewState = viewState.copy(status = LoadStatus.NotLoad)
    }

    private fun loadTasks() {
        viewState = viewState.copy(status = LoadStatus.Loading)
        viewModelScope.launch {

            delay(300)
            val taskList = when(val dataResult = repository.getAllTasks()) {
                is DataResult.Success -> dataResult.data
                is DataResult.Failed -> {
                    viewEffect = MainEffect.ShowToast(message = "Load task list failed")
                    emptyList()
                }
            }

            viewState = viewState.copy(status = LoadStatus.Loaded,tasks = filterTask(taskList))
        }
    }
}