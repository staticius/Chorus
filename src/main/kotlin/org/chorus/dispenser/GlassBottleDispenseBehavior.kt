package org.chorus.dispenser

import org.chorus.block.BlockDispenser
import org.chorus.block.BlockFlowingWater
import org.chorus.item.Item
import org.chorus.math.BlockFace

class GlassBottleDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val target = block.getSide(face)
        if (target is BlockFlowingWater && target.liquidDepth == 0) return Item.get(Item.POTION)
        return super.dispense(block, face, item)
    }
}
