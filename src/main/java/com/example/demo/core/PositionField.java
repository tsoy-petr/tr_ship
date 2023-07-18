package com.example.demo.core;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                        setPositionWithReaction(position);
//                        PositionField.this.position = position;
//                        listener.result(position);
//
//                        PositionField.this.positionJT.setText(position.toString());
                    }
                });

                modelDialog.setVisible(true);

            }
        });
    }

    public void setPositionWithReaction(Position position){
        setPosition(position);
        if(listener != null) {
            listener.result(position);
        }
    }


    public void reset(){
        if (this.position.getTypePosition() == Position.TypePosition.Latitude) {
            this.position = new Position(Position.TypePosition.Latitude, 0, 0, Position.Hemisphere.N);
        } else {
            this.position = new Position(Position.TypePosition.Longitude, 0, 0, Position.Hemisphere.E);
        }
        this.positionJT.setText("");
    }

    private void setupFields() {

        setLayout(new FlowLayout());
        add(this.positionJT);
        add(this.openButton);

    }

    public void setPosition(Position position) {
        if (position == null) {
            reset();
        } else {
            this.position = position;
        }
        this.positionJT.setText(this.position.toString());
    }

    public interface ResultListener {
        void result(Position position);
    }

}
