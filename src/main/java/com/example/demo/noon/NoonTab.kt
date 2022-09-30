package com.example.demo.noon

import com.example.demo.core.TabReport
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.model.SeaPortDto
import com.example.demo.utils.FormsUtils
import com.example.demo.utils.newActionListener
import javax.swing.JComboBox
import javax.swing.JFormattedTextField
import javax.swing.JLabel

class NoonTab(
    private val noonPresenter: NoonPresenter
) : TabReport() {

    private val tfVoyNo = JFormattedTextField()
    private val cbLastPort = JComboBox<SeaPortDto>()
    private val cbNextPort = JComboBox<SeaPortDto>()
    private val cbTZ = JComboBox<String>()

    init {

        tfVoyNo.setColumns(10)

        FormsUtils.initListTimeZone(cbTZ)
        cbTZ.newActionListener { newTZ ->
            noonPresenter.setTZ(newTZ)
        }

        FormsUtils.initListPorts(DataSourcePorts.getInstance(), cbLastPort, setPort = {}, setUnlocode = {}, setLatitude = {}, setLongitude = {})
        FormsUtils.initListPorts(DataSourcePorts.getInstance(), cbNextPort, setPort = {}, setUnlocode = {}, setLatitude = {}, setLongitude = {})


        inflateUI()

    }

    private fun inflateUI() {
        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("Voy"))
            .addNextCellFillBoth(tfVoyNo)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Time Zone GMT"))
            .addNextCellFillBoth(cbTZ)

        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("Last port"))
            .addNextCellFillBoth(cbLastPort)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Next port"))
            .addNextCellFillBoth(cbNextPort)
    }
}