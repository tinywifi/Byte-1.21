package dev.blend.util.misc

import dev.blend.util.IAccessor
import kotlinx.io.IOException
import org.lwjgl.BufferUtils
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel

object MiscUtil: IAccessor {

    @JvmStatic
    fun isOver(x: Number, y: Number, width: Number, height: Number, mouseX: Number, mouseY: Number): Boolean {
        return mouseX.toDouble() > x.toDouble() && mouseX.toDouble() < x.toDouble() + width.toDouble() && mouseY.toDouble() > y.toDouble() && mouseY.toDouble() < y.toDouble() + height.toDouble()
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getResourceAsByteBuffer(resource: String, bufferSize: Int = 1024): ByteBuffer {
        val source = MiscUtil::class.java.getResourceAsStream("/assets/byte/$resource")
        checkNotNull(source)
        val rbc: ReadableByteChannel = Channels.newChannel(source)
        var buffer = BufferUtils.createByteBuffer(bufferSize)
        while (true) {
            val bytes = rbc.read(buffer)
            if (bytes == -1) {
                break
            }
            if (buffer.remaining() == 0) {
                buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2)
            }
        }
        buffer.flip()
        return MemoryUtil.memSlice(buffer)
    }

    private fun resizeBuffer(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
        val newBuffer = BufferUtils.createByteBuffer(newCapacity)
        buffer.flip()
        newBuffer.put(buffer)
        return newBuffer
    }

}