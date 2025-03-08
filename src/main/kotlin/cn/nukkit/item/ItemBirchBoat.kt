package cn.nukkit.item

class ItemBirchBoat : ItemBoat(ItemID.Companion.BIRCH_BOAT) {
    override val boatId: Int
        get() = 2

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}