package com.example.demo.core;

import com.example.demo.utils.GridBagHelper;
import javax.swing.*;
import java.awt.*;
import java.util.BitSet;

public class SaveBtnPanel extends JPanel {

    private GridBagHelper helper = new GridBagHelper();
    private Boolean saveVisible = true;
    private Boolean saveAndSaveVisible = true;
    private ClickSave clickSave;
    private ClickSaveAndSave clickSaveAndSave;


    public SaveBtnPanel(Boolean saveVisible, Boolean saveAndSaveVisible) {
        this();
        this.saveVisible = saveVisible;
        this.saveAndSaveVisible = saveAndSaveVisible;
        initView();
    }
    public SaveBtnPanel() {
        super();
        this.setLayout(new GridBagLayout());
    }

    private void initView() {
        if (saveVisible) {
            initSaveBtn();
        }

        if (saveAndSaveVisible) {

            helper.nextEmptyCell(this, 50);
            helper.nextCell().fillHorizontally();

            initSaveAndSendBtn();
        }
    }


    private void initSaveBtn() {
        final JButton jbSaveReport = new JButton("SAVE");
        jbSaveReport.setVisible(true);
        jbSaveReport.addActionListener(e -> {
            if (clickSave != null) {
                clickSave.onClick();
            }
        });
        add(jbSaveReport, helper.nextCell().fillHorizontally().get());
    }

    private void initSaveAndSendBtn() {
        final JButton jbSaveAndSendMailApp = new JButton("SEND AND SAVE");
        jbSaveAndSendMailApp.setVisible(true);
        jbSaveAndSendMailApp.addActionListener(e -> {
            if (clickSaveAndSave != null) {
                clickSaveAndSave.onClick();
            }
        });
        add(jbSaveAndSendMailApp, helper.fillHorizontally().get());
    }

    public void setClickSave(ClickSave clickSave) {
        this.clickSave = clickSave;
    }

    public void setClickSaveAndSave(ClickSaveAndSave clickSaveAndSave) {
        this.clickSaveAndSave = clickSaveAndSave;
    }

    public interface ClickSave {
        void onClick();
    }

    public interface ClickSaveAndSave {
        void onClick();
    }

}
