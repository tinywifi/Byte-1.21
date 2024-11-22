package com.syuto.bytes.setting.impl;

import com.syuto.bytes.setting.Setting;
import com.syuto.bytes.setting.api.SettingHolder;

import java.util.function.BooleanSupplier;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, SettingHolder parent, BooleanSupplier visibility, Boolean value) {
        super(name, parent, visibility, value);
    }
    public BooleanSetting(String name, SettingHolder parent, Boolean defaultValue) {
        super(name, parent, defaultValue);
    }
}
