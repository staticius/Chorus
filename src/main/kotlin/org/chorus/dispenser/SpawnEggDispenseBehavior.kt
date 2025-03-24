package org.chorus.dispenser

import org.chorus.block.BlockDispenser
import org.chorus.entity.Entity
import org.chorus.entity.EntityLiving
import org.chorus.item.Item
import org.chorus.item.ItemSpawnEgg
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.math.BlockFace

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
                entity.setNameTag(item.customName)
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
