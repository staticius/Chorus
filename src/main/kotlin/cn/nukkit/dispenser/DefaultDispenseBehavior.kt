package cn.nukkit.dispenser

import cn.nukkit.Player
import cn.nukkit.block.BlockDispenser
import cn.nukkit.block.BlockID
import cn.nukkit.inventory.EntityInventoryHolder
import cn.nukkit.item.Item
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace
import cn.nukkit.math.SimpleAxisAlignedBB
import cn.nukkit.math.Vector3
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author CreeperFace
 */
open class DefaultDispenseBehavior : DispenseBehavior {
    var success: Boolean = true


    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val dispensePos = block.dispensePosition

        if (face.axis == BlockFace.Axis.Y) {
            dispensePos.up -= 0.125
        } else {
            dispensePos.up -= 0.15625
        }

        val rand: Random = ThreadLocalRandom.current()
        val motion = Vector3()

        val offset = rand.nextDouble() * 0.1 + 0.2

        motion.south = face.xOffset * offset
        motion.up = 0.20000000298023224
        motion.west = face.zOffset * offset

        motion.south += rand.nextGaussian() * 0.007499999832361937 * 6
        motion.up += rand.nextGaussian() * 0.007499999832361937 * 6
        motion.west += rand.nextGaussian() * 0.007499999832361937 * 6

        val clone = item.clone()
        clone.count = 1


        val dropPos = dispensePos.add(face.xOffset.toDouble(), face.yOffset.toDouble(), face.zOffset.toDouble())
        val bb: AxisAlignedBB = SimpleAxisAlignedBB(
            dropPos.getX() - 0.5,
            dropPos.getY() - 1,
            dropPos.getZ() - 0.5,
            dropPos.getX() + 0.5,
            dropPos.getY() + 1,
            dropPos.getZ() + 0.5
        )
        for (e in block.level.getNearbyEntities(bb)) {
            if (e is EntityInventoryHolder && e.canEquipByDispenser()) {
                if (clone.isHelmet && e.helmet.id == BlockID.AIR) {
                    e.setHelmet(clone)
                    return null
                } else if (clone.isChestplate && e.chestplate.id == BlockID.AIR) {
                    e.setChestplate(clone)
                    return null
                } else if (clone.isLeggings && e.leggings.id == BlockID.AIR) {
                    e.setLeggings(clone)
                    return null
                } else if (clone.isBoots && e.boots.id == BlockID.AIR) {
                    e.setBoots(clone)
                    return null
                } else if (e.itemInHand.id == BlockID.AIR) {
                    e.setItemInHand(clone, true)
                    return null
                }
            } else if (e is Player) {
                val armorInventory = e.inventory
                if (clone.isHelmet && armorInventory.helmet.id == BlockID.AIR) {
                    armorInventory.setHelmet(clone)
                    return null
                } else if (clone.isChestplate && armorInventory.chestplate.id == BlockID.AIR) {
                    armorInventory.setChestplate(clone)
                    return null
                } else if (clone.isLeggings && armorInventory.leggings.id == BlockID.AIR) {
                    armorInventory.setLeggings(clone)
                    return null
                } else if (clone.isBoots && armorInventory.boots.id == BlockID.AIR) {
                    armorInventory.setBoots(clone)
                    return null
                }
            }
        }
        block.level.dropItem(dispensePos, clone, motion)
        return null
    }

    private fun getParticleMetadataForFace(face: BlockFace): Int {
        return face.xOffset + 1 + (face.zOffset + 1) * 3
    }
}
