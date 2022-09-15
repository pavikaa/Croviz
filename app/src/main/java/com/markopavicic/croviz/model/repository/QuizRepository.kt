package com.markopavicic.croviz.model.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.markopavicic.croviz.model.data.Quiz

class QuizRepository {
    private val database = Firebase.database

    private val usersReference = database.getReference("users")
    private val quizReference = database.getReference("quiz")

    fun saveQuiz(quiz: Quiz) {
        quizReference
            .child(quizReference.push().key!!)
            .setValue(quiz)
    }
}