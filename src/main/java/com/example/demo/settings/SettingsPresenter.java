package com.example.demo.settings;

import com.example.demo.model.SettingsEmailDto;

public class SettingsPresenter {

    public void saveSettings(SettingsEmailDto settingsEmail) {
        DataSettings.getInstance().saveSettings(settingsEmail);
    }

    public SettingsEmailDto readSettings() {
        return DataSettings.getInstance().readSettings();
    }

}
