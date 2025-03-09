package org.chorus.item

class ItemMusicDiscWard : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_WARD) {
    override val soundId: String
        get() = "record.ward"
}