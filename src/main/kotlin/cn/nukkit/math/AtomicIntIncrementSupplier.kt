package cn.nukkit.math

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntSupplier
import java.util.stream.IntStream

class AtomicIntIncrementSupplier(first: Int, private val increment: Int) : IntSupplier {
    private val next = AtomicInteger(first)

    override fun getAsInt(): Int {
        return next.getAndAdd(increment)
    }

    fun stream(): IntStream {
        return IntStream.generate(this)
    }
}
