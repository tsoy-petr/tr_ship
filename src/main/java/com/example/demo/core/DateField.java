package com.example.demo.core;

import com.example.demo.departure.DateChangeLister;
import com.example.demo.departure.DateTimeBox;
import com.example.demo.departure.TimeChangeLister;
import com.example.demo.utils.GridBagHelper;

import javax.swing.*;
import java.awt.*;

public class DateField extends JPanel {

    private GridBagHelper helper = new GridBagHelper();
    private JLabel jltitle;
    private DateTimeBox dateTimeBox;

    public DateField(String title, DateChangeLister dateChangeLister, TimeChangeLister timeChangeLister) {
        super();

        setLayout(new GridBagLayout());

        jltitle = new JLabel(title);
        dateTimeBox = new DateTimeBox(dateChangeLister, timeChangeLister);

        add(jltitle, helper.nextCell().alignRight().gap(10).get());
        add(dateTimeBox, helper.nextCell().fillBoth().get());

    }

}
