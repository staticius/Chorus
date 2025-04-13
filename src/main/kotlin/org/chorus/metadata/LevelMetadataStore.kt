package org.chorus.metadata

import org.chorus.level.Level


class LevelMetadataStore : MetadataStore() {
    override fun disambiguate(level: Metadatable, metadataKey: String): String {
        require(level is Level) { "Argument must be a Level instance" }
        return (level.getLevelName() + ":" + metadataKey).lowercase()
    }
}
