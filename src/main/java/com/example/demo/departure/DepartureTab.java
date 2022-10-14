package com.example.demo.departure;

import com.example.demo.SendEmailService;
import com.example.demo.core.*;
import com.example.demo.core.model.ResultSendDeparture;
import com.example.demo.model.SeaPortDto;
import com.example.demo.model.SettingsEmailDto;
import com.example.demo.model.TerminalDto;
import com.example.demo.service.SendReportAndSaveService;
import com.example.demo.settings.DataSettings;
import com.example.demo.settings.SettingsPresenter;
import com.example.demo.utils.FormatHelper;
import com.example.demo.utils.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class DepartureTab extends JPanel {

    WaitLayerUI layerUI = new WaitLayerUI(this.getBackground());

    private final GridBagHelper helper = new GridBagHelper();
    private DeparturePresenter departurePresenter;
    private final JFormattedTextField tfVoyNoDeparture = new JFormattedTextField();
    private final JLabel lbVoyNoDeparture = new JLabel("Voy. No.");
    private final JComboBox<String> cbTZDeparture = new JComboBox<>();
    private final JLabel lbTZDeparture = new JLabel("Time Zone GMT");
    private final JLabel lbPortDeparture = new JLabel("Port");
    private final JComboBox<SeaPortDto> cbPortDeparture = new JComboBox<>();
    private final JComboBox<TerminalDto> cbTerminalDeparture = new JComboBox<>();
    private final JLabel lbTypeReport1 = new JLabel("All clear");
    private final JFormattedTextField tfLSHFOROB = new JFormattedTextField(FormatHelper.getLSHFOFormatter());
    private final JFormattedTextField tfMGO_01_ROB = new JFormattedTextField(FormatHelper.getMGO_01_ROB_Formatter());
    private final JFormattedTextField tfMGO_05_ROB = new JFormattedTextField(FormatHelper.getMGO_05_ROB_Formatter());
    private final JFormattedTextField tfNoOfTugs = new JFormattedTextField(FormatHelper.getNoOfTugs_Formatter());

    private final JComboBox<SeaPortDto> cbNextPort = new JComboBox<>();
    private final JComboBox<SeaPortDto> cbLastPort = new JComboBox<>();

    final JTextArea tfNote = new JTextArea(3, 25);

    public DepartureTab(DeparturePresenter departurePresenter) {

        super();

        DataSettings.getInstance().addListener(new DataSettings.SettingsChangeListener() {
            @Override
            public void change(SettingsEmailDto settingsEmail) {
                if (settingsEmail != null) {
                    tfVoyNoDeparture.setText(settingsEmail.getVoyNo());
                }
            }
        });

        this.departurePresenter = departurePresenter;

        departurePresenter.addChangePortsListener(() -> {
            departurePresenter.initListPorts(cbPortDeparture, cbTerminalDeparture, cbTZDeparture);
            departurePresenter.initCBPorts(cbLastPort);
            departurePresenter.initCBPorts(cbNextPort);
        });

        setLayout(new GridBagLayout());

        setCurrData();

        initCommon();

        initSelectedTypeDeparture(departurePresenter);

        initFirstStatus(departurePresenter);

        initSecondStatus(departurePresenter);

        //новая строка
        helper.insertEmptyRow(this, 10);


        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(50, 50);
            }
        };
        panel.setBackground(this.getBackground());
        final JLayer<JPanel> jlayer = new JLayer<>(panel, layerUI);
        jlayer.setVisible(false);
        add(jlayer, helper.get());

        add(new JLabel("Note"), helper.nextCell().alignRight().gap(10).get());
        tfNote.getDocument().addDocumentListener(new FieldListener(new FieldListener.ChangeListener() {
            @Override
            public void change() {
                departurePresenter.setNote(tfNote.getText());
            }
        }));
        add(tfNote, helper.nextCell().get());

        //новая строка
        helper.insertEmptyRow(this, 10);

        final JButton jbSend = new JButton("SEND");
        jbSend.setVisible(true);
        jbSend.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                jbSend.setVisible(false);
                jlayer.setVisible(true);
                layerUI.start();

                SendEmailService service = new SendEmailService();

                SwingWorker<ResultSendDeparture, Void> worker = new SwingWorker<ResultSendDeparture, Void>() {
                    @Override
                    protected ResultSendDeparture doInBackground() {

                        return service.send(
                                (new SettingsPresenter()).readSettings(),
                                departurePresenter.mapToModelResponse()
                        );

                    }

                    @Override
                    protected void done() {
                        super.done();

                        try {

                            ResultSendDeparture result = get();

                            if (result.getSuccess()) {

                            } else {
                                JOptionPane.showMessageDialog(jbSend, result.getMessage());
                            }

                        } catch (InterruptedException | ExecutionException interruptedException) {
                            interruptedException.printStackTrace();
                        }

                        jlayer.setVisible(false);
                        layerUI.stop();
                        jbSend.setVisible(true);
                    }
                };

                worker.execute();

            }
        });
        //add(jbSend, helper.fillBoth().get());

        //новая строка
        helper.insertEmptyRow(this, 10);

        SaveBtnPanel saveBtnPanel = new SaveBtnPanel(true, true);
        add(saveBtnPanel, helper.span().get());
        saveBtnPanel.setClickSave(() -> {
            ResultSendDeparture result = (new SendReportAndSaveService()).saveDeparture(
                    (new SettingsPresenter()).readSettings(),
                    departurePresenter.mapToModelResponse()
            );
            if (! result.getSuccess()) {
                JOptionPane.showMessageDialog(jbSend, result.getMessage());
            }
        });
        saveBtnPanel.setClickSaveAndSave(() -> {
            ResultSendDeparture result = (new SendReportAndSaveService()).saveAndSandDeparture(
                    (new SettingsPresenter()).readSettings(),
                    departurePresenter.mapToModelResponse()
            );
            if (! result.getSuccess()) {
                JOptionPane.showMessageDialog(jbSend, result.getMessage());
            }
        });

    }

    private void initSelectedTypeDeparture(DeparturePresenter departurePresenter) {
        //новая строка
        helper.insertEmptyRow(this, 10);

        JRadioButton DepartureFromBerth = new JRadioButton("Departure from Berth");
        JRadioButton DepartureFromAnchorage = new JRadioButton("Departure from Anchorage");
        ButtonGroup bg = new ButtonGroup();
        bg.add(DepartureFromBerth);
        bg.add(DepartureFromAnchorage);
        bg.setSelected(DepartureFromBerth.getModel(), true);

        DepartureFromAnchorage.addActionListener(e -> {
                    departurePresenter.setDeparture(DeparturePresenter.DepartureState.ANCHORAGE);
                    lbTypeReport1.setText("Anchor aweigh");
                }
        );
        DepartureFromBerth.addActionListener(e -> {
            departurePresenter.setDeparture(DeparturePresenter.DepartureState.BERTH);
            lbTypeReport1.setText("All clear");
        });

        helper.nextEmptyCell(this, 10);
        add(DepartureFromBerth, helper.nextCell().get());

        helper.nextEmptyCell(this, 10);
        add(DepartureFromAnchorage, helper.nextCell().get());
        helper.nextEmptyCell(this, 10);
    }

    private void initCommon() {
        //отступ сверху
        helper.insertEmptyRow(this, 10);

        add(lbVoyNoDeparture, helper.nextCell().alignRight().gap(10).get());

        tfVoyNoDeparture.setColumns(10);
        add(tfVoyNoDeparture, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 30);

        add(lbTZDeparture, helper.nextCell().alignRight().gap(10).get());

        departurePresenter.initListTimeZone(cbTZDeparture);
        cbTZDeparture.addActionListener(e -> {
            try {
                String tz = (String) cbTZDeparture.getModel().getSelectedItem();
                if (tz != null) {
                    departurePresenter.setTimeZoneGMT(tz);
                }
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        });
        add(cbTZDeparture, helper.nextCell().fillBoth().get());

        //новая строка
        helper.insertEmptyRow(this, 10);

        add(lbPortDeparture, helper.nextCell().alignRight().gap(10).get());

        cbPortDeparture.setEditable(false);
        cbTerminalDeparture.setEditable(false);
        departurePresenter.initListPorts(cbPortDeparture, cbTerminalDeparture, cbTZDeparture);
        cbPortDeparture.addActionListener(e -> {

            switch (e.getActionCommand()) {
                case "comboBoxEdited":
                    cbTerminalDeparture.removeAllItems();
                    departurePresenter.setUnlocode((String) cbPortDeparture.getModel().getSelectedItem());
                    departurePresenter.setPort(null);
                    cbTerminalDeparture.setEditable(true);
                    break;
                case "comboBoxChanged":

                    cbTerminalDeparture.setEditable(false);
                    try {
                        SeaPortDto selectedSeaPort = (SeaPortDto) cbPortDeparture.getModel().getSelectedItem();
                        if (selectedSeaPort != null) {
                            cbTZDeparture.setSelectedItem(selectedSeaPort.getTimeZone());
                            departurePresenter.initListTerminals(cbTerminalDeparture, selectedSeaPort);
                            departurePresenter.setPort(selectedSeaPort);
                        }
                    } catch (ClassCastException ex) {
                        ex.printStackTrace();
                    }

                    break;
            }
        });
        add(cbPortDeparture, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 30);

        JLabel lbTerminalDeparture = new JLabel("Terminal");
        add(lbTerminalDeparture, helper.nextCell().alignRight().gap(10).get());

        cbTerminalDeparture.addActionListener(e -> {
            switch (e.getActionCommand()) {
                case "comboBoxEdited":
                    departurePresenter.setTerminalTitle((String) cbTerminalDeparture.getModel().getSelectedItem());
                    departurePresenter.setTerminal(null);
                    break;
                case "comboBoxChanged":

                    try {
                        TerminalDto selectedTerminal = (TerminalDto) cbTerminalDeparture.getModel().getSelectedItem();
                        departurePresenter.setTerminal(selectedTerminal);
                    } catch (ClassCastException ex) {
                        System.out.println(ex);
                    }

                    break;
            }
        });
        add(cbTerminalDeparture, helper.nextCell().fillBoth().get());
    }

    private void initFirstStatus(DeparturePresenter departurePresenter) {

        //новая строка
        helper.insertEmptyRow(this, 20);
        add(lbTypeReport1, helper.nextCell().alignRight().gap(10).get());
        add(new DateTimeBox(departurePresenter::setDateStatus1, departurePresenter::setTimeStatus1),
                helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 30);
        add(new JLabel("POB"), helper.nextCell().alignRight().gap(10).get());
        add(new DateTimeBox(departurePresenter::setDatePob, departurePresenter::setTimePob),
                helper.nextCell().fillBoth().get());


        //новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("LSHFO ROB (mt)"), helper.nextCell().alignRight().gap(10).get());
        add(tfLSHFOROB, helper.nextCell().fillHorizontally().get());
        tfLSHFOROB.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfLSHFOROB.getValue();
            if (currValue != null) {
                departurePresenter.setLSHFOROB_S1((Double) currValue);
            }
        }));
        //пропуск в виде одной ячейки
        helper.nextCell();
        add(new JLabel("Pilot off"), helper.nextCell().alignRight().gap(10).get());
        add(new DateTimeBox(departurePresenter::setDatePilotOff, departurePresenter::setTimePilotOff),
                helper.nextCell().fillHorizontally().get());


        //новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("MGO 0,1% ROB (mt)"), helper.nextCell().alignRight().gap(10).get());
        add(tfMGO_01_ROB, helper.nextCell().fillHorizontally().get());
        tfMGO_01_ROB.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfMGO_01_ROB.getValue();
            if (currValue != null) {
                departurePresenter.setMGO_01_ROB_S1((Double) currValue);
            }
        }));
        //пропуск в виде одной ячейки
        helper.nextCell();
        add(new JLabel("Tug make fast"), helper.nextCell().alignRight().gap(10).get());
        add(new DateTimeBox(departurePresenter::setDateTugMakeFast, departurePresenter::setTimeTugMakeFast),
                helper.nextCell().fillBoth().get());


        //новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("MGO 0,5% ROB (MT)"), helper.nextCell().alignRight().gap(10).get());
        add(tfMGO_05_ROB, helper.nextCell().fillHorizontally().get());
        tfMGO_05_ROB.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfMGO_05_ROB.getValue();
            if (currValue != null) {
                departurePresenter.setMGO_05_ROB_S1((Double) currValue);
            }
        }));
        //пропуск в виде одной ячейки
        helper.nextCell();
        add(new JLabel("Tug cast off"), helper.nextCell().alignRight().gap(10).get());
        add(new DateTimeBox(departurePresenter::setDateTugCastOff, departurePresenter::setTimeTugCastOff),
                helper.nextCell().fillBoth().get());

        //новая строка
        helper.insertEmptyRow(this, 10);
        helper.nextEmptyCell(this, 10);
        helper.nextEmptyCell(this, 10);
        helper.nextEmptyCell(this, 10);
        add(new JLabel("No of Tugs"), helper.nextCell().alignRight().gap(10).get());
        add(tfNoOfTugs, helper.nextCell().fillBoth().get());
        tfNoOfTugs.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfNoOfTugs.getValue();
            if (currValue != null) {
                departurePresenter.setNoOfTugs((Long) currValue);
            }
        }));
    }

    private void initSecondStatus(DeparturePresenter departurePresenter) {

        //новая строка
        helper.insertEmptyRow(this, 20);

        add(new JLabel("BOSP"), helper.nextCell().alignRight().gap(10).get());
        add(new DateTimeBox(departurePresenter::setDateStatus2, departurePresenter::setTimeStatus2),
                helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("Maneuvering Dist"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfManeuveringDist = new JFormattedTextField(FormatHelper.getManeuveringDist_Formatter());
        tfManeuveringDist.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfLSHFOROB.getValue();
            if (currValue != null) {
                departurePresenter.setLSHFOROB_S1((Double) currValue);
            }
        }));
        add(tfManeuveringDist, helper.nextCell().fillHorizontally().get());


