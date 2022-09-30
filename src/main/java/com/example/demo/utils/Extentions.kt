package com.example.demo.utils

import java.awt.event.ActionEvent
import javax.swing.JComboBox

fun <E : Any?> JComboBox<E>.newActionListener(onResult: (E, action: ActionEvent) -> Unit) {
    this.addActionListener { e ->
        try {
            val item: E = this.model.selectedItem as E
            onResult(item, e)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}