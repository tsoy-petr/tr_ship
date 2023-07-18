package com.example.demo.arrival

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


class ArrivalPresenter(private val paneRouting: JTabbedPaneRouting) : ValidatorResponse, LoadFromFile {

    private val state = MutableStateFlow<State>(State.Init(DepartureTypeArrivalFromSeaAnchorageDrift.FROM_SEA))
    val stateReport = state.asStateFlow()
    private var response = ArrivalResponse()

    private fun updateResponse(mapper: ArrivalResponse.() -> ArrivalResponse = { this }) {
        response = response.mapper()
        println(response)
    }

    fun setDepartureTypeFirst(newDepartureTypeFirst: DepartureTypeArrivalFromSeaAnchorageDrift) {
        state.value = State.Editing(newDepartureTypeFirst)
        updateResponse {
            copy(
                departureType_first = if (newDepartureTypeFirst == DepartureTypeArrivalFromSeaAnchorageDrift.FROM_SEA) 0 else 1,
                seaPassageDistance = if (newDepartureTypeFirst == DepartureTypeArrivalFromSeaAnchorageDrift.FROM_SEA) seaPassageDistance else 0.0
            )
        }
    }

    fun setVoyNo(voyNo: String?) {
        voyNo?.let {
            updateResponse { copy(voyNo = it) }
        }
    }

    fun setTZ_second(newTimeZone: String) {
        updateResponse { copy(timeZone_second = newTimeZone) }
    }

    fun setUnlocodeSecond(seaPortDto: SeaPortDto) {
        updateResponse { copy(unlocode_second = seaPortDto.unlocode) }
    }

    fun setTerminalSecond(terminalDto: TerminalDto) {
        updateResponse { copy(terminalUUID_second = terminalDto.uid) }
    }

    fun setDateFirst(date: LocalDate?) {
        updateResponse { copy(dateFirst = date?.toString() ?: "") }
    }

    fun setTimeFirst(time: LocalTime?) {
        updateResponse { copy(timeFirst = time?.toString() ?: "") }
    }

    fun setDatePOB(date: LocalDate?) {
        updateResponse { copy(datePOB_first = date?.toString() ?: "") }
    }

    fun setTimePOB(time: LocalTime?) {
        updateResponse { copy(timePOB_first = time?.toString() ?: "") }
    }

    fun setPositionLatitudeFirst(position: Position) {
        updateResponse { copy(latitudeFirst = position) }
    }

    fun setPositionLongitudeFirst(position: Position) {
        updateResponse { copy(longitudeFirst = position) }
    }

    fun setLSHFO_ROBFirst(ls_hfo_rob: Double) {
        updateResponse { copy(LSHFO_ROB_first = ls_hfo_rob) }
    }

    fun setDatePilotOffFirst(date: LocalDate?) {
        updateResponse { copy(datePilotOff_first = date?.toString() ?: "") }
    }

    fun setTimePilotOffFirst(time: LocalTime?) {
        updateResponse { copy(timePilotOff_first = time?.toString() ?: "") }
    }

    fun setMGO_01_ROBFirst(mgo_01_rob: Double) {
        updateResponse { copy(MGO_01_ROB_first = mgo_01_rob) }
    }

    fun setMGO_05_ROBFirst(mgo_05_rob: Double) {
        updateResponse { copy(MGO_05_ROB_first = mgo_05_rob) }
    }

    fun setDateTugMakeFastFirst(date: LocalDate?) {
        updateResponse { copy(dateTugMakeFast_first = date?.toString() ?: "") }
    }

    fun setTimeTugMakeFastFirst(time: LocalTime?) {
        updateResponse { copy(timeTugMakeFast_first = time?.toString() ?: "") }
    }

    fun setNoOfTugs(noOfTugs: Long) {
        updateResponse { copy(noOfTugs = noOfTugs) }
    }

    fun setDateSecond(date: LocalDate?) {
        updateResponse { copy(date_second = date?.toString() ?: "") }
    }

    fun setTimeSecond(time: LocalTime?) {
        updateResponse { copy(time_second = time?.toString() ?: "") }
    }

    fun setManeuveringDist(maneuveringDist: Double) {
        updateResponse { copy(maneuveringDist = maneuveringDist) }
    }

    fun setSeaPassageDistance(seaPassageDistance: Double) {
        updateResponse { copy(seaPassageDistance = seaPassageDistance) }
    }

    fun setLSHFO_ROBSecond(LSHFO_ROB_second: Double) {
        updateResponse { copy(LSHFO_ROB_second = LSHFO_ROB_second) }
    }

    fun setMGO_01_ROBSecond(mgo_01_rob: Double) {
        updateResponse { copy(MGO_01_ROB_second = mgo_01_rob) }
    }

    fun setMGO_05_ROBSecond(mgo_05_rob: Double) {
        updateResponse { copy(MGO_05_ROB_second = mgo_05_rob) }
    }

    fun setDateFirstLineSecond(date: LocalDate?) {
        updateResponse { copy(dateFirstLine_second = date?.toString() ?: "") }
    }

