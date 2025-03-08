package cn.nukkit.item

class ItemMusicDiscOtherside : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_OTHERSIDE) {
    override val soundId: String
        get() = "record.otherside"
}