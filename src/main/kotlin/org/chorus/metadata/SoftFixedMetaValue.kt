package org.chorus.metadata

import org.chorus.plugin.Plugin
import java.lang.ref.SoftReference

class SoftFixedMetaValue(owningPlugin: Plugin, value: Any) : LazyMetadataValue(owningPlugin) {
    /**
     * Store the internal value that is represented by this fixed value.
     */
    private val internalValue =
        SoftReference(value)

    override fun invalidate() {
        internalValue.clear()
    }

    override fun value(): Any? {
        return internalValue.get()
    }
}
