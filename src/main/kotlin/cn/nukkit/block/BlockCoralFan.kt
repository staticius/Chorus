package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.item.*
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromHorizontalIndex
import cn.nukkit.utils.Faceable
import java.util.concurrent.ThreadLocalRandom

abstract class BlockCoralFan(blockstate: BlockState?) : BlockFlowable(blockstate), Faceable {
    override val waterloggingLevel: Int
        get() = 1

    open val isDead: Boolean
        get() = false

    abstract val deadCoralFan: Block

    override var blockFace: BlockFace?
        get() = fromHorizontalIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CORAL_FAN_DIRECTION) + 1)
        set(blockFace) {
            super.blockFace = blockFace
        }

    open val rootsFace: BlockFace?
        get() = BlockFace.DOWN

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val side = getSide(rootsFace!!)
            if (!side!!.isSolid || side.id == MAGMA || side.id == SOUL_SAND) {
                level.useBreakOn(this.position)
            } else {
                level.scheduleUpdate(this, 60 + ThreadLocalRandom.current().nextInt(40))
            }
            return type
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            val side = getSide(rootsFace!!)
            if (side!!.id == ICE) {
                level.useBreakOn(this.position)
                return type
            }

            if (!isDead && (getLevelBlockAtLayer(1) !is BlockFlowingWater) && (getLevelBlockAtLayer(1) !is BlockFrostedIce)) {
                val event: BlockFadeEvent = BlockFadeEvent(this, deadCoralFan)
                if (!event.isCancelled) {
                    level.setBlock(this.position, event.newState, true, true)
                }
            }
            return type
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CORAL_FAN_DIRECTION) == 0) {
                setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CORAL_FAN_DIRECTION, 1)
            } else {
                setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CORAL_FAN_DIRECTION, 0)
            }
            level.setBlock(this.position, this, true, true)
            return type
        }
        return 0
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player
    ): Boolean {
        if (face == BlockFace.DOWN) {
            return false
        }

        val layer1 = block.getLevelBlockAtLayer(1)
        val hasWater = layer1 is BlockFlowingWater
        if (!layer1!!.isAir && (!hasWater || layer1.blockState!!.specialValue()
                .toInt() != 0 && layer1.blockState!!.specialValue().toInt() != 8)
        ) {
            return false
        }

        if (hasWater && layer1.blockState!!.specialValue().toInt() == 8) {
            level.setBlock(this.position, 1, BlockFlowingWater(), true, false)
        }

        if (!target.isSolid || target.id == MAGMA || target.id == SOUL_SAND) {
            return false
        }

        if (face == BlockFace.UP) {
            var rotation = player.rotation.yaw % 360
            if (rotation < 0) {
                rotation += 360.0
            }
            val axisBit = if (rotation >= 0 && rotation < 12 || (342 <= rotation && rotation < 360)) 0 else 1
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.CORAL_FAN_DIRECTION, axisBit)
            level.setBlock(
                this.position, 0, if (hasWater) this.clone() else deadCoralFan.setPropertyValues(
                    blockState!!.blockPropertyValues
                ), true, true
            )
        } else {
            val deadBlock = deadCoralFan
            level.setBlock(this.position, 0, deadBlock, true, true)
        }

        return true
    }

    override fun canSilkTouch(): Boolean {
        return true
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) != null) {
            super.getDrops(item)
        } else {
            Item.EMPTY_ARRAY
        }
    }
}
