package com.example.demo.arrival

import com.example.demo.core.SendRepository
import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.interactor.UseCase
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.model.SeaPortDto
import com.example.demo.model.SettingsEmailDto

class ArrivalSendUseCase() : UseCase<UseCase.None, ArrivalSendUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return params.repository.send(
            params.settingsEmailDto,
            "Arrival (${params.settingsEmailDto.imo})",
            responseToBody(params.response)
        )
    }

    private fun responseToBody(response: ArrivalResponse): String {

        val sourcePorts = DataSourcePorts.getInstance()
        val port: SeaPortDto? = sourcePorts.ports.find { port -> port.unlocode.equals(response.unlocode_second) }
        val terminal = port?.terminals!!.find { t -> t.uid.equals(response.terminalUUID_second) }

        val departureType = if (response.departureType_first == 0) {
            "from SEA"
        } else {
            "ANCHORAGE/DRIFT"
        }
        val current = "Arrival Report (from SEA/ANCHORAGE/DRIFT)\t\t\t\t\t: $departureType\n" +
                "Voyage No.\t\t\t\t\t\t\t\t\t: ${response.voyNo}\n" +
                "PORT\t\t\t\t\t\t\t\t\t\t: ${response.unlocode_second}\n" +
                "Time Zone GMT\t\t\t\t\t\t\t\t: ${response.timeZone_second}\n" +
                "Terminal (Name)\t\t\t\t\t\t\t\t: $terminal (*${response.terminalUUID_second}*)\n"

        val firstStatus = if (response.departureType_first == 0) {
            "EOSP (date/time)\t\t\t\t\t\t\t\t: ${response.dateFirst} ${response.timeFirst}\n" +
                    "Distance from Last Report to EOSP\t\t\t\t\t\t: ${response.seaPassageDistance}\n" +
                    "Position of EOSP (Latitude)\t\t\t\t\t\t\t: ${response.latitudeFirst?:""}\n" +
                    "Position of EOSP (Longitude)\t\t\t\t\t\t\t: ${response.longitudeFirst?:""}\n" +
                    "LSHFO of EOSP\t\t\t\t\t\t\t\t\t: ${response.LSHFO_ROB_first}\n" +
                    "MGO 0,1% of EOSP\t\t\t\t\t\t\t\t: ${response.MGO_01_ROB_first}\n" +
                    "MGO 0,5% of EOSP\t\t\t\t\t\t\t\t: ${response.MGO_05_ROB_first}\n" +
                    "P.O.B (date/time)\t\t\t\t\t\t\t\t: ${response.datePOB_first}/${response.timePOB_first}\n" +
                    "Pilot Off (date/time)\t\t\t\t\t\t\t\t: ${response.datePilotOff_first}/${response.timePilotOff_first}\n" +
                    "Tug make fast (date/time)\t\t\t\t\t\t\t: ${response.dateTugMakeFast_first}/${response.timeTugMakeFast_first}\n" +
                    "No. of Tugs \t\t\t\t\t\t\t\t\t: ${response.noOfTugs}\n"
        } else {
            "Anchor up/Drift Completed (date/time)\t\t\t\t\t\t: ${response.dateFirst}/${response.timeFirst}\n" +
                    "LSHFO\t\t\t\t\t\t\t\t\t\t: ${response.LSHFO_ROB_first}\n" +
                    "MGO 0,1%\t\t\t\t\t\t\t\t\t: ${response.MGO_01_ROB_first}\n" +
                    "MGO 0,5%\t\t\t\t\t\t\t\t\t: ${response.MGO_05_ROB_first}\n" +
                    "P.O.B (date/time)\t\t\t\t\t\t\t\t: ${response.datePOB_first}/${response.timePOB_first}\n" +
                    "Pilot Off (date/time)\t\t\t\t\t\t\t\t: ${response.datePilotOff_first}/${response.timePilotOff_first}\n" +
                    "Tug make fast (date/time)\t\t\t\t\t\t\t: ${response.dateTugMakeFast_first}/${response.timeTugMakeFast_first}\n" +
                    "No. of Tugs \t\t\t\t\t\t\t\t\t: ${response.noOfTugs}\n"
        }
        val secondStatus =
                    "All fast (date/time)\t\t\t\t\t\t\t\t: ${response.date_second}/${response.time_second}\n" +
                    "LSHFO of All fast\t\t\t\t\t\t\t\t: ${response.LSHFO_ROB_second}\n" +
                    "MGO 0,1% of All fast\t\t\t\t\t\t\t\t: ${response.MGO_01_ROB_second}\n" +
                    "MGO 0,5% of All fast\t\t\t\t\t\t\t\t: ${response.MGO_05_ROB_second}\n" +
                    "Maneuvering Distance from EOSP/anchor up/drift completed to All fast\t: ${response.maneuveringDist}\n" +
                    "1st. LINE (date/time)\t\t\t\t\t\t\t\t: ${response.dateFirstLine_second}/${response.timeFirstLine_second}\n" +
                    "Tug cast off (date/time)\t\t\t\t\t\t\t\t: ${response.dateTugCastOff_second}/${response.timeTugCastOff_second}\n" +
                    "Fresh water (mt)\t\t\t\t\t\t\t\t: ${response.freshWaterMT_second}\n" +
                    "Notes(if any)\t\t\t\t\t\t\t\t\t: ${response.note}\n"

        return (current + firstStatus + secondStatus +
                " \n" +
                " \n" +
                " \n")

    }

    data class Params(
        val settingsEmailDto: SettingsEmailDto,
        val response: ArrivalResponse,
        val repository: SendRepository
    )

}