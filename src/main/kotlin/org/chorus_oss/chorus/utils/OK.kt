package org.chorus_oss.chorus.utils

import java.util.*

/**
 * RESULTS_THAT_CAN_CARRY_ABNORMAL_INFORMATION
 *
 * @param <E> the error parameter
 */
@JvmRecord
data class OK<E>(val ok: Boolean, val error: E?) {
    constructor(ok: Boolean) : this(ok, null)

    fun getError(): Throwable {
        return if (error is Throwable) {
            AssertionError(error)
        } else {
            AssertionError(Objects.toString(error, "Unknown error"))
        }
    }

    /**
     * Asserts whether the result is true or not, and throws an AssertionError if it is not true
     *
     * @throws AssertionError the assertion error
     */
    @Throws(AssertionError::class)
    fun assertOK() {
        if (!ok) {
            if (error is Throwable) {
                throw AssertionError(error)
            } else {
                throw AssertionError(Objects.toString(error, "Unknown error"))
            }
        }
    }

    companion object {
        val TRUE: OK<Void> = OK(true)
    }
}
