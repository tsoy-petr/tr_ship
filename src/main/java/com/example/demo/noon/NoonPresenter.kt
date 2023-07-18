package com.example.demo.noon

import com.example.demo.core.*
import com.example.demo.core.exception.Failure
import com.example.demo.departure.MEMode
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import com.example.demo.noon.Status.*
import com.example.demo.settings.SettingsPresenter
import com.example.demo.utils.Common
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import javax.swing.JOptionPane

class NoonPresenter(private val paneRouting: JTabbedPaneRouting) : ValidatorResponse, LoadFromFile{

    private var noonDataChangeListner: NoonDataChangeListner? = null

    val state = MutableStateFlow<State>(State.AtSeeAdrift)
    val stateReport = state.asStateFlow()
    var statusReport: Status = AtSeeAdrift

    private var noonResponse = NoonResponse();

    fun setTZ(newTimeZone: String) {
        updateResponse { copy(timeZone = newTimeZone) }
    }

    fun setUnlocodeLast(seaPortDto: SeaPortDto) {
        updateResponse { copy(unlocodeLast = seaPortDto.unlocode) }
    }

    fun setUnlocodeNext(seaPortDto: SeaPortDto) {
        updateResponse { copy(unlocodeNext = seaPortDto.unlocode) }
    }

    fun setDateLT(date: LocalDate?) {
        updateResponse { copy(dateLt = date?.toString() ?: "") }
    }

    fun setTimeLT(time: LocalTime?) {
        updateResponse { copy(timeLt = time?.toString() ?: "") }
    }

    fun setStatus(status: Status) {
        statusReport = status
        updateResponse { copy(status = status.toString()) }
        when (status) {
            AtSeeAdrift -> {
                state.value = State.AtSeeAdrift
            }

            ASseeUnderway -> {
                state.value = State.ASSeeUnderway
            }

            AtAnchor -> {
                state.value = State.AtAnchor
            }

            InPort -> {
                state.value = State.InPort
            }
        }
    }

    fun setPositionLatitude(position: Position) {
        updateResponse { copy(latitude = position) }
    }

    fun setPositionLongitude(position: Position) {
        updateResponse { copy(longitude = position) }
    }

    private fun updateResponse(mapper: NoonResponse.() -> NoonResponse = { this }) {
        noonResponse = noonResponse.mapper()
    }

    fun setMeMode(currMode: MEMode) {
        updateResponse { copy(meMode = currMode.name) }
    }

    fun setSeaPassageDistance(newSeaPassageDistance: Double) {
        updateResponse { copy(seaPassageDistance = newSeaPassageDistance) }
    }

    fun setDistanceToGo(newDistanceToGo: Double) {
        updateResponse { copy(distanceToGo = newDistanceToGo) }
    }

    fun setETADate(dateETA: LocalDate?) {
        updateResponse { copy(dateETA = dateETA?.toString() ?: "") }
    }

    fun setETATime(time: LocalTime?) {
        updateResponse { copy(timeETA = time?.toString() ?: "") }
    }

    fun setMERPM(meRPM: Int) {
        updateResponse { copy(meRPM = meRPM) }
    }

    fun setCourse(course: Int) {
        updateResponse { copy(course = course) }
    }

    fun setHFOROB(hfoROB: Double) {
        updateResponse { copy(hfoROB = hfoROB) }
    }

    fun setSpeed(speed: Double) {
        updateResponse { copy(speed = speed) }
    }

    fun setMGO_ROB_01(mgo_01_ROB: Double) {
        updateResponse { copy(mgo_01_ROB = mgo_01_ROB) }
    }

    fun setMGO_ROB_05(mgo_05_ROB: Double) {
        updateResponse { copy(mgo_05_ROB = mgo_05_ROB) }
    }

    fun setFreshWaterMT(freshWater: Int) {
        updateResponse { copy(freshWater = freshWater) }
    }

    fun setWindScaleBeaufourt(windScaleBeaufourt: Int) {
        updateResponse { copy(windScaleBeaufourt = windScaleBeaufourt) }
    }

    fun setWindDirection(windDirection: Int) {
        updateResponse { copy(windDirection = windDirection) }
    }

    fun setSwellHeight(swellHeight: Int) {
        updateResponse { copy(swellHeight = swellHeight) }
    }

    fun setSwellDirection(swellDirection: Int) {
        updateResponse { copy(swellDirection = swellDirection) }
    }

    fun setNote(note: String) {
        updateResponse { copy(note = note) }
    }

    fun setVoyNo(voyNo: String?) {
        voyNo?.let {
            updateResponse { copy(voyNo = it) }
        }
    }

