package com.syuto.bytes.modules.settings.impl;

import com.syuto.bytes.modules.settings.Setting;

public class BooleanSetting extends Setting<Boolean> {
    private boolean value;

    public BooleanSetting(String name, boolean value) {
        super(name, value);
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
}
