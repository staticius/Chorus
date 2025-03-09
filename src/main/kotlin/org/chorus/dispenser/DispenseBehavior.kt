package org.chorus.dispenser

import org.chorus.block.BlockDispenser
import org.chorus.item.Item
import org.chorus.math.BlockFace

/**
 * @author CreeperFace
 */
interface DispenseBehavior {
    fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item?
}
