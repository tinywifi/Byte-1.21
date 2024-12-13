package dev.blend.ui.dropdown.components.values

import com.syuto.bytes.setting.impl.NumberSetting
import dev.blend.ThemeHandler
import dev.blend.ui.dropdown.components.AbstractValueComponent
import dev.blend.ui.dropdown.components.ModuleComponent
import dev.blend.util.animations.*
import dev.blend.util.render.Alignment
import dev.blend.util.render.ColorUtil
import dev.blend.util.render.DrawUtil

class NumberValueComponent(
    parent: ModuleComponent,
    override val value: NumberSetting
): AbstractValueComponent(
    parent, value, height = 30.0
) {

    private val dragAnimation = LinearAnimation(100)
    private val dragDependentAnimation = SineOutAnimation()
    private val selectAnimation = SineOutAnimation()
    private var held = false

    override fun init() {

    }

    override fun render(mouseX: Int, mouseY: Int) {
        val sliderW = width - (padding * 2.0)
        val sliderH = 2.0
        val sliderX = x + padding
        val sliderY = (y + height) - (padding * 2.0)
        val dragIndicator = 6.0 + (selectAnimation.get() * 2.0)
        val holdIndicator = 4.0
        // (value - min) / (max - min)
        // (15 - 10) / (20 - 10) = 0.5
        val relativeValue = (value.value.toDouble() - value.minValue.toDouble()) / (value.maxValue.toDouble() - value.minValue.toDouble())
        val relativeMouseX = (mouseX - sliderX) / ((sliderX + sliderW) - sliderX)
        if (held) {
            value.value = (value.minValue.toDouble() + relativeMouseX * (value.maxValue.toDouble() - value.minValue.toDouble()))
        }

        val heldColor = ColorUtil.mixColors(ThemeHandler.getTextColor(), ThemeHandler.getPrimary(), selectAnimation.get())
        with(DrawUtil) {
            drawString(value.name, x + padding, y + padding, 8, ThemeHandler.getTextColor(), Alignment.CENTER_LEFT)
            drawString(value.toString(), (x + width) - padding, y + padding, 8, heldColor, Alignment.CENTER_RIGHT)
            roundedRect(sliderX, sliderY, sliderW, sliderH, sliderH / 2.0, ThemeHandler.getContrast(), Alignment.CENTER_LEFT)
            roundedRect(sliderX, sliderY, dragAnimation.get(), sliderH, sliderH / 2.0, ThemeHandler.getPrimary(), Alignment.CENTER_LEFT)
            roundedRect(sliderX + dragAnimation.get(), sliderY, dragIndicator + (dragDependentAnimation.get() * 3.0), dragIndicator, dragIndicator / 2.0, ThemeHandler.getTextColor(), Alignment.CENTER)
            roundedRect(sliderX + dragAnimation.get() , sliderY, holdIndicator + (dragDependentAnimation.get() * 3.0), holdIndicator, holdIndicator / 2.0, heldColor, Alignment.CENTER)
        }
        dragAnimation.animate(relativeValue * sliderW)
        dragDependentAnimation.animate(if (dragAnimation.finished) 0.0 else 1.0)
        selectAnimation.animate(if (held) 1.0 else 0.0)
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        held = true
        return false
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        held = false
        return false
    }

    override fun key(key: Int, scancode: Int, modifiers: Int): Boolean {
        return false
    }

    override fun close() {

    }

}