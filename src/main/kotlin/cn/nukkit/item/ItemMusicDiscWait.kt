package cn.nukkit.item

class ItemMusicDiscWait : ItemMusicDisc(ItemID.Companion.MUSIC_DISC_WAIT) {
    override val soundId: String
        get() = "record.wait"
}