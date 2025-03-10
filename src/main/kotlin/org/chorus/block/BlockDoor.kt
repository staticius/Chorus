package org.chorus.block

import org.chorus.AdventureSettings
import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.MinecraftCardinalDirection
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.event.block.BlockRedstoneEvent
import org.chorus.event.block.DoorToggleEvent
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.level.Locator
import org.chorus.level.Sound
import org.chorus.level.vibration.VibrationEvent
import org.chorus.level.vibration.VibrationType
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace
import org.chorus.math.BlockFace.AxisDirection
import org.chorus.math.SimpleAxisAlignedBB
import org.chorus.utils.Faceable
import org.chorus.utils.RedstoneComponent
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.get
import kotlin.collections.set


abstract class BlockDoor(blockState: BlockState?) : BlockTransparent(blockState), RedstoneComponent,
    Faceable {
    override fun canBeActivated(): Boolean {
        return true
    }

    override val waterloggingLevel: Int
        get() = 1

    override val isSolid: Boolean
        get() = false

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override fun recalculateBoundingBox(): AxisAlignedBB? {
        val position = blockFace!!.getOpposite()
        val isOpen = isOpen
        val isRight = isRightHinged

        return if (isOpen) {
            recalculateBoundingBoxWithPos(if (isRight) position!!.rotateYCCW() else position!!.rotateY())
        } else {
            recalculateBoundingBoxWithPos(position!!)
        }
    }

    private fun recalculateBoundingBoxWithPos(pos: BlockFace): AxisAlignedBB {
        return if (pos.axisDirection == AxisDirection.NEGATIVE) {
            SimpleAxisAlignedBB(
                position.x,
                position.y,
                position.z,
                position.x + 1 + pos.xOffset - (THICKNESS * pos.xOffset),
                position.y + 1,
                position.z + 1 + pos.zOffset - (THICKNESS * pos.zOffset)
            )
        } else {
            SimpleAxisAlignedBB(
                position.x + pos.xOffset - (THICKNESS * pos.xOffset),
                position.y,
                position.z + pos.zOffset - (THICKNESS * pos.zOffset),
                position.x + 1,
                position.y + 1,
                position.z + 1
            )
        }
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            this.onNormalUpdate()
            return type
        }

        if (type == Level.BLOCK_UPDATE_REDSTONE && level.server.settings.levelSettings().enableRedstone()) {
            this.onRedstoneUpdate()
            return type
        }

        return 0
    }

    private fun onNormalUpdate() {
        val down = this.down()
        if (isTop) {
            if (down!!.id != this.id || down.getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.UPPER_BLOCK_BIT)) {
                level.setBlock(this.position, get(AIR), false)
            }

            /* Doesn't work with redstone
            boolean downIsOpen = down.getBooleanValue(OPEN);
            if (downIsOpen != isOpen()) {
                setOpen(downIsOpen);
                level.setBlock(this, this, false, true);
            }*/
            return
        }

        if (down!!.id == AIR) {
            level.useBreakOn(
                this.position,
                if (toolType == ItemTool.TYPE_PICKAXE) Item.get(ItemID.DIAMOND_PICKAXE) else null
            )
        }
    }

    private fun onRedstoneUpdate() {
        if ((this.isOpen != this.isGettingPower) && !this.manualOverride) {
            if (this.isOpen != this.isGettingPower) {
                level.server.pluginManager.callEvent(
                    BlockRedstoneEvent(
                        this,
                        if (this.isOpen) 15 else 0,
                        if (this.isOpen) 0 else 15
                    )
                )

                this.setOpen(null, this.isGettingPower)
            }
        } else if (this.manualOverride && (this.isGettingPower == this.isOpen)) {
            this.manualOverride = false
        }
    }

    var manualOverride: Boolean
        get() {
            val down: Locator
            val up: Locator
            if (this.isTop) {
                down = down()!!.locator
                up = locator
            } else {
                down = locator
                up = up()!!.locator
            }

            return manualOverrides.contains(up) || manualOverrides.contains(
                down
            )
        }
        set(val) {
            val down: Locator
            val up: Locator
            if (this.isTop) {
                down = down()!!.locator
                up = locator
            } else {
                down = locator
                up = up()!!.locator
            }

            if (`val`) {
                manualOverrides.add(up)
                manualOverrides.add(down)
            } else {
                manualOverrides.remove(up)
                manualOverrides.remove(down)
            }
        }

    override val isGettingPower: Boolean
        get() {
            val down: Locator
            val up: Locator
            if (this.isTop) {
                down = down()!!.locator
                up = locator
            } else {
                down = locator
                up = up()!!.locator
            }

            for (side in BlockFace.entries) {
                val blockDown = down.getSide(side)!!.levelBlock
                val blockUp = up.getSide(side)!!.levelBlock

                if (level.isSidePowered(blockDown!!.position, side)
                    || level.isSidePowered(blockUp!!.position, side)
                ) {
                    return true
                }
            }

            return level.isBlockPowered(down.position) || level.isBlockPowered(up.position)
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
        if (position.y > level.maxHeight - 1 || face != BlockFace.UP) {
            return false
        }

        val blockUp = this.up()
        val blockDown = this.down()
        if (!blockUp!!.canBeReplaced() || !blockDown!!.isSolid(BlockFace.UP) && blockDown !is BlockCauldron) {
            return false
        }

        val direction = if (player != null) player.getDirection() else BlockFace.SOUTH
        blockFace = direction

        val left = this.getSide(direction!!.rotateYCCW())
        val right = this.getSide(direction.rotateY())
        if (left!!.id == this.id || (!right!!.isTransparent && left.isTransparent)) { //Door hinge
            isRightHinged = true
        }

        isTop = false

        level.setBlock(block.position, this, true, false) //Bottom

        if (blockUp is BlockLiquid && blockUp.usesWaterLogging()) {
            level.setBlock(blockUp.position, 1, blockUp, true, false)
        }

        val doorTop = clone() as BlockDoor
        doorTop.position.y++
        doorTop.isTop = true
        level.setBlock(doorTop.position, doorTop, true, true) //Top

        level.updateAround(block.position)

        if (level.server.settings.levelSettings().enableRedstone() && !this.isOpen && this.isGettingPower) {
            this.setOpen(null, true)
        }

        return true
    }

    override fun onBreak(item: Item?): Boolean {
        this.manualOverride = false
        if (isTop) {
            val down = this.down()
            if (down!!.id == this.id && !down.getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.UPPER_BLOCK_BIT)) {
                level.setBlock(down.position, get(AIR), true)
            }
        } else {
            val up = this.up()
            if (up!!.id == this.id && up.getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.UPPER_BLOCK_BIT)) {
                level.setBlock(up.position, get(AIR), true)
            }
        }
        level.setBlock(this.position, get(AIR), true)

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
            val itemInHand = player.getInventory().itemInHand
            if (player.isSneaking() && !(itemInHand.isTool || itemInHand.isNull)) return false
        }
        return toggle(player)
    }

    fun playOpenCloseSound() {
        if (this.isTop && down() is BlockDoor) {
            if ((down() as BlockDoor).isOpen) {
                this.playOpenSound()
            } else {
                this.playCloseSound()
            }
        } else if (up() is BlockDoor) {
            if (this.isOpen) {
                this.playOpenSound()
            } else {
                this.playCloseSound()
            }
        }
    }

    open fun playOpenSound() {
        level.addSound(this.position, Sound.RANDOM_DOOR_OPEN)
    }

    open fun playCloseSound() {
        level.addSound(this.position, Sound.RANDOM_DOOR_CLOSE)
    }

    fun toggle(player: Player?): Boolean {
        if (player != null) {
            if (!player.getAdventureSettings()!!.get(AdventureSettings.Type.DOORS_AND_SWITCHED)) return false
        }
        return this.setOpen(player, !this.isOpen)
    }

    fun setOpen(player: Player?, open: Boolean): Boolean {
        var player = player
        if (open == this.isOpen) {
            return false
        }

        val event = DoorToggleEvent(this, player!!)
        level.server.pluginManager.callEvent(event)

        if (event.isCancelled) {
            return false
        }

        player = event.player

        val down: Block?
        val up: Block?
        if (this.isTop) {
            down = down()
            up = this
        } else {
            down = this
            up = up()
        }

        up!!.setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OPEN_BIT, open)
        up.level.setBlock(up.position, up, true, true)

        down!!.setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OPEN_BIT, open)
        down.level.setBlock(down.position, down, true, true)

        if (player != null) {
            this.manualOverride = this.isGettingPower || this.isOpen
        }

        playOpenCloseSound()

        val source = vector3.add(0.5, 0.5, 0.5)
        val vibrationEvent = if (open) VibrationEvent(
            player ?: this,
            source!!, VibrationType.BLOCK_OPEN
        ) else VibrationEvent(
            player ?: this,
            source!!, VibrationType.BLOCK_CLOSE
        )
        level.vibrationManager.callVibrationEvent(vibrationEvent)
        return true
    }

    var isOpen: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OPEN_BIT)
        set(open) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OPEN_BIT, open)
        }

    var isTop: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.UPPER_BLOCK_BIT)
        set(top) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.UPPER_BLOCK_BIT, top)
        }

    var isRightHinged: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.DOOR_HINGE_BIT)
        set(rightHinged) {
            setPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.DOOR_HINGE_BIT, rightHinged)
        }

    override var blockFace: BlockFace?
        get() = DOOR_DIRECTION.inverse()[getPropertyValue(
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION
        ).ordinal]
        set(face) {
            setPropertyValue(
                CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
                MinecraftCardinalDirection.entries[DOOR_DIRECTION[face]!!]
            )
        }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    companion object {
        private const val THICKNESS = 3.0 / 16

        // Contains a list of positions of doors, which have been opened by hand (by a player).
        // It is used to detect on redstone update, if the door should be closed if redstone is off on the update,
        // previously the door always closed, when placing an unpowered redstone at the door, this fixes it
        // and gives the vanilla behavior; no idea how to make this better :d
        private val manualOverrides: MutableList<Locator> = ArrayList()

        protected val DOOR_DIRECTION: BiMap<BlockFace, Int> = HashBiMap.create(4)

        init {
            DOOR_DIRECTION[BlockFace.EAST] = 0
            DOOR_DIRECTION[BlockFace.SOUTH] = 1
            DOOR_DIRECTION[BlockFace.WEST] = 2
            DOOR_DIRECTION[BlockFace.NORTH] = 3
        }
    }
}