    fun setUnlocode(seaPortDto: SeaPortDto) {
        updateResponse { copy(unlocode = seaPortDto.unlocode) }
    }

    fun setTerminal(terminalDto: TerminalDto) {
        updateResponse { copy(terminalUUID = terminalDto.uid) }
    }

    fun saveReport() {

        validateResponse(object : IsValid {
            override fun valid() {

                state.value = State.Upload

                val useCase = NoonSaveUseCase()
                useCase.invoke(
                    NoonSaveUseCase.Params(SettingsPresenter().readSettings(), noonResponse, SaveRepository())
                ) {
                    it.fold(::handleFailure, ::handleSuccess)
                }
            }

        },
            object : IsNotValid {
                override fun isNotValid(message: String, noValidData: ArrayList<NoValidData>) {
                    state.value = State.UploadError(status = statusReport, message, noValidData)
                }

            }
        )
    }

    fun sendReport() {

        validateResponse(object : IsValid {
            override fun valid() {
                state.value = State.UploadSuccess(status = statusReport)
                val useCase = NoonSendUseCase()
                useCase.invoke(
                    NoonSendUseCase.Params(
                        SettingsPresenter().readSettings(),
                        noonResponse,
                        SendRepository()
                    )
                )
            }

        },

            object : IsNotValid {
                override fun isNotValid(message: String, noValidData: ArrayList<NoValidData>) {
                    state.value = State.UploadError(status = statusReport, message, noValidData)
                }
            }
        )
    }

    private fun handleFailure(error: Failure) {
        state.value = State.UploadError(status = statusReport, error.message, arrayListOf())
    }

    private fun handleSuccess(path: String) {
        state.value = State.UploadSuccess(status = statusReport)
    }

    override fun validateResponse(isValid: IsValid, isNotValid: IsNotValid) {

        val settings = SettingsPresenter().readSettings()

        val latitudeIsValidate = (noonResponse.latitude?.isValidate) ?: false
        val longitudeIsValidate = noonResponse.longitude?.isValidate ?: false
        if (
            settings.imo.isNullOrEmpty() ||
            noonResponse.voyNo.isEmpty() ||
            noonResponse.timeZone.isEmpty() || noonResponse.timeZone == "0" ||
            noonResponse.unlocodeLast.isEmpty() ||
            noonResponse.unlocodeNext.isEmpty() ||
            (noonResponse.unlocode.isEmpty() && noonResponse.status == "In port") ||
            noonResponse.dateLt.isEmpty() ||
            noonResponse.timeLt.isEmpty() ||
            (!latitudeIsValidate && noonResponse.status != "In port") ||
            (!longitudeIsValidate && noonResponse.status != "In port") ||
            noonResponse.terminalUUID.isEmpty()
        ) {
            var mess = IsNotValid.message
            if (Common.isEmpty(settings.imo)) {
                mess = "Vessel IMO not filled. \n$mess"
            }

            val noValidData = arrayListOf<NoValidData>()

            if (noonResponse.voyNo.isEmpty()) noValidData.add(NoValidData.VoyNo)
            if (noonResponse.timeZone.isEmpty() || noonResponse.timeZone == "0") noValidData.add(NoValidData.TimeZone)
            if (noonResponse.unlocodeLast.isEmpty()) noValidData.add(NoValidData.UnlocodeLast)
            if (noonResponse.unlocodeNext.isEmpty()) noValidData.add(NoValidData.UnlocodeNext)
            if (noonResponse.unlocode.isEmpty() && noonResponse.status == "In port") noValidData.add(NoValidData.Unlocode)
            if (noonResponse.dateLt.isEmpty() || noonResponse.timeLt.isEmpty()) noValidData.add(NoValidData.DateTimeStatus_1)
            if (!latitudeIsValidate && noonResponse.status != "In port") noValidData.add(NoValidData.Latitude_1)
            if (!longitudeIsValidate && noonResponse.status != "In port") noValidData.add(NoValidData.Longitude_1)
            if (noonResponse.terminalUUID.isEmpty() && noonResponse.status == "In port") noValidData.add(NoValidData.Terminal)

            isNotValid.isNotValid(mess, noValidData)

        } else {
            isValid.valid()
        }

    }

    override fun load(file: File) {
        DefineTabName.selectTab(file, paneRouting) {

        }
    }
}

sealed class State {
    object AtSeeAdrift : State()
    object ASSeeUnderway : State()
    object AtAnchor : State()
    object InPort : State()
    object Upload : State()
    data class UploadSuccess(val status: Status) : State()
    data class UploadError(val status: Status, val message: String, val noValidData: ArrayList<NoValidData>) : State()
}