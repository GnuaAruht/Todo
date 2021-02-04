package com.thuraaung.todo.util

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.thuraaung.todo.vm.MainViewModel
import com.thuraaung.todo.data.TaskRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
        private val application: Application,
        private val tasksRepository: TaskRepository,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null) : AbstractSavedStateViewModelFactory(owner,defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return MainViewModel(application,tasksRepository,handle) as T
    }
}