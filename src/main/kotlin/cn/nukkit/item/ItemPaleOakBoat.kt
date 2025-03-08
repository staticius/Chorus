package cn.nukkit.item

class ItemPaleOakBoat : ItemBoat(ItemID.Companion.PALE_OAK_BOAT) {
    override val boatId: Int
        get() = 9

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}