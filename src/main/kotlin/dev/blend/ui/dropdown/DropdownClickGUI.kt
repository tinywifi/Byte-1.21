package dev.blend.ui.dropdown

import com.syuto.bytes.module.ModuleManager
import com.syuto.bytes.module.api.Category
import com.syuto.bytes.module.impl.render.ClickGUIModule
import dev.blend.ui.dropdown.components.CategoryComponent
import dev.blend.util.animations.BackOutAnimation
import dev.blend.util.render.DrawUtil
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

object DropdownClickGUI: Screen(Text.of("Dropdown Click GUI")) {

    private val components = mutableListOf<CategoryComponent>()
    private val openAnimation = BackOutAnimation()
    private var opened = false
    private var shouldClose = false

    init {
        var x = 20.0
        Category.entries.forEach {
            val component = CategoryComponent(it)
            component.x = x
            component.y = 20.0
            components.add(component)
            x += component.width + 10.0
        }
    }

    override fun init() {
        components.forEach{
            it.init()
        }
        openAnimation.set(-500.0)
        opened = true
        shouldClose = false
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        DrawUtil.begin()
        DrawUtil.translate(0, openAnimation.get()) {
            components.forEach {
                it.render(mouseX, mouseY)
            }
        }
        DrawUtil.end()
        openAnimation.animate(
            if (opened) {
                0.0
            } else if (shouldClose) {
                500.0
            } else {
                -500.0
            }
        )
        openAnimation.duration = 400
        if (shouldClose && openAnimation.finished) {
            ModuleManager.getModule(ClickGUIModule::class.java)?.setEnabled(false)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        components.forEach {
            if (it.isOver(mouseX, mouseY)) {
                if (it.click(mouseX, mouseY, button)) {
                    return true
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        components.forEach {
            if (it.release(mouseX, mouseY, button)) {
                return true
            }
        }
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        components.forEach {
            if (it.key(keyCode, scanCode, modifiers)) {
                return true
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun close() {
        components.forEach{
            it.close()
        }
        opened = false
        shouldClose = true
    }

    override fun shouldPause(): Boolean {
        return false
    }

    override fun shouldCloseOnEsc(): Boolean {
        return true
    }

}