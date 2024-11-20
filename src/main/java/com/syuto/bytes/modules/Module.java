package com.syuto.bytes.modules;

import com.syuto.bytes.modules.settings.Setting;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class Module {
    private boolean enabled = false;
    protected KeyBinding keyBinding;
    private final String name;
    private final Set<Integer> keys = new HashSet<>();
    private final Map<String, Setting<?>> settings = new LinkedHashMap<>();

    public Module(String name, int keyCode, String description) {
        this.name = name;
        setKeyBinding(keyCode, description);
    }

    public void onEnable() {
        enabled = true;
    }

    public void onDisable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public void setKeyBinding(int keyCode, String description) {
        this.keyBinding = new KeyBinding(keyCode, description);
        addKey(keyCode);
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }

    public void addKey(int keyCode) {
        keys.add(keyCode);
        if (keyBinding != null) {
            keyBinding.setKeyCode(keyCode);
        } else {
            keyBinding = new KeyBinding(keyCode, "Key for " + name);
        }
    }

    public void clearKeys() {
        keys.clear();
        keyBinding = null;
    }

    public Set<Integer> getKeys() {
        return keys;
    }

    public void toggle() {
        if (enabled) {
            onDisable();
        } else {
            onEnable();
        }
    }

    public Setting<?> getSetting(String name) {
        return settings.get(name);
    }

    public void addSetting(Setting<?> ...setting) {
        Setting<?>[] setting1 = setting;
        for (Setting<?> settinge : setting1) {
            settings.put(settinge.getName(), settinge);
        }
    }

    public void removeSetting(Setting<?> ...setting) {
        Setting<?>[] setting1 = setting;
        for (Setting<?> settinge : setting1) {
            settings.remove(settinge.getName(), settinge);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getSettingValue(String settingName) {
        Setting<T> setting = (Setting<T>) settings.get(settingName);
        if (setting != null) {
            return setting.getValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> void setSettingValue(String settingName, T newValue) {
        Setting<T> setting = (Setting<T>) settings.get(settingName);
        if (setting != null) {
            setting.setValue(newValue);
        }
    }

    public Map<String, Setting<?>> getSettings() {
        return settings;
    }

    public String getKeysString() {
        StringBuilder keyString = new StringBuilder();
        for (Integer key : keys) {
            if (keyString.length() > 0) {
                keyString.append(", ");
            }
            //keyString.append(Keyboard.getKeyName(key));
        }
        return keyString.toString();
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                ", enabled=" + enabled +
                ", keys=" + getKeysString() +
                ", settings=" + settings +
                '}';
    }
}
