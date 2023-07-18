package com.example.demo.core

import com.google.gson.JsonElement
import javax.swing.JPanel
import javax.swing.JTabbedPane

class JTabbedPaneRouting : JTabbedPane() {

    fun selectTabWithData(nameTab: String, json: JsonElement) {
        val index = indexOfTab(nameTab);
        if (index > -1) {
            selectedIndex = index
            val component = getComponentAt(index)

            if (component is JPanel) {
                val tab = component.getComponent(0)
                if (tab is TabReport) {
                    tab.loadFromJson(json);
                }
            }
        }
    }
}