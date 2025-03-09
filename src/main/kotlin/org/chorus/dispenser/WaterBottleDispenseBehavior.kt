package org.chorus.dispenser

import org.chorus.block.Block
import org.chorus.block.BlockDispenser
import org.chorus.block.BlockID
import org.chorus.entity.effect.PotionType
import org.chorus.item.Item
import org.chorus.item.ItemPotion
import org.chorus.math.BlockFace

class WaterBottleDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        if ((item as ItemPotion).potion !== PotionType.WATER) return super.dispense(block, face, item)
        val targetId = block.getSide(face).id
        if (targetId === BlockID.DIRT || targetId === BlockID.DIRT_WITH_ROOTS) {
            block.level.setBlock(block.position.getSideVec(face), Block.get(BlockID.MUD))
            return null
        }
        return super.dispense(block, face, item)
    }
}
