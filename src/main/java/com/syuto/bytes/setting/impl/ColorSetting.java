package com.syuto.bytes.setting.impl;

import com.syuto.bytes.setting.Setting;
import com.syuto.bytes.setting.api.SettingHolder;

import java.awt.*;
import java.util.function.BooleanSupplier;

public class ColorSetting extends Setting<Color> {
    public ColorSetting(String name, SettingHolder parent, BooleanSupplier visibility, Color value) {
        super(name, parent, visibility, value);
    }

    public ColorSetting(String name, SettingHolder parent, Color defaultValue) {
        super(name, parent, defaultValue);
    }
}