    fun setTimeFirstLineSecond(time: LocalTime?) {
        updateResponse { copy(timeFirstLine_second = time?.toString() ?: "") }
    }

    fun setDateTugCastOffSecond(date: LocalDate?) {
        updateResponse { copy(dateTugCastOff_second = date?.toString() ?: "") }
    }

    fun setTimeTugCastOffSecond(time: LocalTime?) {
        updateResponse { copy(timeTugCastOff_second = time?.toString() ?: "") }
    }

    fun setFreshWaterMT(freshWaterMT: Int) {
        updateResponse { copy(freshWaterMT_second = freshWaterMT) }
    }

    fun setNote(note: String) {
        updateResponse { copy(note = note) }
    }

    fun saveReport() {
        validateResponse(object : IsValid {
            override fun valid() {
                state.value = State.Upload(state.value.departureTypeFirst)
                val useCase = ArrivalSaveUseCase()
                useCase.invoke(
                    ArrivalSaveUseCase.Params(SettingsPresenter().readSettings(), response, SaveRepository()),
                ) {
                    it.fold(::handleFailure, ::handleSuccess)
                }
            }
        },
            object : IsNotValid {
                override fun isNotValid(message: String, noValidData: ArrayList<NoValidData>) {
                    state.value = State.UploadError(state.value.departureTypeFirst, message, noValidData)
                }

            }
        )
    }

    fun sendReport() {
        validateResponse(object : IsValid {
            override fun valid() {
                state.value = State.Upload(state.value.departureTypeFirst)
                val useCase = ArrivalSendUseCase()
                useCase.invoke(
                    ArrivalSendUseCase.Params(
                        SettingsPresenter().readSettings(), response, SendRepository()
                    )
                )

            }
        },

            object : IsNotValid {
                override fun isNotValid(message: String, noValidData: ArrayList<NoValidData>) {
                    state.value = State.UploadError(state.value.departureTypeFirst, message, noValidData)
                }

            }
        )
    }

    private fun handleFailure(error: Failure) {
        state.value = State.UploadError(state.value.departureTypeFirst, error.message, arrayListOf())
    }

    private fun handleSuccess(path: String) {
        state.value = State.UploadSuccess(state.value.departureTypeFirst)
    }

    override fun validateResponse(isValid: IsValid, isNotValid: IsNotValid) {

        val settings = SettingsPresenter().readSettings()

        val latitudeIsValidateFirst = (response.latitudeFirst?.isValidate) ?: false
        val longitudeIsValidateFirst = response.longitudeFirst?.isValidate ?: false

        if (
            settings.imo.isNullOrEmpty() ||
            response.voyNo.isEmpty() ||
            response.dateFirst.isEmpty() ||
            response.timeFirst.isEmpty() ||
            response.timeZone_second.isEmpty() || response.timeZone_second == "0" ||
            response.date_second.isEmpty() ||
            response.time_second.isEmpty() ||
            (!latitudeIsValidateFirst && response.departureType_first == 0) ||
            (!longitudeIsValidateFirst && response.departureType_first == 0)
        ) {
            var mess = IsNotValid.message
            if (Common.isEmpty(settings.imo)) {
                mess = "Vessel IMO not filled. \n$mess"
            }
            val noValidData = arrayListOf<NoValidData>()

            if (response.voyNo.isEmpty()) noValidData.add(NoValidData.VoyNo)
            if (response.dateFirst.isEmpty() || response.timeFirst.isEmpty()) noValidData.add(NoValidData.DateTimeStatus_1)
            if (response.timeZone_second.isEmpty() || response.timeZone_second == "0") noValidData.add(NoValidData.TimeZone)
            if (response.date_second.isEmpty() || response.time_second.isEmpty()) noValidData.add(NoValidData.DateTimeStatus_2)
            if (!latitudeIsValidateFirst && response.departureType_first == 0) noValidData.add(NoValidData.Latitude_1)
            if (!longitudeIsValidateFirst && response.departureType_first == 0) noValidData.add(NoValidData.Longitude_1)

            isNotValid.isNotValid(mess, noValidData)

        } else {
            isValid.valid()
        }

    }

    override fun load(file: File) {
        DefineTabName.selectTab(file, paneRouting) {}
    }


}

sealed class State(val departureTypeFirst: DepartureTypeArrivalFromSeaAnchorageDrift) {
    class Init(departureTypeFirst: DepartureTypeArrivalFromSeaAnchorageDrift) : State(departureTypeFirst)
    class Editing(departureTypeFirst: DepartureTypeArrivalFromSeaAnchorageDrift) : State(departureTypeFirst)
    class Upload(departureTypeFirst: DepartureTypeArrivalFromSeaAnchorageDrift) : State(departureTypeFirst)
    class UploadSuccess(departureTypeFirst: DepartureTypeArrivalFromSeaAnchorageDrift) : State(departureTypeFirst)
    class UploadError(
        departureTypeFirst: DepartureTypeArrivalFromSeaAnchorageDrift,
        val message: String,
        val noValidData: ArrayList<NoValidData>
    ) :
        State(departureTypeFirst)
}