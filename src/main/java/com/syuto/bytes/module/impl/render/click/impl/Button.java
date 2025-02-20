package com.syuto.bytes.module.impl.render.click.impl;

public class Button {

    public boolean extended;
    public double x, y, width, height;

    public Button(boolean extended, double x, double y, double width, double height) {
        this.extended = extended;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
