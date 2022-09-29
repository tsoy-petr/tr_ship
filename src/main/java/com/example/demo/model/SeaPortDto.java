package com.example.demo.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SeaPortDto implements Serializable
{

    private String title;
    private String unlocode;
    private String timeZone;
    private List<TerminalDto> terminals = new ArrayList<>();

    public SeaPortDto(String title, String unlocode) {
        this.title = title;
        this.unlocode = unlocode;
        terminals = new ArrayList<>();
    }

    public SeaPortDto(String title, String unlocode, List<TerminalDto> terminals) {
        this.title = title;
        this.unlocode = unlocode;
        this.terminals = terminals;
    }

    public SeaPortDto(String title, String unlocode, String timeZone, List<TerminalDto> terminals) {
        this.title = title;
        this.unlocode = unlocode;
        this.timeZone = timeZone;
        this.terminals = terminals;
    }

    public SeaPortDto(String title, String unlocode, String timeZone) {
        this.title = title;
        this.unlocode = unlocode;
        this.timeZone = timeZone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnlocode() {
        return unlocode;
    }

    public void setUnlocode(String unlocode) {
        this.unlocode = unlocode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<TerminalDto> getTerminals() {
        if (terminals == null) {
            terminals = new ArrayList<>();
        }
        return terminals;
    }

    public void setTerminals(List<TerminalDto> terminals) {
        this.terminals = terminals;
    }

    @Override
    public String toString() {
        return this.unlocode;
    }
}
