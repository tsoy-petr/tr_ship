package com.example.demo.core;

import com.example.demo.model.SeaPortDto;
import com.example.demo.model.TerminalDto;
import com.example.demo.utils.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogEnterNewTerminal extends JDialog {

    private TerminalDto currTerminal;
    private ResultListener listener;
    private JTextField jtTitle = new JTextField(25);

    public DialogEnterNewTerminal(TerminalDto currTerminal, ResultListener listener) {

        this.currTerminal = currTerminal;
        this.listener = listener;

        if (this.currTerminal != null) {
            jtTitle.setText(this.currTerminal.getTitle());
        }

    }

    public static DialogEnterNewTerminal getInstance(ResultListener listener, TerminalDto terminal) {

        JFrame frame = new JFrame("ADD new port");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DialogEnterNewTerminal modelDialog = new DialogEnterNewTerminal(terminal, listener);
        modelDialog.setTitle("Add new terminal");
        modelDialog.setBounds(132, 132, 350, 120);
        Container dialogContainer = modelDialog.getContentPane();

        SpringLayout layout = new SpringLayout();
        dialogContainer.setLayout(layout);

        JLabel lbTitle = new JLabel("Title");
        dialogContainer.add(lbTitle);
        layout.putConstraint(SpringLayout.WEST , lbTitle, 10,
                SpringLayout.WEST , dialogContainer);
        layout.putConstraint(SpringLayout.NORTH, lbTitle, 10,
                SpringLayout.NORTH, dialogContainer);

        dialogContainer.add(modelDialog.jtTitle);
        layout.putConstraint(SpringLayout.WEST , modelDialog.jtTitle, 10,
                SpringLayout.EAST , lbTitle);
        layout.putConstraint(SpringLayout.NORTH, modelDialog.jtTitle, 0,
                SpringLayout.NORTH, lbTitle);

        JButton jbSave = new JButton("Save");
        jbSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String titleTerminal = modelDialog.jtTitle.getText().trim();

                if (!titleTerminal.isEmpty()) {

                    if (listener != null) {
                        listener.resultAction(new TerminalDto(titleTerminal), modelDialog.currTerminal);
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
        layout.putConstraint(SpringLayout.NORTH, jbSave, 10,
                SpringLayout.SOUTH, lbTitle);

        return modelDialog;

//        dialogContainer.setLayout(new GridBagLayout());
//        GridBagHelper helper = new GridBagHelper();
//
//        JLabel lbTitle = new JLabel("Title");
//        dialogContainer.add(lbTitle, helper.nextCell().fillBoth().get());
//
//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//        dialogContainer.add(modelDialog.jtTitle, helper.nextCell().fillBoth().get());
//
//        //новая строка
//        helper.insertEmptyRow(dialogContainer, 10);
//        JButton jbSave = new JButton("Save");
//        jbSave.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                String titleTerminal= modelDialog.jtTitle.getText().trim();
//
//                if (!titleTerminal.isEmpty()) {
//
//                    if (listener != null) {
//                        listener.resultAction(new TerminalDto(titleTerminal), modelDialog.currTerminal);
//                    }
//
//                    modelDialog.setVisible(false);
//                }
//            }
//        });
//        dialogContainer.add(jbSave, helper.nextCell().fillBoth().get());
//
//        return modelDialog;

    }

    private void initView() {



    }

    public interface ResultListener {
        public void resultAction(TerminalDto newTerminal, TerminalDto currTerminal);
    }
}
