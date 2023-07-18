package com.example.demo.anchorDrift

import com.example.demo.core.SendRepository
import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.interactor.UseCase
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.model.SeaPortDto
import com.example.demo.model.SettingsEmailDto

class AnchorDriftSendUseCase() : UseCase<UseCase.None, AnchorDriftSendUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return params.repository.send(
            params.settingsEmailDto,
            "Anchor/Drift (${params.settingsEmailDto.imo})",
            responseToBody(params.response)
        )
    }

    private fun responseToBody(response: AnchorDriftResponse): String {

        val sourcePorts = DataSourcePorts.getInstance()
        val port: SeaPortDto? = sourcePorts.ports.find { port -> port.unlocode.equals(response.unlocode) }
        val terminal = port?.terminals!!.find { t -> t.uid.equals(response.terminalUUID) }

        val current =
            if (response.departureTypeSecond == 0) {
                "Anchor Report  (from SEA/PORT)\t\t: ${if (response.departureTypeFirst == 0) "SEA" else "PORT"}\n" +
                        "Voy. No.\t\t\t\t\t: ${response.voyNo}\n" +
                        "Port\t\t\t\t\t\t: ${response.unlocode}\n" +
                        "Time Zone GMT\t\t\t\t: ${response.timeZone}\n" +
                        "Terminal (Name)\t\t\t\t: $terminal (*${response.terminalUUID}*)\n"
            } else {
                "Drift Report  (from SEA/PORT)\t\t\t: ${if (response.departureTypeFirst == 0) "SEA" else "PORT"}\n" +
                        "Voy. No.\t\t\t\t\t: ${response.voyNo}\n" +
                        "Port\t\t\t\t\t\t: ${response.unlocode}\n" +
                        "Time Zone GMT\t\t\t\t: ${response.timeZone}\n" +
                        "Terminal (Name)\t\t\t\t: $terminal (*${response.terminalUUID}*)\n"
            }

        val firstStatus =
            if (response.departureTypeFirst == 0) {
                val currFirstStatus = "EOSP (date/time)\t\t\t\t: ${response.dateFirst}/${response.timeFirst}\n" +
                        "Position of EOSP (Latitude)\t\t\t: ${response.latitudeFirst?:""}\n" +
                        "Position of EOSP (Longitude)\t\t\t: ${response.longitudeFirst?:""}\n" +
                        "LSHFO of EOSP\t\t\t\t\t: ${response.LSHFO_ROB_first}\n" +
                        "MGO 0,1% of EOSP\t\t\t\t: ${response.MGO_01_ROB_first}\n" +
                        "MGO 0,5% of EOSP\t\t\t\t: ${response.MGO_05_ROB_first}\n"
                if (response.departureTypeSecond == 0) {
                    currFirstStatus +
                            "Distance from Last Report to EOSP\t\t: ${response.seaPassageDistance}\n" +
                            "P.O.B (date/time)\t\t\t\t: ${response.datePOB}/${response.timePOB}\n" +
                            "Pilot Off (date/time)\t\t\t\t: ${response.datePilotOff}/${response.timePilotOff}\n"
                } else {
                    currFirstStatus +
                            "Distance from last report to EOSP\t\t: ${response.seaPassageDistance}\n" +
                            "P.O.B (date/time)\t\t\t\t: ${response.datePOB}/${response.timePOB}\n" +
                            "Pilot Off (date/time)\t\t\t\t: ${response.datePilotOff}/${response.timePilotOff}\n"
                }
            } else {
                val currFirstStatus = "Last line  (date/time)\t\t\t\t: ${response.dateFirst}/${response.timeFirst}\n" +
                        "LSHFO of Last line\t\t\t\t: ${response.LSHFO_ROB_first}\n" +
                        "MGO 0,1% of Last line\t\t\t\t: ${response.MGO_01_ROB_first}\n" +
                        "MGO 0,5% of Last line\t\t\t\t: ${response.MGO_05_ROB_first}\n"
                if (response.departureTypeSecond == 0) {
                    currFirstStatus +
                            "Distance from last line to anchorage\t\t: ${response.seaPassageDistance}\n" +
                            "P.O.B (date/time)\t\t\t\t: ${response.datePOB}/${response.timePOB}\n" +
                            "Pilot Off (date/time)\t\t\t\t: ${response.datePilotOff}/${response.timePilotOff}\n"
                } else {
                    currFirstStatus +
                            "Distance from last line to start drift\t\t: ${response.seaPassageDistance}\n" +
                            "P.O.B (date/time)\t\t\t\t: ${response.datePOB}/${response.timePOB}\n" +
                            "Pilot Off (date/time)\t\t\t\t: ${response.datePilotOff}/${response.timePilotOff}\n"
                }
            }

        val secondStatus =
            if (response.departureTypeSecond == 0) {
                "Anchor Dropped (date/time)\t\t\t: ${response.dateSecond}/${response.timeSecond}\n" +
                        "Position of dropped anchor (Latitude)\t\t: ${response.latitudeSecond?:""}\n" +
                        "Position of dropped anchor (Longitude)\t\t: ${response.longitudeSecond?:""}\n" +
                        "LSHFO of start anchorage\t\t\t: ${response.LSHFO_ROB_second}\n" +
                        "MGO 0,1% of start anchorage\t\t\t: ${response.MGO_01_ROB_second}\n" +
                        "MGO 0,5% of start anchorage\t\t\t: ${response.MGO_05_ROB_second}\n" +
                        "Distance from EOSP to Anchor Dropped\t: ${response.maneuveringDist}\n" +
                        "Fresh water (mt)\t\t\t\t: ${response.freshWaterMT}\n" +
                        "Notes(if any)\t\t\t\t\t: ${response.note}\n"
            } else {
                "Start Drift (date/time)\t\t\t\t: ${response.dateSecond}/${response.timeSecond}\n" +
                        "Position of start drift (Latitude)\t\t\t: ${response.latitudeSecond?:""}\n" +
                        "Position of start drift (Longitude)\t\t: ${response.longitudeSecond?:""}\n" +
                        "LSHFO of start drift\t\t\t\t: ${response.LSHFO_ROB_second}\\\n" +
                        "MGO 0,1% of start drift\t\t\t\t: ${response.MGO_01_ROB_second}\n" +
                        "MGO 0,5% of start drift\t\t\t\t: ${response.MGO_05_ROB_second}\n" +
                        "Distance from EOSP to Start Drift\t\t: ${response.maneuveringDist}\n" +
                        "Fresh water (mt)\t\t\t\t: ${response.freshWaterMT}\n" +
                        "Notes(if any):\t\t\t\t\t: ${response.note}\n"
            }

        return current + firstStatus + secondStatus +
                " \n" +
                " \n" +
                " \n"
    }

    data class Params(
        val settingsEmailDto: SettingsEmailDto,
        val response: AnchorDriftResponse,
        val repository: SendRepository
    )

}