package com.example.demo.noon

import com.example.demo.core.Position

data class NoonResponse(
    val voyNo: String = "",
    val timeZone: String = "",
    val dateLt: String = "",
    val timeLt: String = "",
    val status: Status = Status.AtSeeAdrift,
    val unlocode: String = "",
    val terminalUUID: String = "",
    var latitude: Position? = null,
    val longitude: Position? = null,
    val seaPassageDistance: Double = 0.0,
    val distanceToGo: Double = 0.0,
    val dateETA: String = "",
    val timeETA: String = "",
    val meMode: String? = null,
    val meRPM: Long = 0,
    val course: Int = 0,
    val hfoROB: Double = 0.0,
    val speed: Double = 0.0,
    val mgo_01_ROB: Double = 0.0,
    val mgo_05_ROB: Double = 0.0,
    val freshWater: Int = 0,
    val windScaleBeaufourt: Int = 0,
    val windDirection: Int = 0,
    val swellHeight: Int = 0,
    val swellDirection: Int = 0,
    val note: String = ""
)

enum class Status {
    AtSeeAdrift, ASseeUnderway, AtAnchor, InPort
}
