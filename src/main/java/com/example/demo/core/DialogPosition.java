package com.example.demo.core;

import com.example.demo.utils.FormatHelper;
import com.example.demo.utils.GridBagHelper;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DialogPosition extends JDialog {

    private Position position;
    private PositionChangeListener listener;

    public DialogPosition(JFrame frame, String title, Position position, PositionChangeListener listener) {
        super(frame, title, Dialog.ModalityType.DOCUMENT_MODAL);
        this.position = position;
        this.listener = listener;
    }

    public static DialogPosition getInstance(Position position, PositionChangeListener listener) {

        String title = "";
        List<Position.Hemisphere> listHemisphere = new ArrayList<>();
        if (position.getTypePosition() == Position.TypePosition.Latitude) {
            title = "Position Latitude";
            listHemisphere.add(Position.Hemisphere.N);
            listHemisphere.add(Position.Hemisphere.S);
        } else {
            title = "Position Longitude";
            listHemisphere.add(Position.Hemisphere.E);
            listHemisphere.add(Position.Hemisphere.W);
        }
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DialogPosition modelDialog = new DialogPosition(frame, title, position, listener);
        modelDialog.setBounds(132, 132, 300, 100);
        Container dialogContainer = modelDialog.getContentPane();
        dialogContainer.setLayout(new GridBagLayout());
        GridBagHelper helper = new GridBagHelper();

//        helper.insertEmptyRow(dialogContainer, 20);

        JFormattedTextField tfDegrees = new JFormattedTextField(selectDegreesFormatter(position.getTypePosition()));
        tfDegrees.setColumns(2);
        tfDegrees.setText(Integer.toString(position.getDegrees()));
        tfDegrees.getDocument().addDocumentListener(new FieldListener(() -> {
            String text = tfDegrees.getText();
            try {
                int currDegrees = Integer.parseInt(text);
                Position currPosition = modelDialog.getPosition();
                currPosition.setDegrees(currDegrees);
            } catch (Exception e) {

            }
//            Object currValue = tfDegrees.getValue();
//            if (currValue != null) {
//                try {
//                    int currDegrees = (Integer) currValue;
//                    Position currPosition = modelDialog.getPosition();
//                    currPosition.setDegrees(currDegrees);
//                    //modelDialog.setPosition(currPosition);
//                } catch (ClassCastException exception) {
//                    exception.printStackTrace();
//                }
//            }
        }));
        dialogContainer.add(tfDegrees, helper.nextCell().fillBoth().get());

        dialogContainer.add(new JLabel("⁰"), helper.nextCell().alignRight().gap(10).get());

        JFormattedTextField tfTime = new JFormattedTextField(FormatHelper.getTimePosition_Formatter());
        tfTime.setColumns(3);
        tfTime.setText(Double.toString(position.getTime()));
        tfTime.getDocument().addDocumentListener(new FieldListener(() -> {

           String text = tfTime.getText();

           try {
               double currDegrees = Double.parseDouble(text);
               Position currPosition = modelDialog.getPosition();
               currPosition.setTime(currDegrees);
           } catch (Exception e) {

           }
//            Object currValue = tfTime.getValue();
//            if (currValue != null) {
//                try {
//                    double currDegrees = (Double) currValue;
//                    Position currPosition = modelDialog.getPosition();
//                    currPosition.setTime(currDegrees);
//                    //modelDialog.setPosition(currPosition);
//                } catch (ClassCastException exception) {
//                    exception.printStackTrace();
//                }
//            }

        }));
        dialogContainer.add(tfTime, helper.nextCell().fillBoth().get());

        dialogContainer.add(new JLabel("’"), helper.nextCell().alignRight().gap(10).get());

        JComboBox<Position.Hemisphere> hemisphereCB = new JComboBox<>();
        hemisphereCB.setEditable(false);
        for (Position.Hemisphere el :
                listHemisphere) {
            hemisphereCB.addItem(el);
        }
        hemisphereCB.setSelectedItem(listHemisphere.get(0));
        hemisphereCB.addActionListener(e -> {
            switch (e.getActionCommand()) {
                case "comboBoxEdited":
                    break;
                case "comboBoxChanged":
                    Position currPosition = modelDialog.getPosition();
                    currPosition.setHemisphere((Position.Hemisphere) hemisphereCB.getModel().getSelectedItem());
                    //modelDialog.setPosition(currPosition);
                    break;
                default:
                    break;
            }
        });
        dialogContainer.add(hemisphereCB, helper.nextCell().fillBoth().get());

        helper.nextEmptyCell(dialogContainer, 50);

        JButton okJB = new JButton("OK");
        okJB.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Position currPosition = modelDialog.getPosition();
                modelDialog.setPosition(currPosition);
                modelDialog.setVisible(false);
            }
        });
        dialogContainer.add(okJB, helper.nextCell().fillBoth().get());


//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//
//        JButton okJB = new JButton("OK");
//        okJB.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                modelDialog.setVisible(false);
//            }
//        });
//        dialogContainer.add(okJB, helper.fillHorizontally().get());

        return modelDialog;
    }

    private static NumberFormatter selectDegreesFormatter(Position.TypePosition typePosition) {
        if (typePosition == Position.TypePosition.Latitude) {
            return FormatHelper.getDegrees_latitude_Formatter();
        } else {
            return FormatHelper.getDegrees_longitude_Formatter();
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        this.listener.positionChange(position);
    }

    public interface PositionChangeListener {
        public void positionChange(Position position);
    }
}
