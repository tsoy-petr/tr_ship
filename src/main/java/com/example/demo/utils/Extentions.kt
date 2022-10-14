package com.example.demo.utils

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JComboBox

fun <E : Any?> JComboBox<E>.newActionListener(onResult: (E, action: ActionEvent) -> Unit, actionListener: ActionListener? = null) {

    if (actionListener == null) {
        this.addActionListener { e: ActionEvent ->
            try {
                (model.selectedItem as E)?.let { onResult(it, e) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    } else {
        this.addActionListener(actionListener)
    }

}

fun <E : Any?> JComboBox<E>.initItems(items: List<E>, actionPresenter: (E) -> Unit) {
    items.forEach { item ->
        this.addItem(item)
    }
    this.selectedItem = items.first()
    actionPresenter(items.first())
}

//inline fun <reified E>JFormattedTextField.setDocumentChangeListener(crossinline execute: (E, action: () -> Unit) -> Unit) {
//    this.document.addDocumentListener(FieldListener {
//        if (this.value != null && this.value is E) {
//            execute(this.value as E)
//        }
//    })
//}