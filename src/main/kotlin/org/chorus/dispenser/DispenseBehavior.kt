package org.chorus.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

/**
 * @author CreeperFace
 */
interface DispenseBehavior {
    fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item?
}
