package com.example.demo.bunker

import com.example.demo.core.*
import com.example.demo.core.exception.Failure
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import com.example.demo.settings.SettingsPresenter
import com.example.demo.utils.Common
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

class BunkerPresenter(private val paneRouting: JTabbedPaneRouting) : ValidatorResponse, LoadFromFile {

    private val state = MutableStateFlow<State>(State.Init("LSHFO bunkering completed"))
    val stateReport = state.asStateFlow()
    private var response = BunkerResponse()

    private fun updateResponse(mapper: BunkerResponse.() -> BunkerResponse = { this }) {
        response = response.mapper()
        println(response)
    }

    fun setVoyNo(voyNo: String?) {
        voyNo?.let {
            updateResponse { copy(voyNo = it) }
        }
    }

    fun setTZ(newTimeZone: String) {
        updateResponse { copy(timeZone = newTimeZone) }
    }

    fun setUnlocode(seaPortDto: SeaPortDto) {
        updateResponse { copy(unlocode = seaPortDto.unlocode) }
    }

    fun setTerminal(terminalDto: TerminalDto) {
        updateResponse { copy(terminalUUID = terminalDto.uid) }
    }

    fun setDate(date: LocalDate?) {
        updateResponse { copy(date = date?.toString() ?: "") }
    }

    fun setTime(time: LocalTime?) {
        updateResponse { copy(time = time?.toString() ?: "") }
    }

    fun setPositionLatitude(position: Position) {
        updateResponse { copy(latitude = position) }
    }

    fun setPositionLongitude(position: Position) {
        updateResponse { copy(longitude = position) }
    }

    fun setStatusShip(statusShip: StatusShip) {
        updateResponse { copy(statusShip = statusShip) }
    }

    fun setTypeOfFuel(typeOfFuel: TypeOfFuel) {
        updateResponse { copy(typeOfFuel = typeOfFuel) }
        val titleDateReport =
            when (typeOfFuel) {
                TypeOfFuel.LSHFO -> {
                    "LSHFO bunkering completed"
                }
                TypeOfFuel.MGO_0_1 -> {
                    "MGO 0,1 bunkering completed"
                }
                else -> "MGO 0,5 bunkering completed"
            }

        state.value = State.Editing(titleDateReport)
    }

    fun setReceivedFuel(receivedFuel: Double) {
        updateResponse { copy(receivedFuel = receivedFuel) }
    }

    fun setNote(note: String) {
        updateResponse { copy(note = note) }
    }

    fun saveReport() {
        validateResponse(object : IsValid {
            override fun valid() {
                state.value = State.Upload(state.value.titleDateReport)

                val useCase = BunkerSaveUseCase()
                useCase.invoke(
                    BunkerSaveUseCase.Params(SettingsPresenter().readSettings(), response, SaveRepository()),
                ) {
                    it.fold(::handleFailure, ::handleSuccess)
                }
            }

        },
            object : IsNotValid {
                override fun isNotValid(message: String, noValidData: ArrayList<NoValidData>) {
                    state.value = State.UploadError(state.value.titleDateReport, message, noValidData)
                }

            }
        )
    }

    fun sendReport() {
        validateResponse(object : IsValid {
            override fun valid() {
                state.value = State.Upload(state.value.titleDateReport)
                val useCase = BunkerSendUseCase()
                useCase.invoke(
                    BunkerSendUseCase.Params(
                        SettingsPresenter().readSettings(), response, SendRepository()
                    )
                )
            }

        },
            object : IsNotValid {
                override fun isNotValid(message: String, noValidData: ArrayList<NoValidData>) {
                    state.value = State.UploadError(state.value.titleDateReport, message, noValidData)
                }

            }
        )
    }

    private fun handleFailure(error: Failure) {
        state.value = State.UploadError(state.value.titleDateReport, error.message, arrayListOf())
    }

    private fun handleSuccess(path: String) {
        state.value = State.UploadSuccess(state.value.titleDateReport)
    }

    override fun validateResponse(isValid: IsValid, isNotValid: IsNotValid) {

        val settings = SettingsPresenter().readSettings()

        if (
            settings.imo.isNullOrEmpty() ||
            response.voyNo.isEmpty() ||
            response.timeZone.isEmpty() || response.timeZone == "0" ||
            response.unlocode.isEmpty() ||
            response.date.isEmpty() || response.time.isEmpty() ||
            (if (response.latitude == null) true else !response.latitude!!.isValidate) ||
            (if (response.longitude == null) true else !response.longitude!!.isValidate) ||
            response.receivedFuel == 0.0

        ) {

            var mess = IsNotValid.message

            if (Common.isEmpty(settings.imo)) {
                mess = "Vessel IMO not filled. \n$mess"
            }
            val noValidData = arrayListOf<NoValidData>()

            if (response.voyNo.isEmpty()) noValidData.add(NoValidData.VoyNo)
            if (response.timeZone.isEmpty() || response.timeZone == "0") noValidData.add(NoValidData.TimeZone)
            if ((response.unlocode.isEmpty())) noValidData.add(NoValidData.Unlocode)
            if (response.date.isEmpty() || response.time.isEmpty()) noValidData.add(NoValidData.DateTimeStatus_1)
                if ((if (response.latitude == null) true else !response.latitude!!.isValidate)) noValidData.add(
                    NoValidData.Latitude_1
                )
            if ((if (response.longitude == null) true else !response.longitude!!.isValidate)) noValidData.add(
                NoValidData.Longitude_1
            )
            if (response.receivedFuel == 0.0) noValidData.add(NoValidData.ReceivedFuel)

            isNotValid.isNotValid(mess, noValidData)

        } else {
            isValid.valid()
        }

    }

    override fun load(file: File) {
        DefineTabName.selectTab(file, paneRouting) {}
    }

}

sealed class State(val titleDateReport: String) {
    class Init(titleDateReport: String) : State(titleDateReport)
    class Editing(titleDateReport: String) : State(titleDateReport)
    class Upload(titleDateReport: String) : State(titleDateReport)
    class UploadSuccess(titleDateReport: String) : State(titleDateReport)
    class UploadError(titleDateReport: String, val message: String, val noValidData: ArrayList<NoValidData>) :
        State(titleDateReport)
}