package com.syuto.bytes.modules.settings.impl;

import com.syuto.bytes.modules.settings.Setting;

public class FloatSetting extends Setting<Float> {
    private float min;
    private float max;

    public FloatSetting(String name, float value, float min, float max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    @Override
    public void setValue(Float value) {
        this.value = (float) (Math.round(value * 100.0) / 100.0);
    }

    public Float getValue() {
        return (float) (Math.round(value * 100.0) / 100.0);
    }
}
