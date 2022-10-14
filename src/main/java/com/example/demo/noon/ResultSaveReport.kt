package com.example.demo.noon

data class ResultSaveReport(
    private var isSuccess: Boolean, private var message: String = "",
    private var pathFile: String = ""
)