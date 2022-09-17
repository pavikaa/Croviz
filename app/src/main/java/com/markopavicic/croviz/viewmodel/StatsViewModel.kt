package com.markopavicic.croviz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.markopavicic.croviz.model.repository.QuizRepository

class StatsViewModel(private val quizRepository: QuizRepository) : ViewModel() {
    private var _userScore: MutableLiveData<Long> = MutableLiveData()
    val userScore: LiveData<Long>
        get() = _userScore

    private var _globalScore: MutableLiveData<Long> = MutableLiveData()
    val globalScore: LiveData<Long>
        get() = _globalScore

}

class StatsViewModelFactory(private val quizRepository: QuizRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(quizRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}