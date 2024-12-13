package dev.blend.ui.dropdown.components

import com.syuto.bytes.module.ModuleManager
import com.syuto.bytes.module.api.Category
import dev.blend.ThemeHandler
import dev.blend.ui.AbstractUIComponent
import dev.blend.util.animations.*
import dev.blend.util.render.Alignment
import dev.blend.util.render.DrawUtil
import org.lwjgl.glfw.GLFW

class CategoryComponent(
    private val category: Category
): AbstractUIComponent(
    width = 100.0,
    height = 20.0
) {

    val components = mutableListOf<ModuleComponent>()
    private val expandAnimation = SineOutAnimation()
    private val expandToggleAnimation = SineOutAnimation()
    private val initialHeight = height;
    private var expanded = true

    init {
        ModuleManager.modules.filter {
            it.category == category
        }.forEach {
            components.add(ModuleComponent(this, it))
        }
    }

    override fun init() {
        components.forEach {
            it.init()
        }
    }

    override fun render(mouseX: Int, mouseY: Int) {
        DrawUtil.save()
        DrawUtil.scissor(x, y, width, height)
        DrawUtil.roundedRect(x, y, width, height, 5, ThemeHandler.getBackground())
        DrawUtil.drawString(category.properName, x + (width / 2), y + (initialHeight / 2), 12, ThemeHandler.getTextColor(), Alignment.CENTER)

        var veryRealHeight = initialHeight
        components.forEach {
            it.x = x
            it.y = y + veryRealHeight
            it.width = width
            it.render(mouseX, mouseY)
            veryRealHeight +=
                if (it.isExpanding()) {
                    it.expandAnimation.get()
                } else {
                    it.height
                }
        }
        DrawUtil.resetScissor()
        DrawUtil.restore()
        if (canAnimateExpansion()) {
            this.height = expandAnimation.get()
        } else {
            expandAnimation.set(veryRealHeight)
            this.height = veryRealHeight
        }
        expandAnimation.animate(
            if (expanded) veryRealHeight else initialHeight
        )
        expandToggleAnimation.animate(if (expanded) 1.0 else 0.0)
    }

    override fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (isOver(x, y, width, initialHeight, mouseX, mouseY)) {
            if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                return true
            } else if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                expanded = !expanded
                return true
            }
        }
        if (expanded) {
            components.forEach {
                if (it.isOver(mouseX, mouseY)) {
                    if (it.click(mouseX, mouseY, mouseButton)) {
                        return true
                    }
                }
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

    fun canAnimateExpansion(): Boolean {
        return !components.any { it.isExpanding() }
    }
    fun isExpanding(): Boolean {
        return !expandAnimation.finished
    }

}