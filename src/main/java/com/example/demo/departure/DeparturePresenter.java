package com.example.demo.departure;

import com.example.demo.core.*;
import com.example.demo.dataPorts.DataSourcePorts;
import com.example.demo.model.DepartureResponse;
import com.example.demo.model.SeaPortDto;
import com.example.demo.model.SettingsEmailDto;
import com.example.demo.model.TerminalDto;
import com.example.demo.settings.SettingsPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.utils.Common.isEmpty;

public class DeparturePresenter implements ValidatorResponse, LoadFromFile, RoutingControllingTabs {


    private DepartureDataChangeListner departureDataChangeListner;

    private JTabbedPaneRouting paneRouting;

    private SeaPortDto port;
    private String unlocode;
    private String timeZoneGMT;
    private String unlocodeNextPort;
    private String unlocodeLastPort;

    private TerminalDto terminal;
    private String terminalTitle;

    private String voyNo;

    private LocalDate dateStatus1 = LocalDate.MIN;
    private LocalTime timeStatus1 = LocalTime.MIN;

    private LocalDate datePob = LocalDate.MIN;
    private LocalTime timePob = LocalTime.MIN;

    private LocalDate datePilotOff = LocalDate.MIN;
    private LocalTime timePilotOff = LocalTime.MIN;

    private double LSHFOROB_S1;
    private double MGO_01_ROB_S1;
    private double MGO_05_ROB_S1;

    private LocalTime timeTugMakeFast = LocalTime.MIN;
    private LocalDate dateTugMakeFast = LocalDate.MIN;

    private LocalTime timeTugCastOff = LocalTime.MIN;
    private LocalDate dateTugCastOff = LocalDate.MIN;

    private long noOfTugs;

    private LocalDate dateStatus2 = LocalDate.MIN;
    private LocalTime timeStatus2 = LocalTime.MIN;

    private double maneuveringDist;

    private Position latitude = new Position(Position.TypePosition.Latitude, 0, 0.0, Position.Hemisphere.N);
    private Position longitude = new Position(Position.TypePosition.Longitude, 0, 0.0, Position.Hemisphere.E);

    private final DataSourcePorts dataSourcePorts = DataSourcePorts.getInstance();

    private long meRPM;
    private MEMode meMode = MEMode.ECO;
    private LocalDate ETA_Next_Port_date = LocalDate.MIN;
    private LocalTime ETA_Next_Port_time = LocalTime.MIN;
    private double distanceToGo;
    private double lshfoROB_S2;
    private double mgo_01_ROB_S2;
    private double mgo_05_ROB_S2;
    private double cargoOnDesk;
    private double cargoHolds;
    private int containerLaden_20;
    private int containerLaden_40;
    private int containerEmpty_20;
    private int containerEmpty_40;
    private int freshWater;
    private DepartureState departure = DepartureState.BERTH;

    private double draftFWD;
    private int ballast;
    private double draftAft;
    private double gm;
    private int dwt;
    private int freshWaterReceived;
    private String note;
    private int displacement;
    private int liveReefers;


    public DeparturePresenter() {}

    public void initListPorts(JComboBox<SeaPortDto> cbPort, JComboBox<TerminalDto> terminalsCb, JComboBox<String> timezoneCb) {

        cbPort.removeAllItems();
        SeaPortDto firstPort = null;

        List<SeaPortDto> ports = dataSourcePorts.getPorts();
        for (SeaPortDto port :
                ports) {
            cbPort.addItem(port);
            if (firstPort == null) {
                firstPort = port;
            }
        }

        if (firstPort != null) {
            if (terminalsCb != null) {
                initListTerminals(terminalsCb, firstPort);
            }
            if (timezoneCb != null) {
                timezoneCb.setSelectedItem(firstPort.getTimeZone());
            }
            this.port = firstPort;
            this.unlocode = firstPort.getUnlocode();
        }
    }

    public void initCBPorts(JComboBox<SeaPortDto> cb) {
        cb.removeAllItems();
        List<SeaPortDto> ports = dataSourcePorts.getPorts();
        for (SeaPortDto port :
                ports) {
            cb.addItem(port);
        }
        cb.setSelectedIndex(-1);
    }

