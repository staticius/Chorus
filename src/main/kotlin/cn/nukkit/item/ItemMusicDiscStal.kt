package cn.nukkit.item

class ItemMusicDiscStal : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_STAL) {
    override val soundId: String
        get() = "record.stal"
}