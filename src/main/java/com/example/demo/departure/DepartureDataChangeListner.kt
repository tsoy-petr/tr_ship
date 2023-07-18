package com.example.demo.departure

import com.example.demo.model.DepartureResponse

interface DepartureDataChangeListner {
    fun change(response: DepartureResponse)
    fun errorChange(error: String)
}