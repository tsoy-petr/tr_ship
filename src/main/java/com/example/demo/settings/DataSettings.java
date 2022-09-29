package com.example.demo.settings;

import com.example.demo.dataPorts.DataSourcePorts;
import com.example.demo.model.SettingsEmailDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataSettings {

    private static volatile DataSettings instance;
    List<SettingsChangeListener> listeners = new ArrayList<>();

    private Properties props = new Properties();

    private final String PROP_KEY_SMTP_SERVER = "smtpServer";
    private final String PROP_KEY_SMTP_PORT = "smtpPort";
    private final String PROP_KEY_EMAIL_USER = "emailUser";
    private final String PROP_KEY_EMAIL_ADDRESS = "emailAddress";
    private final String PROP_KEY_EMAIL_PASS = "emailPass";
    private final String PROP_KEY_IMO = "imo";
    private final String PROP_KEY_VOY_NO = "voyNo";
    private final String PROP_SERVER_EMAIL = "serverEmail";
    private final String PROP_FOLDER_FOR_SAVING_REPORTS = "folderForSavingReports";

    private DataSettings(){}

    public static DataSettings getInstance() {
        DataSettings localInstance = instance;
        if (localInstance == null) {
            synchronized (DataSettings.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataSettings();
                }
            }
        }
        return localInstance;
    }

    public void addListener(SettingsChangeListener listener) {
        listeners.add(listener);
    }

    public void saveSettings(SettingsEmailDto settingsEmail) {
        props.setProperty(PROP_KEY_SMTP_SERVER, settingsEmail.getSmtpServer());
        props.setProperty(PROP_KEY_SMTP_PORT, Integer.toString(settingsEmail.getSmtpPort()));
        props.setProperty(PROP_KEY_EMAIL_USER, settingsEmail.getUser());
        props.setProperty(PROP_KEY_EMAIL_PASS, settingsEmail.getPasswordEmail());
        props.setProperty(PROP_KEY_IMO, settingsEmail.getImo());
        props.setProperty(PROP_KEY_EMAIL_ADDRESS, settingsEmail.getEmail());
        props.setProperty(PROP_KEY_VOY_NO, settingsEmail.getVoyNo());
        props.setProperty(PROP_SERVER_EMAIL, settingsEmail.getServerEmail());
        props.setProperty(PROP_FOLDER_FOR_SAVING_REPORTS, settingsEmail.getFolderForSavingReports());
        try {
            FileOutputStream output = new FileOutputStream(getSettingFileName());
            props.store(output, "Saved settings");
            output.close();
        } catch (Exception ignore) {
            // если не получается сохранить настройки,
            // в следующий раз будут использоваться настройки по умолчанию
        }

        if (!listeners.isEmpty()) {
            SettingsEmailDto settingsEmailDto = readSettings();
            for (SettingsChangeListener listener : listeners) {
                if (listener != null) {
                    listener.change(settingsEmailDto);
                }
            }
        }

    }

    public SettingsEmailDto readSettings() {
        // Загрузка сохраненных настроек
        try {
            FileInputStream input = new FileInputStream(getSettingFileName());
            props.load(input);
            input.close();
        } catch (Exception ignore) {
            // исключение игнорируется, поскольку ожидалось, что
            // файл установочных параметров иногда может не существовать
            // при первом запуске приложения он точно не будет существовать
        }

        String smtpServer = props.getProperty(PROP_KEY_SMTP_SERVER, "");
        String smtpPort = props.getProperty(PROP_KEY_SMTP_PORT, "");
        String emailAddress = props.getProperty(PROP_KEY_EMAIL_ADDRESS, "");
        String emailPass = props.getProperty(PROP_KEY_EMAIL_PASS, "");
        String imo = props.getProperty(PROP_KEY_IMO, "");
        String user = props.getProperty(PROP_KEY_EMAIL_USER, "");
        String voyNo = props.getProperty(PROP_KEY_VOY_NO, "");
        String serverEmail = props.getProperty(PROP_SERVER_EMAIL, "");
        String folderForSavingReports = props.getProperty(PROP_FOLDER_FOR_SAVING_REPORTS, "");

        int port = 0;
        try {
            port = Integer.parseInt(smtpPort);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return new SettingsEmailDto(smtpServer, port, emailAddress, user, emailPass, imo, voyNo, serverEmail, folderForSavingReports);
    }

    private String getSettingFileName() {
        // получаем домашний каталог пользователя
        String homeDir = System.getProperty("user.home");

        return homeDir + File.separator + "transitSettings.properties";
    }

    public interface SettingsChangeListener{
        void change(SettingsEmailDto settingsEmail);
    }
}