    public void initListTimeZone(JComboBox<String> cb) {

        cb.removeAllItems();

        for (int i = 0; i <= 12; i++) {
            if (i == 0) {
                cb.addItem("0");
            } else {
                cb.addItem("+" + i);
            }
        }

        for (int i = 1; i <= 12; i++) {
            cb.addItem("-" + i);
        }

    }

    public void initListTerminals(JComboBox<TerminalDto> cb, SeaPortDto port) {

        cb.removeAllItems();

        for (TerminalDto t :
                port.getTerminals()) {
            cb.addItem(t);
        }

        if (port.getTerminals().size() > 0) {
            DeparturePresenter.this.terminal = port.getTerminals().get(0);
            cb.setSelectedItem(DeparturePresenter.this.terminal);
            setLatitude(DeparturePresenter.this.terminal.getLatitude());
            setLongitude(DeparturePresenter.this.terminal.getLongitude());
        }
    }

    private String dateToString(LocalDate date) {
        if (date != null && date != LocalDate.MIN) {
            return date.toString();
        } else return "";
    }

    private String timeToString(LocalTime time) {
        if (time != null && time != LocalTime.MIN) {
            return time.toString();
        } else return "";
    }

    public void reloadFromJson(JsonElement json) {

        Gson gson = new Gson();
        DepartureResponse response = null;
        try {
            response = gson.fromJson(json, DepartureResponse.class);
        } catch (JsonSyntaxException e) {

        }

        if (response != null
                && departureDataChangeListner != null) {
            departureDataChangeListner.change(response);
        }

    }

    public DepartureResponse mapToModelResponse() {

        DepartureResponse response = new DepartureResponse();

        SettingsPresenter settingsPresenter = new SettingsPresenter();
        SettingsEmailDto settings = settingsPresenter.readSettings();
        response.setImo(settings.getImo());

        response.setTimeZone(this.timeZoneGMT);

        response.setDeparture(this.departure);

        response.setUnlocode(this.unlocode);
        response.setVoyNo(getVoyNo());

        if (terminal != null) {
            response.setTerminalUUID(terminal.getUid());
        } else {
            response.setTerminalUUID("");
        }

        response.setDateStatus1(dateToString(dateStatus1));
        response.setTimeStatus1(timeToString(timeStatus1));

        response.setDatePob(dateToString(datePob));
        response.setTimePob(timeToString(timePob));

        response.setLSHFOROB_S1(LSHFOROB_S1);

        response.setDatePilotOff(dateToString(datePilotOff));
        response.setTimePilotOff(timeToString(timePilotOff));

        response.setMGO_01_ROB_S1(MGO_01_ROB_S1);
        response.setMGO_05_ROB_S1(MGO_05_ROB_S1);

        response.setDateTugMakeFast(dateToString(dateTugMakeFast));
        response.setTimeTugMakeFast(timeToString(timeTugMakeFast));

        response.setDateTugCastOff(dateToString(dateTugCastOff));
        response.setTimeTugCastOff(timeToString(timeTugCastOff));

        response.setNoOfTugs(noOfTugs);

        response.setDateStatus2(dateToString(dateStatus1));
        response.setTimeStatus2(timeToString(timeStatus2));

        response.setManeuveringDist(maneuveringDist);

        response.setLatitude(latitude);
        response.setLongitude(longitude);

        response.setMeRPM(meRPM);

        if (meMode != null) {
            response.setMeMode(meMode.toString());
        } else response.setMeMode("");

        if (unlocodeLastPort != null) {
            response.setUnlocodeLastPort(unlocodeLastPort);
        } else response.setUnlocodeLastPort("");

        if (unlocodeNextPort != null) {
            response.setUnlocodeNextPort(unlocodeNextPort);
        } else response.setUnlocodeNextPort("");

        response.setETA_Next_Port_date(dateToString(ETA_Next_Port_date));
        response.setETA_Next_Port_time(timeToString(ETA_Next_Port_time));

        response.setDistanceToGo(distanceToGo);

        response.setLshfoROB_S2(lshfoROB_S2);

        response.setFreshWater(freshWater);

        response.setMgo_01_ROB_S2(mgo_01_ROB_S2);
        response.setMgo_05_ROB_S2(mgo_05_ROB_S2);

        response.setCargoOnDesk(cargoOnDesk);
        response.setCargoHolds(cargoHolds);

        response.setContainerLaden_20(containerLaden_20);
        response.setContainerEmpty_20(containerEmpty_20);

        response.setContainerLaden_40(containerLaden_40);
        response.setContainerEmpty_40(containerEmpty_40);

        response.setDraftFWD(draftFWD);
        response.setDraftAft(draftAft);

        response.setBallast(ballast);

        response.setGm(gm);

        response.setDwt(dwt);

        response.setFreshWaterReceived(freshWaterReceived);

        response.setNote(note);

        response.setDisplacement(displacement);

        response.setLiveReefers(liveReefers);

        return response;

    }

