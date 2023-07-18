package com.example.demo.utils;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FormatHelper {

    public static NumberFormatter getLSHFOFormatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(4);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0.0);
        formatter.setMaximum(2000.0);
        return formatter;
    }

    public static NumberFormatter getMGO_01_ROB_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0.0);
        formatter.setMaximum(500.0);
        return formatter;
    }

    public static NumberFormatter getMGO_05_ROB_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0.0);
        formatter.setMaximum(300.0);
        return formatter;
    }

    public static NumberFormatter getNoOfTugs_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMaximumIntegerDigits(10);

        return new NumberFormatter(format);
    }

    public static NumberFormatter getManeuveringDist_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMaximumIntegerDigits(7);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        return formatter;
    }

    public static NumberFormatter getDegrees_latitude_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(2);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0);
        formatter.setMaximum(89);
        return formatter;
    }

    public static NumberFormatter getDegrees_longitude_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0);
        formatter.setMaximum(179);
        return formatter;
    }

    public static NumberFormatter getTimePosition_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMaximumIntegerDigits(2);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(59.9);
        return formatter;
    }

    public static NumberFormatter getDistanceToGo_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMaximumIntegerDigits(5);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(15000.0);
        return formatter;
    }

    public static NumberFormatter getLshfoROB_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(4);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.00);
        formatter.setMaximum(2000.00);
        return formatter;
    }

    public static NumberFormatter getMgo_01_ROB_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.00);
        formatter.setMaximum(500.00);
        return formatter;
    }

    public static NumberFormatter getMgo_05_ROB_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.00);
        formatter.setMaximum(300.00);
        return formatter;
    }

    public static NumberFormatter getCargoOnDesk_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(5);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.00);
        formatter.setMaximum(20000.00);
        return formatter;
    }

    public static NumberFormatter getCargoHolds_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(5);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.00);
        formatter.setMaximum(30000.00);
        return formatter;
    }

    public static NumberFormatter getContainer_Formatter() {

        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(4);


        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(1000);

        return formatter;
    }

    public static NumberFormatter getDraftFWD_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMaximumIntegerDigits(2);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(15.0);
        return formatter;
    }

    public static NumberFormatter getBallast_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(5);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(20000);
        return formatter;
    }

    public static NumberFormatter getDraftAft_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMaximumIntegerDigits(2);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(15.0);
        return formatter;
    }

    public static NumberFormatter getGM_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(4);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.40);
        formatter.setMaximum(4.00);
        return formatter;
    }

    public static NumberFormatter getDWT_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(5);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(50000);
        return formatter;
    }

    public static NumberFormatter getDisplacement_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(6);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(100000);
        return formatter;
    }

    public static NumberFormatter getLiveReefers_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(500);
        return formatter;
    }


    public static NumberFormatter getFreshWater_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(300);
        return formatter;
    }

    public static NumberFormatter getMERPM() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(550);
        return formatter;
    }

    public static NumberFormatter getSeaPassageDistance_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMaximumIntegerDigits(10);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        return formatter;
    }

    public static NumberFormatter getCourse_Formatter() {

        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(3);


        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(360);

        return formatter;
    }

    public static NumberFormatter getHFO_ROB_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(4);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0.0);
        formatter.setMaximum(2000.0);
        return formatter;
    }

    public static NumberFormatter getSpeed_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(1);
        format.setMaximumIntegerDigits(2);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(0.0);
        formatter.setMaximum(20.0);
        return formatter;
    }

    public static NumberFormatter getWindScaleBeaufourt_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(2);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(12);
        return formatter;
    }

    public static NumberFormatter getWindDirection_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(360);
        return formatter;
    }

    public static NumberFormatter getSwellDirection_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(3);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(360);
        return formatter;
    }

    public static NumberFormatter getSwellHeight_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMaximumIntegerDigits(2);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(10);
        return formatter;
    }

    public static NumberFormatter getReceivedFuel_Formatter() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMaximumIntegerDigits(15);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        return formatter;
    }
}
