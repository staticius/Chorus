package org.chorus.dispenser

import org.chorus.block.BlockDispenser
import org.chorus.item.Item
import org.chorus.math.BlockFace

class DyeDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val target = block.getSide(face)

        if (item.isFertilizer) {
            if (target.isFertilizable) {
                target.onActivate(item, null, face, 0f, 0f, 0f)
            } else {
                this.success = false
            }
            return null
        }
        return super.dispense(block, face, item)
    }
}
