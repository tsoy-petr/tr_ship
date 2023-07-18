package com.example.demo.arrival

import com.example.demo.core.Position

data class ArrivalResponse(
    val voyNo: String = "",
    val timeZone_second: String = "0",
    val unlocode_second: String = "",
    val terminalUUID_second: String = "",
    val departureType_first: Int = 0,
    val dateFirst: String = "",
    val timeFirst: String = "",
    val datePOB_first: String = "",
    val timePOB_first: String = "",
    var latitudeFirst: Position? = null,
    val longitudeFirst: Position? = null,
    val LSHFO_ROB_first : Double = 0.0,
    val datePilotOff_first: String = "",
    val timePilotOff_first: String = "",
    val MGO_01_ROB_first: Double = 0.0,
    val MGO_05_ROB_first: Double = 0.0,
    val dateTugMakeFast_first: String = "",
    val timeTugMakeFast_first: String = "",
    var noOfTugs: Long = 0,
    val date_second: String = "",
    val time_second: String = "",
    val maneuveringDist: Double = 0.0,
    val LSHFO_ROB_second : Double = 0.0,
    val MGO_01_ROB_second: Double = 0.0,
    val MGO_05_ROB_second: Double = 0.0,
    val dateFirstLine_second: String = "",
    val timeFirstLine_second: String = "",
    val dateTugCastOff_second: String = "",
    val timeTugCastOff_second: String = "",
    val freshWaterMT_second: Int = 0,
    val note: String = "",
    val seaPassageDistance: Double = 0.0,
)

enum class DepartureTypeArrivalFromSeaAnchorageDrift {
    FROM_SEA {
        override fun toString(): String {
            return "Arrival from sea"
        }
    },
    FROM_ANCHORAGE_DRIFT {
        override fun toString(): String {
            return "Arrival from Anchorage/Drift"
        }
    }
}
