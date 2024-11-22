package com.syuto.bytes.module.api;

public enum Category {
    COMBAT,
    MOVEMENT,
    PLAYER,
    RENDER,
    OTHER;
    public final String properName = name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
}
