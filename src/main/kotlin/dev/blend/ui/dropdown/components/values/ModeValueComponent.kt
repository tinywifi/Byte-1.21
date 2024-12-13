package dev.blend.ui.dropdown.components.values

import com.syuto.bytes.setting.impl.ModeSetting
import dev.blend.ThemeHandler
import dev.blend.ui.dropdown.components.AbstractValueComponent
import dev.blend.ui.dropdown.components.ModuleComponent
import dev.blend.util.animations.SineOutAnimation
import dev.blend.util.render.Alignment
import dev.blend.util.render.ColorUtil
import dev.blend.util.render.DrawUtil
import org.lwjgl.glfw.GLFW

class ModeValueComponent(
    parent: ModuleComponent,
    override val value:  ModeSetting
): AbstractValueComponent(
    parent, value, height = 20.0
) {

    private val expandToggleAnimation = SineOutAnimation(100)
    private var clicked = false
    private var button = -1

    override fun init() {

    }

    override fun render(mouseX: Int, mouseY: Int) {
        val e = ColorUtil.applyOpacity(ThemeHandler.getTextColor(), expandToggleAnimation.get())
        with(DrawUtil) {
            drawString(value.name + ": ", x + padding, y + (height / 2.0), 8, ThemeHandler.getTextColor(), Alignment.CENTER_LEFT)
            drawString(value.value, (x + width) - padding, y + (height / 2.0), 8, e, Alignment.CENTER_RIGHT)
        }
        expandToggleAnimation.animate(if (clicked) 0.5 else 1.0)
        if (clicked && expandToggleAnimation.finished) {
            when (button) {
                GLFW.GLFW_MOUSE_BUTTON_LEFT -> value.next()
                GLFW.GLFW_MOUSE_BUTTON_RIGHT -> value.previous()
                else -> {}
            }
        }
        if (expandToggleAnimation.finished) {
            clicked = false
        }
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, height, mouseX, mouseY) && value.modes.size > 1) {
            button = mouseButton
            clicked = true
            return true
        }
        return false
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