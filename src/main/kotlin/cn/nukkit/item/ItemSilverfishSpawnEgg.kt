package cn.nukkit.item

class ItemSilverfishSpawnEgg : ItemSpawnEgg(ItemID.Companion.SILVERFISH_SPAWN_EGG) {
    override val entityNetworkId: Int
        get() = 39

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}