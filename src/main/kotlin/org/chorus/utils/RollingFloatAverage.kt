package org.chorus.utils

import java.util.ArrayDeque

class RollingFloatAverage(private val maxSize: Int) {
    private val queue: ArrayDeque<Float> = ArrayDeque()
    private var sum: Float = 0f

    fun add(value: Float) {
        // Add the new value
        queue.add(value)
        sum += value

        // Remove the oldest value if the queue exceeds the max size
        if (queue.size > maxSize) {
            val removed = queue.removeFirst()
            sum -= removed
        }
    }

    fun getAverage(): Float {
        return if (queue.isNotEmpty()) sum / queue.size else 0f
    }
}
