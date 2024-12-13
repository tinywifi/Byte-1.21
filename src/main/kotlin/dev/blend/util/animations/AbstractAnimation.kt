package dev.blend.util.animations

abstract class AbstractAnimation(
    private val function: (Double) -> Double,
    var duration: Number,
) {
    private var currentTime = 0L
    private var startTime = 0L
    private var initialValue = 0.0
    private var targetValue = 1.0
    private var currentValue = 0.0
    var finished = false

    init {
        startTime = System.currentTimeMillis()
    }

    fun animate(targetValue: Double) {
        currentTime = System.currentTimeMillis()
        if (this.targetValue != targetValue) {
            this.targetValue = targetValue
            reset()
        } else {
            finished = (currentTime - duration.toDouble()) > startTime
            if (finished) {
                currentValue = targetValue
                return
            }
        }
        val result = function(progress())
        currentValue =
            if (currentValue > targetValue) {
                initialValue - (initialValue - targetValue) * result
            } else {
                initialValue + (targetValue - initialValue) * result
            }
    }


    fun progress(): Double {
        // No, this isn't a redundant cast.
        return ((System.currentTimeMillis() - startTime).toDouble() / duration.toDouble()).toDouble()
    }

    fun reset() {
        this.startTime = System.currentTimeMillis()
        this.initialValue = currentValue
        this.finished = false
    }

    fun get(): Double {
        return currentValue
    }
    fun set(value: Double) {
        currentValue = value
    }

}