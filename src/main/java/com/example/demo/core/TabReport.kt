package com.example.demo.core

import com.example.demo.model.ComponentKey
import com.example.demo.utils.GridBagHelper
import com.google.gson.JsonElement
import java.awt.Color
import java.awt.Component
import java.awt.GridBagLayout
import java.util.*
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.border.Border
import kotlin.collections.HashMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

abstract class TabReport : JPanel() {

    internal val elementWithError: HashMap<ComponentKey, Border> = HashMap()

    private val helper = GridBagHelper()

    init {
        layout = GridBagLayout()
    }

    abstract fun loadFromJson(json: JsonElement)

    fun insertEmptyRow(newHeight: Int = 10): TabReport {
        helper.insertEmptyRow(this, newHeight)
        return this
    }

    fun nextEmptyCell(newSize: Int = 30): TabReport {
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, newSize)
        return this
    }

    fun addNextCellAlignRightGap(newComponent: Component, newGap: Int = 10): TabReport {
        add(newComponent, helper.nextCell().alignRight().gap(newGap).get())
        return this
    }

    fun addNextCellFillBoth(newComponent: Component): TabReport {
        add(newComponent, helper.nextCell().fillBoth().get())
        return this
    }

    fun addNextCell(newComponent: Component): TabReport {
        add(newComponent, helper.nextCell().get())
        return this
    }

    fun addSpan(newComponent: Component): TabReport {
        add(newComponent, helper.span().get())
        return this
    }

    inline fun <reified C> castToType(currValue: Any?, setValueToPresenter: (C) -> Unit) {
        if (currValue != null && currValue is C) {
            setValueToPresenter(currValue as C)
        }
    }

    internal fun handleFieldsError(noValidData: ArrayList<NoValidData>) {
        elementWithError.forEach { (componentKey: ComponentKey, border: Border?) ->
            if (noValidData.contains(componentKey.noValidData)) {
                componentKey.component.border = BorderFactory.createLineBorder(Color.red, 4, true)
            } else {
                componentKey.component.border = border
            }
        }
    }
}