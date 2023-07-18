package com.example.demo.anchorDrift

import com.example.demo.core.Position

data class AnchorDriftResponse(
    val voyNo: String = "",
    val timeZone: String = "0",
    val unlocode: String = "",
    val terminalUUID: String = "",
    val departureTypeFirst: Int = 0,
    val dateFirst: String = "",
    val timeFirst: String = "",
    val seaPassageDistance: Double = 0.0,
    var latitudeFirst: Position? = null,
    val longitudeFirst: Position? = null,
    val LSHFO_ROB_first : Double = 0.0,
    val datePOB: String = "",
    val timePOB: String = "",
    val MGO_01_ROB_first: Double = 0.0,
    val MGO_05_ROB_first: Double = 0.0,
    val datePilotOff: String = "",
    val timePilotOff: String = "",
    val departureTypeSecond: Int = 0,
    val dateSecond: String = "",
    val timeSecond: String = "",
    val maneuveringDist: Double = 0.0,
    var latitudeSecond: Position? = null,
    val longitudeSecond: Position? = null,
    val LSHFO_ROB_second : Double = 0.0,
    val freshWaterMT: Int = 0,
    val MGO_01_ROB_second: Double = 0.0,
    val MGO_05_ROB_second: Double = 0.0,
    val note: String = ""

)

enum class DepartureTypeSeaBerth {
    FROM_SEA {
        override fun toString(): String {
            return "From sea"
        }
    },
    FROM_BERTH {
        override fun toString(): String {
            return "From berth"
        }
    }
}

enum class DepartureTypeAnchorDrifting {
    ANCHOR {
        override fun toString(): String {
            return "Anchor"
        }
    },
    DRIFTING {
        override fun toString(): String {
            return "Drifting"
        }
    }
}
