package org.chorus.metadata

import cn.nukkit.IPlayer

/**
 * @author MagicDroidX (Nukkit Project)
 */
class PlayerMetadataStore : MetadataStore() {
    override fun disambiguate(player: Metadatable, metadataKey: String): String {
        require(player is IPlayer) { "Argument must be an IPlayer instance" }
        return (player.name + ":" + metadataKey).lowercase()
    }
}
