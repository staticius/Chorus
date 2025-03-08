/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.nukkit.utils

import lombok.experimental.UtilityClass
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author joserobjr
 * @since 2020-10-11
 */
@UtilityClass
object Validation {
    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Byte) {
        require(value >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Short) {
        require(value >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Int) {
        require(value >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Long) {
        require(value >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Float) {
        require(!(value < 0)) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: Double) {
        require(!(value < 0)) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: BigInteger) {
        require(value.compareTo(BigInteger.ZERO) >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }

    /**
     * Throws an exception if the value is negative.
     *
     * @param arg   The name of the argument, will be placed in front of the exception message if the value is is not null.
     * @param value The argument value to be validated.
     * @throws IllegalArgumentException If the value is negative.
     */
    fun checkPositive(arg: String?, value: BigDecimal) {
        require(value.compareTo(BigDecimal.ZERO) >= 0) { (if (arg != null) "$arg: " else "") + "Negative value is not allowed: " + value }
    }
}
