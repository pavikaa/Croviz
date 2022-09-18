package com.markopavicic.croviz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.data.Result
import com.markopavicic.croviz.model.repository.QuizRepository

class QuizViewModel(private val quizRepository: QuizRepository) : ViewModel() {

    private val _quiz: MutableLiveData<Quiz> = MutableLiveData()
    val quiz: LiveData<Quiz>
        get() = _quiz

    private val _allQuizzes: MutableLiveData<MutableList<Quiz>> = MutableLiveData()
    val allQuizzes: LiveData<MutableList<Quiz>>
        get() = _allQuizzes

    private var _results = mutableListOf<Result>()

    var points = 0

    fun getQuizById(quizId: String) {
        quizRepository.getQuiz(quizId, _quiz)
    }

    fun getAllQuizzes() {
        quizRepository.getAllQuizes(_allQuizzes)
    }

    fun finishQuestion(results: Result, questionId: String) {
        points += results.numCorrect * 10 - results.numIncorrect * 5
        quizRepository.nextQuestion(results, questionId)
    }

    fun completeQuiz() {
        quizRepository.completeQuiz(quiz.value?.quizId)
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