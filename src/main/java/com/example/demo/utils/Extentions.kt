package com.example.demo.utils

import javax.swing.JComboBox

fun JComboBox<String>.newActionListener(onResult: (String) -> Unit) {
    this.addActionListener { _ ->
        try {
            val item: String = this.model.selectedItem as String
            onResult(item)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}