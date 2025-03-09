package org.chorus.dispenser

import cn.nukkit.block.Block
import cn.nukkit.block.BlockDispenser
import cn.nukkit.block.BlockID
import cn.nukkit.entity.effect.PotionType
import cn.nukkit.item.Item
import cn.nukkit.item.ItemPotion
import cn.nukkit.math.BlockFace

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
