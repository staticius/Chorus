package org.chorus_oss.chorus.metadata

import org.chorus_oss.chorus.entity.Entity


class EntityMetadataStore : MetadataStore() {
    override fun disambiguate(entity: Metadatable, metadataKey: String): String {
        require(entity is Entity) { "Argument must be an Entity instance" }
        return entity.getRuntimeID().toString() + ":" + metadataKey
    }
}
