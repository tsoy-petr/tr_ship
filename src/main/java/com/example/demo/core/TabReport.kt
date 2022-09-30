package com.example.demo.core

import com.example.demo.utils.GridBagHelper
import java.awt.Component
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JPanel

abstract class TabReport: JPanel() {
    val helper = GridBagHelper()
    init {
        layout = GridBagLayout()
    }
    fun insertEmptyRow(newHeight: Int = 10) : TabReport{
        helper.insertEmptyRow(this, newHeight)
        return this
    }

    fun nextEmptyCell(newSize: Int = 30) : TabReport {
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, newSize)
        return  this
    }

    fun addNextCellAlignRightGap(newComponent: Component, newGap: Int = 10) : TabReport {
        add(newComponent, helper.nextCell().alignRight().gap(newGap).get())
        return this
    }

    fun addNextCellFillBoth(newComponent: Component): TabReport {
        add(newComponent, helper.nextCell().fillBoth().get())
        return this
    }
}