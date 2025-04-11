package org.chorus.plugin

class InternalPlugin : PluginBase() {
    override val name: String
        get() = "Chorus"

    companion object {
        @JvmField
        val INSTANCE: InternalPlugin = InternalPlugin()
    }
}
