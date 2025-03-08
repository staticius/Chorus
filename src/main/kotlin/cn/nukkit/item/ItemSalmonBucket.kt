package cn.nukkit.item

class ItemSalmonBucket : ItemBucket(ItemID.Companion.SALMON_BUCKET) {
    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}