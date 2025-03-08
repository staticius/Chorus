package cn.nukkit.item

class ItemAllaySpawnEgg : ItemSpawnEgg(ItemID.Companion.ALLAY_SPAWN_EGG) {
    override val entityNetworkId: Int
        get() = 134

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}