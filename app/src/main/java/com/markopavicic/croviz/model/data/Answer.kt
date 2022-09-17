package com.markopavicic.croviz.model.data

data class Answer(
    val correct: Boolean,
    val answer: String
) {
    constructor() : this(false, "")
}
