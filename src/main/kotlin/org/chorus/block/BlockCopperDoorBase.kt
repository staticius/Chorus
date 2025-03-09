package org.chorus.block

import org.chorus.Player
import org.chorus.block.Block.onActivate
import org.chorus.item.*
import org.chorus.math.BlockFace
import org.chorus.math.Vector3.equals
import org.chorus.registry.Registries

abstract class BlockCopperDoorBase(blockState: BlockState?) : BlockDoor(blockState), Oxidizable,
    Waxable {
    override val hardness: Double
        get() = 3.0

    override val resistance: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_STONE

    override fun onActivate(
        item: Item,
        player: Player,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player.isSneaking()) {
            return super<Waxable>.onActivate(item, player, blockFace, fx, fy, fz)
                    || super<Oxidizable>.onActivate(item, player, blockFace, fx, fy, fz)
        }

        return super.onActivate(item, player, blockFace, fx, fy, fz)
    }

    override fun getBlockWithOxidizationLevel(oxidizationLevel: OxidizationLevel): Block {
        return Registries.BLOCK.getBlockProperties(getCopperId(isWaxed, oxidizationLevel)).defaultState.toBlock()
    }

    override fun setOxidizationLevel(oxidizationLevel: OxidizationLevel): Boolean {
        if (oxidizationLevel == oxidizationLevel) {
            return true
        }
        return level.setBlock(this.position, get(getCopperId(isWaxed, oxidizationLevel)))
    }

    override fun setWaxed(waxed: Boolean): Boolean {
        if (isWaxed == waxed) {
            return true
        }
        return level.setBlock(
            this.position, get(
                getCopperId(
                    waxed,
                    oxidizationLevel
                )
            )
        )
    }

    override fun isWaxed(): Boolean {
        return false
    }

    protected fun getCopperId(waxed: Boolean, oxidizationLevel: OxidizationLevel?): String {
        if (oxidizationLevel == null) {
            return id
        }
        return when (oxidizationLevel) {
            OxidizationLevel.UNAFFECTED -> if (waxed) WAXED_COPPER_DOOR else COPPER_DOOR
            OxidizationLevel.EXPOSED -> if (waxed) WAXED_EXPOSED_COPPER_DOOR else EXPOSED_COPPER_DOOR
            OxidizationLevel.WEATHERED -> if (waxed) WAXED_WEATHERED_COPPER_DOOR else WEATHERED_COPPER_DOOR
            OxidizationLevel.OXIDIZED -> if (waxed) WAXED_OXIDIZED_COPPER_DOOR else OXIDIZED_COPPER_DOOR
        }
    }
}
