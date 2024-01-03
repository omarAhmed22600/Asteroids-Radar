package com.udacity.asteroidradar.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {

    private val _explanationDialogStatus = MutableLiveData<Boolean>()
    val explanationDialogStatus: LiveData<Boolean>
        get() = _explanationDialogStatus

    fun onExplanationButtonClicked() {
        _explanationDialogStatus.value = true
    }

    fun onDisplayExplanationDialogDone() {
        _explanationDialogStatus.value = false
    }
}