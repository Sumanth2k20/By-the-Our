package com.bytheour.utils

enum class LocationType(val type: String){
    OTHER("Other"),
    HOME("Home"),
    WORK("Work")
}

object Status {
    enum class OrderStatus(val status: String) {
        IN_PROGRESS("ONGOING"),
        CONFIRMED("UPCOMING"),
        SEARCHING("REQUEST"),
        COMPLETED("COMPLETED"),
        CANCELLED("CANCELLED");
//        PENDING("PENDING");

        companion object {
            fun fromValue(value: String?): OrderStatus? {
                return entries.find { it.status.equals(value, ignoreCase = true) }
            }
        }
    }
}

//enum class BookingStatus(val status: String){
//    UPCOMING("UPCOMING"),
//    PROCESSING("PROCESSING"),
//    COMPLETED("COMPLETED")
//}

enum class ServiceRatingText(val type:String){
    POOR("Poor"),
    BAD("Bad"),
    AVERAGE("Average"),
    GREAT("Great"),
    EXCELLENT("Excellent")
}

enum class Approvals(val type: String){
    ACCEPT("accept"),
    REJECT("decline")
}

