package org.chorus_oss.chorus.metadata

import org.chorus_oss.chorus.plugin.Plugin
import java.lang.ref.WeakReference


abstract class MetadataValue protected constructor(owningPlugin: Plugin) {
    protected val owningPlugin: WeakReference<Plugin> =
        WeakReference(owningPlugin)

    open fun getOwningPlugin(): Plugin? {
        return owningPlugin.get()
    }

    abstract fun value(): Any?

    abstract fun invalidate()
}
