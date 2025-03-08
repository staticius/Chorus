package cn.nukkit.item

import cn.nukkit.entity.EntityID
import cn.nukkit.registry.Registries

class ItemZombieSpawnEgg : ItemSpawnEgg(ItemID.Companion.ZOMBIE_SPAWN_EGG) {
    override val entityNetworkId: Int
        get() = Registries.ENTITY.getEntityNetworkId(EntityID.ZOMBIE)

    override var damage: Int
        get() = super.damage
        set(meta) {
        }
}