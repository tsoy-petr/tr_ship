package com.example.demo.noon

import com.example.demo.core.SendRepository
import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.interactor.UseCase
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.model.SeaPortDto
import com.example.demo.model.SettingsEmailDto

class NoonSendUseCase() : UseCase<UseCase.None, NoonSendUseCase.Params>() {

    override suspend fun run(params: NoonSendUseCase.Params): Either<Failure, UseCase.None> {
        return params.repository.send(
            params.settingsEmailDto,
            "Noon (${params.settingsEmailDto.imo})",
            responseToBody(params.noonResponse)
        )
    }

    private fun responseToBody(response: NoonResponse): String {

        val sourcePorts = DataSourcePorts.getInstance()
        val port: SeaPortDto? = sourcePorts.ports.find { port -> port.unlocode.equals(response.unlocode) }
        val terminal = port?.terminals!!.find { t -> t.uid.equals(response.terminalUUID) }
        val portTerminalData = if (response.status == "In port"||response.status == "At anchor") {
            "Port\t\t\t\t\t\t: ${response.unlocode}\n" +
                    "Terminal (Name)\t\t\t\t: $terminal (*${response.terminalUUID}*)\n"

        } else ""
        return "Voy. No.\t\t\t\t\t: ${response.voyNo}\n" +
                "Last Port\t\t\t\t\t: ${response.unlocodeLast}\n" +
                "Date and time (LT)\t\t\t\t: ${response.dateLt}/${response.timeLt}\n" +
                "Time Zone GMT\t\t\t\t: ${response.timeZone}\n" +
                "Next Port\t\t\t\t\t: ${response.unlocodeNext}\n" +
                "Status (Underway/In Port/At anchor/Adrift) \t: ${response.status}\n" +

                portTerminalData +

                "Position Latitude\t\t\t\t: ${response.latitude ?: ""}\n" +
                "Position Longitude\t\t\t\t: ${response.longitude ?: ""}\n" +
                "Distance run from Last report (NM)\t\t: ${response.seaPassageDistance}\n" +
                "ETA(date/time) LT next port\t\t\t: ${response.dateETA}/${response.timeETA}\n" +
                "Main Engine RPM/Pitch(%)\t\t\t: ${response.meRPM}\n" +
                "LSHFO ROB noon (mt)\t\t\t\t: ${response.hfoROB}\n" +
                "MGO 0,1% ROB noon (mt)\t\t\t: ${response.mgo_01_ROB}\n" +
                "MGO 0,5% ROB noon (mt)\t\t\t: ${response.mgo_05_ROB}\n" +
                "Distance to go (NM)\t\t\t\t: ${response.distanceToGo}\n" +
                "Main Engine mode (Eco/Full)\t\t\t: ${response.meMode}\n" +
                "Course (degree)\t\t\t\t: ${response.course}\n" +
                "Speed (kts)\t\t\t\t\t: ${response.speed}\n" +
                "Fresh Water (MT)\t\t\t\t: ${response.freshWater}\n" +
                "Wind scale (beaufourt) \t\t\t\t: ${response.windScaleBeaufourt}\n" +
                "Swell Height (m)\t\t\t\t: ${response.swellHeight}\n" +
                "Wind direction (0-360)\t\t\t\t: ${response.windDirection}\n" +
                "Swell direction (0-360)\t\t\t\t: ${response.swellDirection}\n" +
                "Notes(if any)\t\t\t\t\t: ${response.note}\n" +
                " \n" +
                " \n" +
                " \n"
    }

    data class Params(
        val settingsEmailDto: SettingsEmailDto,
        val noonResponse: NoonResponse,
        val repository: SendRepository
    )
}