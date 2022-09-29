package com.example.demo.core;

import com.example.demo.utils.GridBagHelper;
import javax.swing.*;
import java.awt.*;

public class SaveBtnPanel extends JPanel {

    private ClickSave clickSave;
    private ClickSaveAndSave clickSaveAndSave;

    public SaveBtnPanel() {

        super();

        GridBagHelper helper = new GridBagHelper();

        this.setLayout(new GridBagLayout());

        final JButton jbSaveReport = new JButton("SAVE");
        jbSaveReport.setVisible(true);
        jbSaveReport.addActionListener(e -> {
            if (clickSave != null) {
                clickSave.onClick();
            }
        });
        add(jbSaveReport, helper.nextCell().fillHorizontally().get());

        helper.nextEmptyCell(this, 50);
        helper.nextCell().fillHorizontally();

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
