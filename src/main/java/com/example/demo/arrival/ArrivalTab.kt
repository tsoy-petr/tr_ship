package com.example.demo.arrival

import com.example.demo.core.*
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.departure.DateTimeBox
import com.example.demo.model.ComponentKey
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
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
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.coroutines.EmptyCoroutineContext

class ArrivalTab(private val presenter: ArrivalPresenter) : TabReport() {

    private val actionChangeTerminal = ActionListener { _: ActionEvent ->
        (cbTerminalSecond.selectedItem)?.let {
            presenter.setTerminalSecond(it as TerminalDto)
        }
    }

    private val tfVoyNo = JFormattedTextField()

    private val jlPortSecond = JLabel("Port")
    private val cbPortSecond = JComboBox<SeaPortDto>()

    private val jlTerminalSecond = JLabel("Terminal")
    private val cbTerminalSecond = JComboBox<TerminalDto>()

    private val cbTZSecond = JComboBox<String>()

    private val bgDepartureTypeArrivalFromSeaAnchorageDrift = ButtonGroup()
    private val fromSea = JRadioButton("Arrival from sea")
    private val fromAnchorageDrift = JRadioButton("Arrival from Anchorage/Drift")

    private val jlDateFirst = JLabel("EOSP")
    private val dtDateTimeFirst = DateTimeBox()

    private val jlPOBFirst = JLabel("POB")
    private val dtPOBFirst = DateTimeBox()

    private val jlPositionLatitudeFirst = JLabel("Position Latitude")
    private val jlPositionLongitudeFirst = JLabel("Position Longitude")
    private var positionFieldLatitudeFirst: PositionField
    private var positionFieldLongitudeFirst: PositionField

    private val jlLSHFOROBFirst = JLabel("LSHFO ROB (mt)")
    private val tfLSHFOROBFirst = JFormattedTextField(FormatHelper.getLSHFOFormatter())

    private val jlDateTimePilotOffFirst = JLabel("Pilot Off")
    private val dtDateTimePilotOffFirst = DateTimeBox()

    private val jlMGO_ROB_01First = JLabel("MGO 0,1% ROB (mt)")
    private val tfMGO_ROB_01First = JFormattedTextField(FormatHelper.getMGO_01_ROB_Formatter())

    private val jlDateTimeTugMakeFastFirst = JLabel("Tug make fast")
    private val dtDateTimeTugMakeFastFirst = DateTimeBox()

    private val jlMGO_ROB_05First = JLabel("MGO 0,5% ROB (mt)")
    private val tfMGO_ROB_05First = JFormattedTextField(FormatHelper.getMGO_05_ROB_Formatter())

    private val jlNoOfTugsFirst = JLabel("No. of Tugs")
    private val tfNoOfTugsFirst = JFormattedTextField(FormatHelper.getNoOfTugs_Formatter())

    private val jlDateTimeSecond = JLabel("All fast")
    private val dtDateTimeSecond = DateTimeBox()

    private val jlManeuveringDistSecond = JLabel("Maneuvering Dist")
    private val tfManeuveringDistSecond = JFormattedTextField(FormatHelper.getManeuveringDist_Formatter())

    private val jlSeaPassageDistance = JLabel("Sea Passage Distance From Last Report to EOSP")
    private val tfSeaPassageDistance = JFormattedTextField(FormatHelper.getSeaPassageDistance_Formatter())

    private val jlLSHFOROBSecond = JLabel("LSHFO ROB (mt)")
    private val tfLSHFOROBSecond = JFormattedTextField(FormatHelper.getLSHFOFormatter())

    private val jlDateTimeFirstLineSecond = JLabel("First Line")
    private val dtDateTimeFirstLineSecond = DateTimeBox()

    private val jlMGO_ROB_01Second = JLabel("MGO 0,1% ROB (mt)")
    private val tfMGO_ROB_01Second = JFormattedTextField(FormatHelper.getMGO_01_ROB_Formatter())

    private val jlMGO_ROB_05Second = JLabel("MGO 0,5% ROB (mt)")
    private val tfMGO_ROB_05Second = JFormattedTextField(FormatHelper.getMGO_05_ROB_Formatter())

    private val jlDateTimeTugCastOffSecond = JLabel("Tug cast off")
    private val dtDateTimeTugCastOffSecond = DateTimeBox()

    private val jlFreshWaterMTSecond = JLabel("Fresh water (mt)")
    private val tfFreshWaterMTSecond = JFormattedTextField(FormatHelper.getFreshWater_Formatter())

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

