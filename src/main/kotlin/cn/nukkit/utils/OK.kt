package cn.nukkit.utils

import java.util.*

/**
 * 可携带异常信息的的结果
 *
 * @param <E> the error parameter
</E> */
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
     * 断言该结果是否为真，如果不为真则会抛出AssertionError
     *
     *
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
