package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.item.*
import cn.nukkit.level.*
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.BlockFace
import cn.nukkit.math.BlockFace.Companion.fromIndex
import cn.nukkit.utils.Faceable
import cn.nukkit.utils.RedstoneComponent
import cn.nukkit.utils.RedstoneComponent.Companion.updateAroundRedstone

/**
 * @author CreeperFace
 * @since 27. 11. 2016
 */
abstract class BlockButton(meta: BlockState?) : BlockFlowable(meta), RedstoneComponent, Faceable {
    override val resistance: Double
        get() = 2.5

    override val hardness: Double
        get() = 0.5

    override val waterloggingLevel: Int
        get() = 1

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (!BlockLever.isSupportValid(target, face)) {
            return false
        }

        blockFace = face
        level.setBlock(block.position, this, true, true)
        return true
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player != null) {
            if (!player.getAdventureSettings().get(AdventureSettings.Type.DOORS_AND_SWITCHED)) return false
            val itemInHand = player.getInventory().itemInHand
            if (player.isSneaking() && !(itemInHand.isTool || itemInHand.isNull)) return false
        }
        if (this.isActivated) {
            return false
        }

        level.scheduleUpdate(this, 30)

        setActivated(true, player)
        level.setBlock(this.position, this, true, false)
        level.addLevelSoundEvent(
            position.add(0.5, 0.5, 0.5)!!, LevelSoundEventPacket.SOUND_POWER_ON,
            blockState!!.blockStateHash()
        )
        if (level.server.settings.levelSettings().enableRedstone()) {
            level.server.pluginManager.callEvent(BlockRedstoneEvent(this, 0, 15))

            updateAroundRedstone()
            updateAroundRedstone(getSide(facing!!.getOpposite()!!)!!, facing)
        }

        return true
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val thisFace = facing
            val touchingFace = thisFace!!.getOpposite()
            val side = this.getSide(touchingFace!!)
            if (!BlockLever.isSupportValid(side, thisFace)) {
                level.useBreakOn(this.position, Item.get(Item.WOODEN_PICKAXE))
                return Level.BLOCK_UPDATE_NORMAL
            }
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (this.isActivated) {
                isActivated = false
                level.setBlock(this.position, this, true, false)
                level.addLevelSoundEvent(
                    position.add(0.5, 0.5, 0.5)!!, LevelSoundEventPacket.SOUND_POWER_OFF,
                    blockState!!.blockStateHash()
                )

                if (level.server.settings.levelSettings().enableRedstone()) {
                    level.server.pluginManager.callEvent(BlockRedstoneEvent(this, 15, 0))

                    updateAroundRedstone()
                    updateAroundRedstone(getSide(facing!!.getOpposite()!!)!!, facing)
                }
            }
            return Level.BLOCK_UPDATE_SCHEDULED
        }

        return 0
    }

    var isActivated: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.BUTTON_PRESSED_BIT)
        set(activated) {
            setActivated(activated, null)
        }

    fun setActivated(activated: Boolean, player: Player?) {
        setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.BUTTON_PRESSED_BIT, activated)
        val pos = this.add(0.5, 0.5, 0.5)
        if (activated) {
            level.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    player
                        ?: this, pos.position, VibrationType.BLOCK_ACTIVATE
                )
            )
        } else {
            level.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    player
                        ?: this, pos.position, VibrationType.BLOCK_DEACTIVATE
                )
            )
        }
    }

    override val isPowerSource: Boolean
        get() = true

    override fun getWeakPower(side: BlockFace?): Int {
        return if (isActivated) 15 else 0
    }

    override fun getStrongPower(side: BlockFace?): Int {
        return if (!isActivated) 0 else (if (facing == side) 15 else 0)
    }

    val facing: BlockFace?
        get() = fromIndex(getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION))

    override fun onBreak(item: Item?): Boolean {
        if (isActivated) {
            level.server.pluginManager.callEvent(BlockRedstoneEvent(this, 15, 0))
        }

        return super.onBreak(item)
    }

    override var blockFace: BlockFace?
        get() = facing
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face!!.index)
        }
}
