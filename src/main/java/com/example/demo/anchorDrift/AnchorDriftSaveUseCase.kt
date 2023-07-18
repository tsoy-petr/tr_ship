package com.example.demo.anchorDrift

import com.example.demo.core.SaveRepository
import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.interactor.UseCase
import com.example.demo.model.SettingsEmailDto
import com.google.gson.GsonBuilder

class AnchorDriftSaveUseCase() : UseCase<String, AnchorDriftSaveUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, String> {

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val json = gson.toJson(params.noonResponse)

        return params.repository.save(params.settingsEmailDto.folderForSavingReports, json, "AnchorDrift")

    }

    data class Params(
        val settingsEmailDto: SettingsEmailDto,
        val noonResponse: AnchorDriftResponse,
        val repository: SaveRepository
    )

}