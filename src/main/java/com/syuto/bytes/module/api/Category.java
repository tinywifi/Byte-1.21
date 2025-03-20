package com.syuto.bytes.module.api;

public enum Category {
    COMBAT,
    MOVEMENT,
    PLAYER,
    RENDER,
    EXPLOIT;
    public final String properName = name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
}
