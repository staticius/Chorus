package cn.nukkit.dispenser

import cn.nukkit.entity.Entity
import cn.nukkit.entity.item.EntityChestBoat
import cn.nukkit.item.Item
import cn.nukkit.item.ItemChestBoat
import cn.nukkit.level.Level
import cn.nukkit.math.Vector3

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
