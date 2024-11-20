package com.syuto.bytes.modules;

import com.syuto.bytes.Byte;
import org.reflections.Reflections;

import java.util.*;

import static com.syuto.bytes.Byte.mc;


public class ModuleManager {
    private static final Map<String, List<Module>> categorizedModules = new HashMap<>();
    private static final Map<String, Module> modules = new HashMap<>();

    public static void registerModules() {
        Reflections reflections = new Reflections("com.syuto.bytes.modules.impl");
        Set<Class<? extends Module>> moduleClasses = reflections.getSubTypesOf(Module.class);

        for (Class<? extends Module> moduleClass : moduleClasses) {
            try {
                Module module = moduleClass.getDeclaredConstructor().newInstance();

                String packageName = moduleClass.getPackage().getName();
                String[] packageParts = packageName.split("\\.");
                String category = packageParts[packageParts.length - 1];


                categorizedModules.computeIfAbsent(category, k -> new ArrayList<>()).add(module);

                modules.put(module.getName().toLowerCase(), module);

                if (module.isEnabled()) {
                    Byte.INSTANCE.eventBus.register(module);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Module> getModulesByCategory(String category) {
        return categorizedModules.getOrDefault(category, Collections.emptyList());
    }


    public static Map<String, Module> getModules() {
        return modules;
    }

    public static Module getModule(String name) {
        return modules.get(name.toLowerCase());
    }

    public static void update() {
        for (List<Module> modulesInCategory : categorizedModules.values()) {
            for (Module module : modulesInCategory) {
                KeyBinding keyBinding = module.getKeyBinding();
                if (keyBinding != null) {
                    keyBinding.update();
                    if (keyBinding.isJustPressed() && mc.isWindowFocused()) {
                        toggleModule(module);
                    }
                }
            }
        }
    }

    public static void toggleModule(Module module) {
        if (module.isEnabled()) {
            disable(module);
        } else {
            enable(module);
        }
    }

    public static void enable(Module module) {
        if (!module.isEnabled()) {
            module.onEnable();
            Byte.INSTANCE.eventBus.register(module);
        }
    }

    public static void disable(Module module) {
        if (module.isEnabled()) {
            module.onDisable();
            Byte.INSTANCE.eventBus.unregister(module);
        }
    }

    public static Set<String> getCategories() {
        return categorizedModules.keySet();
    }
}
