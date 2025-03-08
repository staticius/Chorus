package cn.nukkit.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.item.Item
import cn.nukkit.level.Sound
import cn.nukkit.math.BlockFace

class DropperDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val target = block.getSide(face)

        if (block.level.getBlockEntityIfLoaded(target.position) is InventoryHolder) {
            val invHolder = block.level.getBlockEntityIfLoaded(target.position) as InventoryHolder
            val inv = invHolder.inventory
            val clone = item.clone()
            clone.count = 1

            if (inv.canAddItem(clone)) {
                inv.addItem(clone)
            } else {
                return clone
            }
        } else {
            block.level.addSound(block.position, Sound.RANDOM_CLICK)
            return super.dispense(block, face, item)
        }
        return null
    }
}
