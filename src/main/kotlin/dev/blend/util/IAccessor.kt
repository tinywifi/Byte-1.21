package dev.blend.util

import net.minecraft.client.MinecraftClient

interface IAccessor {
    val mc get() = MinecraftClient.getInstance()!!
}