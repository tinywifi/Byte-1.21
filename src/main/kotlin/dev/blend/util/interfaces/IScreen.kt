package dev.blend.util.interfaces

interface IScreen {
    fun init()
    fun render(mouseX: Int, mouseY: Int)
    fun click(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean
    fun release(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean
    fun key(key: Int, scancode: Int, modifiers: Int): Boolean
    fun close()
}
