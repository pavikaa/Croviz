package com.markopavicic.croviz.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.data.Result
import com.markopavicic.croviz.model.repository.QuizRepository

class QuizViewModel(private val quizRepository: QuizRepository) : ViewModel() {

    private val _quiz: MutableLiveData<Quiz> = MutableLiveData()

    private var _points = 0

    val points: Int
        get() = _points

    private var _results = mutableListOf<Result>()

    val quiz: LiveData<Quiz>
        get() = _quiz

    fun getQuizById(quizId: String) {
        quizRepository.getQuiz(quizId, _quiz)
    }

    fun postAnswers(result: Result) {
        _results.add(result)
    }

    fun completeQuiz() {
        Log.d("results", _results.toString())
        for (result in _results) {
            _points += result.numCorrect * 10
            _points -= result.numIncorrect * 5
        }
        quizRepository.completeQuiz(quiz.value?.quizId, _points)
    }
}

class QuizViewModelFactory(private val quizRepository: QuizRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(quizRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}