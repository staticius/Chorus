package cn.nukkit.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.block.BlockFlowingWater
import cn.nukkit.entity.Entity
import cn.nukkit.entity.item.EntityBoat
import cn.nukkit.item.Item
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3

open class BoatDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val pos = block.getSide(face).position.multiply(1.125)

        val target = block.getSide(face)

        if (target is BlockFlowingWater) {
            pos.y += 1.0
        } else if (!target.isAir || target.down() !is BlockFlowingWater) {
            return super.dispense(block, face, item)
        }

        spawnBoatEntity(
            block.level,
            target.position.add(face.xOffset * 0.75, face.yOffset * 0.75, face.zOffset * 0.75),
            item
        )

        return null
    }

    protected open fun spawnBoatEntity(level: Level, pos: Vector3, item: Item) {
        val boat = EntityBoat(
            level.getChunk(pos.chunkX, pos.chunkZ),
            Entity.getDefaultNBT(pos)
                .putInt("Variant", item.damage)
        )
        boat.spawnToAll()
    }
}
