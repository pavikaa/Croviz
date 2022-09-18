package com.markopavicic.croviz.model.data

data class Stats(
    val userScore: Long,
    val userCorrectAnswers: Long,
    val userIncorrectAnswers: Long,
    val userCompletedQuizzes: Long,
    val globalScore: Long,
    val globalCorrectAnswers: Long,
    val globalIncorrectAnswers: Long,
    val globalCompletedQuizzes: Long,

    )
