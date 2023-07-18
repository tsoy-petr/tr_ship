package com.example.demo.noon

import com.example.demo.model.DepartureResponse

interface NoonDataChangeListner {
    fun change(response: NoonResponse)
    fun errorChange(error: String)
}