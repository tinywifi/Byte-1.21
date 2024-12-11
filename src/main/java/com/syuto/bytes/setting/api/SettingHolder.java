package com.syuto.bytes.setting.api;

import com.syuto.bytes.setting.Setting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SettingHolder {
    public final List<Setting<?>> settings = new ArrayList<>();
    @Nullable
    @SuppressWarnings("unchecked")
    public <S extends Setting<?>> S getSetting(final String name) {
        return (S) this.settings.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