//        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("Position Latitude"), helper.nextCell().alignRight().gap(10).get());
        PositionField positionFieldLatitude = new PositionField(new Position(Position.TypePosition.Latitude, 0, 0, Position.Hemisphere.N), departurePresenter::setLatitude);
        add(positionFieldLatitude, helper.nextCell().fillBoth().get());
//        //пропуск в виде одной ячейки
        helper.nextCell();
        add(new JLabel("Position Longitude"), helper.nextCell().alignRight().gap(10).get());
        PositionField positionFieldLongitude = new PositionField(new Position(Position.TypePosition.Longitude, 0, 0, Position.Hemisphere.E), departurePresenter::setLongitude);
        add(positionFieldLongitude, helper.nextCell().fillBoth().get());

        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("ME (RPM)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfME = new JFormattedTextField(FormatHelper.getMERPM());
        tfME.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfME.getValue();
            if (currValue != null) {
                departurePresenter.setMeRPM((Integer) currValue);
            }
        }));
        add(tfME, helper.nextCell().fillHorizontally().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("ME mode (Eco/Full)"), helper.nextCell().alignRight().gap(10).get());
        JComboBox<MEMode> cbMEMode = new JComboBox<>();
        cbMEMode.addItem(MEMode.ECO);
        cbMEMode.addItem(MEMode.FULL);
        cbMEMode.addActionListener(e -> {
            switch (e.getActionCommand()) {
                case "comboBoxEdited":
                    break;
                case "comboBoxChanged":
                    try {
                        MEMode currMeMode = (MEMode) cbMEMode.getModel().getSelectedItem();
                        departurePresenter.setMeMode(currMeMode);
                    } catch (ClassCastException ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        });
        add(cbMEMode, helper.nextCell().fillBoth().get());

        //новая строка - Last port Next port
        helper.insertEmptyRow(this, 10);
        add(new JLabel("Last  port"), helper.nextCell().alignRight().gap(10).get());
        cbLastPort.setEditable(false);
        departurePresenter.initCBPorts(cbLastPort);
        cbLastPort.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                SeaPortDto selectedSeaPort = (SeaPortDto) cbLastPort.getModel().getSelectedItem();
                if (selectedSeaPort != null) {
                    departurePresenter.setUnlocodeLastPort(selectedSeaPort.getUnlocode());
                }

            }
        });
        add(cbLastPort, helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("Next  port"), helper.nextCell().alignRight().gap(10).get());
        cbNextPort.setEditable(false);
        departurePresenter.initCBPorts(cbNextPort);
        cbNextPort.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                SeaPortDto selectedSeaPort = (SeaPortDto) cbNextPort.getModel().getSelectedItem();
                if (selectedSeaPort != null) {
                    departurePresenter.setUnlocodeNextPort(selectedSeaPort.getUnlocode());
                }

            }
        });
        add(cbNextPort, helper.nextCell().fillBoth().get());


        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("ETA Next Port (LT)"), helper.nextCell().alignRight().gap(10).get());
        add(new DateTimeBox(departurePresenter::setETA_Next_Port_date, departurePresenter::setETA_Next_Port_time),
                helper.nextCell().fillHorizontally().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("Distance to go (NM)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfDistanceToGo = new JFormattedTextField(FormatHelper.getDistanceToGo_Formatter());
        tfDistanceToGo.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfDistanceToGo.getValue();
            if (currValue != null) {
                departurePresenter.setDistanceToGo((Double) currValue);
            }
        }));
        add(tfDistanceToGo, helper.nextCell().fillHorizontally().get());

        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("LSHFO ROB (mt)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfLshfoROB_S2 = new JFormattedTextField(FormatHelper.getLshfoROB_Formatter());
        tfLshfoROB_S2.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfLshfoROB_S2.getValue();
            if (currValue != null) {
                departurePresenter.setLshfoROB_S2((Double) currValue);
            }
        }));
        add(tfLshfoROB_S2, helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("Fresh water (MT)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfFreshWater = new JFormattedTextField(FormatHelper.getFreshWater_Formatter());
        tfFreshWater.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfFreshWater.getValue();
            if (currValue != null) {
                departurePresenter.setFreshWater((Integer) currValue);
            }
        }));
        add(tfFreshWater, helper.nextCell().fillBoth().get());

        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("MGO 0,1% ROB (mt)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfMgo_01_ROB_S2 = new JFormattedTextField(FormatHelper.getMgo_01_ROB_Formatter());
        tfMgo_01_ROB_S2.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfMgo_01_ROB_S2.getValue();
            if (currValue != null) {
                departurePresenter.setMgo_01_ROB_S2((Double) currValue);
            }
        }));
        add(tfMgo_01_ROB_S2, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("MGO 0,5% ROB (MT)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfMgo_05_ROB_S2 = new JFormattedTextField(FormatHelper.getMgo_05_ROB_Formatter());
        tfMgo_05_ROB_S2.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfMgo_05_ROB_S2.getValue();
            if (currValue != null) {
                departurePresenter.setMgo_05_ROB_S2((double) currValue);
            }
        }));
        add(tfMgo_05_ROB_S2, helper.nextCell().fillBoth().get());


        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("Cargo on deck (MT)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfCargoOnDeck = new JFormattedTextField(FormatHelper.getCargoOnDesk_Formatter());
        tfCargoOnDeck.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfCargoOnDeck.getValue();
            if (currValue != null) {
                departurePresenter.setCargoOnDesk((Double) currValue);
            }
        }));
        add(tfCargoOnDeck, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("Cargo Holds (MT)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfCargoHolds = new JFormattedTextField(FormatHelper.getCargoHolds_Formatter());
        tfCargoHolds.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfCargoHolds.getValue();
            if (currValue != null) {
                departurePresenter.setCargoHolds((Double) currValue);
            }
        }));
        add(tfCargoHolds, helper.nextCell().fillBoth().get());


        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("20' Laden"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfLaden_20 = new JFormattedTextField(FormatHelper.getContainer_Formatter());
        tfLaden_20.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfLaden_20.getValue();
            if (currValue != null) {
                departurePresenter.setContainerLaden_20((Integer) currValue);
            }
        }));
        add(tfLaden_20, helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("20' Empty"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfEmpty_20 = new JFormattedTextField(FormatHelper.getContainer_Formatter());
        tfEmpty_20.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfEmpty_20.getValue();
            if (currValue != null) {
                departurePresenter.setContainerEmpty_20((Integer) currValue);
            }
        }));
        add(tfEmpty_20, helper.nextCell().fillBoth().get());

        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("40' Laden"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfLaden_40 = new JFormattedTextField(FormatHelper.getContainer_Formatter());
        tfLaden_40.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfLaden_40.getValue();
            if (currValue != null) {
                departurePresenter.setContainerLaden_40((Integer) currValue);
            }
        }));
        add(tfLaden_40, helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("40' Empty"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfEmpty_40 = new JFormattedTextField(FormatHelper.getContainer_Formatter());

        tfEmpty_40.getDocument().addDocumentListener(new FieldListener(new FieldListener.ChangeListener() {
            @Override
            public void change() {
                Object currValue = tfEmpty_40.getValue();
                if (currValue != null) {
                    departurePresenter.setContainerEmpty_40((Integer) currValue);
                }
            }
        }));
        add(tfEmpty_40, helper.nextCell().fillBoth().get());


        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("Draft FWD( m)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfDraftFWD = new JFormattedTextField(FormatHelper.getDraftFWD_Formatter());
        tfDraftFWD.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfDraftFWD.getValue();
            if (currValue != null) {
                departurePresenter.setDraftFWD((Double) currValue);
            }
        }));
        add(tfDraftFWD, helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("Ballast"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfBallast = new JFormattedTextField(FormatHelper.getBallast_Formatter());
        tfBallast.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfBallast.getValue();
            if (currValue != null) {
                departurePresenter.setBallast((Integer) currValue);
            }
        }));
        add(tfBallast, helper.nextCell().fillBoth().get());

        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("Draft Aft (m)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfDraftAft = new JFormattedTextField(FormatHelper.getDraftAft_Formatter());
        tfDraftAft.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfDraftAft.getValue();
            if (currValue != null) {
                departurePresenter.setDraftAft((Double) currValue);
            }
        }));
        add(tfDraftAft, helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("GM(m)"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfGM = new JFormattedTextField(FormatHelper.getGM_Formatter());
        tfGM.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfGM.getValue();
            if (currValue != null) {
                departurePresenter.setGm((Double) currValue);
            }
        }));
        add(tfGM, helper.nextCell().fillBoth().get());


        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("DWT"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfDWT = new JFormattedTextField(FormatHelper.getDWT_Formatter());
        tfDWT.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfDWT.getValue();
            if (currValue != null) {
                departurePresenter.setDwt((Integer) currValue);
            }
        }));
        add(tfDWT, helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("FW received"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfFWReceived = new JFormattedTextField(FormatHelper.getFreshWater_Formatter());
        tfFWReceived.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfFWReceived.getValue();
            if (currValue != null) {
                departurePresenter.setFreshWaterReceived((Integer) currValue);
            }
        }));
        add(tfFWReceived, helper.nextCell().fillBoth().get());


        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("Displacement"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfDisplacement = new JFormattedTextField(FormatHelper.getDisplacement_Formatter());
        tfDisplacement.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfDisplacement.getValue();
            if (currValue != null) {
                departurePresenter.setDisplacement((Integer) currValue);
            }
        }));
        add(tfDisplacement, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("Live Reefers"), helper.nextCell().alignRight().gap(10).get());
        JFormattedTextField tfLiveReefers = new JFormattedTextField(FormatHelper.getLiveReefers_Formatter());
        tfLiveReefers.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfLiveReefers.getValue();
            if (currValue != null) {
                departurePresenter.setLiveReefers((Integer) currValue);
            }
        }));
        add(tfLiveReefers, helper.nextCell().fillBoth().get());

////        новая строка
//        helper.insertEmptyRow(this, 10);
//        add(new JLabel("Note"), helper.nextCell().alignRight().gap(10).get());
//        JTextArea tfNote = new JTextArea(3,25);
//        add(tfNote, helper.nextCell().fillHorizontally().get());
    }

    private void setCurrData() {
        SettingsPresenter settingsPresenter = new SettingsPresenter();
        tfVoyNoDeparture.setText(settingsPresenter.readSettings().getVoyNo());
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 650);

        frame.add(new DepartureTab(new DeparturePresenter()));
        frame.setVisible(true);
    }

}
