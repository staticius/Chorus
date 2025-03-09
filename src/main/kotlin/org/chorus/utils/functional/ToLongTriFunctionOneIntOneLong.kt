package org.chorus.utils.functional

import java.util.*
import java.util.function.LongUnaryOperator

/**
 * Represents a function that accepts three arguments mixing F, int and long and produces a long result.
 * This is the three-arity specialization of [Function].
 *
 *
 * This is a functional interface
 * whose functional method is [.apply].
 *
 * @param <F> the type of the first argument to the function
 * @see Function
 *
 * @since 1.4.0.0-PN
</F> */
fun interface ToLongTriFunctionOneIntOneLong<F> {
    /**
     * Applies this function to the given arguments.
     *
     * @param f the first function argument
     * @param s the second function argument
     * @param t the third function argument
     * @return the function result
     */
    fun apply(f: F, s: Int, t: Long): Long

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the `after` function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the `after` function
     * @throws NullPointerException if after is null
     */
    fun andThen(after: LongUnaryOperator): ToLongTriFunctionOneIntOneLong<F> {
        Objects.requireNonNull(after)
        return ToLongTriFunctionOneIntOneLong { f: F, s: Int, t: Long -> after.applyAsLong(apply(f, s, t)) }
    }
}
