package org.chorus.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.entity.Entity
import cn.nukkit.entity.item.EntityFireworksRocket
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace
import cn.nukkit.nbt.NBTIO

class FireworksDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val opposite = face.opposite
        val pos = block.getSide(face).position.add(
            0.5 + opposite.xOffset * 0.2,
            0.5 + opposite.yOffset * 0.2,
            0.5 + opposite.zOffset * 0.2
        )

        val nbt = Entity.getDefaultNBT(pos)
        nbt.putCompound("FireworkItem", NBTIO.putItemHelper(item))
        val firework = EntityFireworksRocket(block.level.getChunk(pos.chunkX, pos.chunkZ), nbt)
        firework.spawnToAll()

        return null
    }
}
