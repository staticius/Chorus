package cn.nukkit.metadata

import cn.nukkit.plugin.Plugin

/**
 * @author MagicDroidX (Nukkit Project)
 */
interface Metadatable {
    fun setMetadata(metadataKey: String?, newMetadataValue: MetadataValue?)

    fun getMetadata(metadataKey: String?): List<MetadataValue?>?

    fun getMetadata(metadataKey: String?, plugin: Plugin?): MetadataValue?

    fun hasMetadata(metadataKey: String?): Boolean

    fun hasMetadata(metadataKey: String?, plugin: Plugin?): Boolean

    fun removeMetadata(metadataKey: String?, owningPlugin: Plugin?)
}
