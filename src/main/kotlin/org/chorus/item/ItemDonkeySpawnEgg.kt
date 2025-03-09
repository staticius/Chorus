package org.chorus.item

class ItemDonkeySpawnEgg : ItemSpawnEgg(ItemID.Companion.DONKEY_SPAWN_EGG) {
    override val entityNetworkId: Int
        get() = 24

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}