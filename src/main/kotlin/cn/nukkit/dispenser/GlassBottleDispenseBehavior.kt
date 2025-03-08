package cn.nukkit.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.block.BlockFlowingWater
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

class GlassBottleDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val target = block.getSide(face)
        if (target is BlockFlowingWater && target.liquidDepth == 0) return Item.get(Item.POTION)
        return super.dispense(block, face, item)
    }
}
