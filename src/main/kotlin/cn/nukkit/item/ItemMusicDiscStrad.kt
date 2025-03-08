package cn.nukkit.item

class ItemMusicDiscStrad : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_STRAD) {
    override val soundId: String
        get() = "record.strad"
}