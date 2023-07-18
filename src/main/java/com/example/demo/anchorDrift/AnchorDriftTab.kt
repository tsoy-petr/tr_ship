package com.example.demo.anchorDrift

import com.example.demo.core.*
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.departure.DateTimeBox
import com.example.demo.model.ComponentKey
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import com.example.demo.noon.NoonResponse
import com.example.demo.settings.DataSettings
import com.example.demo.utils.FormatHelper
import com.example.demo.utils.FormsUtils
import com.example.demo.utils.newActionListener
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.coroutines.EmptyCoroutineContext

class AnchorDriftTab(private val presenter: AnchorDriftPresenter) : TabReport() {

    private val actionChangeTerminal = ActionListener { _: ActionEvent ->
        (cbTerminal.selectedItem)?.let {
            presenter.setTerminal(it as TerminalDto)
        }
    }

    private val tfVoyNo = JFormattedTextField()

    private val jlPort = JLabel("Port")
    private val cbPort = JComboBox<SeaPortDto>()

    private val jlTerminal = JLabel("Terminal")
    private val cbTerminal = JComboBox<TerminalDto>()

    private val cbTZ = JComboBox<String>()

    private val bgTypeDepartureSeaBerth = ButtonGroup()
    private val bgTypeDepartureAnchorDrifting = ButtonGroup()
    private val fromSea = JRadioButton("From sea")
    private val fromBerth = JRadioButton("From berth")
    private val fromAnchor = JRadioButton("Anchor")
    private val fromDrifting = JRadioButton("Drifting")

    private val jlDateFirst = JLabel("EOSP")
    private val dtDateTimeFirst = DateTimeBox()

    private val jlSeaPassageDistance = JLabel("Sea Passage Distance (NM)")
    private val tfSeaPassageDistance = JFormattedTextField(FormatHelper.getSeaPassageDistance_Formatter())

    private val jlPositionLatitudeFirst = JLabel("Position Latitude")
    private val jlPositionLongitudeFirst = JLabel("Position Longitude")
    private var positionFieldLatitudeFirst: PositionField
    private var positionFieldLongitudeFirst: PositionField

    private val jlLSHFOROB_first = JLabel("LSHFO ROB (mt)")
    private val tfLSHFOROB_first = JFormattedTextField(FormatHelper.getLSHFOFormatter())

    private val jlPOB = JLabel("POB")
    private val dtPOB = DateTimeBox()

    private val jlMGO_ROB_01_first = JLabel("MGO 0,1% ROB (mt)")
    private val tfMGO_ROB_01_first = JFormattedTextField(FormatHelper.getMGO_01_ROB_Formatter())

    private val jlPilotOff = JLabel("Pilot off")
    private val dtPilotOff = DateTimeBox()

    private val jlMGO_ROB_05_first = JLabel("MGO 0,5% ROB (mt)")
    private val tfMGO_ROB_05_first = JFormattedTextField(FormatHelper.getMGO_05_ROB_Formatter())

    private val jlDateTime_second = JLabel("Drop Anchor")
    private val dtDateTime_second = DateTimeBox()

    private val jlManeuveringDist = JLabel("Maneuvering Dist")
    private val tfManeuveringDist = JFormattedTextField(FormatHelper.getManeuveringDist_Formatter())

    private val jlPositionLatitudeSecond = JLabel("Position Latitude")
    private val jlPositionLongitudeSecond = JLabel("Position Longitude")
    private var positionFieldLatitudeSecond: PositionField
    private var positionFieldLongitudeSecond: PositionField

    private val jlLSHFOROB_second = JLabel("LSHFO ROB (mt)")
    private val tfLSHFOROB_second = JFormattedTextField(FormatHelper.getLSHFOFormatter())

    private val jlFreshWaterMT = JLabel("Fresh water (mt)")
    private val tfFreshWaterMT = JFormattedTextField(FormatHelper.getFreshWater_Formatter())

    private val jlMGO_ROB_01_second = JLabel("MGO 0,1% ROB (mt)")
    private val tfMGO_ROB_01_second = JFormattedTextField(FormatHelper.getMGO_01_ROB_Formatter())

