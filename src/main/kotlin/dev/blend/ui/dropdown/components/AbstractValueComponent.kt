package dev.blend.ui.dropdown.components

import com.syuto.bytes.setting.Setting
import dev.blend.ui.AbstractUIComponent

abstract class AbstractValueComponent(
    val parent: ModuleComponent,
    open val value: Setting<*>,
    width: Double = 0.0,
    height: Double = 0.0,
): AbstractUIComponent(
    width = width,
    height = height
) {
    val padding = 5.0
    open fun isExpanding(): Boolean {
        return false
    }
}