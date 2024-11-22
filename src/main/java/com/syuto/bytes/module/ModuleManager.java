package com.syuto.bytes.module;

import com.syuto.bytes.Byte;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.util.*;

public class ModuleManager {

    public static final List<Module> modules = new ArrayList<>();

    public static void registerModules() {
        Reflections reflections = new Reflections("com.syuto.bytes.module.impl");
        Set<Class<? extends Module>> moduleClasses = reflections.getSubTypesOf(Module.class);
        for (Class<? extends Module> moduleClass : moduleClasses) {
            try {
                Module module = moduleClass.getDeclaredConstructor().newInstance();
                modules.add(module);
            } catch (Exception e) {
                Byte.LOGGER.error("Error registering modules.", e.getCause());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <M extends Module> M getModule(Class<M> module) {
        return (M) modules.stream()
                .filter(m -> m.getClass().equals(module))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <M extends Module> M getModule(String module) {
        return (M) modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(module))
                .findFirst()
                .orElse(null);
    }

}
