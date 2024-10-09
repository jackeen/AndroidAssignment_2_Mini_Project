package app.fatal.androidassignment_2_mini_project.models

import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("response_code") val responseCode: Int,
    val results: List<Question>,
)