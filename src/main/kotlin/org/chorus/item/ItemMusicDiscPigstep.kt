package org.chorus.item

class ItemMusicDiscPigstep : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_PIGSTEP) {
    override val soundId: String
        get() = "record.pigstep"
}