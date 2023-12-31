package com.example.demo;


import com.example.demo.anchorDrift.AnchorDriftPresenter;
import com.example.demo.anchorDrift.AnchorDriftTab;
import com.example.demo.arrival.ArrivalPresenter;
import com.example.demo.arrival.ArrivalTab;
import com.example.demo.bunker.BunkerPresenter;
import com.example.demo.bunker.BunkerTab;
import com.example.demo.core.JTabbedPaneRouting;
import com.example.demo.dataPorts.DataPortsPresenter;
import com.example.demo.dataPorts.DataPortsTab;
import com.example.demo.departure.DeparturePresenter;
import com.example.demo.departure.DepartureTab;
import com.example.demo.model.SeaPortDto;
import com.example.demo.model.SettingsEmailDto;
import com.example.demo.model.TerminalDto;
import com.example.demo.noon.NoonPresenter;
import com.example.demo.noon.NoonTab;
import com.example.demo.settings.SettingsPresenter;
import com.example.demo.settings.SettingsTab;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class HelloApplication {

    DeparturePresenter departurePresenter = new DeparturePresenter();
    SettingsPresenter settingsPresenter = new SettingsPresenter();
    SendEmailService sendEmailService = new SendEmailService();
    private JFormattedTextField tfVoyNoDeparture;
    private JLabel lbVoyNoDeparture;
    private JComboBox<String> cbTZDeparture;
    private JLabel lbTZDeparture;
    private JLabel lbPortDeparture;
    private JComboBox<SeaPortDto> cbPortDeparture;
    private JLabel lbTerminalDeparture;
    private JComboBox<TerminalDto> cbTerminalDeparture;

    public void initWindow() {

        JFrame frame = new JFrame("Tranzit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 1000);

        JTabbedPaneRouting tabbedPane = new JTabbedPaneRouting();

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                int selectedIndex = tabbedPane.getSelectedIndex();

                if (selectedIndex == 1) {
                    SettingsEmailDto settingsEmailDto = settingsPresenter.readSettings();
                }

            }
        });
        tabbedPane.addTab("DEPARTURE", initDer(tabbedPane));
        tabbedPane.addTab("NOON", initNoonReport(tabbedPane));
        tabbedPane.addTab("ANCHOR/DRIFT", initAnchorDriftReport(tabbedPane));
        tabbedPane.addTab("ARRIVAL", initArrivalReport(tabbedPane));
        tabbedPane.addTab("BUNKER", initBunkerReport(tabbedPane));
        tabbedPane.addTab("SETTINGS", initSett());
        tabbedPane.addTab("Data ports", initDataPorts());
        frame.add(BorderLayout.CENTER, new JScrollPane(tabbedPane));

        frame.setVisible(true);

    }

    private JPanel initDer(JTabbedPaneRouting paneRouting) {
        DeparturePresenter departurePresenter = new DeparturePresenter();
        JPanel tab = new JPanel();
        tab.setLayout(new FlowLayout());
        tab.add(new DepartureTab(departurePresenter, paneRouting));
        return tab;
    }

    private JPanel initNoonReport(JTabbedPaneRouting paneRouting) {
        JPanel tab = new JPanel();
        tab.setLayout(new FlowLayout());
        tab.add(new NoonTab(new NoonPresenter(paneRouting)));
        return tab;
    }

    private JPanel initAnchorDriftReport(JTabbedPaneRouting paneRouting) {
        JPanel tab = new JPanel();
        tab.setLayout(new FlowLayout());
        tab.add(new AnchorDriftTab(new AnchorDriftPresenter(paneRouting)));
        return tab;
    }

    private JPanel initArrivalReport(JTabbedPaneRouting paneRouting) {
        JPanel tab = new JPanel();
        tab.setLayout(new FlowLayout());
        tab.add(new ArrivalTab(new ArrivalPresenter(paneRouting)));
        return tab;
    }

    private JPanel initBunkerReport(JTabbedPaneRouting paneRouting) {
        JPanel tab = new JPanel();
        tab.setLayout(new FlowLayout());
        tab.add(new BunkerTab(new BunkerPresenter(paneRouting)));
        return tab;
    }

    private JPanel initSett() {
        JPanel tab = new JPanel();
        tab.setLayout(new FlowLayout());
        tab.add((new SettingsTab(new SettingsPresenter())));
        return tab;
    }

    private JPanel initDataPorts() {
        JPanel tab = new JPanel();
        tab.setLayout(new FlowLayout());
        tab.add((new DataPortsTab(new DataPortsPresenter())));
        return tab;
    }

    private JPanel initDepartureTab() {

        JPanel tab = new JPanel();
        tab.setLayout(new FlowLayout());

        JPanel panel = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        lbVoyNoDeparture = new JLabel("Voy. No.");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lbVoyNoDeparture, gbc);

        tfVoyNoDeparture = new JFormattedTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(tfVoyNoDeparture, gbc);

        addSeparatorGrid(panel, gbc, 2, 0);

        lbTZDeparture = new JLabel("Time Zone GMT");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 3;
        gbc.gridy = 0;
        panel.add(lbTZDeparture, gbc);

        cbTZDeparture = new JComboBox<>();
        departurePresenter.initListTimeZone(cbTZDeparture);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 4;
        gbc.gridy = 0;
        panel.add(cbTZDeparture, gbc);

        addSeparatorGrid(panel, gbc, 0, 2);

        lbPortDeparture = new JLabel("Port");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lbPortDeparture, gbc);

        cbPortDeparture = new JComboBox<SeaPortDto>();
        cbPortDeparture.setEditable(true);
        departurePresenter.initListPorts(cbPortDeparture, cbTerminalDeparture, cbTZDeparture);
        cbPortDeparture.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println(e);
            }
        });
        cbPortDeparture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (((JComboBox<SeaPortDto>) e.getSource()).getActionCommand().equals("comboBoxEdited")) {
                    cbTerminalDeparture.removeAllItems();
                } else if (((JComboBox<SeaPortDto>) e.getSource()).getActionCommand().equals("comboBoxChanged")) {

                    try {
                        SeaPortDto selectedSeaPort = (SeaPortDto) cbPortDeparture.getModel().getSelectedItem();
                        cbTZDeparture.setSelectedItem(selectedSeaPort.getTimeZone());
                        departurePresenter.initListTerminals(cbTerminalDeparture, selectedSeaPort);
                    } catch (ClassCastException ex) {
                        System.out.println(ex);
                    }

                }
            }
        });
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(cbPortDeparture, gbc);

        addSeparatorGrid(panel, gbc, 2, 3);

        lbTerminalDeparture = new JLabel("Terminal");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 3;
        gbc.gridy = 3;
        panel.add(lbTerminalDeparture, gbc);

        cbTerminalDeparture = new JComboBox<TerminalDto>();
        cbTerminalDeparture.setEditable(true);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 4;
        gbc.gridy = 3;
        panel.add(cbTerminalDeparture, gbc);

        addSeparatorGrid(panel, gbc, 2, 4);

        JRadioButton DepartureFromBerth = new JRadioButton("Departure from Berth");
        JRadioButton DepartureFromAnchorage = new JRadioButton("Departure from Anchorage");
        ButtonGroup bg = new ButtonGroup();
        bg.add(DepartureFromBerth);
        bg.add(DepartureFromAnchorage);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(DepartureFromBerth, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 3;
        gbc.gridy = 5;
        panel.add(DepartureFromAnchorage, gbc);

        addSeparatorGrid(panel, gbc, 2, 6);


        tab.add(panel);
        return tab;

    }

    private void addSeparatorGrid(JPanel panel, GridBagConstraints gbc, int gridx, int gridy) {
        JPanel sep = new JPanel();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(sep, gbc);
    }

    public static void main(String[] args) {

        HelloApplication app = new HelloApplication();
        app.initWindow();

    }
}