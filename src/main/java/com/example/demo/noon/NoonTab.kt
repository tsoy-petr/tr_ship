package com.example.demo.noon

import com.example.demo.core.Position
import com.example.demo.core.PositionField
import com.example.demo.core.TabReport
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.departure.DateTimeBox
import com.example.demo.model.SeaPortDto
import com.example.demo.settings.DataSettings
import com.example.demo.utils.FormatHelper
import com.example.demo.utils.FormsUtils
import com.example.demo.utils.newActionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.swing.JComboBox
import javax.swing.JFormattedTextField
import javax.swing.JLabel
import kotlin.coroutines.EmptyCoroutineContext

class NoonTab(
    private val noonPresenter: NoonPresenter
) : TabReport() {

    private val tfVoyNo = JFormattedTextField()
    private val cbLastPort = JComboBox<SeaPortDto>()
    private val cbNextPort = JComboBox<SeaPortDto>()
    private val cbTZ = JComboBox<String>()
    private val dateTimeLT = DateTimeBox()
    private val cbStatus = JComboBox<Status>()

    private val jlPositionLatitude = JLabel("Position Latitude")
    private val jlPositionLongitude = JLabel("Position Longitude")
    private var positionFieldLatitude: PositionField
    private var positionFieldLongitude: PositionField

    private val jlSeaPassagerDistance = JLabel("Sea Passage Distance (NM)")
    private val tfSeaPassagerDistance = JFormattedTextField(FormatHelper.getSeaPassagerDistance_Formatter())

    private val jlDistanceToGo = JLabel("Distance to go (NM)")
    private val tfDistanceToGo = JFormattedTextField(FormatHelper.getDistanceToGo_Formatter())

    init {

        tfVoyNo.columns = 10
        tfVoyNo.text = DataSettings.getInstance().readSettings().voyNo
        FormsUtils.initListTimeZone(cbTZ)
        cbTZ.newActionListener { newTZ, _ ->
            noonPresenter.setTZ(newTZ)
        }

        cbLastPort.run {
            isEditable = false
            this.newActionListener { selectedSeaPort, actionEvent ->
                if (actionEvent.equals("comboBoxChanged")) {
                    noonPresenter.setUnlocodeLast(selectedSeaPort)
                }
            }
        }
        cbNextPort.run {
            isEditable = false
            this.newActionListener { selectedSeaPort, actionEvent ->
                if (actionEvent.equals("comboBoxChanged")) {
                    noonPresenter.setUnlocodeNext(selectedSeaPort)
                }
            }
        }

        FormsUtils.initListPorts(
            DataSourcePorts.getInstance(),
            cbLastPort,
            setPort = {},
            setUnlocode = {},
            setLatitude = {},
            setLongitude = {})

        FormsUtils.initListPorts(
            DataSourcePorts.getInstance(),
            cbNextPort,
            setPort = {},
            setUnlocode = {},
            setLatitude = {},
            setLongitude = {})

        dateTimeLT.setDateChangeLister(noonPresenter::setDateLT)
        dateTimeLT.setTimeChangeLister { noonPresenter::setTimeLT }

        cbStatus.run {
            removeAllItems()
            isEditable = false
            this.newActionListener { newStatus, _ ->
                noonPresenter.setStatus(newStatus)
            }
            addItem(Status.AtSeeAdrift)
            addItem(Status.ASseeUnderway)
            addItem(Status.AtAnchor)
            addItem(Status.InPort)
        }

        positionFieldLatitude = PositionField(Position(Position.TypePosition.Latitude, 0, 0.0, Position.Hemisphere.N), noonPresenter::setPositionLatitude)
        positionFieldLongitude = PositionField(Position(Position.TypePosition.Longitude, 0, 0.0, Position.Hemisphere.E), noonPresenter::setPositionLongitude)

        CoroutineScope(context = EmptyCoroutineContext).launch {
            noonPresenter.state.collectLatest {state->
                when (state){
                    is State.AtSeeAdrift ->{

                    }

                    is State.ASeeUnderway ->{

                    }

                    is State.AtAnchor ->{

                    }

                    is State.InPort ->{

                    }
                }
            }
        }


        inflateUI()

    }



    private fun inflateUI() {

        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("Voy"))
            .addNextCellFillBoth(tfVoyNo)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Time Zone GMT"))
            .addNextCellFillBoth(cbTZ)

        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("Last port"))
            .addNextCellFillBoth(cbLastPort)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Next port"))
            .addNextCellFillBoth(cbNextPort)

        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("Date Time (LT)"))
            .addNextCellFillBoth(dateTimeLT)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Status"))
            .addNextCellFillBoth(cbStatus)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPositionLatitude)
            .addNextCellFillBoth(positionFieldLatitude)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPositionLongitude)
            .addNextCellFillBoth(positionFieldLongitude)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlSeaPassagerDistance)
            .addNextCellFillBoth(tfSeaPassagerDistance)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlDistanceToGo)
            .addNextCellFillBoth(tfDistanceToGo)

    }
}