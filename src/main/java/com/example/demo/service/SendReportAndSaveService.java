package com.example.demo.service;

import com.example.demo.core.model.ResultSendDeparture;
import com.example.demo.model.DepartureResponse;
import com.example.demo.model.SettingsEmailDto;
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
import java.util.ArrayList;
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

        if (!Common.fileExists(folderForSavingReports)) {
            return new ResultSendDeparture(false, "Path to save file does not exist!");
        }

        String fileName = "Report_Departure_" + now() + ".json";

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(response);

        try {

            File saveDirectory = new File(folderForSavingReports);

            String pathFile = saveDirectory.getAbsolutePath() + File.separator + fileName;

            FileWriter file = new FileWriter(pathFile);
            file.write(json);
            file.close();
            File path = new File(pathFile);
            return new ResultSendDeparture(true, "", path.getAbsolutePath());

        } catch (Exception mex) {
            return new ResultSendDeparture(false, mex.getMessage());
        }
    }

    public ResultSendDeparture saveAndSandDeparture(SettingsEmailDto settingsEmailDto, DepartureResponse response) {

        ResultSendDeparture reportSave = saveDeparture(settingsEmailDto, response);

        if (reportSave.getSuccess()) {

            ResultSendDeparture reportSend = sendDeparture(settingsEmailDto, response, reportSave);

            if (reportSend.getSuccess()) {
                return new ResultSendDeparture(true, "");
            } else {
                return reportSend;
            }

        } else {
            return reportSave;
        }

    }

    public ResultSendDeparture sendDepartureOnMailApp(SettingsEmailDto settingsEmailDto, String body, String nameReport) {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.MAIL)) {
            try {

                String subject = nameReport + " (" + settingsEmailDto.getImo() + ")";

                String cc = "";
                ArrayList<String> toCopy = new ArrayList<>();
                if (!settingsEmailDto.getEmailCopy1().isEmpty()){
                    toCopy.add(settingsEmailDto.getEmailCopy1());
                }
                if (!settingsEmailDto.getEmailCopy2().isEmpty()){
                    toCopy.add(settingsEmailDto.getEmailCopy2());
                }
                if (!settingsEmailDto.getEmailCopy3().isEmpty()){
                    toCopy.add(settingsEmailDto.getEmailCopy3());
                }
                if (!toCopy.isEmpty()) {
                    cc = "?cc=";
                    String ccSeparated = toCopy.toString();
                    ccSeparated = ccSeparated.replace("[", "")
                                .replace("]", "")
                            .replace(" ", "");
                    cc += ccSeparated;
                    cc += "&";
                } else cc = "?";

                desktop.mail(new URI(
                        "mailto:" + settingsEmailDto.getServerEmail()
                                + cc
                                + "subject=" + Common.urlEncode(subject)
                                + "&body=" + Common.urlEncode(body)));

                return new ResultSendDeparture(true, "");

            } catch (IOException | URISyntaxException ioe) {
                return new ResultSendDeparture(false, ioe.getMessage());
            }
        } else {
            return new ResultSendDeparture(false, "desktop doesn't support mailto; mail is dead anyway ");
        }
    }

    private ResultSendDeparture sendDeparture(SettingsEmailDto settingsEmailDto, DepartureResponse response, ResultSendDeparture reportSave) {

        Desktop desktop;

        if (Desktop.isDesktopSupported()
                && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {


            URI mailto = null;
            try {

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();

                String json = gson.toJson(response);

//                String uri = "mailto:" + Common.urlEncode((new SettingsPresenter()).readSettings().getServerEmail()) + "?subject=" + Common.urlEncode("Departure") ;
////                        + "&body="
////                        + Common.urlEncode(json);
//
//                if (!reportSave.getPathFile().isEmpty()) {
////                    uri = uri + "&attachment=" + URLEncoder.encode("" + reportSave.getPathFile(), "UTF-8");
//                    uri = uri + "&attachment=" +  Common.urlEncode(reportSave.getPathFile());
////                    uri = uri + "&attachment=" + Common.urlEncode("C:/Users/bildovich/Documents/Report_Departure_2022_09_30_10_30_22.json");
//                }

                String uri = "mailTo:test@gmail.com" + "?subject=" + "TEST%20SUBJECT"
                        + "&body=" +
                        Common.urlEncode("<table border=\"1\">\n" +
                                "   <tr>\n" +
                                "    <th>Ячейка 1</th>\n" +
                                "    <th>Ячейка 2</th>\n" +
                                "   </tr>\n" +
                                "   <tr>\n" +
                                "    <td>Ячейка 3</td>\n" +
                                "    <td>Ячейка 4</td>\n" +
                                "  </tr>\n" +
                                " </table>");
//                        Common.urlEncode("C:/Users/bildovich/Documents/Report_Departure_2022_09_30_10_30_22.json");

                String cmd = "cmd.exe /c start " + uri + "";
                //Call default mail client with paramters
                Runtime.getRuntime().exec(cmd);
//                mailto = new URI(uri);
//
////                URLConnection conn = new URL(uri).openConnection();
////                conn.connect();
////                conn.getOutputStream().write("<h1>This is my first email using URLConnection</h1>".getBytes(StandardCharsets.UTF_8));
////                conn.getOutputStream().close();
//
                try {
                    desktop.mail(mailto);
                    return new ResultSendDeparture(true, "");
                } catch (Exception ioException) {
                    return new ResultSendDeparture(false, ioException.getMessage());
                }


            } catch (Exception uriSyntaxException) {
                return new ResultSendDeparture(false, uriSyntaxException.getMessage());
            }


        } else {
            return new ResultSendDeparture(false, "desktop doesn't support mailto; mail is dead anyway ");
        }

    }

}
