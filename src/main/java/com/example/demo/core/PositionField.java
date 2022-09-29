package com.example.demo.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalTime;

public class PositionField extends JPanel {

    private JButton openButton;
    private JTextField positionJT;

    private Position position;

    private ResultListener listener;

    public PositionField(Position position, ResultListener listener) {

        super();

        this.position = position;
        this.listener = listener;

        initComponents();

        setupFields();

    }

    private void initComponents() {
        this.openButton = new JButton();
        this.openButton.setText("...");
        this.openButton.setFocusPainted(false);
        this.openButton.setFocusable(false);
        this.positionJT = new JTextField();
        this.positionJT.setEditable(false);
        this.positionJT.setColumns(15);
        this.positionJT.setText(position.toString());
        intiOpenBtnListener(this.openButton);
    }

    private void intiOpenBtnListener(Component openComponentButton) {
        openComponentButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                final JDialog modelDialog = DialogPosition.getInstance(PositionField.this.position, new DialogPosition.PositionChangeListener() {
                    @Override
                    public void positionChange(Position position) {
                        PositionField.this.position = position;
                        listener.result(position);

                        PositionField.this.positionJT.setText(position.toString());
                    }
                });

                modelDialog.setVisible(true);

            }
        });
    }

    private void setupFields() {

        setLayout(new FlowLayout());
        add(this.positionJT);
        add(this.openButton);

    }

    public interface ResultListener {
        public void result(Position position);
    }

}
