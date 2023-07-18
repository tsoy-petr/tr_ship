package com.example.demo.model;

import com.example.demo.core.Position;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class TerminalDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String title;
    private Position latitude;
    private Position longitude;
    private String uid;

    public TerminalDto(String title) {
        this.title = title;
        this.uid = UUID.randomUUID().toString();
    }

    public TerminalDto(String title, Position latitude, Position longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid = UUID.randomUUID().toString();
    }


    public TerminalDto(String title, Position latitude, Position longitude, String uid) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Position getLatitude() {
        return latitude;
    }

    public void setLatitude(Position latitude) {
        this.latitude = latitude;
    }

    public Position getLongitude() {
        return longitude;
    }

    public void setLongitude(Position longitude) {
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalDto that = (TerminalDto) o;
        return Objects.equals(title, that.title)
                && Objects.equals(latitude, that.latitude)
                && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, latitude, longitude);
    }
}