    private val jlMGO_ROB_05_second = JLabel("MGO 0,5% ROB (mt)")
    private val tfMGO_ROB_05_second = JFormattedTextField(FormatHelper.getMGO_05_ROB_Formatter())

    private val jlNote = JLabel("Note")
    private val tfNote = JTextArea(3, 25)

    private val saveBtnPanel = SaveBtnPanel(true, false, true, true)

    init {

        positionFieldLatitudeFirst = PositionField(
            Position(Position.TypePosition.Latitude, 0, 0.0, Position.Hemisphere.N)
        ) { newPosition ->
            presenter.setPositionLatitudeFirst(newPosition)
        }
        positionFieldLongitudeFirst = PositionField(
            Position(Position.TypePosition.Longitude, 0, 0.0, Position.Hemisphere.E),
            presenter::setPositionLongitudeFirst
        )

        positionFieldLatitudeSecond = PositionField(
            Position(Position.TypePosition.Latitude, 0, 0.0, Position.Hemisphere.N)
        ) { newPosition: Position ->
            presenter.setPositionLatitudeSecond(newPosition)
        }
        positionFieldLongitudeSecond = PositionField(
            Position(Position.TypePosition.Longitude, 0, 0.0, Position.Hemisphere.E),
            presenter::setPositionLongitudeSecond
        )

        tfVoyNo.columns = 10
        tfVoyNo.text = DataSettings.getInstance().readSettings().voyNo
        presenter.setVoyNo(tfVoyNo.text)
        tfVoyNo.document.addDocumentListener(FieldListener {
            castToType(tfVoyNo.text, presenter::setVoyNo)
        })

        cbPort.run {
            isEditable = false

            this.newActionListener({ seaPortDto, action ->
                if (action.actionCommand.equals("comboBoxChanged")) {
                    presenter.setUnlocode(seaPortDto)
                    cbTZ.selectedItem = seaPortDto.timeZone
                    cbTerminal.removeActionListener(actionChangeTerminal)
                    FormsUtils.initListTerminals(
                        cbTerminal,
                        seaPortDto,
                        setLatitude = {},
                        setLongitude = {},
                        setTerminal = presenter::setTerminal
                    )
                    cbTerminal.addActionListener(actionChangeTerminal)
                }
            })
        }

        cbTerminal.run {
            isEditable = false
            this.addActionListener(actionChangeTerminal)
        }

        FormsUtils.initListTimeZone(cbTZ)
        cbTZ.newActionListener({ newTZ, _ ->
            presenter.setTZ(newTZ)
        })

        val dataSourcePorts = DataSourcePorts.getInstance()

        FormsUtils.initListPorts(
            dataSourcePorts,
            cbPort,
            setPort = (
                    presenter::setUnlocode
                    ),
            terminalsCb = cbTerminal,
            setUnlocode = {},
            setLatitude = {},
            setLongitude = {}, setTerminal = {})

        CoroutineScope(context = EmptyCoroutineContext).launch {
            presenter.stateReport.collectLatest { state ->

                when (state) {
                    is State.UploadError -> {
                        JOptionPane.showMessageDialog(tfVoyNo, state.message)
                        handleFieldsError(state.noValidData)
                    }
                    is State.Editing-> {
                        setVisibleUI(state)
                    }
                    is State.Init-> {
                        setVisibleUI(state)
                    }
                    else -> {
                        handleFieldsError(arrayListOf())
                    }
                }

            }
        }

        initRBTypeDepartureSeaBerth()
        initRBTypeDepartureAnchorDrifting()

        dtDateTimeFirst.setDateChangeLister(presenter::setDateFirst)
        dtDateTimeFirst.setTimeChangeLister(presenter::setTimeFirst)

        tfSeaPassageDistance.document.addDocumentListener(FieldListener {
            castToType(tfSeaPassageDistance.value, presenter::setSeaPassageDistance)
        })

        tfLSHFOROB_first.document.addDocumentListener(FieldListener {
            castToType(tfLSHFOROB_first.value, presenter::setLSHFO_ROB_first)
        })

        dtPOB.setDateChangeLister(presenter::setDatePOB)
        dtPOB.setTimeChangeLister(presenter::setTimePOB)

        tfMGO_ROB_01_first.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_01_first.value, presenter::setMGO_01_ROB_first)
        })

        dtPilotOff.setDateChangeLister(presenter::setDatePilotOff)
        dtPilotOff.setTimeChangeLister(presenter::setTimePilotOff)

        tfMGO_ROB_05_first.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_05_first.value, presenter::setMGO_05_ROB_first)
        })

        dtDateTime_second.setDateChangeLister(presenter::setDateSecond)
        dtDateTime_second.setTimeChangeLister(presenter::setTimeSecond)

        tfManeuveringDist.document.addDocumentListener(FieldListener {
            castToType(tfManeuveringDist.value, presenter::setManeuveringDist)
        })

        tfLSHFOROB_second.document.addDocumentListener(FieldListener {
            castToType(tfLSHFOROB_second.value, presenter::setLSHFO_ROB_second)
        })

        tfFreshWaterMT.document.addDocumentListener(FieldListener {
            castToType(tfFreshWaterMT.value, presenter::setFreshWaterMT)
        })

        tfMGO_ROB_01_second.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_01_second.value, presenter::setMGO_01_ROB_second)
        })

        tfMGO_ROB_05_second.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_05_second.value, presenter::setMGO_05_ROB_second)
        })

        tfNote.document.addDocumentListener(FieldListener {
            castToType(tfNote.text, presenter::setNote)
        })

        saveBtnPanel.setClickSave {
            presenter.saveReport()
        }

        saveBtnPanel.setClickSend() {
            presenter.sendReport()
        }

        saveBtnPanel.setClickLoad {
            val fileChooser = JFileChooser()
            fileChooser.dialogTitle = "Select File"
            val filter = FileNameExtensionFilter(
                "JSON", "json"
            )
            fileChooser.fileFilter = filter
            // Определение режима - только файл
            // Определение режима - только файл
            fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY

            val result = fileChooser.showOpenDialog(this)
            // Если файл выбран, покажем его в сообщении
            // Если файл выбран, покажем его в сообщении
            if (result == JFileChooser.APPROVE_OPTION) {
                presenter.load(fileChooser.selectedFile)
            }
        }

        inflateUI()

        elementWithError.put(ComponentKey(tfVoyNo, NoValidData.VoyNo), tfVoyNo.border)
        elementWithError.put(ComponentKey(dtDateTimeFirst, NoValidData.DateTimeStatus_1), dtDateTimeFirst.border)
        elementWithError.put(ComponentKey(cbTZ, NoValidData.TimeZone), cbTZ.border)
        elementWithError.put(ComponentKey(positionFieldLatitudeFirst, NoValidData.Latitude_1), positionFieldLatitudeFirst.border)
        elementWithError.put(ComponentKey(positionFieldLongitudeFirst, NoValidData.Longitude_1), positionFieldLongitudeFirst.border)
        elementWithError.put(ComponentKey(dtDateTime_second, NoValidData.DateTimeStatus_2), dtDateTime_second.border)
        elementWithError.put(ComponentKey(positionFieldLatitudeSecond, NoValidData.Latitude_2), positionFieldLatitudeSecond.border)
        elementWithError.put(ComponentKey(positionFieldLongitudeSecond, NoValidData.Longitude_2), positionFieldLongitudeSecond.border)

    }

    private fun setVisibleUI(state: State) {

        if (state.departureTypeFirst == DepartureTypeSeaBerth.FROM_SEA) {

            jlDateFirst.text = "EOSP"

            jlPort.isVisible = false
            cbPort.isVisible = false

            jlTerminal.isVisible = false
            cbTerminal.isVisible = false

            jlPositionLatitudeFirst.isVisible = true
            positionFieldLatitudeFirst.isVisible = true

            jlPositionLongitudeFirst.isVisible = true
            positionFieldLongitudeFirst.isVisible = true

            jlSeaPassageDistance.isVisible = true
            tfSeaPassageDistance.isVisible = true

        } else {

            jlDateFirst.text = "All Сlear"

            jlPort.isVisible = true
            cbPort.isVisible = true

            jlTerminal.isVisible = true
            cbTerminal.isVisible = true

            jlPositionLatitudeFirst.isVisible = false
            positionFieldLatitudeFirst.isVisible = false

            jlPositionLongitudeFirst.isVisible = false
            positionFieldLongitudeFirst.isVisible = false

            jlSeaPassageDistance.isVisible = false
            tfSeaPassageDistance.isVisible = false

        }


        if (state.departureTypeSecond == DepartureTypeAnchorDrifting.ANCHOR) {
            jlDateTime_second.text = "Anchor drop"
        } else {
            jlDateTime_second.text= "START drift"
        }

    }

    private fun inflateUI() {

        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("Voy"))
            .addNextCellFillBoth(tfVoyNo)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Time Zone GMT"))
            .addNextCellFillBoth(cbTZ)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPort)
            .addNextCellFillBoth(cbPort)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlTerminal)
            .addNextCellFillBoth(cbTerminal)

        insertEmptyRow(30)
            .nextEmptyCell(10)
            .addNextCellAlignRightGap(fromSea)
            .nextEmptyCell(10)
            .addNextCellFillBoth(fromBerth)
            .nextEmptyCell(10)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlDateFirst)
            .addNextCellFillBoth(dtDateTimeFirst)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlSeaPassageDistance)
            .addNextCellFillBoth(tfSeaPassageDistance)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPositionLatitudeFirst)
            .addNextCellFillBoth(positionFieldLatitudeFirst)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPositionLongitudeFirst)
            .addNextCellFillBoth(positionFieldLongitudeFirst)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlLSHFOROB_first)
            .addNextCellFillBoth(tfLSHFOROB_first)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPOB)
            .addNextCellFillBoth(dtPOB)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_01_first)
            .addNextCellFillBoth(tfMGO_ROB_01_first)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPilotOff)
            .addNextCellFillBoth(dtPilotOff)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_05_first)
            .addNextCellFillBoth(tfMGO_ROB_05_first)
            .nextEmptyCell()

        insertEmptyRow(30)
            .nextEmptyCell(10)
            .addNextCellAlignRightGap(fromAnchor)
            .nextEmptyCell(10)
            .addNextCellFillBoth(fromDrifting)
            .nextEmptyCell(10)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlDateTime_second)
            .addNextCellFillBoth(dtDateTime_second)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlManeuveringDist)
            .addNextCellFillBoth(tfManeuveringDist)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPositionLatitudeSecond)
            .addNextCellFillBoth(positionFieldLatitudeSecond)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPositionLongitudeSecond)
            .addNextCellFillBoth(positionFieldLongitudeSecond)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlLSHFOROB_second)
            .addNextCellFillBoth(tfLSHFOROB_second)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlFreshWaterMT)
            .addNextCellFillBoth(tfFreshWaterMT)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_01_second)
            .addNextCellFillBoth(tfMGO_ROB_01_second)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlMGO_ROB_05_second)
            .addNextCellFillBoth(tfMGO_ROB_05_second)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlNote)
            .addNextCellFillBoth(tfNote.apply {
                background = Color(241, 255, 199)
                border = BorderFactory.createLineBorder(Color.BLUE, 1)
            })

        insertEmptyRow(30)
            .addSpan(saveBtnPanel)

    }

    private fun initRBTypeDepartureSeaBerth() {
        bgTypeDepartureSeaBerth.add(fromSea)
        bgTypeDepartureSeaBerth.add(fromBerth)
        bgTypeDepartureSeaBerth.setSelected(fromSea.model, true)
        fromSea.addActionListener { e: ActionEvent? ->
            presenter.setDepartureTypeFirst(DepartureTypeSeaBerth.FROM_SEA)
        }
        fromBerth.addActionListener { e: ActionEvent? ->
            presenter.setDepartureTypeFirst(DepartureTypeSeaBerth.FROM_BERTH)
        }
    }

    private fun initRBTypeDepartureAnchorDrifting() {

        bgTypeDepartureAnchorDrifting.add(fromAnchor)
        bgTypeDepartureAnchorDrifting.add(fromDrifting)
        bgTypeDepartureAnchorDrifting.setSelected(fromAnchor.model, true)
        fromAnchor.addActionListener { e: ActionEvent? ->
            presenter.setDepartureTypeSecond(DepartureTypeAnchorDrifting.ANCHOR)
        }
        fromDrifting.addActionListener { e: ActionEvent? ->
            presenter.setDepartureTypeSecond(DepartureTypeAnchorDrifting.DRIFTING)
        }
    }

    override fun loadFromJson(json: JsonElement) {
        val gson = Gson()
        var response: AnchorDriftResponse? = null
        try {
            response = gson.fromJson(json, AnchorDriftResponse::class.java)
            change(response)
        } catch (e: JsonSyntaxException) {
            errorChange("File recognition error!")
        }
    }

    fun change(response: AnchorDriftResponse) {

        val dataSourcePorts = DataSourcePorts.getInstance()
        val ports = dataSourcePorts.ports

        tfVoyNo.value = response.voyNo
        cbTZ.selectedItem = response.timeZone

        JComboBoxExtension.setSelectedItem(unlocodePort = response.unlocode, ports = ports, cbSeaPort = cbPort, terminalUUID = response.terminalUUID, cbTerminal = cbTerminal)

        val typeFirst = response.departureTypeFirst
        when (typeFirst) {
            0 -> {fromSea.isSelected = true
                fromBerth.isSelected = false
                presenter.setDepartureTypeFirst(DepartureTypeSeaBerth.FROM_SEA)}
            else ->{fromSea.isSelected = false
                fromBerth.isSelected = true
                presenter.setDepartureTypeFirst(DepartureTypeSeaBerth.FROM_BERTH)}
        }

        dtDateTimeFirst.setDateTime(response.dateFirst, response.timeFirst)
        tfSeaPassageDistance.value = response.seaPassageDistance

        positionFieldLatitudeFirst.setPositionWithReaction(response.latitudeFirst)
        positionFieldLongitudeFirst.setPositionWithReaction(response.longitudeFirst)

        tfLSHFOROB_first.value = response.LSHFO_ROB_first
        dtPOB.setDateTime(response.datePOB, response.timePOB)

        tfMGO_ROB_01_first.value = response.MGO_01_ROB_first
        dtPilotOff.setDateTime(response.datePilotOff, response.timePilotOff)

        tfMGO_ROB_05_first.value = response.MGO_05_ROB_first



        val typeSecond = response.departureTypeSecond
        when (typeSecond) {
            0 -> {
                fromAnchor.isSelected = true
                fromDrifting.isSelected = false
                presenter.setDepartureTypeSecond(DepartureTypeAnchorDrifting.ANCHOR)
            }
            else ->{
                fromAnchor.isSelected = false
                fromDrifting.isSelected = true
                presenter.setDepartureTypeSecond(DepartureTypeAnchorDrifting.DRIFTING)
            }
        }

        dtDateTime_second.setDateTime(response.dateSecond, response.timeSecond)
        tfManeuveringDist.value = response.maneuveringDist

        positionFieldLatitudeSecond.setPositionWithReaction(response.latitudeSecond)
        positionFieldLongitudeSecond.setPositionWithReaction(response.longitudeSecond)

        tfLSHFOROB_second.value = response.LSHFO_ROB_second
        tfFreshWaterMT.value = response.freshWaterMT

        tfMGO_ROB_01_second.value = response.MGO_01_ROB_second
        tfMGO_ROB_05_second.value = response.MGO_05_ROB_second

        tfNote.text = response.note

    }

    fun errorChange(error: String) {
        JOptionPane.showMessageDialog(this, error)
    }

}