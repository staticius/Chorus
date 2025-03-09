package org.chorus.metadata

import cn.nukkit.level.Level

/**
 * @author MagicDroidX (Nukkit Project)
 */
class LevelMetadataStore : MetadataStore() {
    override fun disambiguate(level: Metadatable, metadataKey: String): String {
        require(level is Level) { "Argument must be a Level instance" }
        return ((level as Level).getName() + ":" + metadataKey).lowercase()
    }
}
