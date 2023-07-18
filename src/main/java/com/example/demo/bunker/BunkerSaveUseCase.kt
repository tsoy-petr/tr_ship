package com.example.demo.bunker

import com.example.demo.core.SaveRepository
import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.interactor.UseCase
import com.example.demo.model.SettingsEmailDto
import com.google.gson.GsonBuilder

class BunkerSaveUseCase() : UseCase<String, BunkerSaveUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, String> {

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val json = gson.toJson(params.bunkerResponse)

        return params.repository.save(params.settingsEmailDto.folderForSavingReports, json, "Bunker")
    }

    data class Params(
        val settingsEmailDto: SettingsEmailDto,
        val bunkerResponse: BunkerResponse,
        val repository: SaveRepository
    )
}