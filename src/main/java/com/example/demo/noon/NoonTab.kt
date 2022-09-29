package com.example.demo.noon

import com.example.demo.model.SeaPortDto
import com.example.demo.utils.GridBagHelper
import java.awt.GridBagLayout
import javax.swing.JComboBox
import javax.swing.JFormattedTextField
import javax.swing.JLabel
import javax.swing.JPanel

class NoonTab(
    private val noonPresenter: NoonPresenter
) : JPanel() {

    private val helper = GridBagHelper()
    private val tfVoyNo = JFormattedTextField()
    private val cbLastPort = JComboBox<SeaPortDto>()
    private val cbNextPort = JComboBox<SeaPortDto>()


    init {
        layout = GridBagLayout()


        //отступ сверху
        helper.insertEmptyRow(this, 10)

        add(JLabel("Voy"))
    }
}