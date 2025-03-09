package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.event.player.PlayerInteractEvent
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3
import cn.nukkit.nbt.tag.CompoundTag.putBoolean

class BlockSuspiciousSand @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFallable(blockstate) {
    override val name: String
        get() = "Suspicious Sand"

    override val hardness: Double
        get() = 0.25

    override val resistance: Double
        get() = 1.25

    override fun createFallingEntity(customNbt: CompoundTag): EntityFallingBlock? {
        customNbt.putBoolean("BreakOnGround", true)
        return super.createFallingEntity(customNbt)
    }

    override fun onTouch(
        vector: Vector3,
        item: Item,
        face: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float,
        player: Player?,
        action: PlayerInteractEvent.Action
    ) {
        val progress = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.BRUSHED_PROGRESS)
        if (progress < 3) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.BRUSHED_PROGRESS, progress + 1)
            level.addSound(this.position, Sound.HIT_SUSPICIOUS_SAND)
            level.setBlock(this.position, this)
        } else {
            level.setBlock(this.position, BlockSand.Companion.PROPERTIES.getDefaultState().toBlock())
            level.addSound(this.position, Sound.BREAK_SUSPICIOUS_SAND)
        }
        super.onTouch(vector, item, face, fx, fy, fz, player, action)
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return arrayOf(Item.AIR)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SUSPICIOUS_SAND,
            CommonBlockProperties.HANGING,
            CommonBlockProperties.BRUSHED_PROGRESS
        )
            get() = Companion.field
    }
}