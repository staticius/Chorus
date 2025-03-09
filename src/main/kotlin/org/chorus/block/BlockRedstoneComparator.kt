package org.chorus.block

import org.chorus.Player
import org.chorus.block.Block.Companion.get
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.BooleanPropertyType
import org.chorus.block.property.type.EnumPropertyType
import org.chorus.blockentity.BlockEntity
import org.chorus.item.*
import org.chorus.item.Item.Companion.get
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.nbt.tag.CompoundTag.putList
import org.chorus.nbt.tag.Tag
import org.chorus.utils.RedstoneComponent.Companion.updateAroundRedstone
import org.chorus.utils.RedstoneComponent.updateAroundRedstone
import lombok.extern.slf4j.Slf4j
import kotlin.math.max

/**
 * @author CreeperFace
 */

abstract class BlockRedstoneComparator(blockstate: BlockState?) : BlockRedstoneDiode(blockstate),
    BlockEntityHolder<BlockEntityComparator?> {
    override val blockEntityClass: Class<out Any>
        get() = BlockEntityComparator::class.java

    override val blockEntityType: String
        get() = BlockEntity.COMPARATOR

    override val delay: Int
        get() = 2

    override val facing: BlockFace?
        get() = CommonPropertyMap.CARDINAL_BLOCKFACE.get(
            getPropertyValue<MinecraftCardinalDirection, org.chorus.block.property.type.EnumPropertyType<MinecraftCardinalDirection>>(
                CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION
            )
        )

    var mode: Mode
        get() = if (getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OUTPUT_SUBTRACT_BIT)) Mode.SUBTRACT else Mode.COMPARE
        set(mode) {
            setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.OUTPUT_SUBTRACT_BIT,
                mode == Mode.SUBTRACT
            )
        }

    override val unpowered: Block
        get() = get(BlockID.UNPOWERED_COMPARATOR)
            .setPropertyValues(blockState!!.blockPropertyValues) as BlockRedstoneComparator

    public override fun getPowered(): BlockRedstoneComparator {
        return get(BlockID.POWERED_COMPARATOR).setPropertyValues(blockState!!.blockPropertyValues) as BlockRedstoneComparator
    }

    override val redstoneSignal: Int
        get() {
            val comparator: BlockEntityComparator? = blockEntity
            return if (comparator == null) 0 else comparator.outputSignal
        }

    override fun updateState() {
        if (!level.isBlockTickPending(this.position, this)) {
            val output = this.calculateOutput()
            val power = redstoneSignal

            if (output != power || this.isPowered() != this.shouldBePowered()) {
                level.scheduleUpdate(this, this.position, 2)
            }
        }
    }

    override fun calculateInputStrength(): Int {
        var power = super.calculateInputStrength()
        val face = facing
        var block = this.getSide(face!!)

        if (block!!.hasComparatorInputOverride()) {
            power = block.comparatorInputOverride
        } else if (power < 15 && block.isNormalBlock) {
            block = block.getSide(face)

            if (block!!.hasComparatorInputOverride()) {
                power = block.comparatorInputOverride
            }
        }

        return power
    }

    override fun shouldBePowered(): Boolean {
        val input = this.calculateInputStrength()

        if (input >= 15) {
            return true
        } else if (input == 0) {
            return false
        } else {
            val sidePower = this.powerOnSides
            return sidePower == 0 || input >= sidePower
        }
    }

    private fun calculateOutput(): Int {
        return if (mode == Mode.SUBTRACT) max(
            (this.calculateInputStrength() - this.powerOnSides).toDouble(),
            0.0
        ).toInt() else this.calculateInputStrength()
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (isNotActivate(player)) return false
        if (mode == Mode.SUBTRACT) {
            mode = Mode.COMPARE
        } else {
            mode = Mode.SUBTRACT
        }

        level.addLevelEvent(
            position.add(0.5, 0.5, 0.5)!!,
            LevelEventPacket.EVENT_ACTIVATE_BLOCK,
            if (this.mode == Mode.SUBTRACT) 500 else 550
        )
        level.setBlock(this.position, this, true, false)
        level.updateComparatorOutputLevelSelective(this.position, true)

        //bug?
        this.onChange()
        return true
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            this.onChange()
            return type
        }

        return super.onUpdate(type)
    }

    private fun onChange() {
        if (!level.server.settings.levelSettings().enableRedstone()) {
            return
        }

        val output = this.calculateOutput()
        // We can't use getOrCreateBlockEntity(), because the update method is called on block place,
        // before the "real" BlockEntity is set. That means, if we'd use the other method here,
        // it would create two BlockEntities.
        val blockEntityComparator: BlockEntityComparator = blockEntity ?: return

        val currentOutput: Int = blockEntityComparator.outputSignal
        blockEntityComparator.outputSignal = output

        if (currentOutput != output || mode == Mode.COMPARE) {
            val shouldBePowered = this.shouldBePowered()
            val isPowered = this.isPowered()

            if (isPowered && !shouldBePowered) {
                level.setBlock(this.position, unpowered, true, false)
                level.updateComparatorOutputLevelSelective(this.position, true)
            } else if (!isPowered && shouldBePowered) {
                level.setBlock(this.position, powered, true, false)
                level.updateComparatorOutputLevelSelective(this.position, true)
            }

            val side = this.getSide(facing!!.getOpposite()!!)
            side!!.onUpdate(Level.BLOCK_UPDATE_REDSTONE)
            RedstoneComponent.updateAroundRedstone(side)
        }
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
        val layer0 = level.getBlock(this.position, 0)
        val layer1 = level.getBlock(this.position, 1)
        if (!super.place(item, block, target, face, fx, fy, fz, player)) {
            return false
        }

        try {
            createBlockEntity(CompoundTag().putList("Items", ListTag<Tag>()))
        } catch (e: Exception) {
            BlockRedstoneComparator.log.warn("Failed to create the block entity {} at {}", blockEntityType, locator, e)
            level.setBlock(layer0!!.position, 0, layer0, true)
            level.setBlock(layer1!!.position, 1, layer1, true)
            return false
        }

        onUpdate(Level.BLOCK_UPDATE_REDSTONE)
        return true
    }

    override fun isPowered(): Boolean {
        return this.isPowered || getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.OUTPUT_LIT_BIT)
    }

    override fun toItem(): Item? {
        return ItemComparator()
    }

    enum class Mode {
        COMPARE,
        SUBTRACT
    }
}
