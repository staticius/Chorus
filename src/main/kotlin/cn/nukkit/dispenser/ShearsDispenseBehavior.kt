package cn.nukkit.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.entity.EntityShearable
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace
import cn.nukkit.math.SimpleAxisAlignedBB

class ShearsDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        var item = item
        item = item.clone()
        val target = block.getSide(face)
        val bb = SimpleAxisAlignedBB(
            0.0, 0.0, 0.0,
            1.0, 1.0, 1.0
        )
            .offset(target.position.south, target.position.up, target.position.west)
        for (entity in block.level.getCollidingEntities(bb)) {
            if (entity !is EntityShearable) {
                continue
            }
            if (!entity.shear()) {
                continue
            }
            item.useOn(entity)
            return if (item.damage >= item.maxDurability) null else item
        }
        return item
    }
}