    public long getMeRPM() {
        return meRPM;
    }

    public void setMeRPM(long meRPM) {
        this.meRPM = meRPM;
    }

    public void addChangePortsListener(DataSourcePorts.PortsChangeListener listener) {
        dataSourcePorts.addListener(listener);
    }

    public DepartureState getDeparture() {
        return departure;
    }

    public void setDeparture(DepartureState departure) {
        this.departure = departure;
    }

    public LocalDate getDateStatus1() {
        return dateStatus1;
    }

    public void setDateStatus1(LocalDate dateStatus1) {
        this.dateStatus1 = dateStatus1;
    }

    public LocalTime getTimeStatus1() {
        return timeStatus1;
    }

    public void setTimeStatus1(LocalTime timeStatus1) {
        this.timeStatus1 = timeStatus1;
    }

    public LocalDate getDatePob() {
        return datePob;
    }

    public void setDatePob(LocalDate datePob) {
        this.datePob = datePob;
    }

    public LocalTime getTimePob() {
        return timePob;
    }

    public void setTimePob(LocalTime timePob) {
        this.timePob = timePob;
    }

    public SeaPortDto getPort() {
        return port;
    }

    public void setPort(SeaPortDto port) {
        this.port = port;
        this.unlocode = port.getUnlocode();
    }

    public String getUnlocode() {
        return unlocode;
    }

    public void setUnlocode(String unlocode) {
        this.unlocode = unlocode;
    }

