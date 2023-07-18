package com.example.demo.core

interface ValidatorResponse {

    fun validateResponse(isValid: IsValid, isNotValid: IsNotValid)

}

interface IsValid {

    fun valid()

}

interface IsNotValid {

    fun isNotValid(message: String, noValidData: ArrayList<NoValidData>)

    companion object {

        const val message = "Required data not filled!"

    }

}

enum class NoValidData {
    VoyNo, Unlocode, UnlocodeNext, UnlocodeLast, TimeZone, DateTimeStatus_1, DateTimeStatus_2, Latitude_1, Latitude_2, Longitude_1, Longitude_2, ReceivedFuel,Terminal
}