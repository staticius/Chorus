package org.chorus.plugin

import lombok.AccessLevel
import lombok.NoArgsConstructor

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class InternalPlugin : PluginBase() {
    override val name: String
        get() = "PowerNukkitX"

    companion object {
        @JvmField
        val INSTANCE: InternalPlugin = InternalPlugin()
    }
}
