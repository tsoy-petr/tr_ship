package com.example.demo.bunker

import com.example.demo.core.Position

data class BunkerResponse(
    val voyNo: String = "",
    val timeZone: String = "0",
    val unlocode: String = "",
    val terminalUUID: String = "",
    var latitude: Position? = null,
    val longitude: Position? = null,
    val statusShip: StatusShip = StatusShip.AT_ANCHOR,
    val typeOfFuel: TypeOfFuel = TypeOfFuel.LSHFO,
    val date: String = "",
    val time: String = "",
    val receivedFuel: Double = 0.0,
    val note: String = ""
) {
}

enum class StatusShip{
    AT_ANCHOR {
        override fun toString(): String {
            return "At Anchor"
        }
    },
    AT_BERTH {
        override fun toString(): String {
            return "At Berth"
        }
    },
    AT_DRIFT{
        override fun toString(): String {
            return "At Drift"
        }
    }
}

//LSHFO(LSHFO), MGO 0,1%(MGO_0_1), MGO 0,5%(MGO_0_5)
enum class TypeOfFuel{
    LSHFO {
        override fun toString(): String {
            return "LSHFO"
        }
    },
    MGO_0_1 {
        override fun toString(): String {
            return "MGO 0,1%"
        }
    },
    MGO_0_5 {
        override fun toString(): String {
            return "MGO 0,5%"
        }
    }
}