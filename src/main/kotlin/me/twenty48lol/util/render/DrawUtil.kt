package me.twenty48lol.util.render

import org.lwjgl.nanovg.NanoVGGL3
import java.awt.Color

object DrawUtil {

    private var context = -1L

    @JvmStatic
    fun initializeNanoVG() {
        context = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS or NanoVGGL3.NVG_STENCIL_STROKES)
        if (context == -1L)
            throw IllegalStateException("Could not initialize NanoVG context!")
    }

    @JvmStatic
    fun rect(x: Number, y: Number, w: Number, h: Number, color: Color) {

    }

}