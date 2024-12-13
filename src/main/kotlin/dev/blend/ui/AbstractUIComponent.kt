package dev.blend.ui

import dev.blend.util.interfaces.IScreen

abstract class AbstractUIComponent(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var width: Double = 0.0,
    var height: Double = 0.0,
): IScreen {
    fun isOver(mouseX: Number, mouseY: Number): Boolean {
        return mouseX.toDouble() > x && mouseX.toDouble() < x + width && mouseY.toDouble() > y && mouseY.toDouble() < y + height
    }
    fun isOver(x: Number, y: Number, width: Number, height: Number, mouseX: Number, mouseY: Number): Boolean {
        return mouseX.toDouble() > x.toDouble() && mouseX.toDouble() < x.toDouble() + width.toDouble() && mouseY.toDouble() > y.toDouble() && mouseY.toDouble() < y.toDouble() + height.toDouble()
    }
}