package dev.blend.ui.dropdown.components.values

import com.syuto.bytes.setting.impl.ColorSetting
import dev.blend.ThemeHandler
import dev.blend.ui.dropdown.components.AbstractValueComponent
import dev.blend.ui.dropdown.components.ModuleComponent
import dev.blend.util.animations.LinearAnimation
import dev.blend.util.animations.SineOutAnimation
import dev.blend.util.render.*
import java.awt.Color
import kotlin.math.*

class ColorValueComponent(
    parent: ModuleComponent,
    override val value:  ColorSetting
): AbstractValueComponent(
    parent, value, height = 20.0
) {

    val expandAnimation = SineOutAnimation()
    private val hueAnimation = LinearAnimation(100)
    private val pickerHoldAnimation = SineOutAnimation()
    private val initialHeight = height
    private var expanded = false
    private var pickerHeld = false
    private var hueSliderHeld = false

    override fun init() {

    }

    override fun render(mouseX: Int, mouseY: Int) {
        var height = initialHeight
        val pickerX = x + padding
        val pickerY = y + initialHeight + padding
        val pickerWidth = width - (padding * 2.0)
        val pickerHeight = pickerWidth - (initialHeight + padding)
        val relativeMouseX = (mouseX - pickerX) / ((pickerX + pickerWidth) - pickerX)
        val relativePickerY = (mouseY - pickerY) / ((pickerY + pickerHeight) - pickerY)
        if (pickerHeld) {
            value.saturation = max(0.0, min(relativeMouseX, 1.0)).toFloat()
            value.brightness = max(0.0, min((1.0 - relativePickerY), 1.0)).toFloat()
        }
        if (hueSliderHeld) {
            value.hue = max(0.0, min(relativeMouseX, 1.0)).toFloat()
        }
        val pickerSelectorColor = ColorUtil.mixColors(Color.WHITE, value.value, pickerHoldAnimation.get())
        val hueSelectorColor = ColorUtil.mixColors(Color.WHITE, Color.getHSBColor(value.hue, 1.0f, 1.0f), hueAnimation.get())
        with(DrawUtil) {
            save()
            intersectScissor(x, y, width, this@ColorValueComponent.height)
            drawString(value.name, x + 5.0, y + (height / 2.0), 8, ThemeHandler.getTextColor(), Alignment.CENTER_LEFT)

            roundedRect(x + (width - padding), y + (height / 2.0), 20.0, height - (padding * 2.0), 2.0, value.value, Alignment.CENTER_RIGHT)
            roundedRect(x + (width - padding), y + (height / 2.0), 20.0, height - (padding * 2.0), 2.0, 1.0, ThemeHandler.getContrast(), Alignment.CENTER_RIGHT)

            height += padding
            height += pickerHeight
            roundedRect(
                pickerX, pickerY,
                pickerWidth, pickerHeight, 2.0,
                Gradient(
                    Color.WHITE,
                    Color.getHSBColor(value.hue, 1.0f, 1.0f),
                    Point(pickerX, pickerY),
                    Point(x + padding + pickerWidth, pickerY)
                )
            )
            roundedRect(
                pickerX, pickerY,
                pickerWidth, pickerHeight, 2.0,
                Gradient(
                    Color(255, 255, 255, 0),
                    Color.BLACK,
                    Point(pickerX, pickerY),
                    Point(pickerX, pickerY + pickerHeight)
                )
            )
            roundedRect(pickerX + (value.saturation.toDouble() * pickerWidth), pickerY + (1.0 - value.brightness.toDouble()) * pickerHeight, padding + (2.0 * pickerHoldAnimation.get()), padding + (2.0 * pickerHoldAnimation.get()), 2.0, Color.WHITE, Alignment.CENTER)
            roundedRect(pickerX + (value.saturation.toDouble() * pickerWidth), pickerY + (1.0 - value.brightness.toDouble()) * pickerHeight, 3.0, 3.0, 3.0, pickerSelectorColor, Alignment.CENTER)

            height += initialHeight
            rainbowBar(
                pickerX, pickerY + pickerHeight + padding,
                pickerWidth, initialHeight - (padding * 2.0), 2.0
            )
            roundedRect(pickerX + (value.hue.toDouble() * pickerWidth), (pickerY + pickerHeight + padding) + ((initialHeight - (padding * 2.0)) / 2.0), padding + (2.0 * hueAnimation.get()), padding + (2.0 * hueAnimation.get()), 2.0, Color.WHITE, Alignment.CENTER)
            roundedRect(pickerX + (value.hue.toDouble() * pickerWidth), (pickerY + pickerHeight + padding) + ((initialHeight - (padding * 2.0)) / 2.0), 3.0, 3.0, 3.0, hueSelectorColor, Alignment.CENTER)

            restore()
        }
        this.height = expandAnimation.get()
        expandAnimation.animate(
            if (expanded) {
                height
            } else {
                initialHeight
            }
        )
        pickerHoldAnimation.animate(
            if (pickerHeld) {
                1.0
            } else {
                0.0
            }
        )
        hueAnimation.animate(
            if (hueSliderHeld) {
                1.0
            } else {
                0.0
            }
        )
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        val pickerX = x + padding
        val pickerY = y + initialHeight
        val pickerWidth = width - (padding * 2.0)
        val pickerHeight = pickerWidth - (initialHeight + padding)
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            expanded = !expanded
            return true
        }
        if (isOver(pickerX, pickerY, pickerWidth, pickerHeight, mouseX, mouseY)) {
            pickerHeld = true
            return true
        }
        if (isOver(pickerX, pickerY + pickerHeight + padding, pickerWidth, initialHeight - (padding), mouseX, mouseY)) {
            hueSliderHeld = true
            return true
        }
        return false
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (pickerHeld)
            pickerHeld = false
        if (hueSliderHeld)
            hueSliderHeld = false
        return false
    }

    override fun key(key: Int, scancode: Int, modifiers: Int): Boolean {
        return false
    }

    override fun close() {

    }

    override fun isExpanding(): Boolean {
        return !expandAnimation.finished
    }

}