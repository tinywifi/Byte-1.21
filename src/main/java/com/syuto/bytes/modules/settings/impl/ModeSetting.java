package com.syuto.bytes.modules.settings.impl;

import com.syuto.bytes.modules.settings.Setting;

public class ModeSetting extends Setting<String> {
    private final String[] modes;

    public ModeSetting(String name, String initialValue, String... modes) {
        super(name, initialValue);
        this.modes = modes;
    }

    public String[] getModes() {
        return modes;
    }

    public void setMode(String mode) {
        this.value = mode;
    }
}
