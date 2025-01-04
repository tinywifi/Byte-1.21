package dev.blend.ui.dropdown.components

import com.syuto.bytes.module.Module
import com.syuto.bytes.setting.impl.BooleanSetting
import com.syuto.bytes.setting.impl.ColorSetting
import com.syuto.bytes.setting.impl.ModeSetting
import com.syuto.bytes.setting.impl.NumberSetting
import dev.blend.ThemeHandler
import dev.blend.ui.AbstractUIComponent
import dev.blend.ui.dropdown.components.values.BooleanValueComponent
import dev.blend.ui.dropdown.components.values.ColorValueComponent
import dev.blend.ui.dropdown.components.values.ModeValueComponent
import dev.blend.ui.dropdown.components.values.NumberValueComponent
import dev.blend.util.animations.SineOutAnimation
import dev.blend.util.render.Alignment
import dev.blend.util.render.ColorUtil
import dev.blend.util.render.DrawUtil
import org.lwjgl.glfw.GLFW
import kotlin.math.max

class ModuleComponent(
    private val parent: CategoryComponent,
    private val module: Module
): AbstractUIComponent(
    width = parent.width,
    height = parent.height
) {

    val components = mutableListOf<AbstractValueComponent>()
    val expandAnimation = SineOutAnimation()
    private val toggleAnimation = SineOutAnimation()
    private val lastElementAnimation = SineOutAnimation()
    private val expandToggleAnimation = SineOutAnimation()
    private val initialHeight = height
    private var expanded = false
    private var last = false

    init {
        module.values.forEach {
            when (it) {
                is BooleanSetting -> components.add(BooleanValueComponent(this, it))
                is NumberSetting -> components.add(NumberValueComponent(this, it))
                is ColorSetting -> components.add(ColorValueComponent(this, it))
                is ModeSetting -> components.add(ModeValueComponent(this, it))
                else -> throw IllegalArgumentException("There is no component defined for ${it::class.simpleName} in Dropdown ClickGUI")
            }
        }
    }

    override fun init() {
        components.forEach {
            it.init()
        }
    }

    override fun render(mouseX: Int, mouseY: Int) {
        DrawUtil.save()
        DrawUtil.intersectScissor(x, y, width, height)
        DrawUtil.roundedRect(x, y, width, initialHeight, doubleArrayOf(0.0, 0.0, lastElementAnimation.get(), lastElementAnimation.get()), ColorUtil.applyOpacity(
            ThemeHandler.getPrimary(), toggleAnimation.get()))
        DrawUtil.drawString(module.name, x + (width / 2), y + (initialHeight / 2), 12, ThemeHandler.getTextColor(), Alignment.CENTER)

        var veryRealHeight = initialHeight
        components.forEach {
            it.x = x
            it.y = y + veryRealHeight
            it.width = width
            if (it.value.visibility.asBoolean) {
                it.render(mouseX, mouseY)
                veryRealHeight +=
                    if (it.isExpanding() && (it is ColorValueComponent)) {
                        it.expandAnimation.get()
                    } else {
                        it.height
                    }
            }
        }
        DrawUtil.restore()

        if (canAnimateExpansion()) {
            this.height = expandAnimation.get()
        } else {
            expandAnimation.set(veryRealHeight)
            this.height = veryRealHeight
        }
        last = parent.components.last() == this && !expanded
        expandAnimation.duration = 200 * max(1.0, (veryRealHeight / 150))
        expandAnimation.animate(
            if (expanded) veryRealHeight else initialHeight
        )
        expandToggleAnimation.animate(if (expanded) 1.0 else 0.0)
        toggleAnimation.animate(if (module.enabled) 0.75 else 0.0)
        lastElementAnimation.animate(if (last) 5.0 else 0.0)
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                module.toggle()
            } else if (module.values.isNotEmpty()) {
                expanded = !expanded
            }
        }
        components.filter {
            expanded && it.value.visibility.asBoolean && it.isOver(mouseX, mouseY)
        }.forEach {
            if (it.click(mouseX, mouseY, mouseButton)) {
                return true
            }
        }
        return false
    }

    override fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        components.forEach {
            if (it.release(mouseX, mouseY, mouseButton)) {
                return true
            }
        }
        return false
    }

    override fun key(key: Int, scancode: Int, modifiers: Int): Boolean {
        components.forEach {
            if (it.key(key, scancode, modifiers)) {
                return true
            }
        }
        return false
    }

    override fun close() {
        components.forEach {
            it.close()
        }
    }

    private fun canAnimateExpansion(): Boolean {
        return !components.any { it.isExpanding() }
    }
    fun isExpanding(): Boolean {
        return !expandAnimation.finished
    }

}