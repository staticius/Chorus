package org.chorus.metadata

import org.chorus.plugin.Plugin

/**
 * A FixedMetadataValue is a special case metadata item that contains the same
 * value forever after initialization. Invalidating a FixedMetadataValue has
 * no effect.
 *
 *
 * This class extends LazyMetadataValue for historical reasons, even though it
 * overrides all the implementation methods. it is possible that in the future
 * that the inheritance hierarchy may change.
 */
class FixedMetadataValue
/**
 * Initializes a FixedMetadataValue with an Object
 *
 * @param owningPlugin the [Plugin] that created this metadata value
 * @param value        the value assigned to this metadata value
 */(
    owningPlugin: Plugin,
    /**
     * Store the internal value that is represented by this fixed value.
     */
    private val internalValue: Any
) : LazyMetadataValue(owningPlugin) {
    override fun invalidate() {
    }

    override fun value(): Any {
        return internalValue
    }
}
