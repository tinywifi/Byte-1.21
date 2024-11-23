package com.syuto.bytes.eventbus;

import com.syuto.bytes.Byte;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    private final Map<Class<?>, List<MethodListener>> listeners = new ConcurrentHashMap<>();

    private record MethodListener(Object target, Method method) {

        void invoke(Object event) {
                try {
                    method.setAccessible(true);
                    method.invoke(target, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    public void register(Object listener) {
        Byte.LOGGER.info("Registering event listener: {}", listener.getClass().getSimpleName());
        Method[] methods = listener.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && Event.class.isAssignableFrom(params[0])) {
                    listeners.computeIfAbsent(params[0], k -> new CopyOnWriteArrayList<>())
                            .add(new MethodListener(listener, method));
                    Byte.LOGGER.info("Registered method: {} for event: {}", method.getName(), params[0].getSimpleName());
                }
            }
        }
    }

    public void unregister(Object listener) {
        Byte.LOGGER.info("Unregistering event listener: {}", listener.getClass().getSimpleName());
        listeners.values().forEach(methodListeners ->
                methodListeners.removeIf(methodListener -> {
                    if (methodListener.target == listener) {
                        Byte.LOGGER.info("Unregistered method: {} for event: {}", methodListener.method.getName(), methodListener.method.getParameterTypes()[0].getSimpleName());
                        return true;
                    }
                    return false;
                })
        );
        listeners.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        Byte.LOGGER.info("Removed listener: {}", listeners);
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