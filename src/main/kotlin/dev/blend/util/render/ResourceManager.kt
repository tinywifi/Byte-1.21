package dev.blend.util.render

object ResourceManager {

    @JvmStatic
    fun init() {
        FontResources.init()
        ImageResources.init()
    }

    object FontResources {
        lateinit var regular: FontResource
        lateinit var ubuntu: FontResource
        fun init() {
            regular = FontResource("regular")
            ubuntu = FontResource("ubuntu")
        }
    }

    object ImageResources {
        fun init() {

        }
    }

}