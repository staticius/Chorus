package org.chorus.dispenser

import org.chorus.entity.Entity
import org.chorus.entity.item.EntityChestBoat
import org.chorus.item.Item
import org.chorus.item.ItemChestBoat
import org.chorus.level.Level
import org.chorus.math.Vector3

class ChestBoatDispenseBehavior : BoatDispenseBehavior() {
    override fun spawnBoatEntity(level: Level, pos: Vector3, item: Item) {
        val boat = EntityChestBoat(
            level.getChunk(pos.chunkX, pos.chunkZ),
            Entity.getDefaultNBT(pos)
                .putInt("Variant", (item as ItemChestBoat).boatId)
        )
        boat.spawnToAll()
    }
}
