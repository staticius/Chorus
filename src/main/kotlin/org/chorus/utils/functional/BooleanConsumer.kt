package org.chorus.utils.functional

import java.util.*

interface BooleanConsumer {
    fun accept(value: Boolean)


    fun andThen(after: BooleanConsumer): BooleanConsumer {
        Objects.requireNonNull(after)
        return BooleanConsumer { t: Boolean ->
            accept(t)
            after.accept(t)
        }
    }
}
