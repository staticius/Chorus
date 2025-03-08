package cn.nukkit.item

class ItemEvokerSpawnEgg : ItemSpawnEgg(ItemID.Companion.EVOKER_SPAWN_EGG) {
    override val entityNetworkId: Int
        get() = 104

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}