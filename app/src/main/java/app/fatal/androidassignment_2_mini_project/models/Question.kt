package app.fatal.androidassignment_2_mini_project.models

import com.google.gson.annotations.SerializedName

data class Question (
    val type: String,
    val category: String,
    val difficulty: String,
    val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>,
)