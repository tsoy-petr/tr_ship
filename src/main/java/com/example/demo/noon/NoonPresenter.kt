package com.example.demo.noon

import com.example.demo.core.Position
import com.example.demo.core.SaveRepository
import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.functional.map
import com.example.demo.departure.MEMode
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import com.example.demo.noon.Status.*
import com.example.demo.settings.SettingsPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.MainScope

import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.LocalTime

class NoonPresenter {

    val state = MutableStateFlow<State>(State.AtSeeAdrift)

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

    fun setDateLT(date: LocalDate) {
        updateResponse { copy(dateLt = date.toString()) }
    }

    fun setTimeLT(time: LocalTime) {
        updateResponse { copy(timeLt = time.toString()) }
    }

    fun setStatus(status: Status) {
        updateResponse { copy(status = status.name) }
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
        println(noonResponse)
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

    fun setETADate(dateETA: LocalDate) {
        updateResponse { copy(dateETA = dateETA.toString()) }
    }

    fun setETATime(time: LocalTime) {
        updateResponse { copy(timeETA = time.toString()) }
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
        updateResponse{copy(terminalUUID = terminalDto.uid)}
    }

    fun saveReport() {

        state.value = State.Upload

        val useCase = NoonSaveUseCase()
        useCase.invoke(
            NoonSaveUseCase.Params(SettingsPresenter().readSettings(), noonResponse, SaveRepository()),
            MainScope()

        ) {
            it.fold(::handleFailure, ::handleSuccess)
        }

    }

    private fun handleFailure(error: Failure) {
        state.value = State.UploadError(error.message)
    }

    private fun handleSuccess(path: String) {
        state.value = State.UploadSuccess
    }

}

sealed class State {
    object AtSeeAdrift : State()
    object ASSeeUnderway : State()
    object AtAnchor : State()
    object InPort : State()
    object Upload : State()
    object UploadSuccess : State()
    data class UploadError(val message: String) : State()
}