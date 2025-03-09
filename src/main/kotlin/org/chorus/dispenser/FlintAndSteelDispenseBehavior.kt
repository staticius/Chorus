package org.chorus.dispenser

import org.chorus.block.Block
import org.chorus.block.BlockDispenser
import org.chorus.block.BlockID
import org.chorus.item.Item
import org.chorus.level.Level
import org.chorus.level.Sound
import org.chorus.math.BlockFace

class FlintAndSteelDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        var item = item
        val target = block.getSide(face)
        item = item.clone()

        val down = target.down()
        if (down.id === BlockID.OBSIDIAN) {
            if (down.level.dimension != Level.DIMENSION_THE_END) {
                if (down.level.createPortal(down)) {
                    item.useOn(target)
                    return if (item.damage >= item.maxDurability) null else item
                }
            }
        }

        if (target.id === BlockID.AIR) {
            block.level.addSound(block.position, Sound.RANDOM_CLICK, 1.0f, 1.0f)
            block.level.setBlock(target.position, Block.get(BlockID.FIRE))
            item.useOn(target)
            return if (item.damage >= item.maxDurability) null else item
        } else if (target.id === BlockID.TNT) {
            block.level.addSound(block.position, Sound.RANDOM_CLICK, 1.0f, 1.0f)
            target.onActivate(item, null, face, 0f, 0f, 0f)
            item.useOn(target)
            return if (item.damage >= item.maxDurability) null else item
        } else {
            this.success = false
        }

        block.level.addSound(block.position, Sound.RANDOM_CLICK, 1.0f, 1.2f)
        return item
    }
}
