package org.chorus.plugin




(access = AccessLevel.PRIVATE)
class InternalPlugin : PluginBase() {
    override val name: String
        get() = "PowerNukkitX"

    companion object {
        @JvmField
        val INSTANCE: InternalPlugin = InternalPlugin()
    }
}
