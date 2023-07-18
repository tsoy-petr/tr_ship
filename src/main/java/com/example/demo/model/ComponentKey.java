package com.example.demo.model;

import com.example.demo.core.NoValidData;

import javax.swing.*;

public class ComponentKey {

    public JComponent component;
    public NoValidData noValidData;

    public ComponentKey(JComponent component, NoValidData noValidData) {
        this.component = component;
        this.noValidData = noValidData;
    }
}
