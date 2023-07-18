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

    public void resetTime() {
        date.setDate(null);
        timePicker.setTime(null);
    }

    public void setDateTime(String dateStr, String timeStr) {

        LocalDate localDate = null;
        LocalTime localTime = null;

        if (!dateStr.isEmpty()) {
            localDate = LocalDate.parse(dateStr);
        }

        if (!timeStr.isEmpty()) {
            localTime = LocalTime.parse(timeStr);
        }
        date.setDate(localDate);
        timePicker.setTime(localTime);
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

