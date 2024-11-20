package com.syuto.bytes.modules.settings.impl;

import com.syuto.bytes.modules.settings.Setting;

public class DoubleSetting extends Setting<Double> {
    private double min;
    private double max;

    public DoubleSetting(String name, double value, double min, double max) {
        super(name, value);
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public void setValue(Double value) {
        this.value = Math.round(value * 100.0) / 100.0;
    }

    public Double getValue() {
        return Math.round(value * 100.0) / 100.0;
    }
}
