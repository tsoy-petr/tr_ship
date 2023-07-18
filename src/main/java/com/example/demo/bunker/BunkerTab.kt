package com.example.demo.bunker

import com.example.demo.arrival.ArrivalResponse
import com.example.demo.core.*
import com.example.demo.dataPorts.DataSourcePorts
import com.example.demo.departure.DateTimeBox
import com.example.demo.model.ComponentKey
import com.example.demo.model.SeaPortDto
import com.example.demo.model.TerminalDto
import com.example.demo.settings.DataSettings
import com.example.demo.utils.FormatHelper
import com.example.demo.utils.FormsUtils
import com.example.demo.utils.GridBagHelper
import com.example.demo.utils.newActionListener
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.awt.Color
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.coroutines.EmptyCoroutineContext

class BunkerTab(private val presenter: BunkerPresenter) : TabReport() {

    private val actionChangeTerminal = ActionListener { _: ActionEvent ->
        (cbTerminalSecond.selectedItem)?.let {
            presenter.setTerminal(it as TerminalDto)
        }
    }

    private val tfVoyNo = JFormattedTextField()

    private val jlPort = JLabel("Port")
    private val cbPort = JComboBox<SeaPortDto>()

    private val jlTerminalSecond = JLabel("Terminal")
    private val cbTerminalSecond = JComboBox<TerminalDto>()

    private val cbTZ = JComboBox<String>()

    private val jlDate = JLabel("Date")
    private val dtDateTime = DateTimeBox()

    private val jlPositionLatitude = JLabel("Position Latitude")
    private val jlPositionLongitude = JLabel("Position Longitude")
    private var positionFieldLatitude: PositionField
    private var positionFieldLongitude: PositionField

    private val jlReceivedFuel = JLabel("Received (mt)")
    private val tfReceivedFuel = JFormattedTextField(FormatHelper.getReceivedFuel_Formatter())

    private val cbStatusShip = JComboBox<StatusShip>()
    private val cbTypeOfFuel = JComboBox<TypeOfFuel>()

    private val jlNote = JLabel("Note")
    private val tfNote = JTextArea(3, 50)

    private val saveBtnPanel = SaveBtnPanel(true, false, true, true)

