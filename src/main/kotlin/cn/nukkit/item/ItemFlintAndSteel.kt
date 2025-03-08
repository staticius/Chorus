package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.event.block.BlockIgniteEvent
import cn.nukkit.level.Level
import cn.nukkit.level.Sound
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.BlockFace
import java.util.concurrent.ThreadLocalRandom

class ItemFlintAndSteel @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemTool(ItemID.Companion.FLINT_AND_STEEL, meta, count, "Flint and Steel") {
    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        level: Level,
        player: Player,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double
    ): Boolean {
        if (player.isAdventure) {
            return false
        }

        if (block.isAir && target.burnChance != -1 && (target.isSolid || target.burnChance > 0)) {
            if (target.id == BlockID.OBSIDIAN) {
                if (level.dimension != Level.DIMENSION_THE_END) {
                    if (level.createPortal(target)) {
                        damageItem(player, block)
                        return true
                    }
                }
            }

            val fire = Block.get(BlockID.FIRE) as BlockFire
            fire.position.south = block.position.south
            fire.position.up = block.position.up
            fire.position.west = block.position.west
            fire.level = level

            if (fire.isBlockTopFacingSurfaceSolid(fire.down()) || fire.canNeighborBurn()) {
                val e = BlockIgniteEvent(block, null, player, BlockIgniteEvent.BlockIgniteCause.FLINT_AND_STEEL)
                block.level.server.pluginManager.callEvent(e)

                if (!e.isCancelled) {
                    level.setBlock(fire.position, fire, true)
                    level.scheduleUpdate(fire, fire.tickRate() + ThreadLocalRandom.current().nextInt(10))
                }
                damageItem(player, block)
                return true
            }

            damageItem(player, block)
            return true
        }
        damageItem(player, block)
        return false
    }

    private fun damageItem(player: Player, block: Block) {
        if (!player.isCreative && useOn(block)) {
            if (this.damage >= this.maxDurability) {
                this.count = 0
                player.getInventory().setItemInHand(Item.Companion.AIR)
            } else {
                player.getInventory().setItemInHand(this)
            }
        }
        block.level.addSound(block.position, Sound.FIRE_IGNITE)
    }

    override val maxDurability: Int
        get() = ItemTool.Companion.DURABILITY_FLINT_STEEL

    override fun useOn(block: Block): Boolean {
        //todo: initiator should be an entity who use it but not null
        block.level.vibrationManager.callVibrationEvent(
            VibrationEvent(
                null,
                block.position.add(0.5, 0.5, 0.5),
                VibrationType.BLOCK_PLACE
            )
        )
        return super.useOn(block)
    }
}