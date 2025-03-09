package org.chorus.metadata

import cn.nukkit.plugin.Plugin
import java.lang.ref.WeakReference

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class MetadataValue protected constructor(owningPlugin: Plugin) {
    protected val owningPlugin: WeakReference<Plugin> =
        WeakReference(owningPlugin)

    open fun getOwningPlugin(): Plugin? {
        return owningPlugin.get()
    }

    abstract fun value(): Any?

    abstract fun invalidate()
}
