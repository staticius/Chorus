package cn.nukkit.metadata

import cn.nukkit.entity.Entity

/**
 * @author MagicDroidX (Nukkit Project)
 */
class EntityMetadataStore : MetadataStore() {
    override fun disambiguate(entity: Metadatable, metadataKey: String): String {
        require(entity is Entity) { "Argument must be an Entity instance" }
        return entity.getId().toString() + ":" + metadataKey
    }
}
