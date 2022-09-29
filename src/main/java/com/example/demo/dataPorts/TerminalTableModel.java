package com.example.demo.dataPorts;

import com.example.demo.model.SeaPortDto;
import com.example.demo.model.TerminalDto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TerminalTableModel extends AbstractTableModel {

    List<TerminalDto> terminals = new ArrayList<>();

    public TerminalTableModel(List<TerminalDto> terminals) {
        this.terminals = terminals;
    }

    @Override
    public int getRowCount() {
        return terminals.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 1: return String.class;
            default: return String.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 1: return "Title";
            default: return "Title";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        TerminalDto terminal = terminals.get(rowIndex);

        switch (columnIndex) {
            case 0: return terminal.getTitle();
        }
        return "Не определена";
    }
}
