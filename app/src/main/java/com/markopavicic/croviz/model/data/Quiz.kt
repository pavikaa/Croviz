package com.markopavicic.croviz.model.data

data class Quiz(
    val quizId: String,
    val quizCategory: String,
    val creator: String,
    val createdAt: String,
    val questions: List<Question>

)
