package com.example.demo.settings;

import com.example.demo.core.FolderPicker;
import com.example.demo.model.SettingsEmailDto;
import com.example.demo.utils.GridBagHelper;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsTab extends JPanel {

    JFormattedTextField tfSmtpServer;
    JFormattedTextField tfSmtpPort;
    JFormattedTextField tfUser;
    JPasswordField tfPass;
    JFormattedTextField tfEmail;
    JFormattedTextField tfServerEmail;
    JFormattedTextField tfImo;
    JFormattedTextField tfVoyNo;
    FolderPicker tfFolderForSavingReports;
    JFormattedTextField tfEmailCopy1;
    JFormattedTextField tfEmailCopy2;
    JFormattedTextField tfEmailCopy3;

    private GridBagHelper helper = new GridBagHelper();
    SettingsPresenter presenter;

    public SettingsTab(SettingsPresenter presenter) {

        super();

        this.presenter = presenter;

        setLayout(new GridBagLayout());

        //отступ сверху
        helper.insertEmptyRow(this, 10);

//        JLabel lbSmtpServer = new JLabel("SMTP");
//        add(lbSmtpServer, helper.nextCell().alignRight().gap(10).get());

        tfSmtpServer = new JFormattedTextField();
        tfSmtpServer.setColumns(10);
//        add(tfSmtpServer, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
//        helper.nextEmptyCell(this, 30);

//        JLabel lbSmtpPort = new JLabel("Port");
//        add(lbSmtpPort, helper.nextCell().alignRight().gap(10).get());

        tfSmtpPort = new JFormattedTextField();
        tfSmtpPort.setColumns(10);
//        add(tfSmtpPort, helper.nextCell().fillBoth().get());

        //разделитель
//        helper.insertEmptyRow(this, 10);

//        JLabel lbUser = new JLabel("User");
//        add(lbUser, helper.nextCell().alignRight().gap(10).get());

        tfUser = new JFormattedTextField();
        tfUser.setColumns(10);
//        add(tfUser, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
//        helper.nextEmptyCell(this, 30);

//        JLabel lbPass = new JLabel("Password");
//        add(lbPass, helper.nextCell().alignRight().gap(10).get());
//
        tfPass = new JPasswordField();
        tfPass.setColumns(10);
//        add(tfPass, helper.nextCell().fillBoth().get());
//
//        //разделитель
//        helper.insertEmptyRow(this, 10);
//
//        JLabel lbEmail = new JLabel("EMAIL");
//        add(lbEmail, helper.nextCell().alignLeft().get());
//        helper.insertEmptyRow(this, 1);
        tfEmail = new JFormattedTextField();
//        add(tfEmail, helper.fillHorizontally().get());

        //разделитель
        helper.insertEmptyRow(this, 10);

        JLabel lbServerEmail = new JLabel("SERVER EMAIL");
        add(lbServerEmail, helper.nextCell().alignLeft().get());
        helper.insertEmptyRow(this, 1);
        tfServerEmail = new JFormattedTextField();
        add(tfServerEmail, helper.fillHorizontally().get());

        //разделитель
        helper.insertEmptyRow(this, 10);

        JLabel lbToCopyEmail = new JLabel("Copy to");
        add(lbToCopyEmail, helper.nextCell().alignLeft().get());
        helper.insertEmptyRow(this, 1);

        tfEmailCopy1 = new JFormattedTextField();
        add(tfEmailCopy1, helper.fillHorizontally().get());
        helper.insertEmptyRow(this, 3);
        tfEmailCopy2 = new JFormattedTextField();
        add(tfEmailCopy2, helper.fillHorizontally().get());
        helper.insertEmptyRow(this, 3);
        tfEmailCopy3 = new JFormattedTextField();
        add(tfEmailCopy3, helper.fillHorizontally().get());

        //разделитель
        helper.insertEmptyRow(this, 10);

        JLabel lbFolderForSavingReports = new JLabel("Folder for saving reports");
        add(lbFolderForSavingReports, helper.nextCell().alignLeft().get());
        helper.insertEmptyRow(this, 1);
        tfFolderForSavingReports = new FolderPicker();
        add(tfFolderForSavingReports, helper.fillHorizontally().get());

        helper.insertEmptyRow(this, 30);

        JLabel lbIMO = new JLabel("IMO");
        add(lbIMO, helper.nextCell().alignRight().gap(10).get());

        tfImo = new JFormattedTextField();
        tfImo.setColumns(10);
        add(tfImo, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 30);

        JLabel lbVoyNo = new JLabel("Voy. No");
        add(lbVoyNo, helper.nextCell().alignRight().gap(10).get());

        tfVoyNo = new JFormattedTextField();
        tfVoyNo.setColumns(10);
        add(tfVoyNo, helper.nextCell().fillBoth().get());

        //разделитель
        helper.insertEmptyRow(this, 10);

        JButton jbSend = new JButton("SAVE");
        jbSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        add(jbSend, helper.fillHorizontally().get());

        loadData();

    }

    private void saveData() {

        presenter.saveSettings(
                new SettingsEmailDto(
                        tfSmtpServer.getText().trim(),
                        Integer.parseInt(tfSmtpPort.getText().trim()),
                        tfEmail.getText().trim(),
                        tfUser.getText().trim(),
                        tfPass.getText().trim(),
                        tfImo.getText().trim(),
                        tfVoyNo.getText().trim(),
                        tfServerEmail.getText().trim(), tfFolderForSavingReports.getPath(),
                        tfEmailCopy1.getText().trim(),
                        tfEmailCopy2.getText().trim(),
                        tfEmailCopy3.getText().trim())
        );

    }

    private void loadData() {

        SettingsEmailDto data = presenter.readSettings();

        tfSmtpServer.setText(data.getSmtpServer());
        tfSmtpPort.setText(String.valueOf(data.getSmtpPort()));
        tfUser.setText(data.getUser());
        tfPass.setText(data.getPasswordEmail());
        tfEmail.setText(data.getEmail());
        tfImo.setText(data.getImo());
        tfVoyNo.setText(data.getVoyNo());
        tfServerEmail.setText(data.getServerEmail());
        tfFolderForSavingReports.setPath(data.getFolderForSavingReports());
        tfEmailCopy1.setText(data.getEmailCopy1());
        tfEmailCopy2.setText(data.getEmailCopy2());
        tfEmailCopy3.setText(data.getEmailCopy3());

    }
}
