package com.example.demo.arrival

import com.example.demo.core.SaveRepository
import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.interactor.UseCase
import com.example.demo.model.SettingsEmailDto
import com.google.gson.GsonBuilder

class ArrivalSaveUseCase() : UseCase<String, ArrivalSaveUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, String> {

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val json = gson.toJson(params.noonResponse)

        return params.repository.save(params.settingsEmailDto.folderForSavingReports, json, "Arrival")

    }

    data class Params(
        val settingsEmailDto: SettingsEmailDto,
        val noonResponse: ArrivalResponse,
        val repository: SaveRepository
    )

}