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
            cb: JComboBox<SeaPortDto?>,
            terminalsCb: JComboBox<TerminalDto?>?,
            timezoneCb: JComboBox<String?>?,
            setPort: (SeaPortDto) -> Unit,
            setUnlocode: (String) -> Unit, setLatitude: (Position) -> Unit, setLongitude: (Position) -> Unit
        ) {


            cb.removeAllItems()
            var firstPort: SeaPortDto? = null
            val ports: List<SeaPortDto> = dataSourcePorts.getPorts()
            for (port in ports) {
                cb.addItem(port)
                if (firstPort == null) {
                    firstPort = port
                }
            }
            if (firstPort != null) {
                if (terminalsCb != null) {
                    initListTerminals(terminalsCb, firstPort, setLatitude, setLongitude)
                }
                if (timezoneCb != null) {
                    timezoneCb.selectedItem = firstPort.timeZone
                }
                setPort(firstPort)
                setUnlocode(firstPort.unlocode)
            }
        }

        fun initListTerminals(
            cb: JComboBox<TerminalDto?>, port: SeaPortDto, setLatitude: (Position) -> Unit,
            setLongitude: (Position) -> Unit
        ) {
            cb.removeAllItems()
            for (t in port.terminals) {
                cb.addItem(t)
            }
            if (port.terminals.size > 0) {
                val terminalDto = port.terminals[0]
                cb.selectedItem = terminalDto
                setLatitude(terminalDto.getLatitude())
                setLongitude(terminalDto.getLongitude())
            }

        }

    }

}