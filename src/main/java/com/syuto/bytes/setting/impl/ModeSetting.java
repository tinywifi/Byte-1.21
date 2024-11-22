package com.syuto.bytes.setting.impl;

import com.syuto.bytes.setting.Setting;
import com.syuto.bytes.setting.api.SettingHolder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

@Getter
public class ModeSetting extends Setting<String> {

    private final List<String> modes = new ArrayList<>();

    public ModeSetting(String name, SettingHolder parent, BooleanSupplier visibility, String @NotNull ... values) {
        super(name, parent, visibility, values[0]);
        modes.addAll(List.of(values));
    }
    public ModeSetting(String name, SettingHolder parent, String @NotNull ... values) {
        super(name, parent, values[0]);
        modes.addAll(List.of(values));
    }

    @Override
    public void setValue(String value) {
        if (modes.contains(value)) {
            this.value = value;
        }
    }

    public boolean is(final String value) {
        return this.value.equalsIgnoreCase(value);
    }
    public boolean isNot(final String value) {
        return !is(value);
    }

}
