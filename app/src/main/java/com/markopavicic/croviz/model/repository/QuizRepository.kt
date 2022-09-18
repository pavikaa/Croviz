package com.markopavicic.croviz.model.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.model.data.Result
import com.markopavicic.croviz.model.data.Stats
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

    fun getUserStats(_stats: MutableLiveData<Stats>) {
        val statsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userScore = 0.toLong()
                if (dataSnapshot.child(userId!!).child(Constants.POINTS_KEY).exists())
                    userScore = dataSnapshot.child(userId).child(Constants.POINTS_KEY)
                        .getValue(Long::class.java)!!

                var userCorrectAnswers = 0.toLong()
                if (dataSnapshot.child(userId).child(Constants.CORRECT_ANSWERS_PATH).exists())
                    userCorrectAnswers =
                        dataSnapshot.child(userId).child(Constants.CORRECT_ANSWERS_PATH)
                            .getValue(Long::class.java)!!

                var userIncorrectAnswers = 0.toLong()
                if (dataSnapshot.child(userId).child(Constants.INCORRECT_ANSWERS_PATH).exists())
                    userIncorrectAnswers =
                        dataSnapshot.child(userId).child(Constants.INCORRECT_ANSWERS_PATH)
                            .getValue(Long::class.java)!!

                var userCompletedQuizzes = 0.toLong()
                if (dataSnapshot.child(userId).child(Constants.COMPLETED_QUIZZES).exists())
                    userCompletedQuizzes =
                        dataSnapshot.child(userId).child(Constants.COMPLETED_QUIZZES)
                            .getValue(Long::class.java)!!
                var userCompletedQuestions = 0.toLong()
                if (dataSnapshot.child(userId).child(Constants.USER_COMPLETED_QUESTIONS).exists())
                    userCompletedQuestions =
                        dataSnapshot.child(userId).child(Constants.USER_COMPLETED_QUESTIONS)
                            .getValue(Long::class.java)!!

                var globalScore = 0.toLong()
                if (dataSnapshot.child(Constants.GLOBAL_POINTS_KEY).exists())
                    globalScore =
                        dataSnapshot.child(Constants.GLOBAL_POINTS_KEY)
                            .getValue(Long::class.java)!!

                var globalCorrectAnswers = 0.toLong()
                if (dataSnapshot.child(Constants.GLOBAL_CORRECT_ANSWERS_PATH).exists())
                    globalCorrectAnswers =
                        dataSnapshot.child(Constants.GLOBAL_CORRECT_ANSWERS_PATH)
                            .getValue(Long::class.java)!!

                var globalIncorrectAnswers = 0.toLong()
                if (dataSnapshot.child(Constants.GLOBAL_INCORRECT_ANSWERS_PATH).exists())
                    globalIncorrectAnswers =
                        dataSnapshot.child(Constants.GLOBAL_INCORRECT_ANSWERS_PATH)
                            .getValue(Long::class.java)!!

                var globalCompletedQuizzes = 0.toLong()
                if (dataSnapshot.child(Constants.GLOBAL_COMPLETED_QUIZZES).exists())
                    globalCompletedQuizzes =
                        dataSnapshot.child(Constants.GLOBAL_COMPLETED_QUIZZES)
                            .getValue(Long::class.java)!!

                var globalCompletedQuestions = 0.toLong()
                if (dataSnapshot.child(Constants.GLOBAL_COMPLETED_QUESTIONS).exists())
                    globalCompletedQuestions =
                        dataSnapshot.child(Constants.GLOBAL_COMPLETED_QUESTIONS)
                            .getValue(Long::class.java)!!

                _stats.postValue(
                    Stats(
                        userScore,
                        userCorrectAnswers,
                        userIncorrectAnswers,
                        userCompletedQuizzes,
                        userCompletedQuestions,
                        globalScore,
                        globalCorrectAnswers,
                        globalIncorrectAnswers,
                        globalCompletedQuizzes,
                        globalCompletedQuestions
                    )
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        usersReference.addValueEventListener(statsListener)
    }


    fun getAllQuizzes(liveData: MutableLiveData<MutableList<Quiz>>) {
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

            usersReference
                .child(userId)
                .child(Constants.COMPLETED_QUIZZES)
                .setValue(ServerValue.increment(1))
        }
        usersReference
            .child(Constants.GLOBAL_COMPLETED_QUIZZES)
            .setValue(ServerValue.increment(1))
    }

    fun nextQuestion(results: Result, questionId: String?) {
        val points = results.numCorrect * 10 - results.numIncorrect * 5
        if (userId != null) {
            addPointsToUser(results, points)
            addQuestionIdToUser(questionId)
        }
        usersReference
            .child(Constants.GLOBAL_POINTS_KEY)
            .setValue(ServerValue.increment(points.toLong()))

        usersReference
            .child(Constants.GLOBAL_CORRECT_ANSWERS_PATH)
            .setValue(ServerValue.increment(results.numCorrect.toLong()))

        usersReference
            .child(Constants.GLOBAL_INCORRECT_ANSWERS_PATH)
            .setValue(ServerValue.increment(results.numIncorrect.toLong()))

        usersReference
            .child(Constants.GLOBAL_COMPLETED_QUESTIONS)
            .setValue(ServerValue.increment(1))
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
        usersReference
            .child(userId)
            .child(Constants.USER_COMPLETED_QUESTIONS)
            .setValue(ServerValue.increment(1))
    }

    fun clearStats() {
        if (userId != null) {
            usersReference
                .child(userId)
                .child(Constants.POINTS_KEY)
                .setValue(0.toLong())
            usersReference
                .child(userId)
                .child(Constants.CORRECT_ANSWERS_PATH)
                .setValue(0.toLong())
            usersReference
                .child(userId)
                .child(Constants.INCORRECT_ANSWERS_PATH)
                .setValue(0.toLong())
            usersReference
                .child(userId)
                .child(Constants.COMPLETED_QUIZZES)
                .setValue(0.toLong())
            usersReference
                .child(userId)
                .child(Constants.USER_COMPLETED_QUESTIONS)
                .setValue(0.toLong())
        }
    }

    fun checkIfQuizExists(key: String, check: MutableLiveData<Boolean>) {
        var quizExists = false
        val quizExistsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.child(key).exists())
                    quizExists = true

                check.postValue(quizExists)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        quizReference.addValueEventListener(quizExistsListener)
    }

}