package com.example.demo.dataPorts;

import com.example.demo.core.Position;
import com.example.demo.model.SeaPortDto;
import com.example.demo.model.TerminalDto;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class DataPortsPresenter {

    DataSourcePorts dataSource = DataSourcePorts.getInstance();

    public List<SeaPortDto> getPorts() {
        return dataSource.getPorts();
    }

    public Boolean savePorts(List<SeaPortDto> ports) {
        return dataSource.savePorts(ports);
    }

    public boolean saveFromFile(File file) {

        boolean success = false;

        List<SeaPortDto> ports = new ArrayList<>();

        if (file == null) {
            return false;
        }

        try (Reader reader = Files.newBufferedReader(file.toPath(),
                StandardCharsets.UTF_8)) {

            JsonParser parser = new JsonParser();
            JsonElement tree = parser.parse(reader);

            JsonArray array = tree.getAsJsonArray();

            for (JsonElement element : array) {

                if (element.isJsonObject()) {

                    JsonObject jPort = element.getAsJsonObject();
                    if (jPort.isJsonObject()) {
                        if (jPort.has("title")
                                && jPort.has("unlocode")
                                && jPort.has("timeZone")
                                && jPort.has("terminals")) {


                            JsonElement jTitle = jPort.get("title");
                            String title = jTitle.getAsString();

                            JsonElement jUnlocode = jPort.get("unlocode");
                            String unlocode = jUnlocode.getAsString();

                            JsonElement jTimeZone = jPort.get("timeZone");
                            String timeZone = jTimeZone.getAsString();

                            List<TerminalDto> terminals = getTerminalsFromJElement(jPort.get("terminals"));
//                            String title, String unlocode, String timeZone, List<TerminalDto> terminals
                            ports.add(
                                    new SeaPortDto(
                                            title, unlocode, timeZone, terminals
                                    )
                            );
                        }
                    }
                }
            }

            savePorts(ports);

            success = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;

    }

    private List<TerminalDto> getTerminalsFromJElement(JsonElement jsonElement) {

        List<TerminalDto> terminals = new ArrayList<>();

        if (jsonElement.isJsonArray()) {

            for (JsonElement el :
                    jsonElement.getAsJsonArray()) {

                if (el.isJsonObject()) {

                    JsonObject jTerminal = el.getAsJsonObject();

                    if (jTerminal.has("title")
                            && jTerminal.has("latitude")
                            && jTerminal.has("longitude")
                            && jTerminal.has("uid")) {

                        JsonElement jTitle = jTerminal.get("title");
                        String title = jTitle.toString().replace("\"", "");

                        JsonElement jUID = jTerminal.get("uid");
                        String uid = jUID.toString();

                        Position latitude = getPositionFromJElement(jTerminal.get("latitude"));
                        Position longitude = getPositionFromJElement(jTerminal.get("longitude"));

                        terminals.add(
                                new TerminalDto(
                                        title, latitude, longitude, uid
                                )
                        );

                    }

                }

            }
        }

        return terminals;

    }

    private Position getPositionFromJElement(JsonElement jsonElement) {

        if (jsonElement.isJsonObject()) {
            JsonObject jPosition = jsonElement.getAsJsonObject();
            if (jPosition.has("typePosition")
                    && jPosition.has("degrees")
                    && jPosition.has("time")
                    && jPosition.has("hemisphere")) {

                JsonElement jTypePosition = jPosition.get("typePosition");
                String tpString = jTypePosition.toString().replace("\"", "");
                Position.TypePosition tp = Position.TypePosition.valueOf(tpString);

                JsonElement jDegrees = jPosition.get("degrees");
                int degrees = jDegrees.getAsInt();

                JsonElement jTime = jPosition.get("time");
                double time = jTime.getAsDouble();

                JsonElement jHemisphere = jPosition.get("hemisphere");
                Position.Hemisphere hemisphere = null;

                try {
                    hemisphere = Position.Hemisphere.valueOf(jHemisphere.toString().replace("\"", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new Position(
                        tp, degrees, time, hemisphere
                );

            }
        }

        return null;
    }
}
