package org.chorus_oss.chorus.metadata

import org.chorus_oss.chorus.plugin.Plugin


interface Metadatable {
    fun setMetadata(metadataKey: String, newMetadataValue: MetadataValue)

    fun getMetadata(metadataKey: String): List<MetadataValue?>?

    fun getMetadata(metadataKey: String, plugin: Plugin): MetadataValue?

    fun hasMetadata(metadataKey: String): Boolean

    fun hasMetadata(metadataKey: String, plugin: Plugin): Boolean

    fun removeMetadata(metadataKey: String, owningPlugin: Plugin)
}
