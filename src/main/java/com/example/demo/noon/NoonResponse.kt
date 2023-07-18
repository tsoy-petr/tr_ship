package com.example.demo.noon

import com.example.demo.core.Position

data class NoonResponse(
    val voyNo: String = "",
    val timeZone: String = "0",
    val dateLt: String = "",
    val timeLt: String = "",
    val status: String = "",
    val unlocode: String = "",
    val unlocodeNext: String = "",
    val unlocodeLast: String = "",
    val terminalUUID: String = "",
    var latitude: Position? = null,
    val longitude: Position? = null,
    val seaPassageDistance: Double = 0.0,
    val distanceToGo: Double = 0.0,
    val dateETA: String = "",
    val timeETA: String = "",
    val meMode: String = "",
    val meRPM: Int = 0,
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
) {

    companion object {
        fun findStatusByString(statusName: String):Status {

            return when(statusName) {
                "At see/Adrift" -> Status.AtSeeAdrift
                "At see/Underway" -> Status.ASseeUnderway
                "At anchor" -> Status.AtAnchor
                "In port" -> Status.InPort
                else -> Status.AtSeeAdrift
            }

        }
    }
}

enum class Status {
    AtSeeAdrift {
        override fun toString() = "At see/Adrift"
    },
    ASseeUnderway {
        override fun toString() = "At see/Underway"
    },
    AtAnchor {
        override fun toString() = "At anchor"
    },
    InPort {
        override fun toString() = "In port"
    }
}



