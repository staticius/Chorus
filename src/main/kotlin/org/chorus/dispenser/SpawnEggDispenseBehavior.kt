package org.chorus.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityLiving
import cn.nukkit.item.Item
import cn.nukkit.item.ItemSpawnEgg
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.BlockFace

class SpawnEggDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val pos = block.getSide(face).position.add(0.5, 0.7, 0.5)

        val entity = Entity.createEntity(
            (item as ItemSpawnEgg).entityNetworkId, block.level.getChunk(pos.chunkX, pos.chunkZ),
            Entity.getDefaultNBT(pos)
        )

        this.success = entity != null

        if (this.success) {
            if (item.hasCustomName() && entity is EntityLiving) {
                entity.setNameTag(item.getCustomName())
            }

            entity!!.spawnToAll()

            block.level.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    this,
                    pos.clone(),
                    VibrationType.ENTITY_PLACE
                )
            )
            return null
        }

        return super.dispense(block, face, item)
    }
}
