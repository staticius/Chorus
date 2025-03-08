package cn.nukkit.item

class ItemMusicDiscMall : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_MALL) {
    override val soundId: String
        get() = "record.mall"
}