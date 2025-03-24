package org.chorus.dispenser

import org.chorus.block.BlockDispenser
import org.chorus.item.Item
import org.chorus.math.BlockFace


interface DispenseBehavior {
    fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item?
}
