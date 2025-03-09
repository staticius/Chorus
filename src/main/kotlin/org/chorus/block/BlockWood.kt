package org.chorus.block

import org.chorus.block.Block.name
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType.name
import org.chorus.registry.Registries
import org.chorus.utils.DyeColor.name

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
