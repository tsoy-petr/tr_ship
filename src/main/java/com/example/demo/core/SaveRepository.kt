package com.example.demo.core

import com.example.demo.core.exception.Failure
import com.example.demo.core.functional.Either
import com.example.demo.utils.Common
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SaveRepository {

    companion object {
        private const val DATE_FORMAT_NOW = "yyyy_MM_dd_HH_mm_ss"
    }

    fun save(folderForSavingReports: String, json: String, filePrefix: String): Either<Failure, String> {

        if (folderForSavingReports.isEmpty()) {
            return Either.Left(Failure.Common("Path to save reports not configured!"))
        }

        if (!Common.fileExists(folderForSavingReports)) {
            return Either.Left(Failure.Common("Path to save file does not exist!"))
        }

        val fileName = filePrefix + "_" + now() + ".json"

        return try {
            val saveDirectory = File(folderForSavingReports)
            val pathFile = saveDirectory.absolutePath + File.separator + fileName
            val file = FileWriter(pathFile)
            file.write(json)
            file.close()
            val path = File(pathFile)
            Either.Right(path.absolutePath)
        } catch (e: Exception) {
            Either.Left(Failure.Common(e.message.toString()))
        }

    }

    private fun now():String {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat(DATE_FORMAT_NOW)
        return sdf.format(cal.time)
    }

}