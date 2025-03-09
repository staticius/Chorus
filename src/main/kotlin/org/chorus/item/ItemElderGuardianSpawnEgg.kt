package org.chorus.item

class ItemElderGuardianSpawnEgg : ItemSpawnEgg(ItemID.Companion.ELDER_GUARDIAN_SPAWN_EGG) {
    override var damage: Int
        get() = super.damage
        set(meta) {
        }

    override val entityNetworkId: Int
        get() = 50
}