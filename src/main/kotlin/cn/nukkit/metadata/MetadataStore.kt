package cn.nukkit.metadata

import cn.nukkit.plugin.Plugin
import cn.nukkit.utils.PluginException
import cn.nukkit.utils.ServerException
import java.util.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class MetadataStore {
    protected val metadataMap: MutableMap<String, MutableMap<Plugin, MetadataValue?>> = HashMap()

    open fun setMetadata(subject: Metadatable, metadataKey: String, newMetadataValue: MetadataValue) {
        if (newMetadataValue == null) {
            throw ServerException("Value cannot be null")
        }
        val owningPlugin = newMetadataValue.getOwningPlugin()
            ?: throw PluginException("Plugin cannot be null")
        val key = this.disambiguate(subject, metadataKey)
        val entry = metadataMap.computeIfAbsent(key) { k: String? -> WeakHashMap(1) }
        entry[owningPlugin] = newMetadataValue
    }

    open fun getMetadata(subject: Metadatable, metadataKey: String): List<MetadataValue?>? {
        val key = this.disambiguate(subject, metadataKey)
        if (metadataMap.containsKey(key)) {
            val values: Collection<MetadataValue?> = metadataMap[key]!!.values
            return Collections.unmodifiableList(ArrayList(values))
        }
        return emptyList<MetadataValue>()
    }

    open fun getMetadata(subject: Metadatable, metadataKey: String, plugin: Plugin): MetadataValue? {
        val key = this.disambiguate(subject, metadataKey)
        if (metadataMap.containsKey(key)) {
            val pluginMetadataValueMap: Map<Plugin, MetadataValue?> =
                metadataMap[key]!!
            return pluginMetadataValueMap[plugin]
        }
        return null
    }

    open fun hasMetadata(subject: Metadatable, metadataKey: String): Boolean {
        return metadataMap.containsKey(this.disambiguate(subject, metadataKey))
    }

    open fun hasMetadata(subject: Metadatable, metadataKey: String, plugin: Plugin): Boolean {
        val pluginMetadataValueMap: Map<Plugin, MetadataValue?>? =
            metadataMap[disambiguate(subject, metadataKey)]
        return pluginMetadataValueMap?.containsKey(plugin) ?: false
    }

    open fun removeMetadata(subject: Metadatable, metadataKey: String, owningPlugin: Plugin) {
        if (owningPlugin == null) {
            throw PluginException("Plugin cannot be null")
        }
        val key = this.disambiguate(subject, metadataKey)
        val entry = metadataMap[key] ?: return
        entry.remove(owningPlugin)
        if (entry.isEmpty()) {
            metadataMap.remove(key)
        }
    }

    fun invalidateAll(owningPlugin: Plugin) {
        if (owningPlugin == null) {
            throw PluginException("Plugin cannot be null")
        }
        for (value in metadataMap.values) {
            if (value.containsKey(owningPlugin)) {
                value[owningPlugin]!!.invalidate()
            }
        }
    }

    protected abstract fun disambiguate(subject: Metadatable, metadataKey: String): String
}
