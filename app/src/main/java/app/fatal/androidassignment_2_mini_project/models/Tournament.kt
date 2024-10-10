package app.fatal.androidassignment_2_mini_project.models

import java.util.Date

data class Tournament (
    val id: String = "",
    val name: String = "",
    val category: Category = Category.ANY,
    val difficulty: Difficulty = Difficulty.ANY,
    val type: Type = Type.BOOLEAN,
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val amount: Int = 10,
    val likes: List<String> = listOf(),
    val questions: List<Question> = listOf(),
)

//{
//    public fun isOnGoing() : Boolean {
//        return Date() in startDate..endDate
//    }
//
//    public fun isPast() : Boolean {
//        return Date() > endDate
//    }
//
//    public fun isComing() : Boolean {
//        return Date() < startDate
//    }
//}
