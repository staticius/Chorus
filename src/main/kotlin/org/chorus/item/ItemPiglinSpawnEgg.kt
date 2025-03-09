package org.chorus.item

class ItemPiglinSpawnEgg : ItemSpawnEgg(ItemID.Companion.PIGLIN_SPAWN_EGG) {
    override val entityNetworkId: Int
        get() = 123

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}