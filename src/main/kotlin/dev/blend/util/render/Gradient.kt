package dev.blend.util.render

import dev.blend.util.IAccessor
import net.minecraft.client.MinecraftClient
import java.awt.Color

class Gradient(
    val primary: Color,
    val secondary: Color,
    var origin: Point = Point(0, 0),
    var end: Point = Point(MinecraftClient.getInstance().window.width, MinecraftClient.getInstance().window.height)
): IAccessor

class Point(val x: Number, val y: Number)
