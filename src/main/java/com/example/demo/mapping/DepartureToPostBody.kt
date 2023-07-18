package com.example.demo.mapping

import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.departure.DeparturePresenter
import com.example.demo.model.DepartureResponse
import com.example.demo.model.SeaPortDto

class DepartureToPostBody {

    fun map(response: DepartureResponse): String {

        val sourcePorts = DataSourcePorts.getInstance()
        val port: SeaPortDto? = sourcePorts.ports.find { port -> port.unlocode.equals(response.unlocode) }
        val terminal = port?.terminals!!.find { t -> t.uid.equals(response.terminalUUID) }

        var title_dateStatus_1 = if (response.departure.equals(DeparturePresenter.DepartureState.BERTH)) {
            "All clear\t\t\t\t\t\t:"
        } else {
            "Anchor aweigh\t\t\t\t\t\t:"
        }
        return " \n" +
                "Departure Report (from BERTH/ANCHORAGE)\t\t: ${response.departure}\n" +
                "PORT\t\t\t\t\t\t\t: ${response.unlocode}\n" +
                "Voyage No.\t\t\t\t\t\t: ${response.voyNo}\n" +
                "Time Zone GMT\t\t\t\t\t: ${response.timeZone}\n" +
                "Terminal (Name)\t\t\t\t\t: $terminal (*${response.terminalUUID}*)\n\n" +

                title_dateStatus_1 + " ${response.dateStatus1}/${response.timeStatus1}\n" +
                "LSHFO\t\t\t\t\t\t\t: ${response.lshforoB_S1}\n" +
                "MGO 0,1%\t\t\t\t\t\t: ${response.mgO_01_ROB_S1}\n" +
                "MGO 0,5%\t\t\t\t\t\t: ${response.mgO_05_ROB_S1}\n" +
                "P.O.B (date/time)\t\t\t\t\t: ${response.datePob}/${response.timePob}\n" +
                "Pilot Off (date/time)\t\t\t\t\t: ${response.datePilotOff}/${response.timePilotOff}\n" +
                "Tug make fast (date/time)\t\t\t\t: ${response.dateTugMakeFast}/${response.timeTugMakeFast}\n" +
                "Tug cast off (date/time)\t\t\t\t\t: ${response.dateTugCastOff}/${response.timeTugCastOff}\n" +
                "No. of Tugs \t\t\t\t\t\t: ${response.noOfTugs}\n\n" +


                "BOSP (date/time)\t\t\t\t\t: ${response.dateStatus2}/${response.timeStatus2}\n" +
                "Position of BOSP (Latitude)\t\t\t\t: ${response.latitude}\n" +
                "Position of BOSP (Longitude)\t\t\t\t: ${response.longitude}\n" +
                "Maneuvering Distance from Berth/anchor up to BOSP\t: ${response.maneuveringDist}\n" +
                "ME (RPM)\t\t\t\t\t\t: ${response.meRPM}\n" +
                "ME mode (Eco/Full)\t\t\t\t\t: ${response.meMode}\n" +
                "Last Port\t\t\t\t\t\t: ${response.unlocodeLastPort}\n" +
                "Next Port\t\t\t\t\t\t: ${response.unlocodeNextPort}\n" +
                "ETA Next Port (date/time) LT\t\t\t\t: ${response.etA_Next_Port_date}/${response.etA_Next_Port_time}\n" +
                "Distance to go (NM)\t\t\t\t\t: ${response.distanceToGo}\n" +
                "LSHFO of BOSP\t\t\t\t\t\t: ${response.lshfoROB_S2}\n" +
                "MGO 0,1% of BOSP\t\t\t\t\t: ${response.mgo_01_ROB_S2}\n" +
                "MGO 0,5% of BOSP\t\t\t\t\t: ${response.mgo_05_ROB_S2}\n" +
                "Fresh water (MT)\t\t\t\t\t: ${response.freshWater}\n" +
                "Cargo on deck (MT)\t\t\t\t\t: ${response.cargoOnDesk}\n" +
                "Cargo in holds (MT)\t\t\t\t\t: ${response.cargoHolds}\n" +
                "20ft Laden on board (quantity)\t\t\t\t: ${response.containerLaden_20}\n" +
                "20ft empty on board (quantity) \t\t\t: ${response.containerEmpty_20}\n" +
                "40ft Laden on board (quantity)\t\t\t\t: ${response.containerLaden_40}\n" +
                "40ft empty on board (quantity)\t\t\t\t: ${response.containerEmpty_40}\n" +
                "Draft FWD (m)\t\t\t\t\t\t: ${response.draftFWD}\n" +
                "Draft AFT (m)\t\t\t\t\t\t: ${response.draftAft}\n" +
                "Ballast (MT)\t\t\t\t\t\t: ${response.ballast}\n" +
                "GM (m)\t\t\t\t\t\t\t: ${response.gm}\n" +
                "DWT \t\t\t\t\t\t\t: ${response.dwt}\n" +
                "FW received\t\t\t\t\t\t: ${response.freshWaterReceived}\n" +
                "Displacement\t\t\t\t\t\t: ${response.displacement}\n" +
                "Life Reefers on board (quantity) \t\t\t: ${response.liveReefers}\n" +
                "Notes(if any)\t\t\t\t\t\t: ${response.note}\n" +
                " \n" +
                " \n" +
                " \n"

    }

}