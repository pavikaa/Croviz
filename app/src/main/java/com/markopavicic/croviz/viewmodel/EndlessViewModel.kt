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

    private val _quiz: MutableLiveData<Quiz> = MutableLiveData()
    val quiz: LiveData<Quiz>
        get() = _quiz

    private val _allQuizzes: MutableLiveData<MutableList<Quiz>> = MutableLiveData()
    val allQuizzes: LiveData<MutableList<Quiz>>
        get() = _allQuizzes

    fun getAllQuizzes() {
        quizRepository.getAllQuizes(_allQuizzes)
    }

    fun getRandomQuestion(): Question {
        val randomQuizPosition = allQuizzes.value?.let { Random.nextInt(0, it.size) }
        val randomQuiz = randomQuizPosition?.let { allQuizzes.value?.get(it) }
        val randomQuestionPosition = randomQuiz?.questions?.let { Random.nextInt(0, it.size) }
        return randomQuestionPosition?.let { randomQuiz?.questions?.get(it) }!!
    }

    fun finishQuestion(results: Result) {
        val points = results.numCorrect * 10 - results.numIncorrect * 5
        quizRepository.endless(points)

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