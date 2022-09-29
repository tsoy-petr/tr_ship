package com.example.demo.dataPorts;

import com.example.demo.model.SeaPortDto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataSourcePorts {

    private static volatile DataSourcePorts instance;

    List<PortsChangeListener> listeners = new ArrayList<>();

    private DataSourcePorts(){}

    public static DataSourcePorts getInstance() {
        DataSourcePorts localInstance = instance;
        if (localInstance == null) {
            synchronized (DataSourcePorts.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataSourcePorts();
                }
            }
        }
        return localInstance;
    }

    public void addListener(PortsChangeListener listener) {
        listeners.add(listener);
    }
    public List<SeaPortDto> getPorts() {

        List result = new ArrayList();
        try (FileInputStream fis = new FileInputStream(getSettingFileName());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            result = (List) ois.readObject();
        } catch (Exception ignore) {
        }
        return result;

    }

    private String getSettingFileName() {
        // получаем домашний каталог пользователя
        String homeDir = System.getProperty("user.home");

        return homeDir + File.separator + "ports.bin";
    }

    public Boolean savePorts(List<SeaPortDto> ports) {
        try (FileOutputStream fos = new FileOutputStream(getSettingFileName());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(ports);
            oos.flush();
            observeListeners();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void observeListeners(){
        for (PortsChangeListener listener :
                listeners) {
            listener.change();
        }
    }

    public interface PortsChangeListener{
        void change();
    }
}
