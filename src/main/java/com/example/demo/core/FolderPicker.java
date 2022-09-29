package com.example.demo.core;

import com.privatejgoodies.forms.factories.CC;
import com.privatejgoodies.forms.layout.FormLayout;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FolderPicker extends JPanel {

    private JTextField explorerTextField;
    private JButton chooseButton;

    private String path;

    public FolderPicker() {

        this.explorerTextField = new JTextField();
        this.explorerTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                FolderPicker.this.path = FolderPicker.this.explorerTextField.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                FolderPicker.this.path = FolderPicker.this.explorerTextField.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                FolderPicker.this.path = FolderPicker.this.explorerTextField.getText();
            }
        });

        this.chooseButton = new JButton();

        this.setLayout(new FormLayout("pref:grow, [3px,pref], [26px,pref]", "fill:pref:grow"));

        this.add(this.explorerTextField, CC.xy(1, 1));

        this.chooseButton.setText("...");
        this.chooseButton.setFocusPainted(false);
        this.chooseButton.setFocusable(false);
        this.chooseButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showFileFolder();
            }
        });
        this.add(this.chooseButton, CC.xy(3, 1));

    }

    private void showFileFolder() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        // Если файл выбран, покажем его в сообщении
        if (result == JFileChooser.APPROVE_OPTION ){
            File file = fileChooser.getSelectedFile();
            this.setPath(file.getAbsolutePath());
            System.out.println(result);

        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.explorerTextField.setText(path);
    }
}
