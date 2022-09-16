package com.markopavicic.croviz.model.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.utils.Constants

class QuizRepository {
    private val database = Firebase.database

    private val usersReference = database.getReference(Constants.USERS_REF)
    private val quizReference = database.getReference(Constants.QUIZ_REF)

    fun saveQuiz(quiz: Quiz) {
        quizReference
            .child(quizReference.push().key!!)
            .setValue(quiz)
    }

    fun getKey(): String {
        return quizReference.push().key!!
    }
}