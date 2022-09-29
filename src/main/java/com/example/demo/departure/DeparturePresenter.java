package com.example.demo.departure;

import com.example.demo.core.Position;
import com.example.demo.dataPorts.DataSourcePorts;
import com.example.demo.model.DepartureResponse;
import com.example.demo.model.SeaPortDto;
import com.example.demo.model.SettingsEmailDto;
import com.example.demo.model.TerminalDto;
import com.example.demo.settings.SettingsPresenter;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DeparturePresenter {

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

    private Position latitude;
    private Position longitude;

    private final DataSourcePorts dataSourcePorts = DataSourcePorts.getInstance();

    private long meRPM;
    private MEMode meMode;
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


    public DeparturePresenter() {
    }

    public void initListPorts(JComboBox<SeaPortDto> cb, JComboBox<TerminalDto> terminalsCb, JComboBox<String> timezoneCb) {

        cb.removeAllItems();
        SeaPortDto firstPort = null;

        List<SeaPortDto> ports = dataSourcePorts.getPorts();
        for (SeaPortDto port :
                ports) {
            cb.addItem(port);
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

    public DepartureResponse mapToModelResponse() {

        DepartureResponse response = new DepartureResponse();

        SettingsPresenter settingsPresenter = new SettingsPresenter();
        SettingsEmailDto settings = settingsPresenter.readSettings();
        response.setImo(settings.getImo());

        response.setTimeZone(timeZoneGMT);

        response.setDeparture(this.departure);

        if (this.dateStatus1 != null) {
            response.setDateStatus1(this.dateStatus1.toString());
        }

        if (this.timeStatus1 != null) {
            response.setTimeStatus1(this.timeStatus1.toString());
        }

        response.setUnlocode(this.unlocode);
        response.setVoyNo(settings.getVoyNo());

        if (terminal != null){
            response.setTerminalUUID(terminal.getUid());
        } else {
            response.setTerminalUUID("");
        }

        response.setDateStatus1(dateStatus1.toString());
        response.setTimeStatus1(timeStatus1.toString());

        response.setDatePob(datePob.toString());
        response.setTimePob(timePob.toString());

        response.setLSHFOROB_S1(LSHFOROB_S1);

        response.setDatePilotOff(datePilotOff.toString());
        response.setTimePilotOff(timePilotOff.toString());

        response.setMGO_01_ROB_S1(MGO_01_ROB_S1);
        response.setMGO_05_ROB_S1(MGO_05_ROB_S1);

        response.setDateTugMakeFast(dateTugMakeFast.toString());
        response.setTimeTugMakeFast(timeTugMakeFast.toString());

        response.setDateTugCastOff(dateTugCastOff.toString());
        response.setTimeTugCastOff(timeTugCastOff.toString());

        response.setNoOfTugs(noOfTugs);

        //
        response.setDateStatus2(dateStatus1.toString());
        response.setTimeStatus2(timeStatus2.toString());

        response.setManeuveringDist(maneuveringDist);

        response.setLatitude(latitude);
        response.setLongitude(longitude);

        response.setMeRPM(meRPM);

        if (meMode != null){
            response.setMeMode(meMode.toString());
        }

        response.setUnlocodeLastPort(unlocodeLastPort);
        response.setUnlocodeNextPort(unlocodeNextPort);

        response.setETA_Next_Port_date(ETA_Next_Port_date.toString());
        response.setETA_Next_Port_time(ETA_Next_Port_time.toString());

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
        response.setContainerEmpty_20(containerEmpty_40);

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

    public enum DepartureState {
        BERTH, ANCHORAGE
    }

}
