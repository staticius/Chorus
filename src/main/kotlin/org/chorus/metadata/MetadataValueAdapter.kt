package org.chorus.metadata

import org.chorus.plugin.Plugin
import org.chorus.utils.NumberConversions

/**
 * Optional base class for facilitating MetadataValue implementations.
 *
 *
 * This provides all the conversion functions for MetadataValue so that
 * writing an implementation of MetadataValue is as simple as implementing
 * value() and invalidate().
 */
abstract class MetadataValueAdapter protected constructor(owningPlugin: Plugin) : MetadataValue(owningPlugin) {
    override fun getOwningPlugin(): Plugin? {
        return owningPlugin.get()
    }

    fun asInt(): Int {
        return NumberConversions.toInt(value())
    }

    fun asFloat(): Float {
        return NumberConversions.toFloat(value())
    }

    fun asDouble(): Double {
        return NumberConversions.toDouble(value())
    }

    fun asLong(): Long {
        return NumberConversions.toLong(value())
    }

    fun asShort(): Short {
        return NumberConversions.toShort(value())
    }

    fun asByte(): Byte {
        return NumberConversions.toByte(value())
    }

    fun asBoolean(): Boolean {
        val value = value()
        if (value is Boolean) {
            return value
        }

        if (value is Number) {
            return value.toInt() != 0
        }

        if (value is String) {
            return value.toBoolean()
        }

        return value != null
    }

    fun asString(): String {
        val value = value() ?: return ""

        return value.toString()
    }
}
