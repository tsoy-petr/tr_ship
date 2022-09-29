package com.example.demo.dataPorts;

import com.example.demo.model.SeaPortDto;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class PortTableModel extends AbstractTableModel {

    List<SeaPortDto> ports = new ArrayList<>();

    public PortTableModel(List<SeaPortDto> ports) {
        this.ports = ports;
    }

    @Override
    public int getRowCount() {
        return this.ports.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 1: return String.class;
            case 2: return String.class;
            default: return String.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 1: return "Title";
            case 2: return "UNLOCODE";
            default: return "Time zone";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        SeaPortDto port = ports.get(rowIndex);

        switch (columnIndex) {
            case 0: return port.getTitle();
            case 1: return port.getUnlocode();
            case 2: return port.getTimeZone();
        }
        return "Не определена";
    }
}
