package com.example.demo.core;

import java.io.Serializable;
import java.util.UUID;

public class Position implements Serializable {

    private TypePosition typePosition;
    private int degrees;
    private double time;
    private Hemisphere hemisphere;
    private String uid;

    public Position(TypePosition typePosition, int degrees, double time, Hemisphere hemisphere) {
        this.typePosition = typePosition;
        this.degrees = degrees;
        this.time = time;
        this.hemisphere = hemisphere;
        this.uid = UUID.randomUUID().toString();
    }

    public Position(TypePosition typePosition, int degrees, double time, Hemisphere hemisphere, String uid) {
        this.typePosition = typePosition;
        this.degrees = degrees;
        this.time = time;
        this.hemisphere = hemisphere;
        this.uid = uid;
    }

    public TypePosition getTypePosition() {
        return typePosition;
    }

    public void setTypePosition(TypePosition typePosition) {
        this.typePosition = typePosition;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Hemisphere getHemisphere() {
        return hemisphere;
    }

    public void setHemisphere(Hemisphere hemisphere) {
        this.hemisphere = hemisphere;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
//        42⁰38.7’N
          return degrees + "⁰" + time + "’" + hemisphere;
    }

    public enum TypePosition {
        Latitude, Longitude
    }

    public enum Hemisphere {
        N, S, E, W
    }
}
