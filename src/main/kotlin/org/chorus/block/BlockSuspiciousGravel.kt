package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.event.player.PlayerInteractEvent
import org.chorus.item.Item
import org.chorus.math.BlockFace
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag.putBoolean

class BlockSuspiciousGravel @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockFallable(blockstate) {
    override val name: String
        get() = "Suspicious Gravel"

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
            level.addSound(this.position, Sound.HIT_SUSPICIOUS_GRAVEL)
            level.setBlock(this.position, this)
        } else {
            level.setBlock(this.position, BlockGravel.properties.getDefaultState().toBlock())
            level.addSound(this.position, Sound.BREAK_SUSPICIOUS_GRAVEL)
        }
        super.onTouch(vector, item, face, fx, fy, fz, player, action)
    }

    override fun getDrops(item: Item): Array<Item> {
        return arrayOf(Item.AIR)
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
            BlockID.SUSPICIOUS_GRAVEL,
            CommonBlockProperties.HANGING,
            CommonBlockProperties.BRUSHED_PROGRESS
        )

    }
}