package org.chorus.utils

import org.chorus.utils.functional.BooleanConsumer
import java.util.function.BooleanSupplier
import java.util.function.Supplier

/**
 * @author joserobjr
 */
enum class OptionalBoolean(private val value: Boolean?) {
    TRUE(java.lang.Boolean.TRUE),


    FALSE(java.lang.Boolean.FALSE),


    EMPTY(null);

    val asBoolean: Boolean
        get() {
            if (value == null) {
                throw NoSuchElementException("No value present")
            }
            return value
        }

    val isPresent: Boolean
        get() = value != null

    fun ifPresent(consumer: BooleanConsumer) {
        if (value != null) {
            consumer.accept(value)
        }
    }

    fun orElse(other: Boolean): Boolean {
        return value ?: other
    }

    fun orElseGet(other: BooleanSupplier): Boolean {
        return value ?: other.asBoolean
    }

    fun <X : Throwable> orElseThrow(exceptionSupplier: Supplier<X>): Boolean {
        if (value != null) {
            return value
        } else {
            throw exceptionSupplier.get()
        }
    }

    override fun toString(): String {
        return if (value == null) "OptionalBoolean.empty" else if (value) "OptionalBoolean[true]" else "OptionalBoolean[false]"
    }


    fun toOptionalValue(): OptionalValue<Boolean?> {
        return OptionalValue.Companion.ofNullable<Boolean?>(this.value)
    }

    companion object {
        fun of(value: Boolean): OptionalBoolean {
            return if (value) TRUE else FALSE
        }

        fun ofNullable(value: Boolean?): OptionalBoolean {
            return if (value == null) EMPTY else of(value)
        }

        @JvmStatic
        fun empty(): OptionalBoolean {
            return EMPTY
        }
    }
}
