package com.example.demo.bunker

import com.example.demo.core.SendRepository
import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.interactor.UseCase
import com.example.demo.model.SettingsEmailDto

class BunkerSendUseCase : UseCase<UseCase.None, BunkerSendUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        return params.repository.send(
            params.settingsEmailDto,
            "Bunker (${params.settingsEmailDto.imo})",
            responseToBody(params.response)
        )
    }

    private fun responseToBody(response: BunkerResponse): String {

        return "Voy No\t\t\t\t: ${response.voyNo}\n" +
                "Port\t\t\t\t: ${response.unlocode}\n" +
                "Time Zone\t\t\t: ${response.timeZone}\n" +
                "Position Lattitude\t\t: ${response.latitude}\n" +
                "Position Longitude\t\t: ${response.longitude}\n" +
                "Status\t\t\t\t: ${response.statusShip}\n" +
                "Type of Fuel\t\t\t: ${response.typeOfFuel}\n" +
                "LSHFO bunkering completed\t: ${response.date}/${response.time}\n" +
                "Received\t\t\t: ${response.receivedFuel}\n" +
                "Note\t\t\t\t: ${response.note}\n" +
                " \n" +
                " \n" +
                " \n"
    }

    data class Params(
        val settingsEmailDto: SettingsEmailDto,
        val response: BunkerResponse,
        val repository: SendRepository
    )

}