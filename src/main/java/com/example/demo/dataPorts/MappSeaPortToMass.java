package com.example.demo.dataPorts;

import com.example.demo.model.SeaPortDto;

import java.util.List;

public class MappSeaPortToMass {

    public static Object[][] toMass(List<SeaPortDto> ports) {

        if (ports.size() == 0) {
            return new String[][]{};
        }

        String[][] result = new String[ports.size()][3];

        for (int i = 0; i < ports.size(); i++) {

            SeaPortDto port = ports.get(i);
            result[i][0] = port.getTitle();
            result[i][1] = port.getUnlocode();
            result[i][2] = port.getTimeZone();
        }
        return result;
    }

}
