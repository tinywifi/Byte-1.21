package com.syuto.bytes.modules.settings.impl;

import com.syuto.bytes.modules.settings.Setting;

public class IntSetting extends Setting<Integer> {
    private int min;
    private int max;

    public IntSetting(String name, int value, int min, int max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
