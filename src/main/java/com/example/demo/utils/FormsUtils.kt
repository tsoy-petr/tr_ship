package com.example.demo.utils

import com.example.demo.core.Position
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import javax.swing.JComboBox

class FormsUtils {

    companion object {

        fun initListPorts(
            dataSourcePorts: DataSourcePorts,
            cb: JComboBox<SeaPortDto>,
            terminalsCb: JComboBox<TerminalDto>? = null,
            timezoneCb: JComboBox<String>? = null,
            setPort: (SeaPortDto) -> Unit,
            setUnlocode: (String) -> Unit, setLatitude: (Position) -> Unit, setLongitude: (Position) -> Unit,
            setTerminal: (TerminalDto) -> Unit
        ) {


            cb.removeAllItems()
            var firstPort: SeaPortDto? = null
            val ports: List<SeaPortDto> = dataSourcePorts.ports
            for (port in ports) {
                cb.addItem(port)
                if (firstPort == null) {
                    firstPort = port
                }
            }
            if (firstPort != null) {
                terminalsCb?.let { initListTerminals(it, firstPort, setLatitude, setLongitude, setTerminal) }
                if (timezoneCb != null) {
                    timezoneCb.selectedItem = firstPort.timeZone
                }
                setPort(firstPort)
                setUnlocode(firstPort.unlocode)
            }
        }

        fun initListTerminals(
            cb: JComboBox<TerminalDto>, port: SeaPortDto, setLatitude: (Position) -> Unit,
            setLongitude: (Position) -> Unit,
            setTerminal: (TerminalDto) -> Unit
        ) {
            cb.removeAllItems()
            for (t in port.terminals) {
                cb.addItem(t)
            }
            if (port.terminals.size > 0) {
                val terminalDto = port.terminals[0]
                cb.selectedItem = terminalDto
                setTerminal.invoke(terminalDto)
                setLatitude(terminalDto.latitude)
                setLongitude(terminalDto.longitude)
            }

        }

        fun initListTimeZone(cb: JComboBox<String>) {
            cb.removeAllItems()
            for (i in 0..12) {
                if (i == 0) {
                    cb.addItem("0")
                } else {
                    cb.addItem("+$i")
                }
            }
            for (i in 1..12) {
                cb.addItem("-$i")
            }
        }
    }

}