package cn.nukkit.block

import cn.nukkit.item.ItemTool

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class BlockPlanks(blockState: BlockState?) : BlockSolid(blockState) {
    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 15.0

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 20

    override val toolType: Int
        get() = ItemTool.TYPE_AXE
}
