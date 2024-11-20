package com.syuto.bytes.modules;

import com.sun.jna.platform.KeyboardUtils;
import org.lwjgl.glfw.GLFW;

import static com.syuto.bytes.Byte.mc;

public class KeyBinding {
    private int keyCode;
    private final String description;
    private boolean wasPressed;
    private boolean isPressed;

    public KeyBinding(int keyCode, String description) {
        this.keyCode = keyCode;
        this.description = description;
        this.wasPressed = false;
        this.isPressed = false;
    }

    public void update() {
        wasPressed = isPressed;
        isPressed = GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), keyCode) == GLFW.GLFW_PRESS;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean isJustPressed() {
        return isPressed && !wasPressed;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public String getDescription() {
        return description;
    }
}