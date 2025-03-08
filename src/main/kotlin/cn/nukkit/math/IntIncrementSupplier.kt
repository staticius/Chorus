package cn.nukkit.math

import java.util.function.IntSupplier
import java.util.stream.IntStream

class IntIncrementSupplier(private var next: Int, private val increment: Int) : IntSupplier {
    override fun getAsInt(): Int {
        val current = next
        next = current + increment
        return current
    }

    fun stream(): IntStream {
        return IntStream.generate(this)
    }
}
