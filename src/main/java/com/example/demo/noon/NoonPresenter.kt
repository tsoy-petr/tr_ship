package com.example.demo.noon

class NoonPresenter {

    private var noonResponse = NoonResponse();

    fun setTZ(newTimeZone: String) {
        this.noonResponse = noonResponse.copy(timeZone = newTimeZone)
    }

}