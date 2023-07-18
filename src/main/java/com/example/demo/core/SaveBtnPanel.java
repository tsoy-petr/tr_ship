package com.example.demo.core;

import com.example.demo.utils.GridBagHelper;

import javax.swing.*;
import java.awt.*;

public class SaveBtnPanel extends JPanel {

    private GridBagHelper helper = new GridBagHelper();
    private Boolean saveVisible = true;
    private Boolean loadVisible = false;
    private Boolean saveAndSaveVisible = true;
    private Boolean sendVisible = true;
    private ClickSave clickSave;
    private ClickSaveAndSave clickSaveAndSave;
    private ClickSend clickSend;
    private ClickLoad clickLoad;

    public SaveBtnPanel(Boolean saveVisible, Boolean saveAndSaveVisible) {
        this();
        this.saveVisible = saveVisible;
        this.saveAndSaveVisible = saveAndSaveVisible;
        this.sendVisible = false;
        initView();
    }

    public SaveBtnPanel(Boolean saveVisible, Boolean saveAndSaveVisible, Boolean sendVisible) {
        this();
        this.saveVisible = saveVisible;
        this.saveAndSaveVisible = saveAndSaveVisible;
        this.sendVisible = sendVisible;
        initView();
    }

    public SaveBtnPanel(Boolean saveVisible, Boolean saveAndSaveVisible, Boolean sendVisible, Boolean loadVisible) {
        this();
        this.saveVisible = saveVisible;
        this.saveAndSaveVisible = saveAndSaveVisible;
        this.sendVisible = sendVisible;
        this.loadVisible = loadVisible;

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

        if (loadVisible) {

            helper.nextEmptyCell(this, 50);
            helper.nextCell().fillHorizontally();

            initLoadBtn();
        }

        if (saveAndSaveVisible) {

            helper.nextEmptyCell(this, 50);
            helper.nextCell().fillHorizontally();

            initSaveAndSendBtn();
        }

        if (sendVisible) {

            helper.nextEmptyCell(this, 50);
            helper.nextCell().fillHorizontally();

            initSendBtn();
        }

    }


    private void initSaveBtn() {
        final JButton jbSaveReport = new JButton("SAVE TO FILE");
        jbSaveReport.setVisible(true);
        jbSaveReport.addActionListener(e -> {
            if (clickSave != null) {
                clickSave.onClick();
            }
        });
        add(jbSaveReport, helper.nextCell().fillHorizontally().get());
    }

    private void initLoadBtn() {
        final JButton jbLoadReport = new JButton("LOAD FROM FILE");
        jbLoadReport.setVisible(true);
        jbLoadReport.addActionListener(e -> {
            if (clickLoad!= null) {
                clickLoad.onClick();
            }
        });
        add(jbLoadReport, helper.nextCell().fillHorizontally().get());
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

    private void initSendBtn() {
        final JButton jbSendMailApp = new JButton("SEND");
        jbSendMailApp.setVisible(true);
        jbSendMailApp.addActionListener(e -> {
            if (clickSend != null) {
                clickSend.onClick();
            }
        });
        add(jbSendMailApp, helper.fillHorizontally().get());
    }

    public void setClickSave(ClickSave clickSave) {
        this.clickSave = clickSave;
    }

    public void setClickSaveAndSave(ClickSaveAndSave clickSaveAndSave) {
        this.clickSaveAndSave = clickSaveAndSave;
    }

    public void setClickSend(ClickSend clickSend) {
        this.clickSend = clickSend;
    }

    public void setClickLoad(ClickLoad clickLoad) {
        this.clickLoad = clickLoad;
    }

    public interface ClickSave {
        void onClick();
    }

    public interface ClickLoad {
        void onClick();
    }

    public interface ClickSend {
        void onClick();
    }

    public interface ClickSaveAndSave {
        void onClick();
    }

}
