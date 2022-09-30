package com.example.demo.departure;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.optionalusertools.TimeChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.TimeChangeEvent;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeBox extends JPanel {

    private DateChangeLister dateChangeLister;
    private TimeChangeLister timeChangeLister;
    private DatePicker date = new DatePicker();
    private TimePicker timePicker = new TimePicker();

    public DateTimeBox() {

        super();

        setupFields();

    }


    public DateTimeBox(DateChangeLister dateChangeLister, TimeChangeLister timeChangeLister){

        super();

        setupFields();

        this.dateChangeLister = dateChangeLister;
        this.timeChangeLister = timeChangeLister;

    }

    private void setupFields() {

        setLayout(new FlowLayout());

        add(date);
        add(timePicker);

        date.addDateChangeListener(dateChangeEvent -> {
            if (dateChangeLister != null) {
                dateChangeLister.change(dateChangeEvent.getNewDate());
            }
        });

        timePicker.addTimeChangeListener(timeChangeEvent -> {
            if (timeChangeLister != null) {
                timeChangeLister.change(timeChangeEvent.getNewTime());
            }
        });

    }

    public DateChangeLister getDateChangeLister() {
        return dateChangeLister;
    }

    public void setDateChangeLister(DateChangeLister dateChangeLister) {
        this.dateChangeLister = dateChangeLister;
    }

    public TimeChangeLister getTimeChangeLister() {
        return timeChangeLister;
    }

    public void setTimeChangeLister(TimeChangeLister timeChangeLister) {
        this.timeChangeLister = timeChangeLister;
    }
}

