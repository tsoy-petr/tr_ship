package com.example.demo

import com.example.demo.dataPorts.DataPortsPresenter
import com.example.demo.dataPorts.DataPortsTab
import com.example.demo.departure.DeparturePresenter
import com.example.demo.departure.DepartureTab
import com.example.demo.model.SeaPortDto
import com.example.demo.model.SettingsEmailDto
import com.example.demo.model.TerminalDto
import com.example.demo.noon.NoonPresenter
import com.example.demo.noon.NoonTab
import com.example.demo.settings.SettingsPresenter
import com.example.demo.settings.SettingsTab
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.*
import javax.swing.event.ChangeEvent

class Starter {

    var departurePresenter = DeparturePresenter()
    var settingsPresenter = SettingsPresenter()
    var sendEmailService = SendEmailService()
    private var tfVoyNoDeparture: JFormattedTextField? = null
    private var lbVoyNoDeparture: JLabel? = null
    private var cbTZDeparture: JComboBox<String>? = null
    private var lbTZDeparture: JLabel? = null
    private var lbPortDeparture: JLabel? = null
    private var cbPortDeparture: JComboBox<SeaPortDto>? = null
    private var lbTerminalDeparture: JLabel? = null
    private var cbTerminalDeparture: JComboBox<TerminalDto>? = null

    fun initWindow() {
        val frame = JFrame("Tranzit")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(950, 1000)
        val tabbedPane = JTabbedPane()
        tabbedPane.tabLayoutPolicy = JTabbedPane.SCROLL_TAB_LAYOUT
        tabbedPane.addChangeListener {
            val selectedIndex = tabbedPane.selectedIndex
            if (selectedIndex == 1) {
                val settingsEmailDto = settingsPresenter.readSettings()
            }
        }
        tabbedPane.addTab("DEPARTURE", initDer())
        tabbedPane.addTab("NOON", initNoonReport())
        tabbedPane.addTab("SETTINGS", initSett())
        tabbedPane.addTab("Data ports", initDataPorts())
        frame.add(BorderLayout.CENTER, JScrollPane(tabbedPane))
        frame.isVisible = true
    }

    private fun initDer(): JPanel? {
        val tab = JPanel()
        tab.layout = FlowLayout()
        tab.add(DepartureTab(DeparturePresenter()))
        return tab
    }

    private fun initNoonReport(): JPanel? {
        val tab = JPanel()
        tab.layout = FlowLayout()
        tab.add(NoonTab(NoonPresenter()))
        return tab
    }

    private fun initSett(): JPanel? {
        val tab = JPanel()
        tab.layout = FlowLayout()
        tab.add(SettingsTab(SettingsPresenter()))
        return tab
    }

    private fun initDataPorts(): JPanel? {
        val tab = JPanel()
        tab.layout = FlowLayout()
        tab.add(DataPortsTab(DataPortsPresenter()))
        return tab
    }

    private fun initDepartureTab(): JPanel? {
        val tab = JPanel()
        tab.layout = FlowLayout()
        val panel = JPanel()
        val layout = GridBagLayout()
        panel.layout = layout
        val gbc = GridBagConstraints()
        lbVoyNoDeparture = JLabel("Voy. No.")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 0
        panel.add(lbVoyNoDeparture, gbc)
        tfVoyNoDeparture = JFormattedTextField()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        gbc.gridy = 0
        panel.add(tfVoyNoDeparture, gbc)
        addSeparatorGrid(panel, gbc, 2, 0)
        lbTZDeparture = JLabel("Time Zone GMT")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 3
        gbc.gridy = 0
        panel.add(lbTZDeparture, gbc)
        cbTZDeparture = JComboBox()
        departurePresenter.initListTimeZone(cbTZDeparture)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 4
        gbc.gridy = 0
        panel.add(cbTZDeparture, gbc)
        addSeparatorGrid(panel, gbc, 0, 2)
        lbPortDeparture = JLabel("Port")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridy = 3
        panel.add(lbPortDeparture, gbc)
        cbPortDeparture = JComboBox()
        cbPortDeparture!!.setEditable(true)
        departurePresenter.initListPorts(cbPortDeparture, cbTerminalDeparture, cbTZDeparture)
        cbPortDeparture!!.addItemListener(ItemListener { e -> println(e) })
        cbPortDeparture!!.addActionListener(ActionListener { e ->
            if ((e.source as JComboBox<SeaPortDto?>).actionCommand == "comboBoxEdited") {
                cbTerminalDeparture!!.removeAllItems()
            } else if ((e.source as JComboBox<SeaPortDto?>).actionCommand == "comboBoxChanged") {
                try {
                    val selectedSeaPort = cbPortDeparture!!.getModel().selectedItem as SeaPortDto
                    cbTZDeparture!!.setSelectedItem(selectedSeaPort.timeZone)
                    departurePresenter.initListTerminals(cbTerminalDeparture, selectedSeaPort)
                } catch (ex: ClassCastException) {
                    println(ex)
                }
            }
        })
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        gbc.gridy = 3
        panel.add(cbPortDeparture, gbc)
        addSeparatorGrid(panel, gbc, 2, 3)
        lbTerminalDeparture = JLabel("Terminal")
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 3
        gbc.gridy = 3
        panel.add(lbTerminalDeparture, gbc)
        cbTerminalDeparture = JComboBox()
        cbTerminalDeparture!!.setEditable(true)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 4
        gbc.gridy = 3
        panel.add(cbTerminalDeparture, gbc)
        addSeparatorGrid(panel, gbc, 2, 4)
        val DepartureFromBerth = JRadioButton("Departure from Berth")
        val DepartureFromAnchorage = JRadioButton("Departure from Anchorage")
        val bg = ButtonGroup()
        bg.add(DepartureFromBerth)
        bg.add(DepartureFromAnchorage)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 1
        gbc.gridy = 5
        panel.add(DepartureFromBerth, gbc)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 3
        gbc.gridy = 5
        panel.add(DepartureFromAnchorage, gbc)
        addSeparatorGrid(panel, gbc, 2, 6)
        tab.add(panel)
        return tab
    }

    private fun addSeparatorGrid(panel: JPanel, gbc: GridBagConstraints, gridx: Int, gridy: Int) {
        val sep = JPanel()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = gridx
        gbc.gridy = gridy
        panel.add(sep, gbc)
    }
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val app = Starter()
            app.initWindow()
        }
    }

}