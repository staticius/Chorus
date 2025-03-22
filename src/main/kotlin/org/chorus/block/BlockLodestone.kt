package org.chorus.block

import org.chorus.Player
import org.chorus.Server
import org.chorus.blockentity.*
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.math.BlockFace
import org.chorus.utils.Loggable

import java.io.IOException

class BlockLodestone : BlockSolid, BlockEntityHolder<BlockEntityLodestone> {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override fun getBlockEntityClass() = BlockEntityLodestone::class.java

    override fun getBlockEntityType(): String {
        return BlockEntityID.LODESTONE
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
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player == null || item.isNothing || item.id != ItemID.COMPASS && item.id != ItemID.LODESTONE_COMPASS) {
            return false
        }

        val compass = Item.get(ItemID.LODESTONE_COMPASS) as ItemLodestoneCompass
        if (item.hasCompoundTag()) {
            compass.setCompoundTag(item.compoundTag.clone())
        }

        val trackingHandle: Int
        try {
            trackingHandle = getOrCreateBlockEntity()!!.requestTrackingHandler()
            compass.trackingHandle = trackingHandle
        } catch (e: Exception) {
            BlockLodestone.log.warn(
                "Could not create a lodestone compass to {} for {}",
                locator,
                player.getName(),
                e
            )
            return false
        }

        var added = true
        if (item.getCount() == 1) {
            player.getInventory().setItemInHand(compass)
        } else {
            val clone: Item = item.clone()
            clone.count--
            player.getInventory().setItemInHand(clone)
            for (failed in player.getInventory().addItem(compass)) {
                added = false
                player.level!!.dropItem(player.getLocator().position, failed)
            }
        }

        level.addSound(player.getLocator().position, Sound.LODESTONE_COMPASS_LINK_COMPASS_TO_LODESTONE)

        if (added) {
            try {
                Server.instance.getPositionTrackingService().startTracking(player, trackingHandle, false)
            } catch (e: IOException) {
                BlockLodestone.log.warn(
                    "Failed to make the player {} track {} at {}",
                    player.getName(),
                    trackingHandle,
                    locator,
                    e
                )
            }
            level.scheduler.scheduleTask(null, player::updateTrackingPositions)
        }

        return true
    }

    override val name: String
    get() = "Lodestone"

    override val hardness: Double
    get() = 2.0

    override val resistance: Double
    get() = 3.5

    override val toolType: Int
    get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
    get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object : Loggable {
        val properties: BlockProperties = BlockProperties(BlockID.LODESTONE)
    }
}
