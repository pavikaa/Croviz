package com.markopavicic.croviz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.markopavicic.croviz.model.data.Question
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.data.Result
import com.markopavicic.croviz.model.repository.QuizRepository
import kotlin.random.Random

class EndlessViewModel(private val quizRepository: QuizRepository) : ViewModel() {

    private lateinit var currentQuiz: Quiz


    private val _allQuizzes: MutableLiveData<MutableList<Quiz>> = MutableLiveData()
    val allQuizzes: LiveData<MutableList<Quiz>>
        get() = _allQuizzes

    fun getAllQuizzes() {
        quizRepository.getAllQuizes(_allQuizzes)
    }

    fun getRandomQuestion(): Question {
        val randomQuizPosition = allQuizzes.value?.let { Random.nextInt(0, it.size) }
        val randomQuiz = randomQuizPosition?.let { allQuizzes.value?.get(it) }
        currentQuiz = randomQuiz!!
        val randomQuestionPosition = randomQuiz.questions.let { Random.nextInt(0, it.size) }
        return randomQuestionPosition.let { randomQuiz.questions[it] }
    }

    fun finishQuestion(results: Result) {
        quizRepository.nextQuestion(results, currentQuiz.quizId)
    }


    class EndlessViewModelFactory(private val quizRepository: QuizRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EndlessViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EndlessViewModel(quizRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}