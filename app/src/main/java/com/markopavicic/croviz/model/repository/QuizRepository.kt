package com.markopavicic.croviz.model.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.utils.Constants

class QuizRepository {
    private val database = Firebase.database
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val usersReference = database.getReference(Constants.USERS_REF)
    private val quizReference = database.getReference(Constants.QUIZ_REF)

    fun saveQuiz(quiz: Quiz, key: String) {
        quizReference
            .child(key)
            .setValue(quiz)

        usersReference
            .child(userId!!)
            .child(Constants.USER_QUIZ_PATH)
            .child(quiz.quizId)
            .setValue("")
    }

    fun getQuiz(quizId: String, liveData: MutableLiveData<Quiz>) {
        val quizListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                liveData.postValue(dataSnapshot.child(quizId).getValue(Quiz::class.java))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Quiz failed, log a message
            }
        }
        quizReference.addListenerForSingleValueEvent(quizListener)
    }

    fun getKey(): String {
        return quizReference.push().key!!
    }
}