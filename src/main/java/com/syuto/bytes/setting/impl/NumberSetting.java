package com.syuto.bytes.setting.impl;

import com.syuto.bytes.setting.Setting;
import com.syuto.bytes.setting.api.SettingHolder;
import lombok.Getter;

import java.util.function.BooleanSupplier;

@Getter
public class NumberSetting extends Setting<Number> {

    public final Number minValue, maxValue, increment;

    public NumberSetting(String name, SettingHolder parent, BooleanSupplier visibility, Number defaultValue, Number minValue, Number maxValue, Number increment) {
        super(name, parent, visibility, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
    }
    public NumberSetting(String name, SettingHolder parent, Number defaultValue, Number minValue, Number maxValue, Number increment) {
        super(name, parent, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
    }

}
