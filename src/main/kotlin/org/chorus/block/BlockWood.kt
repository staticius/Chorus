package org.chorus.block

import cn.nukkit.block.Block.name
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType.name
import cn.nukkit.registry.Registries
import cn.nukkit.utils.DyeColor.name

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class BlockWood(blockstate: BlockState?) : BlockLog(blockstate) {
    abstract val woodType: WoodType

    override val name: String
        get() = woodType.name + " Wood"

    override fun getStrippedState(): BlockState {
        val strippedId = when (woodType) {
            WoodType.OAK -> BlockID.STRIPPED_OAK_WOOD
            WoodType.SPRUCE -> BlockID.STRIPPED_SPRUCE_WOOD
            WoodType.BIRCH -> BlockID.STRIPPED_BIRCH_WOOD
            WoodType.JUNGLE -> BlockID.STRIPPED_JUNGLE_WOOD
            WoodType.ACACIA -> BlockID.STRIPPED_ACACIA_WOOD
            WoodType.DARK_OAK -> BlockID.STRIPPED_DARK_OAK_WOOD
            WoodType.PALE_OAK -> BlockID.STRIPPED_PALE_OAK_WOOD
            WoodType.CHERRY -> BlockID.STRIPPED_CHERRY_WOOD
        }
        return Registries.BLOCK.getBlockProperties(strippedId)
            .getBlockState(CommonBlockProperties.PILLAR_AXIS, pillarAxis)
    }
}
