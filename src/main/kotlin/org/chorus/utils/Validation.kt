package org.chorus.utils

import java.math.BigDecimal
import java.math.BigInteger

object Validation {
    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Byte) {
        require(value >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Short) {
        require(value >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Int) {
        require(value >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Long) {
        require(value >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Float) {
        require(!(value < 0)) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Double) {
        require(!(value < 0)) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: BigInteger) {
        require(value >= BigInteger.ZERO) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: BigDecimal) {
        require(value >= BigDecimal.ZERO) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }
}
