package org.chorus.block

import cn.nukkit.AdventureSettings
import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.LeverDirection
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.event.block.BlockRedstoneEvent
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.level.Sound
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.BlockFace
import cn.nukkit.utils.Faceable
import cn.nukkit.utils.RedstoneComponent
import cn.nukkit.utils.RedstoneComponent.Companion.updateAroundRedstone

/**
 * @author Nukkit Project Team
 */
class BlockLever @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockFlowable(blockstate), RedstoneComponent, Faceable {
    override val name: String
        get() = "Lever"

    override fun canBeActivated(): Boolean {
        return true
    }

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 2.5

    override fun toItem(): Item? {
        return ItemBlock(this, 0)
    }

    var isPowerOn: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OPEN_BIT)
        set(powerOn) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OPEN_BIT, powerOn)
        }

    var leverOrientation: LeverDirection?
        get() = getPropertyValue(CommonBlockProperties.LEVER_DIRECTION)
        set(value) {
            setPropertyValue(
                CommonBlockProperties.LEVER_DIRECTION,
                value
            )
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
            if (!player.getAdventureSettings()!!.get(AdventureSettings.Type.DOORS_AND_SWITCHED)) return false
            if (isNotActivate(player)) return false
        }
        level.server.pluginManager.callEvent(
            BlockRedstoneEvent(
                this,
                if (isPowerOn) 15 else 0,
                if (isPowerOn) 0 else 15
            )
        )
        isPowerOn = !isPowerOn
        val pos = this.add(0.5, 0.5, 0.5)
        level.vibrationManager.callVibrationEvent(
            VibrationEvent(
                player
                    ?: this,
                pos.position,
                if (isPowerOn) VibrationType.BLOCK_ACTIVATE else VibrationType.BLOCK_DEACTIVATE
            )
        )
        level.setBlock(this.position, this, false, true)
        level.addSound(this.position, Sound.RANDOM_CLICK, 0.8f, if (isPowerOn) 0.58f else 0.5f)

        val orientation = leverOrientation!!
        val face = orientation.facing

        if (level.server.settings.levelSettings().enableRedstone()) {
            updateAroundRedstone()
            updateAroundRedstone(getSide(face.getOpposite()!!)!!, face)
        }
        return true
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val blockFace = leverOrientation!!.facing.getOpposite()
            val side = this.getSide(blockFace!!)
            if (!isSupportValid(side!!, blockFace.getOpposite()!!)) {
                level.useBreakOn(this.position)
            }
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
        var target = target
        var face = face
        if (target.canBeReplaced()) {
            target = target.down()!!
            face = BlockFace.UP
        }

        if (!isSupportValid(target, face)) {
            return false
        }
        leverOrientation = LeverDirection.forFacings(face, player.getHorizontalFacing())
        return level.setBlock(block.position, this, true, true)
    }

    override fun onBreak(item: Item?): Boolean {
        level.setBlock(this.position, get(BlockID.AIR), true, true)

        if (isPowerOn) {
            val face = leverOrientation!!.facing
            level.updateAround(position.getSide(face.getOpposite()!!)!!)

            if (level.server.settings.levelSettings().enableRedstone()) {
                updateAroundRedstone()
                updateAroundRedstone(getSide(face.getOpposite()!!)!!, face)
            }
        }
        return true
    }

    override fun getWeakPower(side: BlockFace?): Int {
        return if (isPowerOn) 15 else 0
    }

    override fun getStrongPower(side: BlockFace?): Int {
        return if (!isPowerOn) 0 else if (leverOrientation!!.facing == side) 15 else 0
    }

    override val isPowerSource: Boolean
        get() = true

    override val waterloggingLevel: Int
        get() = 2

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override var blockFace: BlockFace
        get() = leverOrientation!!.facing
        set(blockFace) {
            super.blockFace = blockFace
        }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LEVER, CommonBlockProperties.LEVER_DIRECTION, CommonBlockProperties.OPEN_BIT)
            get() = Companion.field

        /**
         * Check if the given block and its block face is a valid support for a lever
         *
         * @param support The block that the lever is being placed against
         * @param face    The face that the torch will be touching the block
         * @return If the support and face combinations can hold the lever
         */
        @JvmStatic
        fun isSupportValid(support: Block, face: BlockFace): Boolean {
            when (support.id) {
                BlockID.FARMLAND, BlockID.GRASS_PATH -> {
                    return true
                }

                else -> {}
            }

            if (face == BlockFace.DOWN) {
                return support.isSolid(BlockFace.DOWN) && (support.isFullBlock || !support.isTransparent)
            }

            if (support.isSolid(face)) {
                return true
            }

            if (support is BlockWallBase || support is BlockFence) {
                return face == BlockFace.UP
            }

            return false
        }
    }
}
