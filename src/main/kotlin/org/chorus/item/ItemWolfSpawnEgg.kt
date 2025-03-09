package org.chorus.item

class ItemWolfSpawnEgg : ItemSpawnEgg(ItemID.Companion.WOLF_SPAWN_EGG) {
    override val entityNetworkId: Int
        get() = 14

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}