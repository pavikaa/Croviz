package com.markopavicic.croviz.model.data

data class Question(
    val questionId: String,
    val question: String,
    val answers: List<Answer>
) {
    constructor() : this("", "", listOf())
}
