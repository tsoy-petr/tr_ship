package com.example.demo.core;

import com.example.demo.model.SeaPortDto;
import com.example.demo.utils.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogEnterNewPort extends JDialog {

    private SeaPortDto currPort;
    private ResultListener listener;
    private JTextField jtTitle = new JTextField(25);
    private JTextField jtUNLOCODE = new JTextField(25);
    private JComboBox<String> cbTZ = new JComboBox<>();

    private DialogEnterNewPort(){
        initTimeZone();
    }

    public DialogEnterNewPort(ResultListener listener) {

        this();

        this.listener = listener;
        jtTitle.setColumns(50);
        jtUNLOCODE.setColumns(25);


    }

    public DialogEnterNewPort(SeaPortDto currPort, ResultListener listener) {

        this();

        this.currPort = currPort;
        this.listener = listener;

        if (this.currPort != null) {
            jtTitle.setText(this.currPort.getTitle());
            jtUNLOCODE.setText(this.currPort.getUnlocode());
            cbTZ.setSelectedItem(this.currPort.getTimeZone());
        }

    }

    private void initTimeZone() {
        for (int i = 0; i <= 12; i++) {
            if (i == 0) {
                cbTZ.addItem("0");
            } else {
                cbTZ.addItem("+" + i);
            }
        }

        for (int i = 1; i <= 12; i++) {
            cbTZ.addItem("-" + i);
        }
    }

    public static DialogEnterNewPort getInstance(ResultListener listener, SeaPortDto port) {

        JFrame frame = new JFrame("ADD new port");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DialogEnterNewPort modelDialog = new DialogEnterNewPort(port, listener);
        modelDialog.setTitle("ADD new port");
        modelDialog.setBounds(132, 132, 390, 220);
        Container dialogContainer = modelDialog.getContentPane();

        SpringLayout layout = new SpringLayout();
        dialogContainer.setLayout(layout);

        JLabel lbTitle = new JLabel("Title");
        JLabel lbUNLOCODE = new JLabel("UNLOCODE");
        JLabel lbTimeZone = new JLabel("Time zone");

        dialogContainer.add(lbTitle);
        dialogContainer.add(modelDialog.jtTitle);

        dialogContainer.add(lbUNLOCODE);
        dialogContainer.add(modelDialog.jtUNLOCODE);

        dialogContainer.add(lbTimeZone);
        dialogContainer.add(modelDialog.cbTZ);

        layout.putConstraint(SpringLayout.WEST , lbTitle, 10,
                SpringLayout.WEST , dialogContainer);
        layout.putConstraint(SpringLayout.NORTH, lbTitle, 25,
                SpringLayout.NORTH, dialogContainer);

        layout.putConstraint(SpringLayout.NORTH, modelDialog.jtTitle, 25,
                SpringLayout.NORTH, dialogContainer);
        layout.putConstraint(SpringLayout.WEST , modelDialog.jtTitle, 80,
                SpringLayout.WEST , dialogContainer );



        layout.putConstraint(SpringLayout.WEST , lbUNLOCODE, 10,
                SpringLayout.WEST , dialogContainer);
        layout.putConstraint(SpringLayout.NORTH, lbUNLOCODE, 20,
                SpringLayout.SOUTH, lbTitle);

        layout.putConstraint(SpringLayout.NORTH, modelDialog.jtUNLOCODE, 0,
                SpringLayout.NORTH, lbUNLOCODE);
        layout.putConstraint(SpringLayout.WEST , modelDialog.jtUNLOCODE, 80,
                SpringLayout.WEST , dialogContainer );



        layout.putConstraint(SpringLayout.WEST , lbTimeZone, 10,
                SpringLayout.WEST , dialogContainer);
        layout.putConstraint(SpringLayout.NORTH, lbTimeZone, 20,
                SpringLayout.SOUTH, lbUNLOCODE);

        layout.putConstraint(SpringLayout.NORTH, modelDialog.cbTZ, -5,
                SpringLayout.NORTH, lbTimeZone);
        layout.putConstraint(SpringLayout.WEST , modelDialog.cbTZ, 80,
                SpringLayout.WEST , dialogContainer );


        JButton jbSave = new JButton("Save");
        jbSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String titlePort = modelDialog.jtTitle.getText().trim();
                String unlocode = modelDialog.jtUNLOCODE.getText().trim();

                String timeZone = (String) modelDialog.cbTZ.getModel().getSelectedItem();

                if (!titlePort.isEmpty() && !unlocode.isEmpty() && !timeZone.isEmpty()) {

                    if (listener != null) {
                        listener.resultAction(new SeaPortDto(titlePort, unlocode, timeZone), modelDialog.currPort);
                    }

                    modelDialog.setVisible(false);
                }
            }
        });
        dialogContainer.add(jbSave);
        layout.putConstraint(SpringLayout.WEST , jbSave, 10,
                SpringLayout.WEST , dialogContainer);
        layout.putConstraint(SpringLayout.EAST , jbSave, -10,
                SpringLayout.EAST , dialogContainer);
        layout.putConstraint(SpringLayout.NORTH, jbSave, 20,
                SpringLayout.SOUTH, lbTimeZone);

//        dialogContainer.setLayout(new GridBagLayout());
//
//        SpringLayout layout = new SpringLayout();
//
//        GridBagHelper helper = new GridBagHelper();
//
//        JLabel lbTitle = new JLabel("Title");
//        dialogContainer.add(lbTitle, helper.nextCell().fillBoth().get());
//
//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//        dialogContainer.add(modelDialog.jtTitle, helper.nextCell().get());
//
//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//        JLabel lbUNLOCODE = new JLabel("UNLOCODE");
//        dialogContainer.add(lbUNLOCODE, helper.nextCell().get());
//
//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//        dialogContainer.add(modelDialog.jtUNLOCODE, helper.nextCell().get());
//
//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//        JLabel lbTimeZone = new JLabel("Time zone");
//        dialogContainer.add(lbTimeZone, helper.nextCell().fillHorizontally().get());
//
//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//        dialogContainer.add(modelDialog.cbTZ, helper.nextCell().get());
//
//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//        JButton jbSave = new JButton("Save");
//        jbSave.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                String titlePort = modelDialog.jtTitle.getText().trim();
//                String unlocode = modelDialog.jtUNLOCODE.getText().trim();
//
//                String timeZone = (String)modelDialog.cbTZ.getModel().getSelectedItem();
//
//                if (!titlePort.isEmpty() && !unlocode.isEmpty() && !timeZone.isEmpty()) {
//
//                    if (listener != null) {
//                        listener.resultAction(new SeaPortDto(titlePort, unlocode, timeZone), modelDialog.currPort);
//                    }
//
//                    modelDialog.setVisible(false);
//                }
//            }
//        });
//        dialogContainer.add(jbSave, helper.nextCell().fillBoth().get());

        return modelDialog;

    }

    private void initView() {



    }

    public interface ResultListener {
        public void resultAction(SeaPortDto newPort, SeaPortDto currPort);
    }
}
