package cn.nukkit.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.block.BlockRail
import cn.nukkit.entity.Entity
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

class MinecartDispenseBehavior(protected val entityType: String) : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val target = block.getSide(face)
        if (target is BlockRail) {
            target.position.x += 0.5
            target.position.y += 0.125
            target.position.z += 0.5
        } else return super.dispense(block, face, item)
        val minecart = Entity.createEntity(this.entityType, target)
        minecart?.spawnToAll()
        return null
    }
}
