package com.example.demo.core

import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import javax.swing.JComboBox

object JComboBoxExtension {

    fun setSelectedItem(unlocodePort: String, terminalUUID: String? = null, ports: List<SeaPortDto>, cbSeaPort: JComboBox<SeaPortDto>, cbTerminal: JComboBox<TerminalDto>? = null) {

        for (port in ports) {
            if (port.unlocode == unlocodePort) {

                cbSeaPort.selectedItem = port

                terminalUUID?.let {tuid ->
                    cbTerminal?.let {cbt ->
                        val terminalDto = port.findTerminalByUUID(tuid)
                        cbt.setSelectedItem(terminalDto)
                    }
                }
                break
            }
        }

    }
}