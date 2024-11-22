package com.syuto.bytes.setting;

import com.syuto.bytes.setting.api.SettingHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BooleanSupplier;

@Getter
@Setter
@AllArgsConstructor
public abstract class Setting<T> {
    protected final String name;
    protected final SettingHolder parent;
    protected final BooleanSupplier visibility;
    protected T value;

    protected Setting(String name, SettingHolder parent, T defaultValue) {
        this(name, parent, () -> true, defaultValue);
    }

}