    public TerminalDto getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalDto terminal) {
        this.terminal = terminal;
    }

    public String getTerminalTitle() {
        return terminalTitle;
    }

    public void setTerminalTitle(String terminalTitle) {
        this.terminalTitle = terminalTitle;
    }

    public String getVoyNo() {
        return voyNo;
    }

    public void setVoyNo(String voyNo) {
        this.voyNo = voyNo;
    }

    public LocalDate getDatePilotOff() {
        return datePilotOff;
    }

    public void setDatePilotOff(LocalDate datePilotOff) {
        this.datePilotOff = datePilotOff;
    }

    public LocalTime getTimePilotOff() {
        return timePilotOff;
    }

    public void setTimePilotOff(LocalTime timePilotOff) {
        this.timePilotOff = timePilotOff;
    }

    public double getLSHFOROB_S1() {
        return LSHFOROB_S1;
    }

    public void setLSHFOROB_S1(double LSHFOROB_S1) {
        this.LSHFOROB_S1 = LSHFOROB_S1;
    }

    public double getMGO_01_ROB_S1() {
        return MGO_01_ROB_S1;
    }

    public void setMGO_01_ROB_S1(double MGO_01_ROB_S1) {
        this.MGO_01_ROB_S1 = MGO_01_ROB_S1;
    }

    public double getMGO_05_ROB_S1() {
        return MGO_05_ROB_S1;
    }

    public void setMGO_05_ROB_S1(double MGO_05_ROB_S1) {
        this.MGO_05_ROB_S1 = MGO_05_ROB_S1;
    }

    public LocalTime getTimeTugMakeFast() {
        return timeTugMakeFast;
    }

    public void setTimeTugMakeFast(LocalTime timeTugMakeFast) {
        this.timeTugMakeFast = timeTugMakeFast;
    }

    public LocalDate getDateTugMakeFast() {
        return dateTugMakeFast;
    }

    public void setDateTugMakeFast(LocalDate dateTugMakeFast) {
        this.dateTugMakeFast = dateTugMakeFast;
    }

    public LocalTime getTimeTugCastOff() {
        return timeTugCastOff;
    }

    public void setTimeTugCastOff(LocalTime timeTugCastOff) {
        this.timeTugCastOff = timeTugCastOff;
    }

    public LocalDate getDateTugCastOff() {
        return dateTugCastOff;
    }

    public void setDateTugCastOff(LocalDate dateTugCastOff) {
        this.dateTugCastOff = dateTugCastOff;
    }

    public long getNoOfTugs() {
        return noOfTugs;
    }

    public void setNoOfTugs(long noOfTugs) {
        this.noOfTugs = noOfTugs;
    }

    public LocalDate getDateStatus2() {
        return dateStatus2;
    }

    public void setDateStatus2(LocalDate dateStatus2) {
        this.dateStatus2 = dateStatus2;
    }

    public LocalTime getTimeStatus2() {
        return timeStatus2;
    }

    public void setTimeStatus2(LocalTime timeStatus2) {
        this.timeStatus2 = timeStatus2;
    }

    public double getManeuveringDist() {
        return maneuveringDist;
    }

    public void setManeuveringDist(double maneuveringDist) {
        this.maneuveringDist = maneuveringDist;
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

    public MEMode getMeMode() {
        return meMode;
    }

    public void setMeMode(MEMode meMode) {
        this.meMode = meMode;
    }

    public LocalDate getETA_Next_Port_date() {
        return ETA_Next_Port_date;
    }

    public void setETA_Next_Port_date(LocalDate ETA_Next_Port_date) {
        this.ETA_Next_Port_date = ETA_Next_Port_date;
    }

    public LocalTime getETA_Next_Port_time() {
        return ETA_Next_Port_time;
    }

    public void setETA_Next_Port_time(LocalTime ETA_Next_Port_time) {
        this.ETA_Next_Port_time = ETA_Next_Port_time;
    }

    public double getDistanceToGo() {
        return distanceToGo;
    }

    public void setDistanceToGo(double distanceToGo) {
        this.distanceToGo = distanceToGo;
    }

    public double getLshfoROB_S2() {
        return lshfoROB_S2;
    }

    public void setLshfoROB_S2(double lshfoROB_S2) {
        this.lshfoROB_S2 = lshfoROB_S2;
    }

    public int getFreshWater() {
        return freshWater;
    }

    public void setFreshWater(int freshWater) {
        this.freshWater = freshWater;
    }

    public double getMgo_01_ROB_S2() {
        return mgo_01_ROB_S2;
    }

    public void setMgo_01_ROB_S2(double mgo_01_ROB_S2) {
        this.mgo_01_ROB_S2 = mgo_01_ROB_S2;
    }

    public double getMgo_05_ROB_S2() {
        return mgo_05_ROB_S2;
    }

    public void setMgo_05_ROB_S2(double mgo_05_ROB_S2) {
        this.mgo_05_ROB_S2 = mgo_05_ROB_S2;
    }

    public double getCargoOnDesk() {
        return cargoOnDesk;
    }

    public void setCargoOnDesk(double cargoOnDesk) {
        this.cargoOnDesk = cargoOnDesk;
    }

    public double getCargoHolds() {
        return cargoHolds;
    }

    public void setCargoHolds(double cargoHolds) {
        this.cargoHolds = cargoHolds;
    }

    public int getContainerLaden_20() {
        return containerLaden_20;
    }

    public void setContainerLaden_20(int containerLaden_20) {
        this.containerLaden_20 = containerLaden_20;
    }

    public int getContainerLaden_40() {
        return containerLaden_40;
    }

    public void setContainerLaden_40(int containerLaden_40) {
        this.containerLaden_40 = containerLaden_40;
    }

    public int getContainerEmpty_20() {
        return containerEmpty_20;
    }

    public void setContainerEmpty_20(int containerEmpty_20) {
        this.containerEmpty_20 = containerEmpty_20;
    }

    public int getContainerEmpty_40() {
        return containerEmpty_40;
    }

    public void setContainerEmpty_40(int containerEmpty_40) {
        this.containerEmpty_40 = containerEmpty_40;
    }

    public double getDraftFWD() {
        return draftFWD;
    }

    public void setDraftFWD(double draftFWD) {
        this.draftFWD = draftFWD;
    }

    public int getBallast() {
        return ballast;
    }

    public void setBallast(int ballast) {
        this.ballast = ballast;
    }

    public double getDraftAft() {
        return draftAft;
    }

    public void setDraftAft(double draftAft) {
        this.draftAft = draftAft;
    }

    public double getGm() {
        return gm;
    }

    public void setGm(double gm) {
        this.gm = gm;
    }

    public int getDwt() {
        return dwt;
    }

    public void setDwt(int dwt) {
        this.dwt = dwt;
    }

    public int getFreshWaterReceived() {
        return freshWaterReceived;
    }

    public void setFreshWaterReceived(int freshWaterReceived) {
        this.freshWaterReceived = freshWaterReceived;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDisplacement() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public int getLiveReefers() {
        return liveReefers;
    }

    public void setLiveReefers(int liveReefers) {
        this.liveReefers = liveReefers;
    }

    public String getUnlocodeNextPort() {
        return unlocodeNextPort;
    }

    public void setUnlocodeNextPort(String unlocodeNextPort) {
        this.unlocodeNextPort = unlocodeNextPort;
    }

    public String getUnlocodeLastPort() {
        return unlocodeLastPort;
    }

    public void setUnlocodeLastPort(String unlocodeLastPort) {
        this.unlocodeLastPort = unlocodeLastPort;
    }

    public void setTimeZoneGMT(String timeZoneGMT) {
        this.timeZoneGMT = timeZoneGMT;
    }

    @Override
    public void validateResponse(@NotNull IsValid isValid, @NotNull IsNotValid isNotValid) {
        DepartureResponse response = mapToModelResponse();
        SettingsEmailDto settings = new SettingsPresenter().readSettings();
        if (isEmpty(response.getVoyNo())
                || isEmpty(response.getImo())
                || isEmpty(response.getUnlocode())
                || (isEmpty(response.getTimeZone()) || response.getTimeZone().equals("0"))
                || isEmpty(response.getDateStatus1())
                || isEmpty(response.getTimeStatus1())
                || isEmpty(response.getDateStatus2())
                || isEmpty(response.getTimeStatus2())
                || !response.getLatitude().isValidate()
                || !response.getLongitude().isValidate()
                || isEmpty(response.getTerminalUUID())
        ) {

            ArrayList<NoValidData> noValidData = new ArrayList<>();

            if (isEmpty(response.getVoyNo())) {
                noValidData.add(NoValidData.VoyNo);
            }

            if (isEmpty(response.getUnlocode())) {
                noValidData.add(NoValidData.Unlocode);
            }

            if ((isEmpty(response.getTimeZone()) || response.getTimeZone().equals("0"))) {
                noValidData.add(NoValidData.TimeZone);
            }

            if (isEmpty(response.getDateStatus1())) {
                noValidData.add(NoValidData.DateTimeStatus_1);
            }

            if (isEmpty(response.getDateStatus2())) {
                noValidData.add(NoValidData.DateTimeStatus_2);
            }

            if (isEmpty(response.getTimeStatus1())) {
                noValidData.add(NoValidData.DateTimeStatus_1);
            }

            if (isEmpty(response.getTimeStatus2())) {
                noValidData.add(NoValidData.DateTimeStatus_2);
            }

            if (!response.getLatitude().isValidate()) {
                noValidData.add(NoValidData.Latitude_1);
            }

            if (!response.getLongitude().isValidate()) {
                noValidData.add(NoValidData.Longitude_1);
            }

            String mess = isNotValid.message;
            if (isEmpty(settings.getImo())) {
                mess = "Vessel IMO not filled. \n" + mess;
            }

            if (isEmpty(response.getTerminalUUID())) noValidData.add(NoValidData.Terminal);

            isNotValid.isNotValid(mess, noValidData);
        } else isValid.valid();
    }

    @Override
    public void load(@NotNull File file) {

        String nameTab = DefineTabName.INSTANCE.define(file.getName());

        if (!nameTab.isEmpty()) {
            try (Reader reader = Files.newBufferedReader(file.toPath(),
                    StandardCharsets.UTF_8)) {

                JsonParser parser = new JsonParser();
                JsonElement tree = parser.parse(reader);

                if (tree.isJsonObject()) {

                    if (paneRouting != null) {
                        paneRouting.selectTabWithData("DEPARTURE", tree);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (departureDataChangeListner != null) {
                departureDataChangeListner.errorChange("Report type not defined!");
            }
        }
    }

    @Override
    public void rout() {

    }

    public enum DepartureState {
        BERTH, ANCHORAGE
    }

}
