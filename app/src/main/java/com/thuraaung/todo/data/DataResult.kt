package com.thuraaung.todo.data

sealed class DataResult<out T> {

    data class Success<out R>(val data : R) : DataResult<R>()
    data class Failed(val message : String) : DataResult<Nothing>()

}