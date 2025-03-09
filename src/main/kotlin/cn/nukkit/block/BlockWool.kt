package cn.nukkit.block

import cn.nukkit.block.Block.name
import cn.nukkit.block.property.enums.WoodType.name
import cn.nukkit.item.ItemTool
import cn.nukkit.utils.DyeColor.name

abstract class BlockWool(blockstate: BlockState?) : BlockSolid(blockstate) {
    override val name: String
        get() = dyeColor.name + " Wool"

    override val toolType: Int
        get() = ItemTool.TYPE_SHEARS

    override val hardness: Double
        get() = 0.8

    override val resistance: Double
        get() = 0.8

    override val burnChance: Int
        get() = 30

    override val burnAbility: Int
        get() = 60

    open val dyeColor: DyeColor
        get() = DyeColor.WHITE
}
