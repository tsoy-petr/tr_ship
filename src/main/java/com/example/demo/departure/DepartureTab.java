package com.example.demo.departure;

import com.example.demo.SendEmailService;
import com.example.demo.core.*;
import com.example.demo.core.model.ResultSendDeparture;
import com.example.demo.dataPorts.DataSourcePorts;
import com.example.demo.mapping.DepartureToPostBody;
import com.example.demo.model.ComponentKey;
import com.example.demo.model.DepartureResponse;
import com.example.demo.model.SeaPortDto;
import com.example.demo.model.TerminalDto;
import com.example.demo.service.SendReportAndSaveService;
import com.example.demo.settings.DataSettings;
import com.example.demo.settings.SettingsPresenter;
import com.example.demo.utils.FormatHelper;
import com.example.demo.utils.GridBagHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DepartureTab extends TabReport {

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

    private PositionField positionFieldLatitude;
    private PositionField positionFieldLongitude;

    private final DateTimeBox dateTime_1;
    private final DateTimeBox dateTime_2;

    private final Map<ComponentKey, Border> elementWithError = new HashMap();

    private JTabbedPaneRouting paneRouting;
    private JRadioButton departureFromBerth;
    private JRadioButton departureFromAnchorage;
    private DateTimeBox dateTime_POB;
    private DateTimeBox dateTimePilotOff;
    private DateTimeBox dateTimeTugMakeFast;
    private DateTimeBox dateTimeTugCastOff;
    private JFormattedTextField tfManeuveringDist;
    private JFormattedTextField tfME;
    private JComboBox<MEMode> cbMEMode;
    private DateTimeBox dateTimeETANextPortLT;
    private JFormattedTextField tfDistanceToGo;
    private JFormattedTextField tfLshfoROB_s2;
    private JFormattedTextField tfFreshWater;
    private JFormattedTextField tfMgo_01_rob_s2;
    private JFormattedTextField tfMgo_05_rob_s2;
    private JFormattedTextField tfCargoOnDeck;
    private JFormattedTextField tfCargoHolds;
    private JFormattedTextField tfLaden_20;
    private JFormattedTextField tfLaden_40;
    private JFormattedTextField tfEmpty_20;
    private JFormattedTextField tfEmpty_40;
    private JFormattedTextField tfDraftFWD;
    private JFormattedTextField tfBallast;
    private JFormattedTextField tfDraftAft;
    private JFormattedTextField tfGM;
    private JFormattedTextField tfDWT;
    private JFormattedTextField tfFWReceived;
    private JFormattedTextField tfDisplacement;
    private JFormattedTextField tfLiveReefers;

    public DepartureTab(DeparturePresenter departurePresenter, JTabbedPaneRouting paneRouting) {

        super();

        this.paneRouting = paneRouting;

        DataSettings.getInstance().addListener(settingsEmail -> {
            if (settingsEmail != null) {
                tfVoyNoDeparture.setText(settingsEmail.getVoyNo());
            }
        });

        this.departurePresenter = departurePresenter;

        dateTime_1 = new DateTimeBox(departurePresenter::setDateStatus1, departurePresenter::setTimeStatus1);
        dateTime_2 = new DateTimeBox(departurePresenter::setDateStatus2, departurePresenter::setTimeStatus2);
        dateTime_POB = new DateTimeBox(departurePresenter::setDatePob, departurePresenter::setTimePob);
        dateTimePilotOff = new DateTimeBox(departurePresenter::setDatePilotOff, departurePresenter::setTimePilotOff);
        dateTimeTugMakeFast = new DateTimeBox(departurePresenter::setDateTugMakeFast, departurePresenter::setTimeTugMakeFast);
        dateTimeTugCastOff = new DateTimeBox(departurePresenter::setDateTugCastOff, departurePresenter::setTimeTugCastOff);
        tfManeuveringDist = new JFormattedTextField(FormatHelper.getManeuveringDist_Formatter());
        tfME = new JFormattedTextField(FormatHelper.getMERPM());
        cbMEMode = new JComboBox<>();
        dateTimeETANextPortLT = new DateTimeBox(departurePresenter::setETA_Next_Port_date, departurePresenter::setETA_Next_Port_time);
        tfDistanceToGo = new JFormattedTextField(FormatHelper.getDistanceToGo_Formatter());
        tfLshfoROB_s2 = new JFormattedTextField(FormatHelper.getLshfoROB_Formatter());
        tfFreshWater = new JFormattedTextField(FormatHelper.getFreshWater_Formatter());
        tfMgo_01_rob_s2 = new JFormattedTextField(FormatHelper.getMgo_01_ROB_Formatter());
        tfMgo_05_rob_s2 = new JFormattedTextField(FormatHelper.getMgo_05_ROB_Formatter());
        tfCargoOnDeck = new JFormattedTextField(FormatHelper.getCargoOnDesk_Formatter());
        tfCargoHolds = new JFormattedTextField(FormatHelper.getCargoHolds_Formatter());
        tfLaden_20 = new JFormattedTextField(FormatHelper.getContainer_Formatter());
        tfLaden_40 = new JFormattedTextField(FormatHelper.getContainer_Formatter());
        tfEmpty_20 = new JFormattedTextField(FormatHelper.getContainer_Formatter());
        tfEmpty_40 = new JFormattedTextField(FormatHelper.getContainer_Formatter());
        tfDraftFWD = new JFormattedTextField(FormatHelper.getDraftFWD_Formatter());
        tfBallast = new JFormattedTextField(FormatHelper.getBallast_Formatter());
        tfDraftAft = new JFormattedTextField(FormatHelper.getDraftAft_Formatter());
        tfGM = new JFormattedTextField(FormatHelper.getGM_Formatter());
        tfDWT = new JFormattedTextField(FormatHelper.getDWT_Formatter());
        tfFWReceived = new JFormattedTextField(FormatHelper.getFreshWater_Formatter());
        tfDisplacement = new JFormattedTextField(FormatHelper.getDisplacement_Formatter());
        tfLiveReefers = new JFormattedTextField(FormatHelper.getLiveReefers_Formatter());

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
        tfNote.getDocument().addDocumentListener(new FieldListener(() -> departurePresenter.setNote(tfNote.getText())));
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

        SaveBtnPanel saveBtnPanel = new SaveBtnPanel(true, false, true, true);
        add(saveBtnPanel, helper.span().get());
        saveBtnPanel.setClickSave(() -> {

            departurePresenter.validateResponse(() -> {
                handleFieldsError(new ArrayList<>());
                ResultSendDeparture result = (new SendReportAndSaveService()).saveDeparture(
                        (new SettingsPresenter()).readSettings(),
                        departurePresenter.mapToModelResponse()
                );
                if (!result.getSuccess()) {
                    JOptionPane.showMessageDialog(jbSend, result.getMessage());

                }
            }, (message, noValidData) -> {
                JOptionPane.showMessageDialog(jbSend, message);
                handleFieldsError(noValidData);
            });
        });

        saveBtnPanel.setClickSend(() -> {

            departurePresenter.validateResponse(() -> {

                        handleFieldsError(new ArrayList<>());

                        SettingsPresenter settingsPresenter = new SettingsPresenter();

                        ResultSendDeparture result = (new SendReportAndSaveService()).sendDepartureOnMailApp(
                                settingsPresenter.readSettings(),
                                (new DepartureToPostBody()).map(departurePresenter.mapToModelResponse()),
                                "Departure"
                        );
                        if (!result.getSuccess()) {
                            JOptionPane.showMessageDialog(jbSend, result.getMessage());
                        }
                    },
                    (message, noValidData) -> {
                        JOptionPane.showMessageDialog(jbSend, message);
                        handleFieldsError(noValidData);
                    }
            );

        });

        saveBtnPanel.setClickLoad(() -> {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select File");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JSON", "json");
            fileChooser.setFileFilter(filter);
            // Определение режима - только файл
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = fileChooser.showOpenDialog(this);
            // Если файл выбран, покажем его в сообщении
            if (result == JFileChooser.APPROVE_OPTION) {

//                departurePresenter.load(fileChooser.getSelectedFile());
                File file = fileChooser.getSelectedFile();
                String nameTab = DefineTabName.INSTANCE.define(file.getName());

                if (!nameTab.isEmpty()) {
                    try (Reader reader = Files.newBufferedReader(file.toPath(),
                            StandardCharsets.UTF_8)) {

                        JsonParser parser = new JsonParser();
                        JsonElement tree = parser.parse(reader);

                        if (tree.isJsonObject()) {

                            if (paneRouting != null) {
                                paneRouting.selectTabWithData(nameTab, tree);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    errorChange("Report type not defined!");
                }


            }
        });

        elementWithError.put(new ComponentKey(tfVoyNoDeparture, NoValidData.VoyNo), tfVoyNoDeparture.getBorder());
        elementWithError.put(new ComponentKey(cbPortDeparture, NoValidData.Unlocode), cbPortDeparture.getBorder());
        elementWithError.put(new ComponentKey(cbTZDeparture, NoValidData.TimeZone), cbTZDeparture.getBorder());
        elementWithError.put(new ComponentKey(dateTime_1, NoValidData.DateTimeStatus_1), dateTime_1.getBorder());
        elementWithError.put(new ComponentKey(dateTime_2, NoValidData.DateTimeStatus_2), dateTime_2.getBorder());
        elementWithError.put(new ComponentKey(positionFieldLatitude, NoValidData.Latitude_1), positionFieldLatitude.getBorder());
        elementWithError.put(new ComponentKey(positionFieldLongitude, NoValidData.Longitude_1), positionFieldLongitude.getBorder());
        elementWithError.put(new ComponentKey(cbTerminalDeparture, NoValidData.Terminal), cbTerminalDeparture.getBorder());
    }

    private void handleFieldsError(ArrayList<NoValidData> noValidData) {
        elementWithError.forEach((componentKey, border) -> {
            if (noValidData.contains(componentKey.noValidData)) {
                componentKey.component.setBorder(BorderFactory.createLineBorder(Color.red, 4, true));
            } else {
                componentKey.component.setBorder(border);
            }
        });
    }

    private void initSelectedTypeDeparture(DeparturePresenter departurePresenter) {
        //новая строка
        helper.insertEmptyRow(this, 10);

        departureFromBerth = new JRadioButton("Departure from Berth");
        departureFromAnchorage = new JRadioButton("Departure from Anchorage");
        ButtonGroup bg = new ButtonGroup();
        bg.add(departureFromBerth);
        bg.add(departureFromAnchorage);
        bg.setSelected(departureFromBerth.getModel(), true);

        departureFromAnchorage.addActionListener(e -> {
                    departurePresenter.setDeparture(DeparturePresenter.DepartureState.ANCHORAGE);
                    lbTypeReport1.setText("Anchor aweigh");
                }
        );
        departureFromBerth.addActionListener(e -> {
                    departurePresenter.setDeparture(DeparturePresenter.DepartureState.BERTH);
                    lbTypeReport1.setText("All clear");
                }
        );

        helper.nextEmptyCell(this, 10);
        add(departureFromBerth, helper.nextCell().get());

        helper.nextEmptyCell(this, 10);
        add(departureFromAnchorage, helper.nextCell().get());
        helper.nextEmptyCell(this, 10);
    }

    private void initCommon() {
        //отступ сверху
        helper.insertEmptyRow(this, 10);

        add(lbVoyNoDeparture, helper.nextCell().alignRight().gap(10).get());

        tfVoyNoDeparture.setColumns(10);
        tfVoyNoDeparture.setText(DataSettings.getInstance().readSettings().getVoyNo());
        departurePresenter.setVoyNo(DataSettings.getInstance().readSettings().getVoyNo());
        tfVoyNoDeparture.getDocument().addDocumentListener(new FieldListener(() ->
                departurePresenter.setVoyNo(tfVoyNoDeparture.getText())
        ));

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

                            positionFieldLatitude.setPosition(departurePresenter.getLatitude());
                            positionFieldLongitude.setPosition(departurePresenter.getLongitude());

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
        add(dateTime_1, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 30);
        add(new JLabel("POB"), helper.nextCell().alignRight().gap(10).get());
        add(dateTime_POB, helper.nextCell().fillBoth().get());


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
        add(dateTimePilotOff,
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
        add(dateTimeTugMakeFast,
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
        add(dateTimeTugCastOff,
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
        add(dateTime_2, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("Maneuvering Dist"), helper.nextCell().alignRight().gap(10).get());

        tfManeuveringDist.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfManeuveringDist.getValue();
            if (currValue != null) {
                departurePresenter.setManeuveringDist((Double) currValue);
            }
        }));
        add(tfManeuveringDist, helper.nextCell().fillHorizontally().get());


//        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("Position Latitude"), helper.nextCell().alignRight().gap(10).get());
        positionFieldLatitude = new PositionField(new Position(Position.TypePosition.Latitude, 0, 0, Position.Hemisphere.N), departurePresenter::setLatitude);
        positionFieldLatitude.setPosition(departurePresenter.getLatitude());
        add(positionFieldLatitude, helper.nextCell().fillBoth().get());
//        //пропуск в виде одной ячейки
        helper.nextCell();
        add(new JLabel("Position Longitude"), helper.nextCell().alignRight().gap(10).get());
        positionFieldLongitude = new PositionField(new Position(Position.TypePosition.Longitude, 0, 0, Position.Hemisphere.E), departurePresenter::setLongitude);
        positionFieldLongitude.setPosition(departurePresenter.getLongitude());
        add(positionFieldLongitude, helper.nextCell().fillBoth().get());

        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("ME (RPM)"), helper.nextCell().alignRight().gap(10).get());

        tfME.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfME.getValue();
            if (currValue != null) {
                departurePresenter.setMeRPM((Long) currValue);
            }
        }));
        add(tfME, helper.nextCell().fillHorizontally().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("ME mode (Eco/Full)"), helper.nextCell().alignRight().gap(10).get());

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
        add(dateTimeETANextPortLT, helper.nextCell().fillHorizontally().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("Distance to go (NM)"), helper.nextCell().alignRight().gap(10).get());
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
        tfLshfoROB_s2.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfLshfoROB_s2.getValue();
            if (currValue != null) {
                departurePresenter.setLshfoROB_S2((Double) currValue);
            }
        }));
        add(tfLshfoROB_s2, helper.nextCell().fillBoth().get());
        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);
        add(new JLabel("Fresh water (MT)"), helper.nextCell().alignRight().gap(10).get());
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
        tfMgo_01_rob_s2.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfMgo_01_rob_s2.getValue();
            if (currValue != null) {
                departurePresenter.setMgo_01_ROB_S2((Double) currValue);
            }
        }));
        add(tfMgo_01_rob_s2, helper.nextCell().fillBoth().get());

        //пропуск в виде одной ячейки
        helper.nextEmptyCell(this, 10);

        add(new JLabel("MGO 0,5% ROB (MT)"), helper.nextCell().alignRight().gap(10).get());
        tfMgo_05_rob_s2.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfMgo_05_rob_s2.getValue();
            if (currValue != null) {
                departurePresenter.setMgo_05_ROB_S2((double) currValue);
            }
        }));
        add(tfMgo_05_rob_s2, helper.nextCell().fillBoth().get());


        //        новая строка
        helper.insertEmptyRow(this, 10);
        add(new JLabel("Cargo on deck (MT)"), helper.nextCell().alignRight().gap(10).get());
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
        tfLiveReefers.getDocument().addDocumentListener(new FieldListener(() -> {
            Object currValue = tfLiveReefers.getValue();
            if (currValue != null) {
                departurePresenter.setLiveReefers((Integer) currValue);
            }
        }));
        add(tfLiveReefers, helper.nextCell().fillBoth().get());

    }

    private void setCurrData() {
        SettingsPresenter settingsPresenter = new SettingsPresenter();
        tfVoyNoDeparture.setText(settingsPresenter.readSettings().getVoyNo());
    }

    @Override
    public void loadFromJson(@NotNull JsonElement json) {
        Gson gson = new Gson();
        DepartureResponse response = null;
        try {
            response = gson.fromJson(json, DepartureResponse.class);
        } catch (JsonSyntaxException e) {
        }
        change(response);
    }

    public void change(@NotNull DepartureResponse response) {

        DataSourcePorts dataSourcePorts = DataSourcePorts.getInstance();
        List<SeaPortDto> ports = dataSourcePorts.getPorts();

        JComboBoxExtension.INSTANCE.setSelectedItem(response.getUnlocode(), response.getTerminalUUID(), ports, cbPortDeparture, cbTerminalDeparture);


        cbTZDeparture.setSelectedItem(response.getTimeZone());
        tfVoyNoDeparture.setText(response.getVoyNo());

        DeparturePresenter.DepartureState departureState = response.getDeparture();
        if (departureState == DeparturePresenter.DepartureState.BERTH) {
            departureFromBerth.setSelected(true);
            departureFromAnchorage.setSelected(false);

            departurePresenter.setDeparture(DeparturePresenter.DepartureState.BERTH);
            lbTypeReport1.setText("All clear");
        } else {
            departureFromBerth.setSelected(false);
            departureFromAnchorage.setSelected(true);

            lbTypeReport1.setText("Anchor aweigh");
            departurePresenter.setDeparture(DeparturePresenter.DepartureState.ANCHORAGE);
        }

        dateTime_1.setDateTime(response.getDateStatus1(), response.getTimeStatus1());
        dateTime_POB.setDateTime(response.getDatePob(), response.getTimePob());

        tfLSHFOROB.setValue(response.getLSHFOROB_S1());
        dateTimePilotOff.setDateTime(response.getDatePilotOff(), response.getTimePilotOff());

        tfMGO_01_ROB.setValue(response.getMGO_01_ROB_S1());
        dateTimeTugMakeFast.setDateTime(response.getDateTugMakeFast(), response.getTimeTugMakeFast());

        tfMGO_05_ROB.setValue(response.getMGO_05_ROB_S1());
        dateTimeTugCastOff.setDateTime(response.getDateTugCastOff(), response.getTimeTugCastOff());

        tfNoOfTugs.setValue(response.getNoOfTugs());


        dateTime_2.setDateTime(response.getDateStatus2(), response.getTimeStatus2());
        tfManeuveringDist.setValue(response.getManeuveringDist());

        positionFieldLatitude.setPositionWithReaction(response.getLatitude());
        positionFieldLongitude.setPositionWithReaction(response.getLongitude());

        tfME.setValue(response.getMeRPM());
        cbMEMode.setSelectedItem(MEMode.valueOf(response.getMeMode()));

        JComboBoxExtension.INSTANCE.setSelectedItem(response.getUnlocodeLastPort(), null, ports, cbLastPort, null);
        JComboBoxExtension.INSTANCE.setSelectedItem(response.getUnlocodeNextPort(), null, ports, cbNextPort, null);

        dateTimeETANextPortLT.setDateTime(response.getETA_Next_Port_date(), response.getETA_Next_Port_time());
        tfDistanceToGo.setValue(response.getDistanceToGo());

        tfLshfoROB_s2.setValue(response.getLshfoROB_S2());
        tfFreshWater.setValue(response.getFreshWater());

        tfMgo_01_rob_s2.setValue(response.getMgo_01_ROB_S2());
        tfMgo_05_rob_s2.setValue(response.getMgo_05_ROB_S2());

        tfCargoOnDeck.setValue(response.getCargoOnDesk());
        tfCargoHolds.setValue(response.getCargoHolds());

        tfLaden_20.setValue(response.getContainerLaden_20());
        tfEmpty_20.setValue(response.getContainerEmpty_20());

        tfLaden_40.setValue(response.getContainerLaden_40());
        tfEmpty_40.setValue(response.getContainerEmpty_40());

        tfDraftFWD.setValue(response.getDraftFWD());
        tfBallast.setValue(response.getBallast());

        tfDraftAft.setValue(response.getDraftAft());
        tfGM.setValue(response.getGm());

        tfDWT.setValue(response.getDwt());
        tfFWReceived.setValue(response.getFreshWaterReceived());

        tfDisplacement.setValue(response.getDisplacement());
        tfLiveReefers.setValue(response.getLiveReefers());

        tfNote.setText(response.getNote());

        departurePresenter.setVoyNo(response.getVoyNo());
        departurePresenter.setTimeZoneGMT(response.getTimeZone());
    }

    public void errorChange(@NotNull String error) {
        JOptionPane.showMessageDialog(this, error);
    }
}

