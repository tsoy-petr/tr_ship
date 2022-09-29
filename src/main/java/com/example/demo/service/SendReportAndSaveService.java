package com.example.demo.service;

import com.example.demo.core.model.ResultSendDeparture;
import com.example.demo.model.DepartureResponse;
import com.example.demo.model.SettingsEmailDto;
import com.example.demo.settings.SettingsPresenter;
import com.example.demo.utils.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendReportAndSaveService {

    public static final String DATE_FORMAT_NOW = "yyyy_MM_dd_HH_mm_ss";

    private static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public ResultSendDeparture saveDeparture(SettingsEmailDto settingsEmailDto, DepartureResponse response) {

        String folderForSavingReports = settingsEmailDto.getFolderForSavingReports();

        if (folderForSavingReports.isEmpty()) {
            return new ResultSendDeparture(false, "Path to save reports not configured!");
        }

        if (! Common.fileExists(folderForSavingReports)) {
            return new ResultSendDeparture(false, "Path to save file does not exist!");
        }

        String fileName = "Report_Departure_" + now() + ".json";

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(response);

        try {

            File saveDirectory = new File(folderForSavingReports);
            FileWriter file = new FileWriter(saveDirectory.getAbsolutePath() + File.separator + fileName);
            file.write(json);
            file.close();

            return new ResultSendDeparture(true, "");

        } catch (Exception mex) {
            return new ResultSendDeparture(false, mex.getMessage());
        }
    }

    public ResultSendDeparture saveAndSandDeparture(SettingsEmailDto settingsEmailDto, DepartureResponse response) {

        ResultSendDeparture reportSave = saveDeparture(settingsEmailDto, response);

        if (reportSave.getSuccess()) {

            ResultSendDeparture reportSend = sendDeparture(settingsEmailDto, response);

            if (reportSend.getSuccess()) {
                return new ResultSendDeparture(true, "");
            } else {
                return reportSend;
            }

        } else {
            return reportSave;
        }

    }

    private ResultSendDeparture sendDeparture(SettingsEmailDto settingsEmailDto, DepartureResponse response) {

        Desktop desktop;

        if (Desktop.isDesktopSupported()
                && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
            URI mailto = null;
            try {

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                String json = gson.toJson(response);

                mailto = new URI("mailto:" + Common.urlEncode((new SettingsPresenter()).readSettings().getServerEmail()) + "?subject=" + Common.urlEncode("Departure") + "&body="
                        + Common.urlEncode(json));
            } catch (URISyntaxException uriSyntaxException) {
                return new ResultSendDeparture(false, uriSyntaxException.getMessage());
            }
            try {
                desktop.mail(mailto);
                return new ResultSendDeparture(true, "");
            } catch (IOException ioException) {
                return new ResultSendDeparture(false, ioException.getMessage());
            }
        } else {
            return new ResultSendDeparture(false, "desktop doesn't support mailto; mail is dead anyway ");
        }

    }

}
