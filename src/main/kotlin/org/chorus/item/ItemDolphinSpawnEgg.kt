package org.chorus.item

class ItemDolphinSpawnEgg : ItemSpawnEgg(ItemID.Companion.DOLPHIN_SPAWN_EGG) {
    override val entityNetworkId: Int
        get() = 31

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}