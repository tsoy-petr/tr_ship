package com.example.demo.noon

import com.example.demo.core.Position
import com.example.demo.model.SeaPortDto
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.LocalTime

class NoonPresenter {

    val state = MutableStateFlow<State>(State.AtSeeAdrift)

    private var noonResponse = NoonResponse();

    fun setTZ(newTimeZone: String) {
        update { copy(timeZone = newTimeZone) }
    }

    fun setUnlocodeLast(seaPortDto: SeaPortDto) {
        update { copy(unlocodeLast = seaPortDto.unlocode) }
    }

    fun setUnlocodeNext(seaPortDto: SeaPortDto) {
        update { copy(unlocodeNext = seaPortDto.unlocode) }
    }

    fun setDateLT(date: LocalDate) {
        update { copy(dateLt = dateLt) }
    }

    fun setTimeLT(time: LocalTime) {
        update { copy(timeLt = timeLt) }
    }

    fun setStatus(status: Status) {
        update { copy(status = status.name) }
    }

    fun setPositionLatitude(position: Position) {
        update{copy(latitude = position)}
    }
    fun setPositionLongitude(position: Position) {
        update{copy(longitude = position)}
    }
    private fun update(mapper: NoonResponse.() -> NoonResponse ={this} ){

    }

}

sealed class State {
    object AtSeeAdrift : State()
    object ASeeUnderway : State()
    object AtAnchor : State()
    object InPort : State()
}