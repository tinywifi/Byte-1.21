package com.syuto.bytes.modules.settings.impl;

import com.syuto.bytes.modules.settings.Setting;

import java.awt.*;

public class ColorSetting extends Setting<Color> {
    private Color value;

    public ColorSetting(String name, Color initialValue) {
        super(name, initialValue);
        this.value = initialValue;
    }

    public Color getValue() {
        return value;
    }

    public void setValue(Color value) {
        this.value = value;
    }
}