        tfVoyNo.columns = 10
        tfVoyNo.text = DataSettings.getInstance().readSettings().voyNo
        presenter.setVoyNo(tfVoyNo.text)
        tfVoyNo.document.addDocumentListener(FieldListener {
            castToType(tfVoyNo.text, presenter::setVoyNo)
        })

        cbPortSecond.run {
            isEditable = false

            this.newActionListener({ seaPortDto, action ->
                if (action.actionCommand.equals("comboBoxChanged")) {
                    presenter.setUnlocodeSecond(seaPortDto)
                    cbTZSecond.selectedItem = seaPortDto.timeZone
                    cbTerminalSecond.removeActionListener(actionChangeTerminal)
                    FormsUtils.initListTerminals(
                        cbTerminalSecond,
                        seaPortDto,
                        setLatitude = {},
                        setLongitude = {},
                        setTerminal = presenter::setTerminalSecond
                    )
                    cbTerminalSecond.addActionListener(actionChangeTerminal)
                }
            })
        }

        cbTerminalSecond.run {
            isEditable = false
            this.addActionListener(actionChangeTerminal)
        }

        FormsUtils.initListTimeZone(cbTZSecond)
        cbTZSecond.newActionListener({ newTZ, _ ->
            presenter.setTZ_second(newTZ)
        })

        val dataSourcePorts = DataSourcePorts.getInstance()

