package com.syuto.bytes.eventbus;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private final Map<Class<?>, List<MethodListener>> listeners = new ConcurrentHashMap<>();

    private static class MethodListener {
        private final Object target;
        private final Method method;

        MethodListener(Object target, Method method) {
            this.target = target;
            this.method = method;
        }

        void invoke(Object event) {
            try {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(target, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void register(Object listener) {
        System.out.println("Registering event listener: " + listener.getClass().getSimpleName());
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && Event.class.isAssignableFrom(params[0])) {
                    Class<?> eventType = params[0];
                    listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                            .add(new MethodListener(listener, method));
                    System.out.println("Registered method: " + method.getName() + " for event: " + eventType.getSimpleName());
                }
            }
        }
    }

    public void unregister(Object listener) {
        System.out.println("Unregistering event listener: " + listener.getClass().getSimpleName());
        listeners.values().forEach(methodListeners ->
                methodListeners.removeIf(methodListener -> {
                    if (methodListener.target == listener) {
                        System.out.println("Unregistered method: " + methodListener.method.getName() +
                                " for event: " + methodListener.method.getParameterTypes()[0].getSimpleName());
                        return true;
                    }
                    return false;
                })
        );
        listeners.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public void post(Event event) {
        List<MethodListener> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (MethodListener listener : eventListeners) {
                listener.invoke(event);
            }
        }
    }
}