package org.chorus.item

class ItemMusicDiscRelic : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_RELIC) {
    override val soundId: String
        get() = "record.relic"
}