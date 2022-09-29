package com.example.demo.dataPorts;

import com.example.demo.core.DialogEnterNewPort;
import com.example.demo.core.DialogEnterNewTerminal;
import com.example.demo.model.SeaPortDto;
import com.example.demo.model.TerminalDto;
import com.example.demo.utils.GridBagHelper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DataPortsTab extends JPanel {

    final JTable tablePorts = new JTable();
    final JTable tableTerminals = new JTable();

    private JButton jbAddTerminal = new JButton("ADD");
    private JButton jbDelPort = new JButton("DEL");
    private JButton jbDelTerminal = new JButton("DEL");
    private SeaPortDto currPort;
    private TerminalDto currTerminal;

    private DataPortsPresenter presenter;
    private final GridBagHelper helper = new GridBagHelper();
    private List<SeaPortDto> ports = new ArrayList<>();

    public DataPortsTab(DataPortsPresenter presenter) {

        this.presenter = presenter;

        setLayout(new GridBagLayout());

        ports = presenter.getPorts();

        initView();

        presenter.dataSource.addListener(new DataSourcePorts.PortsChangeListener() {
            @Override
            public void change() {

                ports = presenter.getPorts();

                currPort = null;
                currTerminal = null;

                initModelPortTable();
                initTableTerminals();
            }
        });

    }

    private void initView() {

        //отступ сверху
        helper.insertEmptyRow(this, 10);

        JPanel panelBtnPortsTable = new JPanel();
        panelBtnPortsTable.setLayout(new FlowLayout());

        JButton jbAddPort = new JButton("ADD");
        jbAddPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialogEnterNewPort(true);
            }
        });
        panelBtnPortsTable.add(jbAddPort);

        jbDelPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currPort != null) {
                    ports.remove(currPort);
                    currPort = null;
                    initModelPortTable();
                    initTableTerminals();
                    savePorts();
                }
            }
        });
        jbDelPort.setVisible(false);
        panelBtnPortsTable.add(jbDelPort);

        add(panelBtnPortsTable, helper.nextCell().get());

        JPanel panelBtnTerminalsTable = new JPanel();
        panelBtnTerminalsTable.setLayout(new FlowLayout());
        jbAddTerminal.setVisible(false);
        jbAddTerminal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialogAddNewTerminal(true);
            }
        });
        jbDelTerminal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currPort != null && currTerminal != null) {
                    currPort.getTerminals().remove(currTerminal);
                    currTerminal = null;
                    initTableTerminals();
                    savePorts();
                }
            }
        });
        jbDelTerminal.setVisible(false);
        panelBtnTerminalsTable.add(jbAddTerminal);
        panelBtnTerminalsTable.add(jbDelTerminal);
        add(panelBtnTerminalsTable, helper.nextCell().get());

        helper.insertEmptyRow(this, 10);
        add(new JLabel("PORTS"), helper.nextCell().get());
        add(new JLabel("TERMINALS"), helper.nextCell().get());

        helper.insertEmptyRow(this, 10);
        initModelPortTable();
        tablePorts.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePorts.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int currRow = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {

                    currPort = ports.get(currRow);
                    if (currPort != null) {
                        showDialogEnterNewPort(false);
                    }

                    jbAddTerminal.setVisible(false);
                    jbDelPort.setVisible(false);

                } else if (mouseEvent.getClickCount() == 1 && table.getSelectedRow() != -1) {
                    currPort = ports.get(currRow);
                    if (currPort != null) {
                        jbAddTerminal.setVisible(true);
                        jbDelPort.setVisible(true);
                        initTableTerminals();
                    }
                }
            }
        });

        tableTerminals.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int currRow = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {

                    jbDelTerminal.setVisible(false);

                    if (currPort != null
                            && currRow <= currPort.getTerminals().size() - 1) {
                        currTerminal = currPort.getTerminals().get(currRow);
                        jbDelTerminal.setVisible(true);

                        showDialogAddNewTerminal(false);
                    }

                } else if (mouseEvent.getClickCount() == 1 && table.getSelectedRow() != -1) {
                    jbDelTerminal.setVisible(false);
                    if (currPort != null
                            && currRow <= currPort.getTerminals().size() - 1) {
                        currTerminal = currPort.getTerminals().get(currRow);
                        jbDelTerminal.setVisible(true);
                    }
                }
            }
        });

        add(new JScrollPane(tablePorts), helper.nextCell().fillBoth().get());
        add(new JScrollPane(tableTerminals), helper.nextCell().fillBoth().get());

        //новая строка
        helper.insertEmptyRow(this, 10);

        JButton jButtonCFile = new JButton("Reload ports (from file)");
        jButtonCFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowFileChooser();
            }
        });
        add(jButtonCFile, helper.nextCell().fillHorizontally().get());
    }

    private void ShowFileChooser() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JSON", "json");
        fileChooser.setFileFilter(filter);
        // Определение режима - только файл
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        // Если файл выбран, покажем его в сообщении
        if (result == JFileChooser.APPROVE_OPTION ){
            presenter.saveFromFile(fileChooser.getSelectedFile());
//            JOptionPane.showMessageDialog(this,
//                    "Выбран файл ( " +
//                            fileChooser.getSelectedFile() + " )");
        }

    }

    private void showDialogAddNewTerminal(Boolean isAdd) {

        if (isAdd) {
            currTerminal = null;
        }

        final JDialog modelDialog = DialogEnterNewTerminal.getInstance(new DialogEnterNewTerminal.ResultListener() {
            @Override
            public void resultAction(TerminalDto newTerminal, TerminalDto oldTerminal) {
                if (currPort != null) {
                    if (oldTerminal != null) {
                        for (TerminalDto terminal :
                                currPort.getTerminals()) {
                            if (terminal.getTitle().equals(oldTerminal.getTitle())) {
                                terminal.setTitle(newTerminal.getTitle());
                                break;
                            }
                        }
                    } else {
                        currPort.getTerminals().add(newTerminal);
                    }
                    savePorts();
                    initTableTerminals();
                }
            }
        }, currTerminal);
        modelDialog.setVisible(true);
    }

    private void savePorts() {
        presenter.savePorts(ports);
    }

    private void showDialogEnterNewPort(Boolean isAdd) {

        if (isAdd) {
            currPort = null;
        }

        final JDialog modelDialog = DialogEnterNewPort.getInstance(new DialogEnterNewPort.ResultListener() {
            @Override
            public void resultAction(SeaPortDto newPort, SeaPortDto oldPort) {
                if (oldPort != null) {
                    for (SeaPortDto port : ports) {
                        if (port.getUnlocode().equals(oldPort.getUnlocode())) {
                            port.setTitle(newPort.getTitle());
                            port.setUnlocode(newPort.getUnlocode());
                            port.setTimeZone(newPort.getTimeZone());
                            break;
                        }
                    }
                } else {
                    ports.add(newPort);
                }

                savePorts();
                initModelPortTable();
            }
        }, currPort);
        modelDialog.setVisible(true);
    }

    private void initModelPortTable() {
        PortTableModel tableModel = new PortTableModel(ports);
        tablePorts.setModel(tableModel);
    }

    private void initTableTerminals() {
        TerminalTableModel tableModel;
        if (currPort != null) {
            tableModel = new TerminalTableModel(currPort.getTerminals());
        } else {
            tableModel = new TerminalTableModel(new ArrayList<>());
            jbDelTerminal.setVisible(false);
        }
        tableTerminals.setModel(tableModel);
    }
}
