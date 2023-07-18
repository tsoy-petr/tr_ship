package com.example.demo.core;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FieldListener implements DocumentListener {

    private ChangeListener listener;

    public interface ChangeListener{
        public void change();
    }

    public FieldListener(ChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (listener != null) {
            listener.change();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if (listener != null) {
            listener.change();
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
