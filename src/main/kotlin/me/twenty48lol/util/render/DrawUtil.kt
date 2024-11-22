package me.twenty48lol.util.render

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3
import org.lwjgl.opengl.GL11
import java.awt.Color

@Suppress("unused")
object DrawUtil {

    private val mc get() = MinecraftClient.getInstance()
    private var context = -1L

    @JvmStatic
    fun initializeNanoVG() {
        context = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS or NanoVGGL3.NVG_STENCIL_STROKES)
        if (context == -1L)
            throw IllegalStateException("Could not initialize NanoVG context!")
    }

    /**
     * Must be called before doing NanoVG operations.
     */
    @JvmStatic
    fun begin() {
        preRender()
        nvgBeginFrame(context, mc.window.width.toFloat(), mc.window.height.toFloat(), 1.0F)
        save()
        scale()
    }

    /**
     * Must be called after doing NanoVG operations.
     */
    @JvmStatic
    fun end() {
        restore()
        nvgEndFrame(context)
        postRender()
    }

    @JvmStatic
    fun rect(x: Number, y: Number, width: Number, height: Number, color: Color) {
        NVGColor.calloc()
            .r(color.red / 255f)
            .g(color.green / 255f)
            .b(color.blue / 255f)
            .a(color.alpha / 255f)
            .use { nvgColor ->
                nvgBeginPath(context)
                nvgShapeAntiAlias(context, true)
                nvgRect(context, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
                nvgFillColor(context, nvgColor)
//                nvgFill(context)
                nvgClosePath(context)
        }
    }

    /**
     * Save nanovg state.
     */
    @JvmStatic
    fun save() = nvgSave(context)
    /**
     * Restore nanovg to last saved state.
     */
    @JvmStatic
    fun restore() = nvgRestore(context)

    /**
     * Scales rendering according to minecraft's scaling.
     */
    @JvmStatic
    fun scale() = scale(mc.window.scaleFactor)
    @JvmStatic
    fun scale(scale: Number) = scale(scale.toFloat(), scale.toFloat())
    @JvmStatic
    fun scale(xScale: Number, yScale: Number) = nvgScale(context, xScale.toFloat(), yScale.toFloat())

    @JvmStatic
    fun translate(x: Number, y: Number) = nvgTranslate(context, x.toFloat(), y.toFloat())

    /**
     * Starts a rectangular scissor in the provided values.
     */
    @JvmStatic
    fun scissor(x: Number, y: Number, width: Number, height: Number) = nvgScissor(context, x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
    /**
     * Ends scissoring operations.
     */
    @JvmStatic
    fun endScissor() = nvgResetScissor(context)

    private fun preRender() {
        RenderSystem.enableBlend()
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE)
        RenderSystem.enableDepthTest()
        RenderSystem.depthFunc(GL11.GL_LESS)
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT)
    }
    private fun postRender() {
        RenderSystem.disableCull()
        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE)
    }

}