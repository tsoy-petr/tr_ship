package com.example.demo.noon

import com.example.demo.core.*
import com.example.demo.core.JComboBoxExtension.setSelectedItem
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.departure.DateTimeBox
import com.example.demo.departure.MEMode
import com.example.demo.model.ComponentKey
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import com.example.demo.noon.Status.valueOf
import com.example.demo.settings.DataSettings
import com.example.demo.utils.FormatHelper
import com.example.demo.utils.FormsUtils
import com.example.demo.utils.initItems
import com.example.demo.utils.newActionListener
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.awt.Color
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.coroutines.EmptyCoroutineContext

class NoonTab(
    private val noonPresenter: NoonPresenter
) : TabReport() {

    private val actionChangeTerminal = ActionListener { _: ActionEvent ->
        (cbTerminal.selectedItem)?.let {
            noonPresenter.setTerminal(it as TerminalDto)
        }
    }

    private val tfVoyNo = JFormattedTextField()

    private val jlPort = JLabel("Port")
    private val cbPort = JComboBox<SeaPortDto>()

    private val jlTerminal = JLabel("Terminal")
    private val cbTerminal = JComboBox<TerminalDto>()

    private val cbLastPort = JComboBox<SeaPortDto>()
    private val cbNextPort = JComboBox<SeaPortDto>()
    private val cbTZ = JComboBox<String>()
    private val dateTimeLT = DateTimeBox()
    private val cbStatus = JComboBox<Status>()

    private val jlPositionLatitude = JLabel("Position Latitude")
    private val jlPositionLongitude = JLabel("Position Longitude")
    private var positionFieldLatitude: PositionField
    private var positionFieldLongitude: PositionField

    private val jlSeaPassageDistance = JLabel("Sea Passage Distance (NM)")
    private val tfSeaPassageDistance = JFormattedTextField(FormatHelper.getSeaPassageDistance_Formatter())

    private val jlDistanceToGo = JLabel("Distance to go (NM)")
    private val tfDistanceToGo = JFormattedTextField(FormatHelper.getDistanceToGo_Formatter())

    private val jlETADateTime = JLabel("ETA Date & Time")
    private val dtETADateTime = DateTimeBox()

    private val jlMeMode = JLabel("ME mode (Eco/Full)")
    private val cbMeMode = JComboBox<MEMode>()

    private val jlMERPM = JLabel("ME RPM")
    private val tfMERPM = JFormattedTextField(FormatHelper.getMERPM())

    private val jlCourse = JLabel("Course")
    private val tfCourse = JFormattedTextField(FormatHelper.getCourse_Formatter())

    private val jlHFOROB = JLabel("HFO ROB (mt)")
    private val tfHFOROB = JFormattedTextField(FormatHelper.getHFO_ROB_Formatter())

    private val jlSpeed = JLabel("Speed")
    private val tfSpeed = JFormattedTextField(FormatHelper.getSpeed_Formatter())

    private val jlMGO_ROB_01 = JLabel("MGO 0,1% ROB (mt)")
    private val tfMGO_ROB_01 = JFormattedTextField(FormatHelper.getMGO_01_ROB_Formatter())

    private val jlMGO_ROB_05 = JLabel("MGO 0,5% ROB (mt)")
    private val tfMGO_ROB_05 = JFormattedTextField(FormatHelper.getMGO_05_ROB_Formatter())

    private val jlFreshWaterMT = JLabel("Fresh water (mt)")
    private val tfFreshWaterMT = JFormattedTextField(FormatHelper.getFreshWater_Formatter())

    private val jlWindScaleBeaufourt = JLabel("Wind scale (beaufourt)")
    private val tfWindScaleBeaufourt = JFormattedTextField(FormatHelper.getWindScaleBeaufourt_Formatter())

    private val jlWindDirection = JLabel("Wind direction (0-360)")
    private val tfWindDirection = JFormattedTextField(FormatHelper.getWindDirection_Formatter())

    private val jlSwellHeight = JLabel("Swell Height (m)")
    private val tfSwellHeight = JFormattedTextField(FormatHelper.getSwellHeight_Formatter())

    private val jlSwellDirection = JLabel("Swell direction (0-360)")
    private val tfSwellDirection = JFormattedTextField(FormatHelper.getSwellDirection_Formatter())

    private val jlNote = JLabel("Note")
    private val tfNote = JTextArea(3, 25)

    private val saveBtnPanel = SaveBtnPanel(true, false, true, true)

    init {

        tfVoyNo.columns = 10
        tfVoyNo.text = DataSettings.getInstance().readSettings().voyNo
        noonPresenter.setVoyNo(tfVoyNo.text)
        tfVoyNo.document.addDocumentListener(FieldListener {
            castToType(tfVoyNo.text, noonPresenter::setVoyNo)
        })

        cbPort.run {
            isEditable = false

            this.newActionListener({ seaPortDto, action ->
                if (action.actionCommand.equals("comboBoxChanged")) {
                    noonPresenter.setUnlocode(seaPortDto)

                    cbTerminal.removeActionListener(actionChangeTerminal)
                    FormsUtils.initListTerminals(
                        cbTerminal,
                        seaPortDto,
                        setLatitude = {},
                        setLongitude = {},
                        setTerminal = noonPresenter::setTerminal
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
            noonPresenter.setTZ(newTZ)
        })

        cbLastPort.run {
            isEditable = false
            this.newActionListener({ selectedSeaPort, actionEvent ->
                if (actionEvent.actionCommand.equals("comboBoxChanged")) {
                    noonPresenter.setUnlocodeLast(selectedSeaPort)
                }
            })
        }
        cbNextPort.run {
            isEditable = false
            this.newActionListener({ selectedSeaPort, actionEvent ->
                if (actionEvent.actionCommand.equals("comboBoxChanged")) {
                    noonPresenter.setUnlocodeNext(selectedSeaPort)
                }
            })
        }

        val dataSourcePorts = DataSourcePorts.getInstance()

        FormsUtils.initListPorts(
            dataSourcePorts,
            cbPort,
            setPort = (
                    noonPresenter::setUnlocode
                    ),
            terminalsCb = cbTerminal,
            setUnlocode = {},
            setLatitude = {},
            setLongitude = {}, setTerminal = {})

        FormsUtils.initListPorts(
            dataSourcePorts,
            cbLastPort,
            setPort = (
                    noonPresenter::setUnlocodeLast
                    ),
            setUnlocode = {},
            setLatitude = {},
            setLongitude = {}, setTerminal = {})

        FormsUtils.initListPorts(
            DataSourcePorts.getInstance(),
            cbNextPort,
            setPort = (
                    noonPresenter::setUnlocodeNext
                    ),
            setUnlocode = {},
            setLatitude = {},
            setLongitude = {}, setTerminal = {})

        dateTimeLT.setDateChangeLister(noonPresenter::setDateLT)
        dateTimeLT.setTimeChangeLister(noonPresenter::setTimeLT)

        cbStatus.run {
            removeAllItems()
            isEditable = false
            this.newActionListener({ newStatus, _ ->
                noonPresenter.setStatus(newStatus)
            })
            addItem(Status.AtSeeAdrift)
            addItem(Status.ASseeUnderway)
            addItem(Status.AtAnchor)
            addItem(Status.InPort)
        }

        positionFieldLatitude = PositionField(
            Position(Position.TypePosition.Latitude, 0, 0.0, Position.Hemisphere.N),
            noonPresenter::setPositionLatitude
        )
        positionFieldLongitude = PositionField(
            Position(Position.TypePosition.Longitude, 0, 0.0, Position.Hemisphere.E),
            noonPresenter::setPositionLongitude
        )

        tfSeaPassageDistance.document.addDocumentListener(FieldListener {
            castToType(tfSeaPassageDistance.value, noonPresenter::setSeaPassageDistance)
        })

        tfDistanceToGo.document.addDocumentListener(FieldListener {
            castToType(tfDistanceToGo.value, noonPresenter::setDistanceToGo)
        })

        dtETADateTime.setDateChangeLister(noonPresenter::setETADate)
        dtETADateTime.setTimeChangeLister(noonPresenter::setETATime)



        cbMeMode.initItems(listOf(MEMode.ECO, MEMode.FULL), noonPresenter::setMeMode)
        cbMeMode.newActionListener({ currMode, _ ->
            noonPresenter.setMeMode(currMode)
        })

        tfMERPM.document.addDocumentListener(FieldListener {
            castToType(tfMERPM.value, noonPresenter::setMERPM)
        })

        tfCourse.document.addDocumentListener(FieldListener {
            castToType(tfCourse.value, noonPresenter::setCourse)
        })

        tfHFOROB.document.addDocumentListener(FieldListener {
            castToType(tfHFOROB.value, noonPresenter::setHFOROB)
        })

        tfSpeed.document.addDocumentListener(FieldListener {
            castToType(tfSpeed.value, noonPresenter::setSpeed)
        })

        tfMGO_ROB_01.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_01.value, noonPresenter::setMGO_ROB_01)
        })

        tfMGO_ROB_05.document.addDocumentListener(FieldListener {
            castToType(tfMGO_ROB_05.value, noonPresenter::setMGO_ROB_05)
        })

        tfFreshWaterMT.document.addDocumentListener(FieldListener {
            castToType(tfFreshWaterMT.value, noonPresenter::setFreshWaterMT)
        })

        tfWindScaleBeaufourt.document.addDocumentListener(FieldListener {
            castToType(tfWindScaleBeaufourt.value, noonPresenter::setWindScaleBeaufourt)
        })

        tfWindDirection.document.addDocumentListener(FieldListener {
            castToType(tfWindDirection.value, noonPresenter::setWindDirection)
        })

        tfSwellHeight.document.addDocumentListener(FieldListener {
            castToType(tfSwellHeight.value, noonPresenter::setSwellHeight)
        })

        tfSwellDirection.document.addDocumentListener(FieldListener {
            castToType(tfSwellDirection.value, noonPresenter::setSwellDirection)
        })

        tfNote.document.addDocumentListener(FieldListener {
            castToType(tfNote.text, noonPresenter::setNote)
        })

        saveBtnPanel.setClickSave {
            noonPresenter.saveReport()
        }

        saveBtnPanel.setClickSend {
            noonPresenter.sendReport()
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
                noonPresenter.load(fileChooser.selectedFile)
            }
        }

        inflateUI()

        elementWithError.put(ComponentKey(tfVoyNo, NoValidData.VoyNo), tfVoyNo.border)
        elementWithError.put(ComponentKey(cbTZ, NoValidData.TimeZone), cbTZ.border)
        elementWithError.put(ComponentKey(cbPort, NoValidData.Unlocode), cbPort.border)
        elementWithError.put(ComponentKey(cbNextPort, NoValidData.UnlocodeNext), cbNextPort.border)
        elementWithError.put(ComponentKey(cbLastPort, NoValidData.UnlocodeLast), cbLastPort.border)
        elementWithError.put(ComponentKey(dateTimeLT, NoValidData.DateTimeStatus_1), dateTimeLT.border)
        elementWithError.put(ComponentKey(positionFieldLatitude, NoValidData.Latitude_1), positionFieldLatitude.border)
        elementWithError.put(
            ComponentKey(positionFieldLongitude, NoValidData.Longitude_1),
            positionFieldLongitude.border
        )
        elementWithError.put(ComponentKey(cbTerminal, NoValidData.Terminal), cbTerminal.border)

        CoroutineScope(context = EmptyCoroutineContext).launch {
            noonPresenter.stateReport.collectLatest { state ->
                val isAtAnchor = state is State.AtAnchor
                val isInPort = state is State.InPort
                val isUpload = state is State.Upload
                val isUploadSuccess = state is State.UploadSuccess
                val isUploadError = state is State.UploadError

                val visiblePortTerminal =
                    isInPort || isAtAnchor || (isUploadSuccess && state is State.UploadSuccess && (state.status == Status.AtAnchor || state.status == Status.InPort))
                jlPort.isVisible = visiblePortTerminal
                cbPort.isVisible = visiblePortTerminal
                jlTerminal.isVisible = visiblePortTerminal
                cbTerminal.isVisible = visiblePortTerminal

                if (isAtAnchor) {

                    jlSeaPassageDistance.isVisible = !isAtAnchor
                    tfSeaPassageDistance.isVisible = !isAtAnchor

                    jlDistanceToGo.isVisible = !isAtAnchor
                    tfDistanceToGo.isVisible = !isAtAnchor

                    jlMERPM.isVisible = !isAtAnchor
                    tfMERPM.isVisible = !isAtAnchor

                    jlMeMode.isVisible = !isAtAnchor
                    cbMeMode.isVisible = !isAtAnchor

                    jlCourse.isVisible = !isAtAnchor
                    tfCourse.isVisible = !isAtAnchor

                    jlSpeed.isVisible = !isAtAnchor
                    tfSpeed.isVisible = !isAtAnchor

                    jlETADateTime.isVisible = true
                    dtETADateTime.isVisible = true

                    tfSeaPassageDistance.value = 0
                    tfMERPM.value = 0
                    tfDistanceToGo.value = 0
                    cbMeMode.selectedItem = MEMode.ECO
                    tfCourse.value = 0
                    tfSpeed.value = 0

                } else if (isInPort) {

                    jlPositionLatitude.isVisible = !isInPort
                    jlPositionLongitude.isVisible = !isInPort
                    positionFieldLatitude.isVisible = !isInPort
                    positionFieldLongitude.isVisible = !isInPort

                    jlSeaPassageDistance.isVisible = !isInPort
                    tfSeaPassageDistance.isVisible = !isInPort

                    jlETADateTime.isVisible = !isInPort
                    dtETADateTime.isVisible = !isInPort

                    jlMERPM.isVisible = !isInPort
                    tfMERPM.isVisible = !isInPort

                    jlDistanceToGo.isVisible = !isInPort
                    tfDistanceToGo.isVisible = !isInPort

                    jlMeMode.isVisible = !isInPort
                    cbMeMode.isVisible = !isInPort

                    jlCourse.isVisible = !isInPort
                    tfCourse.isVisible = !isInPort

                    jlSpeed.isVisible = !isInPort
                    tfSpeed.isVisible = !isInPort

                    jlPort.isVisible = isInPort
                    cbPort.isVisible = isInPort

                    jlTerminal.isVisible = isInPort
                    cbTerminal.isVisible = isInPort

                    tfSeaPassageDistance.value = 0
                    dtETADateTime.resetTime()
                    tfMERPM.value = 0
                    tfDistanceToGo.value = 0
                    cbMeMode.selectedItem = MEMode.ECO
                    tfCourse.value = 0
                    tfSpeed.value = 0
                    positionFieldLatitude.reset()
                    positionFieldLongitude.reset()

                } else if (isUpload) {

                } else if (isUploadSuccess) {
                    handleFieldsError(arrayListOf())
                } else if (isUploadError) {
                    JOptionPane.showMessageDialog(saveBtnPanel, (state as State.UploadError).message)
                    handleFieldsError(state.noValidData)
                } else {

                    jlPositionLongitude.isVisible = true
                    jlPositionLatitude.isVisible = true
                    positionFieldLatitude.isVisible = true
                    positionFieldLongitude.isVisible = true

                    jlSeaPassageDistance.isVisible = true
                    tfSeaPassageDistance.isVisible = true

                    jlETADateTime.isVisible = true
                    dtETADateTime.isVisible = true

                    jlMERPM.isVisible = true
                    tfMERPM.isVisible = true

                    jlDistanceToGo.isVisible = true
                    tfDistanceToGo.isVisible = true

                    jlMeMode.isVisible = true
                    cbMeMode.isVisible = true

                    jlCourse.isVisible = true
                    tfCourse.isVisible = true

                    jlSpeed.isVisible = true
                    tfSpeed.isVisible = true

                    jlPort.isVisible = false
                    cbPort.isVisible = false

                    jlTerminal.isVisible = false
                    cbTerminal.isVisible = false

                }
            }
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
            .addNextCellAlignRightGap(jlPort)
            .addNextCellFillBoth(cbPort)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlTerminal)
            .addNextCellFillBoth(cbTerminal)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPositionLatitude)
            .addNextCellFillBoth(positionFieldLatitude)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPositionLongitude)
            .addNextCellFillBoth(positionFieldLongitude)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlSeaPassageDistance)
            .addNextCellFillBoth(tfSeaPassageDistance)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlDistanceToGo)
            .addNextCellFillBoth(tfDistanceToGo)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlETADateTime)
            .addNextCellFillBoth(dtETADateTime)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlMeMode)
            .addNextCellFillBoth(cbMeMode)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMERPM)
            .addNextCellFillBoth(tfMERPM)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlCourse)
            .addNextCellFillBoth(tfCourse)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlHFOROB)
            .addNextCellFillBoth(tfHFOROB)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlSpeed)
            .addNextCellFillBoth(tfSpeed)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_01)
            .addNextCellFillBoth(tfMGO_ROB_01)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlFreshWaterMT)
            .addNextCellFillBoth(tfFreshWaterMT)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlMGO_ROB_05)
            .addNextCellFillBoth(tfMGO_ROB_05)

        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("<html><font color=green>Present Weather</font></html>").apply {
                font = Font(font.name, Font.ITALIC, font.size + 3)

            })

        insertEmptyRow()
            .addNextCellAlignRightGap(jlWindScaleBeaufourt)
            .addNextCellFillBoth(tfWindScaleBeaufourt)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlWindDirection)
            .addNextCellFillBoth(tfWindDirection)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlSwellHeight)
            .addNextCellFillBoth(tfSwellHeight)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlSwellDirection)
            .addNextCellFillBoth(tfSwellDirection)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlNote)
            .addNextCellFillBoth(tfNote.apply {
                background = Color(241, 255, 199)
                border = BorderFactory.createLineBorder(Color.BLUE, 1)
            })

        insertEmptyRow(30)
            .addSpan(saveBtnPanel)
    }

    override fun loadFromJson(json: JsonElement) {
        val gson = Gson()
        var response: NoonResponse? = null
        try {
            response = gson.fromJson(json, NoonResponse::class.java)
            change(response)
        } catch (e: JsonSyntaxException) {
            errorChange("File recognition error!")
        }
    }

    fun change(response: NoonResponse) {

        val dataSourcePorts = DataSourcePorts.getInstance()
        val ports = dataSourcePorts.ports

        tfVoyNo.value = response.voyNo
        cbTZ.selectedItem = response.timeZone

        setSelectedItem(unlocodePort = response.unlocodeLast, ports = ports, cbSeaPort = cbLastPort)
        setSelectedItem(unlocodePort = response.unlocodeNext, ports = ports, cbSeaPort = cbNextPort)

        dateTimeLT.setDateTime(response.dateLt, response.timeLt)
        val newStatus = NoonResponse.findStatusByString(response.status)
        cbStatus.selectedItem = newStatus
        noonPresenter.setStatus(newStatus)

        positionFieldLatitude.setPositionWithReaction(response.latitude)
        positionFieldLongitude.setPositionWithReaction(response.longitude)

        tfSeaPassageDistance.value = response.seaPassageDistance
        tfDistanceToGo.value = response.distanceToGo

        dtETADateTime.setDateTime(response.dateETA, response.timeETA)
        cbMeMode.selectedItem = MEMode.valueOf(response.meMode)

        tfMERPM.value = response.meRPM
        tfCourse.value = response.course

        tfHFOROB.value = response.hfoROB
        tfSpeed.value = response.speed

        tfMGO_ROB_01.value = response.mgo_01_ROB
        tfFreshWaterMT.value = response.freshWater

        tfMGO_ROB_05.value = response.mgo_05_ROB

        tfWindScaleBeaufourt.value = response.windScaleBeaufourt
        tfWindDirection.value = response.windDirection

        tfSwellHeight.value = response.swellHeight
        tfSwellDirection.value = response.swellDirection

        tfNote.text = response.note

    }

    fun errorChange(error: String) {
        JOptionPane.showMessageDialog(this, error)
    }

}