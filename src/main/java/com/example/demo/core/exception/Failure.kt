package com.example.demo.core.exception

sealed class Failure(
    open val message: String
) {
    data class Common(override val message: String) : Failure(message)
}
