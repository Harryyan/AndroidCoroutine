package com.example.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
            Log.d("ViewModel:", Thread.currentThread().name)
            delay(1000)
            Log.d("ViewModel:", Thread.currentThread().name)
        }
    }
}