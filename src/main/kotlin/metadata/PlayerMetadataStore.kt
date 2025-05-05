package org.chorus_oss.chorus.metadata

import org.chorus_oss.chorus.IPlayer


class PlayerMetadataStore : MetadataStore() {
    override fun disambiguate(player: Metadatable, metadataKey: String): String {
        require(player is IPlayer) { "Argument must be an IPlayer instance" }
        return (player.name + ":" + metadataKey).lowercase()
    }
}
