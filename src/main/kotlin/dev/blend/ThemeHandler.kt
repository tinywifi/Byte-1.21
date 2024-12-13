package dev.blend

import com.syuto.bytes.module.ModuleManager
import com.syuto.bytes.module.impl.render.ThemeModule
import dev.blend.util.animations.SineInAnimation
import dev.blend.util.animations.SineOutAnimation
import dev.blend.util.render.ColorUtil
import java.awt.Color

object ThemeHandler {

    private val module = ModuleManager.getModule(ThemeModule::class.java)!!
    private val background = SineOutAnimation(500)
    private val text = SineInAnimation(500)
    private val foreground = SineInAnimation(500)
    val gray = Color(120, 120, 120)
    val theme get() = module.theme.value

    fun update() {
        background.animate(
            if (theme.equals("light", true)) {
                255.0
            } else {
                0.0
            }
        )
        text.animate(
            if (theme.equals("light", true)) {
                0.0
            } else {
                255.0
            }
        )
        foreground.animate(
            if (theme.equals("light", true)) {
                50.0
            } else {
                200.0
            }
        )
    }

    @JvmStatic
    fun getPrimary(): Color {
        return module.accent.value
    }
    @JvmStatic
    fun getBackground(): Color {
        return ColorUtil.mixColors(getBaseColor(), getPrimary(), 0.05)
    }
    @JvmStatic
    fun getTextColor(): Color {
        return Color(text.get().toInt(), text.get().toInt(), text.get().toInt())
    }
    @JvmStatic
    fun getContrast(): Color {
        return Color(foreground.get().toInt(), foreground.get().toInt(), foreground.get().toInt())
    }
    fun getBaseColor(): Color {
        return Color(background.get().toInt(), background.get().toInt(), background.get().toInt())
    }

}