package com.markopavicic.croviz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.markopavicic.croviz.model.data.Answer
import com.markopavicic.croviz.model.data.Question
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.repository.QuizRepository
import java.text.SimpleDateFormat
import java.util.*

class QuizViewModel(private val quizRepository: QuizRepository) : ViewModel() {
    private var _answers: MutableLiveData<MutableList<Answer>> =
        MutableLiveData(mutableListOf())

    val answers: LiveData<MutableList<Answer>>
        get() = _answers

    private var _questions: MutableLiveData<MutableList<Question>> =
        MutableLiveData(mutableListOf())

    val questions: LiveData<MutableList<Question>>
        get() = _questions

    fun submitQuiz(quizName: String, quizCategory: String, hideCreator: Boolean): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        var creator = "Anonymous"
        if (!hideCreator)
            creator = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val key = quizRepository.getKey()
        val quiz = Quiz(
            key,
            quizName,
            quizCategory,
            creator,
            currentDate,
            questions.value?.toList()!!
        )
        quizRepository.saveQuiz(quiz)
        return key
    }

    fun addAnswer(answer: Answer) {
        _answers.value?.add(answer)
    }

    fun clearAnswers() {
        _answers.value?.clear()
    }

    fun addQuestion(
        question: String,
        answers: List<Answer>
    ) {
        _questions.value?.add(Question(quizRepository.getKey(), question, answers))
    }

    fun checkAnswers(): Boolean {
        var oneCorrect = false
        var oneIncorrect = false
        if (answers.value?.isEmpty()!!)
            return false
        for (answer in answers.value!!) {
            if (answer.isCorrect)
                oneCorrect = true
            if (!answer.isCorrect)
                oneIncorrect = true
        }
        if (!oneCorrect || !oneIncorrect)
            return false

        return true

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