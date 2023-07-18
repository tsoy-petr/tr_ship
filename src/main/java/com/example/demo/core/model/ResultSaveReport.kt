package com.example.demo.core.model

data class ResultSaveReport(
    private var isSuccess: Boolean, private var message: String = "",
    private var pathFile: String = ""
)