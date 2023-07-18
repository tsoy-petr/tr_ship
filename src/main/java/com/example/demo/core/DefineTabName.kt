package com.example.demo.core

import com.google.gson.JsonParser
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files

object DefineTabName {

    fun define(fileName: String): String {

        return if (fileName.startsWith("Report_Departure")) {
            "DEPARTURE"

        }
        else if(fileName.startsWith("Noon")) {
            "NOON"
        }
        else if(fileName.startsWith("AnchorDrift")) {
            "ANCHOR/DRIFT"
        }
        else if(fileName.startsWith("Arriva")) {
            "ARRIVAL"
        }
        else if(fileName.startsWith("Bunker")) {
            "BUNKER"
        }
        else {
            ""
        }
    }

    fun selectTab(file: File, paneRouting: JTabbedPaneRouting?, errorChange: (String) -> Unit) {

        val nameTab = define(fileName = file.name)

        if (nameTab.isNotEmpty()) {
            try {
                Files.newBufferedReader(
                    file.toPath(),
                    StandardCharsets.UTF_8
                ).use { reader ->
                    val parser = JsonParser()
                    val tree = parser.parse(reader)
                    if (tree.isJsonObject) {
                        paneRouting?.selectTabWithData(nameTab, tree)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            errorChange("Report type not defined!")
        }

    }
}