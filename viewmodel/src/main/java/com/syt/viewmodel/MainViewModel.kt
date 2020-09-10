package com.syt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _number = MutableLiveData<Int>().also {
        if (!savedStateHandle.contains("number")) {
            savedStateHandle.set("number", 0)
        }
        it.value = savedStateHandle.get("number")
    }

    val number: LiveData<Int> = _number

    fun addOne() {
        _number.value = _number.value?.plus(1)
        savedStateHandle.set("number",_number.value)
    }
}