    init {

        FormsUtils.initListTimeZone(cbTZ)
        cbTZ.newActionListener({ newTZ, _ ->
            presenter.setTZ(newTZ)
        })

        FormsUtils.iniListStatusShip(cbStatusShip)
        cbStatusShip.newActionListener({ newStatusShip, _ ->
            presenter.setStatusShip(newStatusShip)
        })

        FormsUtils.iniListTypesOfFuel(cbTypeOfFuel)
        cbTypeOfFuel.newActionListener({ newTypeOfFuel, _ ->
            presenter.setTypeOfFuel(newTypeOfFuel)
        })

        positionFieldLatitude = PositionField(
            Position(Position.TypePosition.Latitude, 0, 0.0, Position.Hemisphere.N)
        ) { newPosition ->
            presenter.setPositionLatitude(newPosition)
        }
        positionFieldLongitude = PositionField(
            Position(Position.TypePosition.Longitude, 0, 0.0, Position.Hemisphere.E),
            presenter::setPositionLongitude
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
                    cbTerminalSecond.removeActionListener(actionChangeTerminal)
                    FormsUtils.initListTerminals(
                        cbTerminalSecond,
                        seaPortDto,
                        setLatitude = {},
                        setLongitude = {},
                        setTerminal = presenter::setTerminal
                    )
                    cbTerminalSecond.addActionListener(actionChangeTerminal)
                }
            })
        }

        cbStatusShip.newActionListener({ statusShip, action ->
            if (action.actionCommand.equals("comboBoxChanged")) {
                presenter.setStatusShip(statusShip)
            }
        })

        cbTypeOfFuel.newActionListener({ typeOfFuel, action ->
            if (action.actionCommand.equals("comboBoxChanged")) {
                presenter.setTypeOfFuel(typeOfFuel)
            }
        })

        val dataSourcePorts = DataSourcePorts.getInstance()

        FormsUtils.initListPorts(
            dataSourcePorts,
            cbPort,
            setPort = (
                    presenter::setUnlocode
                    ),
            terminalsCb = cbTerminalSecond,
            setUnlocode = {},
            setLatitude = {},
            setLongitude = {}, setTerminal = {})

        dtDateTime.setDateChangeLister(presenter::setDate)
        dtDateTime.setTimeChangeLister(presenter::setTime)



        tfReceivedFuel.document.addDocumentListener(FieldListener {
            castToType(tfReceivedFuel.value, presenter::setReceivedFuel)
        })

        tfNote.document.addDocumentListener(FieldListener {
            castToType(tfNote.text, presenter::setNote)
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

        CoroutineScope(context = EmptyCoroutineContext).launch {
            presenter.stateReport.collectLatest { state ->

                when (state) {
                    is State.UploadError -> {
                        JOptionPane.showMessageDialog(tfVoyNo, state.message)
                        handleFieldsError(state.noValidData)
                    }
                    else -> {
                        jlDate.text = state.titleDateReport
                        handleFieldsError(arrayListOf())
                    }
                }

            }
        }

        inflateUI()

        elementWithError.put(ComponentKey(tfVoyNo, NoValidData.VoyNo), tfVoyNo.border)
        elementWithError.put(ComponentKey(cbTZ, NoValidData.TimeZone), cbTZ.border)
        elementWithError.put(ComponentKey(cbPort, NoValidData.Unlocode), cbPort.border)
        elementWithError.put(ComponentKey(dtDateTime, NoValidData.DateTimeStatus_1), dtDateTime.border)
        elementWithError.put(ComponentKey(positionFieldLatitude, NoValidData.Latitude_1), positionFieldLatitude.border)
        elementWithError.put(ComponentKey(positionFieldLongitude, NoValidData.Longitude_1), positionFieldLongitude.border)
        elementWithError.put(ComponentKey(tfReceivedFuel, NoValidData.ReceivedFuel), tfReceivedFuel.border)
    }

    private fun inflateUI() {

        insertEmptyRow()
            .addNextCellAlignRightGap(JLabel("Voy"))
            .addNextCellFillBoth(tfVoyNo)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Time Zone GMT"))
            .addNextCellFillBoth(cbTZ)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPositionLatitude)
            .addNextCellFillBoth(positionFieldLatitude)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlPositionLongitude)
            .addNextCellFillBoth(positionFieldLongitude)

        insertEmptyRow()
            .addNextCellAlignRightGap(jlPort)
            .addNextCellFillBoth(cbPort)
            .nextEmptyCell()
            .addNextCellAlignRightGap(JLabel("Status"))
            .addNextCellFillBoth(cbStatusShip)


        insertEmptyRow(20)
            .addSpan(initTypeOfFluePanel())

        insertEmptyRow()
            .addNextCellAlignRightGap(jlDate)
            .addNextCellFillBoth(dtDateTime)
            .nextEmptyCell()
            .addNextCellAlignRightGap(jlReceivedFuel)
            .addNextCellFillBoth(tfReceivedFuel)

        insertEmptyRow(20)
            .addSpan(iniNotePanel())

        insertEmptyRow(30)
            .addSpan(saveBtnPanel)

    }

    private fun initTypeOfFluePanel() : JPanel{
        val jpTypeOfFuel = JPanel()
        jpTypeOfFuel.layout = GridBagLayout()
        val jpHelper = GridBagHelper()
        jpTypeOfFuel.add(JLabel("Type of fuel"), jpHelper.nextCell().fillHorizontally().get())
        jpHelper.nextEmptyCell(jpTypeOfFuel, 10)
        jpTypeOfFuel.add(cbTypeOfFuel, jpHelper.nextCell().fillHorizontally().get())
        return jpTypeOfFuel
    }

    private fun iniNotePanel() : JPanel {
        val notePanel = JPanel()
        notePanel.layout = GridBagLayout()
        val jpHelper = GridBagHelper()
        notePanel.add(jlNote, jpHelper.nextCell().fillHorizontally().get())
        jpHelper.insertEmptyRow(notePanel, 5)
        notePanel.add(tfNote.apply {
            background = Color(241, 255, 199)
            border = BorderFactory.createLineBorder(Color.BLUE, 1)
        }, jpHelper.span().get())
        return notePanel
    }

    override fun loadFromJson(json: JsonElement) {
        val gson = Gson()
        var response: BunkerResponse? = null
        try {
            response = gson.fromJson(json, BunkerResponse::class.java)
            change(response)
        } catch (e: JsonSyntaxException) {
            errorChange("File recognition error!")
        }
    }

    fun change(response: BunkerResponse) {

        val dataSourcePorts = DataSourcePorts.getInstance()
        val ports = dataSourcePorts.ports

        tfVoyNo.value = response.voyNo

        JComboBoxExtension.setSelectedItem(unlocodePort = response.unlocode, ports = ports, cbSeaPort = cbPort, terminalUUID = response.terminalUUID, cbTerminal = cbTerminalSecond)

        cbTZ.selectedItem = response.timeZone

        positionFieldLatitude.setPositionWithReaction(response.latitude)
        positionFieldLongitude.setPositionWithReaction(response.longitude)

        cbStatusShip.selectedItem = response.statusShip
        cbTypeOfFuel.selectedItem = response.typeOfFuel

        dtDateTime.setDateTime(response.date, response.time)
        tfReceivedFuel.value = response.receivedFuel

        tfNote.text = response.note

    }

    fun errorChange(error: String) {
        JOptionPane.showMessageDialog(this, error)
    }

}