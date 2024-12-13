package dev.blend.ui.dropdown.components.values

import com.syuto.bytes.setting.impl.BooleanSetting
import dev.blend.ThemeHandler
import dev.blend.ui.dropdown.components.AbstractValueComponent
import dev.blend.ui.dropdown.components.ModuleComponent
import dev.blend.util.animations.*
import dev.blend.util.render.Alignment
import dev.blend.util.render.ColorUtil
import dev.blend.util.render.DrawUtil

class BooleanValueComponent(
    parent: ModuleComponent,
    override val value: BooleanSetting
): AbstractValueComponent(
    parent, value, height = 20.0
) {

    private val toggleAnimation = SineOutAnimation()
    private val toggleDependentAnimation = SineOutAnimation()

    override fun init() {

    }

    override fun render(mouseX: Int, mouseY: Int) {
        val pillHeight = 8.0
        val pillWidth = 16.0
        val pillX = ((x + width) - padding)
        val pillY = y + (height / 2.0)
        val indicatorRadius = 6.0
        val indicatorOffset = 1.0
        val indicatorX = (pillX - (pillWidth - indicatorRadius + (indicatorOffset * 2.0))) + (pillWidth - (indicatorRadius + (indicatorOffset * 2.0))) * toggleAnimation.get()

        val pillColor = ColorUtil.mixColors(ThemeHandler.gray, ThemeHandler.getPrimary(), toggleAnimation.get())
        with(DrawUtil) {
            drawString(value.name, x + padding, y + (height / 2.0), 8, ThemeHandler.getTextColor(), Alignment.CENTER_LEFT)
            roundedRect(pillX, pillY, pillWidth, pillHeight, pillHeight / 2.0, pillColor, Alignment.CENTER_RIGHT)
            roundedRect(indicatorX, pillY, indicatorRadius + ((indicatorRadius / 3.0) * toggleDependentAnimation.get()), indicatorRadius, indicatorRadius / 2.0, ThemeHandler.getTextColor(), Alignment.CENTER)
        }
        toggleAnimation.animate(if (value.value) 1.0 else 0.0)
        toggleDependentAnimation.animate(if (toggleAnimation.finished) 0.0 else 1.0)
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        value.toggle()
        return true
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        return false
    }

    override fun key(key: Int, scancode: Int, modifiers: Int): Boolean {
        return false
    }

    override fun close() {

    }
}