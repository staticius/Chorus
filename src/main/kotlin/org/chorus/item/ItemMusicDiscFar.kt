package org.chorus.item

class ItemMusicDiscFar : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_FAR) {
    override val soundId: String
        get() = "record.far"
}