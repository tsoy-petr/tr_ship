package com.example.demo.core

import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.core.interactor.UseCase
import com.example.demo.model.SettingsEmailDto
import com.example.demo.utils.Common
import java.awt.Desktop
import java.net.URI

class SendRepository() {

    fun send(
        settingsEmailDto: SettingsEmailDto,
        subject: String,
        emailBody: String
    ): Either<Failure, UseCase.None> =
        try {

            val desktop = Desktop.getDesktop()

            if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.MAIL)) {
                var cc = ""
                val toCopy = mutableListOf<String>()
                if (settingsEmailDto.emailCopy1.isNotEmpty()){
                    toCopy.add(settingsEmailDto.emailCopy1)
                }
                if (settingsEmailDto.emailCopy2.isNotEmpty()){
                    toCopy.add(settingsEmailDto.emailCopy2)
                }
                if (settingsEmailDto.emailCopy3.isNotEmpty()){
                    toCopy.add(settingsEmailDto.emailCopy3)
                }
                if (toCopy.isNotEmpty()) {
                    cc = "$cc?cc="
                    cc += toCopy.joinToString(separator = ",")
                    cc = "$cc&"
                } else cc = "?"
                desktop.mail(
                    URI(
                        "mailto:" + settingsEmailDto.serverEmail
                                + cc
                                + "subject=" + Common.urlEncode(subject)
                                + "&body=" + Common.urlEncode(emailBody)
                    )
                )
                Either.Right(UseCase.None())
            } else {
                Either.Left(Failure.Common("Desktop doesn't support mailto; mail is dead anyway!"))
            }

        } catch (e: Exception) {
            Either.Left(Failure.Common(e.message.toString()))
        }
}