package com.example.demo.anchorDrift

import com.example.demo.core.*
import com.example.demo.core.exception.Failure
import com.example.demo.core.interactor.UseCase
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import com.example.demo.settings.SettingsPresenter
import com.example.demo.utils.Common
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

class AnchorDriftPresenter(private val paneRouting: JTabbedPaneRouting) : ValidatorResponse, LoadFromFile {

    private val state =
        MutableStateFlow<State>(State.Init(DepartureTypeSeaBerth.FROM_SEA, DepartureTypeAnchorDrifting.ANCHOR))
    val stateReport = state.asStateFlow()
    private var response = AnchorDriftResponse()

    private fun updateResponse(mapper: AnchorDriftResponse.() -> AnchorDriftResponse = { this }) {
        response = response.mapper()
        println(response)
    }

    fun setDepartureTypeFirst(newDepartureTypeFirst: DepartureTypeSeaBerth) {
        state.value = State.Editing(newDepartureTypeFirst, state.value.departureTypeSecond)
        updateResponse { copy(departureTypeFirst = if (newDepartureTypeFirst == DepartureTypeSeaBerth.FROM_SEA) 0 else 1) }
    }

    fun setDepartureTypeSecond(newDepartureTypeSecond: DepartureTypeAnchorDrifting) {
        state.value = State.Editing(state.value.departureTypeFirst, newDepartureTypeSecond)
        updateResponse { copy(departureTypeSecond = if (newDepartureTypeSecond == DepartureTypeAnchorDrifting.ANCHOR) 0 else 1) }
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

    fun setDateFirst(date: LocalDate?) {
        updateResponse { copy(dateFirst = date?.toString() ?: "") }
    }

    fun setTimeFirst(time: LocalTime?) {
        updateResponse { copy(timeFirst = time?.toString() ?: "") }
    }

    fun setSeaPassageDistance(newSeaPassageDistance: Double) {
        updateResponse { copy(seaPassageDistance = newSeaPassageDistance) }
    }

    fun setPositionLatitudeFirst(position: Position) {
        updateResponse { copy(latitudeFirst = position) }
    }

    fun setPositionLongitudeFirst(position: Position) {
        updateResponse { copy(longitudeFirst = position) }
    }

    fun setLSHFO_ROB_first(ls_hfo_rob: Double) {
        updateResponse { copy(LSHFO_ROB_first = ls_hfo_rob) }
    }

    fun setDatePOB(date: LocalDate?) {
        updateResponse { copy(datePOB = date?.toString() ?: "") }
    }

    fun setTimePOB(time: LocalTime?) {
        updateResponse { copy(timePOB = time?.toString() ?: "") }
    }

    fun setMGO_01_ROB_first(mgo_01_rob: Double) {
        updateResponse { copy(MGO_01_ROB_first = mgo_01_rob) }
    }

    fun setMGO_05_ROB_first(mgo_05_rob: Double) {
        updateResponse { copy(MGO_05_ROB_first = mgo_05_rob) }
    }

    fun setDatePilotOff(date: LocalDate?) {
        updateResponse { copy(datePilotOff = date?.toString() ?: "") }
    }

    fun setTimePilotOff(time: LocalTime?) {
        updateResponse { copy(timePilotOff = time?.toString() ?: "") }
    }

    fun setDateSecond(date: LocalDate?) {
        updateResponse { copy(dateSecond = date?.toString() ?: "") }
    }

    fun setTimeSecond(time: LocalTime?) {
        updateResponse { copy(timeSecond = time?.toString() ?: "") }
    }

    fun setManeuveringDist(maneuveringDist: Double) {
        updateResponse { copy(maneuveringDist = maneuveringDist) }
    }

    fun setPositionLatitudeSecond(position: Position) {
        updateResponse { copy(latitudeSecond = position) }
    }

    fun setPositionLongitudeSecond(position: Position) {
        updateResponse { copy(longitudeSecond = position) }
    }

    fun setLSHFO_ROB_second(ls_hfo_rob: Double) {
        updateResponse { copy(LSHFO_ROB_second = ls_hfo_rob) }
    }

    fun setFreshWaterMT(freshWaterMT: Int) {
        updateResponse { copy(freshWaterMT = freshWaterMT) }
    }

    fun setMGO_01_ROB_second(mgo_01_rob: Double) {
        updateResponse { copy(MGO_01_ROB_second = mgo_01_rob) }
    }

    fun setMGO_05_ROB_second(mgo_05_rob: Double) {
        updateResponse { copy(MGO_05_ROB_second = mgo_05_rob) }
    }

    fun setNote(note: String) {
        updateResponse { copy(note = note) }
    }

    fun saveReport() {

        validateResponse(object : IsValid {
            override fun valid() {
                state.value = State.Upload(state.value.departureTypeFirst, state.value.departureTypeSecond)

                val useCase = AnchorDriftSaveUseCase()
                useCase.invoke(
                    AnchorDriftSaveUseCase.Params(SettingsPresenter().readSettings(), response, SaveRepository()),
                ) {
                    it.fold(::handleFailure, ::handleSuccess)
                }
            }

        },

            object : IsNotValid {
                override fun isNotValid(message: String, noValidData: ArrayList<NoValidData>) {
                    state.value = State.UploadError(state.value.departureTypeFirst, state.value.departureTypeSecond, message, noValidData)
                }

        })


    }

    fun sendReport() {

        validateResponse(object : IsValid {
            override fun valid() {
                state.value = State.Upload(state.value.departureTypeFirst, state.value.departureTypeSecond)

                val useCase = AnchorDriftSendUseCase()
                useCase.invoke(
                    AnchorDriftSendUseCase.Params(SettingsPresenter().readSettings(), response, SendRepository()),
                ) {
                    it.fold(::handleFailure, ::handleSuccessNone)
                }
            }

        },
            object : IsNotValid {
                override fun isNotValid(message: String, noValidData: ArrayList<NoValidData>) {
                    state.value = State.UploadError(state.value.departureTypeFirst, state.value.departureTypeSecond, message, noValidData)
                }

        }
        )
    }

    private fun handleFailure(error: Failure) {
        state.value = State.UploadError(state.value.departureTypeFirst, state.value.departureTypeSecond, error.message, arrayListOf())
    }

    private fun handleSuccess(path: String) {
        state.value = State.UploadSuccess(state.value.departureTypeFirst, state.value.departureTypeSecond)
    }

    private fun handleSuccessNone(none: UseCase.None) {
        state.value = State.UploadSuccess(state.value.departureTypeFirst, state.value.departureTypeSecond)
    }

    override fun validateResponse(isValid: IsValid, isNotValid: IsNotValid) {

        val settings = SettingsPresenter().readSettings()

        val latitudeIsValidateFirst = (response.latitudeFirst?.isValidate) ?: false
        val longitudeIsValidateFirst = response.longitudeFirst?.isValidate ?: false

        val latitudeIsValidateSecond = (response.latitudeSecond?.isValidate) ?: false
        val longitudeIsValidateSecond = response.longitudeSecond?.isValidate ?: false

        if (
            settings.imo.isNullOrEmpty() ||
            response.voyNo.isEmpty() ||
            response.dateFirst.isEmpty() ||
            response.timeFirst.isEmpty() ||
            response.timeZone.isEmpty() || response.timeZone == "0" ||
            (!latitudeIsValidateFirst && response.departureTypeFirst == 0) ||
            (!longitudeIsValidateFirst && response.departureTypeFirst == 0) ||
            (response.dateSecond.isEmpty()) ||
            (response.timeSecond.isEmpty()) ||
            (!latitudeIsValidateSecond) ||
            (!longitudeIsValidateSecond)
        ) {

            val noValidData = arrayListOf<NoValidData>()
            var mess = IsNotValid.message
            if (Common.isEmpty(settings.imo)) {
                mess = "Vessel IMO not filled. \n$mess"
            }

            if (response.voyNo.isEmpty()) noValidData.add(NoValidData.VoyNo)
            if (response.dateFirst.isEmpty() || response.timeFirst.isEmpty()) noValidData.add(NoValidData.DateTimeStatus_1)
            if (response.timeZone.isEmpty() || response.timeZone == "0") noValidData.add(NoValidData.TimeZone)
            if (!latitudeIsValidateFirst && response.departureTypeFirst == 0) noValidData.add(NoValidData.Latitude_1)
            if (!longitudeIsValidateFirst && response.departureTypeFirst == 0) noValidData.add(NoValidData.Longitude_1)
            if (response.dateSecond.isEmpty() || response.timeSecond.isEmpty()) noValidData.add(NoValidData.DateTimeStatus_2)
            if (!latitudeIsValidateSecond) noValidData.add(NoValidData.Latitude_2)
            if (!longitudeIsValidateSecond) noValidData.add(NoValidData.Longitude_2)

            isNotValid.isNotValid(mess, noValidData)

        } else {
            isValid.valid()
        }

    }

    override fun load(file: File) {
        DefineTabName.selectTab(file, paneRouting) {}
    }


}

sealed class State(
    val departureTypeFirst: DepartureTypeSeaBerth,
    val departureTypeSecond: DepartureTypeAnchorDrifting
) {
    class Init(departureTypeFirst: DepartureTypeSeaBerth, departureTypeSecond: DepartureTypeAnchorDrifting) :
        State(departureTypeFirst, departureTypeSecond)

    class Editing(departureTypeFirst: DepartureTypeSeaBerth, departureTypeSecond: DepartureTypeAnchorDrifting) :
        State(departureTypeFirst, departureTypeSecond)

    class Upload(departureTypeFirst: DepartureTypeSeaBerth, departureTypeSecond: DepartureTypeAnchorDrifting) :
        State(departureTypeFirst, departureTypeSecond)

    class UploadSuccess(departureTypeFirst: DepartureTypeSeaBerth, departureTypeSecond: DepartureTypeAnchorDrifting) :
        State(departureTypeFirst, departureTypeSecond)

    class UploadError(
        departureTypeFirst: DepartureTypeSeaBerth,
        departureTypeSecond: DepartureTypeAnchorDrifting,
        val message: String, val noValidData: ArrayList<NoValidData>
    ) : State(departureTypeFirst, departureTypeSecond)
}