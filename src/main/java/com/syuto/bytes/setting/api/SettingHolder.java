package com.syuto.bytes.setting.api;

import com.syuto.bytes.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public abstract class SettingHolder {
    public final List<Setting<?>> settings = new ArrayList<>();
}