        FormsUtils.initListPorts(
            dataSourcePorts,
            cbPortSecond,
            setPort = (
                    presenter::setUnlocodeSecond
                    ),
            terminalsCb = cbTerminalSecond,
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
                    is State.Editing -> {
                        if (state.departureTypeFirst == DepartureTypeArrivalFromSeaAnchorageDrift.FROM_SEA) {

                            jlDateFirst.text = "EOSP"

                            jlPositionLatitudeFirst.isVisible = true
                            positionFieldLatitudeFirst.isVisible = true

                            jlPositionLongitudeFirst.isVisible = true
                            positionFieldLongitudeFirst.isVisible = true

                            jlSeaPassageDistance.isVisible = true
                            tfSeaPassageDistance.isVisible = true

                        } else {

                            jlDateFirst.text = "Anchor up/Compl. drift"

                            jlPositionLatitudeFirst.isVisible = false
                            positionFieldLatitudeFirst.isVisible = false

                            jlPositionLongitudeFirst.isVisible = false
                            positionFieldLongitudeFirst.isVisible = false

                            jlSeaPassageDistance.isVisible = false
                            tfSeaPassageDistance.isVisible = false

                        }
                    }
                    else -> {
                        handleFieldsError(arrayListOf())
                    }
                }

            }
        }

        initRBDepartureTypeArrivalFromSeaAnchorageDrift()

        dtDateTimeFirst.setDateChangeLister(presenter::setDateFirst)
        dtDateTimeFirst.setTimeChangeLister(presenter::setTimeFirst)

        dtPOBFirst.setDateChangeLister(presenter::setDatePOB)
        dtPOBFirst.setTimeChangeLister(presenter::setTimePOB)

        tfLSHFOROBFirst.document.addDocumentListener(FieldListener {
            castToType(tfLSHFOROBFirst.value, presenter::setLSHFO_ROBFirst)
        })

        dtDateTimePilotOffFirst.setDateChangeLister(presenter::setDatePilotOffFirst)
        dtDateTimePilotOffFirst.setTimeChangeLister(presenter::setTimePilotOffFirst)

        tfMGO_ROB_01First.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_01First.value, presenter::setMGO_01_ROBFirst)
        })

        tfMGO_ROB_05First.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_05First.value, presenter::setMGO_05_ROBFirst)
        })

        dtDateTimeTugMakeFastFirst.setDateChangeLister(presenter::setDateTugMakeFastFirst)
        dtDateTimeTugMakeFastFirst.setTimeChangeLister(presenter::setTimeTugMakeFastFirst)

        tfNoOfTugsFirst.document.addDocumentListener(FieldListener {
            castToType(tfNoOfTugsFirst.value, presenter::setNoOfTugs)
        })


        dtDateTimeSecond.setDateChangeLister(presenter::setDateSecond)
        dtDateTimeSecond.setTimeChangeLister(presenter::setTimeSecond)

        tfManeuveringDistSecond.document.addDocumentListener(FieldListener {
            castToType(tfManeuveringDistSecond.value, presenter::setManeuveringDist)
        })

        tfSeaPassageDistance.document.addDocumentListener(FieldListener {
            castToType(tfSeaPassageDistance.value, presenter::setSeaPassageDistance)
        })

        tfLSHFOROBSecond.document.addDocumentListener(FieldListener {
            castToType(tfLSHFOROBSecond.value, presenter::setLSHFO_ROBSecond)
        })

        dtDateTimeFirstLineSecond.setDateChangeLister(presenter::setDateFirstLineSecond)
        dtDateTimeFirstLineSecond.setTimeChangeLister(presenter::setTimeFirstLineSecond)

        tfMGO_ROB_01Second.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_01Second.value, presenter::setMGO_01_ROBSecond)
        })

        tfMGO_ROB_05Second.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_05Second.value, presenter::setMGO_05_ROBSecond)
        })

        dtDateTimeTugCastOffSecond.setDateChangeLister(presenter::setDateTugCastOffSecond)
        dtDateTimeTugCastOffSecond.setTimeChangeLister(presenter::setTimeTugCastOffSecond)

        tfNote.document.addDocumentListener(FieldListener {
            castToType(tfNote.text, presenter::setNote)
        })

        tfFreshWaterMTSecond.document.addDocumentListener(FieldListener {
            castToType(tfFreshWaterMTSecond.value, presenter::setFreshWaterMT)
        })

        saveBtnPanel.setClickSave {
            presenter.saveReport()
        }
        saveBtnPanel.setClickSend {
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
        elementWithError.put(ComponentKey(cbTZSecond, NoValidData.TimeZone), cbTZSecond.border)
        elementWithError.put(ComponentKey(dtDateTimeSecond, NoValidData.DateTimeStatus_2), dtDateTimeSecond.border)
        elementWithError.put(
            ComponentKey(positionFieldLatitudeFirst, NoValidData.Latitude_1),
            positionFieldLatitudeFirst.border
        )
        elementWithError.put(
            ComponentKey(positionFieldLongitudeFirst, NoValidData.Longitude_1),
            positionFieldLongitudeFirst.border
        )

    }

    private fun inflateUI() {

        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("Voy"))
            .addNextCellFillBoth(tfVoyNo)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Time Zone GMT"))
            .addNextCellFillBoth(cbTZSecond)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPortSecond)
            .addNextCellFillBoth(cbPortSecond)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlTerminalSecond)
            .addNextCellFillBoth(cbTerminalSecond)

        insertEmptyRow(30)
            .nextEmptyCell(10)
            .addNextCellAlignRightGap(fromSea)
            .nextEmptyCell(10)
            .addNextCellFillBoth(fromAnchorageDrift)
            .nextEmptyCell(10)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlDateFirst)
            .addNextCellFillBoth(dtDateTimeFirst)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPOBFirst)
            .addNextCellFillBoth(dtPOBFirst)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPositionLatitudeFirst)
            .addNextCellFillBoth(positionFieldLatitudeFirst)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPositionLongitudeFirst)
            .addNextCellFillBoth(positionFieldLongitudeFirst)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlSeaPassageDistance)
            .addNextCellFillBoth(tfSeaPassageDistance)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlLSHFOROBFirst)
            .addNextCellFillBoth(tfLSHFOROBFirst)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlDateTimePilotOffFirst)
            .addNextCellFillBoth(dtDateTimePilotOffFirst)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_01First)
            .addNextCellFillBoth(tfMGO_ROB_01First)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlDateTimeTugMakeFastFirst)
            .addNextCellFillBoth(dtDateTimeTugMakeFastFirst)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_05First)
            .addNextCellFillBoth(tfMGO_ROB_05First)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlNoOfTugsFirst)
            .addNextCellFillBoth(tfNoOfTugsFirst)



        insertEmptyRow()
            .addNextCellAlignRightGap(jlDateTimeSecond)
            .addNextCellFillBoth(dtDateTimeSecond)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlManeuveringDistSecond)
            .addNextCellFillBoth(tfManeuveringDistSecond)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlLSHFOROBSecond)
            .addNextCellFillBoth(tfLSHFOROBSecond)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlDateTimeFirstLineSecond)
            .addNextCellFillBoth(dtDateTimeFirstLineSecond)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_01Second)
            .addNextCellFillBoth(tfMGO_ROB_01Second)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlDateTimeTugCastOffSecond)
            .addNextCellFillBoth(dtDateTimeTugCastOffSecond)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_05Second)
            .addNextCellFillBoth(tfMGO_ROB_05Second)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlFreshWaterMTSecond)
            .addNextCellFillBoth(tfFreshWaterMTSecond)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlNote)
            .addNextCellFillBoth(tfNote.apply {
                background = Color(241, 255, 199)
                border = BorderFactory.createLineBorder(Color.BLUE, 1)
            })

        insertEmptyRow(30)
            .addSpan(saveBtnPanel)
    }

    private fun initRBDepartureTypeArrivalFromSeaAnchorageDrift() {

        bgDepartureTypeArrivalFromSeaAnchorageDrift.add(fromSea)
        bgDepartureTypeArrivalFromSeaAnchorageDrift.add(fromAnchorageDrift)
        bgDepartureTypeArrivalFromSeaAnchorageDrift.setSelected(fromSea.model, true)
        fromSea.addActionListener { e: ActionEvent? ->
            presenter.setDepartureTypeFirst(DepartureTypeArrivalFromSeaAnchorageDrift.FROM_SEA)
        }
        fromAnchorageDrift.addActionListener { e: ActionEvent? ->
            presenter.setDepartureTypeFirst(DepartureTypeArrivalFromSeaAnchorageDrift.FROM_ANCHORAGE_DRIFT)
        }

    }

    override fun loadFromJson(json: JsonElement) {
        val gson = Gson()
        var response: ArrivalResponse? = null
        try {
            response = gson.fromJson(json, ArrivalResponse::class.java)
            change(response)
        } catch (e: JsonSyntaxException) {
            errorChange("File recognition error!")
        }
    }

    fun change(response: ArrivalResponse) {

        val dataSourcePorts = DataSourcePorts.getInstance()
        val ports = dataSourcePorts.ports

        JComboBoxExtension.setSelectedItem(unlocodePort = response.unlocode_second, ports = ports, cbSeaPort = cbPortSecond, terminalUUID = response.terminalUUID_second, cbTerminal = cbTerminalSecond)

        tfVoyNo.value = response.voyNo
        cbTZSecond.selectedItem = response.timeZone_second

        val typeFirst = response.departureType_first
        when (typeFirst) {
            0 -> {
                fromSea.isSelected = true
                fromAnchorageDrift.isSelected = false
                presenter.setDepartureTypeFirst(DepartureTypeArrivalFromSeaAnchorageDrift.FROM_SEA)
            }
            else -> {
                fromSea.isSelected = false
                fromAnchorageDrift.isSelected = true
                presenter.setDepartureTypeFirst(DepartureTypeArrivalFromSeaAnchorageDrift.FROM_ANCHORAGE_DRIFT)
            }
        }

        dtDateTimeFirst.setDateTime(response.dateFirst, response.timeFirst)
        dtPOBFirst.setDateTime(response.datePOB_first, response.timePOB_first)

        positionFieldLatitudeFirst.setPositionWithReaction(response.latitudeFirst)
        positionFieldLongitudeFirst.setPositionWithReaction(response.longitudeFirst)

        tfSeaPassageDistance.value = response.seaPassageDistance

        tfLSHFOROBFirst.value = response.LSHFO_ROB_first
        dtDateTimePilotOffFirst.setDateTime(response.datePilotOff_first, response.timePilotOff_first)

        tfMGO_ROB_01First.value = response.MGO_01_ROB_first
        dtDateTimeTugMakeFastFirst.setDateTime(response.dateTugMakeFast_first, response.timeTugMakeFast_first)

        tfMGO_ROB_05First.value = response.MGO_05_ROB_first
        tfNoOfTugsFirst.value = response.noOfTugs


        dtDateTimeSecond.setDateTime(response.date_second, response.time_second)
        tfManeuveringDistSecond.value = response.maneuveringDist

        tfLSHFOROBSecond.value = response.LSHFO_ROB_second
        dtDateTimeFirst.setDateTime(response.dateFirst, response.timeFirst)

        tfMGO_ROB_01Second.value = response.MGO_01_ROB_second
        dtDateTimeTugCastOffSecond.setDateTime(response.dateTugCastOff_second, response.timeTugCastOff_second)

        tfMGO_ROB_05Second.value = response.MGO_05_ROB_second
        tfFreshWaterMTSecond.value = response.freshWaterMT_second

        tfNoOfTugsFirst.text = response.note
    }

    fun errorChange(error: String) {
        JOptionPane.showMessageDialog(this, error)
    }
}