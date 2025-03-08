package cn.nukkit.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.entity.Entity
import cn.nukkit.entity.item.EntityTnt
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

class TNTDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val pos = block.getSide(face).position.add(0.5, 0.0, 0.5)

        val tnt = EntityTnt(
            block.level.getChunk(pos.chunkX, pos.chunkZ),
            Entity.getDefaultNBT(pos)
        )
        tnt.spawnToAll()

        return null
    }
}
