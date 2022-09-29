package com.example.demo.model;

import com.example.demo.core.Position;
import static com.example.demo.departure.DeparturePresenter.*;

public class DepartureResponse {

    private String imo;
    private DepartureState departure;
    private String unlocode;
    private String terminalUUID;
    private String timeZone;

    private String voyNo;

    private String dateStatus1;
    private String timeStatus1;
    private String datePob;
    private String timePob;

    private double LSHFOROB_S1;

    private String datePilotOff;
    private String timePilotOff;

    private double MGO_01_ROB_S1;
    private double MGO_05_ROB_S1;

    private String dateTugMakeFast;
    private String timeTugMakeFast;

    private String dateTugCastOff;
    private String timeTugCastOff;

    private long noOfTugs;

    private String dateStatus2;
    private String timeStatus2;

    private double maneuveringDist;

    private Position latitude;
    private Position longitude;

    private long meRPM;
    private String meMode;

    private String unlocodeNextPort;
    private String unlocodeLastPort;

    private String ETA_Next_Port_date;
    private String ETA_Next_Port_time;

    private double distanceToGo;

    private double lshfoROB_S2;

    private int freshWater;

    private double mgo_01_ROB_S2;
    private double mgo_05_ROB_S2;

    private double cargoOnDesk;
    private double cargoHolds;

    private int containerLaden_20;
    private int containerLaden_40;
    private int containerEmpty_20;
    private int containerEmpty_40;

    private double draftFWD;
    private int ballast;
    private double draftAft;
    private double gm;
    private int dwt;
    private int freshWaterReceived;
    private String note;
    private int displacement;
    private int liveReefers;

    public DepartureResponse(){}

    public String getUnlocode() {
        return unlocode;
    }

    public void setUnlocode(String unlocode) {
        this.unlocode = unlocode;
    }

    public String getTerminalUUID() {
        return terminalUUID;
    }

    public void setTerminalUUID(String terminalUUID) {
        this.terminalUUID = terminalUUID;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getVoyNo() {
        return voyNo;
    }

    public void setVoyNo(String voyNo) {
        this.voyNo = voyNo;
    }


    public DepartureState getDeparture() {
        return departure;
    }

    public void setDeparture(DepartureState departure) {
        this.departure = departure;
    }

    public String getDateStatus1() {
        return dateStatus1;
    }

    public void setDateStatus1(String dateStatus1) {
        this.dateStatus1 = dateStatus1;
    }

    public String getTimeStatus1() {
        return timeStatus1;
    }

    public void setTimeStatus1(String timeStatus1) {
        this.timeStatus1 = timeStatus1;
    }

    public String getDatePob() {
        return datePob;
    }

    public void setDatePob(String datePob) {
        this.datePob = datePob;
    }

    public String getTimePob() {
        return timePob;
    }

    public void setTimePob(String timePob) {
        this.timePob = timePob;
    }

    public double getLSHFOROB_S1() {
        return LSHFOROB_S1;
    }

    public void setLSHFOROB_S1(double LSHFOROB_S1) {
        this.LSHFOROB_S1 = LSHFOROB_S1;
    }

    public String getDatePilotOff() {
        return datePilotOff;
    }

    public void setDatePilotOff(String datePilotOff) {
        this.datePilotOff = datePilotOff;
    }

    public String getTimePilotOff() {
        return timePilotOff;
    }

    public void setTimePilotOff(String timePilotOff) {
        this.timePilotOff = timePilotOff;
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

    public String getDateTugMakeFast() {
        return dateTugMakeFast;
    }

    public void setDateTugMakeFast(String dateTugMakeFast) {
        this.dateTugMakeFast = dateTugMakeFast;
    }

    public String getTimeTugMakeFast() {
        return timeTugMakeFast;
    }

    public void setTimeTugMakeFast(String timeTugMakeFast) {
        this.timeTugMakeFast = timeTugMakeFast;
    }

    public String getDateTugCastOff() {
        return dateTugCastOff;
    }

    public void setDateTugCastOff(String dateTugCastOff) {
        this.dateTugCastOff = dateTugCastOff;
    }

    public String getTimeTugCastOff() {
        return timeTugCastOff;
    }

    public void setTimeTugCastOff(String timeTugCastOff) {
        this.timeTugCastOff = timeTugCastOff;
    }

    public long getNoOfTugs() {
        return noOfTugs;
    }

    public void setNoOfTugs(long noOfTugs) {
        this.noOfTugs = noOfTugs;
    }

    public String getDateStatus2() {
        return dateStatus2;
    }

    public void setDateStatus2(String dateStatus2) {
        this.dateStatus2 = dateStatus2;
    }

    public String getTimeStatus2() {
        return timeStatus2;
    }

    public void setTimeStatus2(String timeStatus2) {
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

    public long getMeRPM() {
        return meRPM;
    }

    public void setMeRPM(long meRPM) {
        this.meRPM = meRPM;
    }

    public String getMeMode() {
        return meMode;
    }

    public void setMeMode(String meMode) {
        this.meMode = meMode;
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

    public String getETA_Next_Port_date() {
        return ETA_Next_Port_date;
    }

    public void setETA_Next_Port_date(String ETA_Next_Port_date) {
        this.ETA_Next_Port_date = ETA_Next_Port_date;
    }

    public String getETA_Next_Port_time() {
        return ETA_Next_Port_time;
    }

    public void setETA_Next_Port_time(String ETA_Next_Port_time) {
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

    public String getImo() {
        return imo;
    }

    public void setImo(String imo) {
        this.imo = imo;
    }

}
