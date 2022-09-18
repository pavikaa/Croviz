package com.markopavicic.croviz.model.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.data.Result
import com.markopavicic.croviz.utils.Constants

class QuizRepository {
    private val database = Firebase.database
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val usersReference = database.getReference(Constants.USERS_REF)
    private val quizReference = database.getReference(Constants.QUIZ_REF)
    private val pointsReference = database.getReference(Constants.POINTS_REF)


    private lateinit var userQuestions: MutableList<String>
    private lateinit var userQuizzes: MutableList<String>

    fun saveQuiz(quiz: Quiz, key: String) {
        quizReference
            .child(key)
            .setValue(quiz)

        usersReference
            .child(userId!!)
            .child(Constants.USER_CREATED_QUIZ_PATH)
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

    fun getAllQuizes(liveData: MutableLiveData<MutableList<Quiz>>) {
        val quizList = mutableListOf<Quiz>()
        val quizEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val quiz = dataSnapshot.getValue(Quiz::class.java)
                quizList.add(quiz!!)
                liveData.postValue(quizList)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val quiz = dataSnapshot.getValue(Quiz::class.java)
                quizList.add(quiz!!)
                liveData.postValue(quizList)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val quiz = dataSnapshot.getValue(Quiz::class.java)
                quizList.remove(quiz!!)
                liveData.postValue(quizList)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val quiz = dataSnapshot.getValue(Quiz::class.java)
                quizList.add(quiz!!)
                liveData.postValue(quizList)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        quizReference.addChildEventListener(quizEventListener)
    }

    fun getKey(): String {
        return quizReference.push().key!!
    }

    fun completeQuiz(quizId: String?) {
        if (userId != null) {
            usersReference
                .child(userId)
                .child(Constants.USER_QUIZ_PATH)
                .child(quizId!!)
                .setValue("")
        }
    }

    fun nextQuestion(results: Result, questionId: String?) {
        val points = results.numCorrect * 10 - results.numIncorrect * 5
        if (userId != null) {
            addPointsToUser(results, points)
            addQuestionIdToUser(questionId)
        }
        pointsReference
            .child(Constants.POINTS_KEY)
            .setValue(ServerValue.increment(points.toLong()))
    }

    private fun addPointsToUser(results: Result, points: Int) {
        if (userId != null) {
            usersReference
                .child(userId)
                .child(Constants.POINTS_KEY)
                .setValue(ServerValue.increment(points.toLong()))

            usersReference
                .child(userId)
                .child(Constants.CORRECT_ANSWERS_PATH)
                .setValue(ServerValue.increment(results.numCorrect.toLong()))

            usersReference
                .child(userId)
                .child(Constants.INCORRECT_ANSWERS_PATH)
                .setValue(ServerValue.increment(results.numIncorrect.toLong()))
        }
    }

    private fun addQuestionIdToUser(questionId: String?) {
        usersReference
            .child(userId!!)
            .child(Constants.USER_QUESTIONS_PATH)
            .child(questionId!!)
            .setValue("")
    }
}