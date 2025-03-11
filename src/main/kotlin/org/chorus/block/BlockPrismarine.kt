package org.chorus.block

import org.chorus.block.property.enums.PrismarineBlockType
import org.chorus.item.*

open class BlockPrismarine : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val hardness: Double
        get() = 1.5

    override val resistance: Double
        get() = 30.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val name: String
        get() = when (prismarineBlockType) {
            PrismarineBlockType.DEFAULT -> "Prismarine"
            PrismarineBlockType.DARK -> "Dark Prismarine"
            PrismarineBlockType.BRICKS -> "Prismarine Bricks"
        }

    var prismarineBlockType: PrismarineBlockType
        get() = when (this) {
            -> PrismarineBlockType.BRICKS
            -> PrismarineBlockType.DARK
            else -> PrismarineBlockType.DEFAULT
        }
        set(prismarineBlockType) {
            this.blockState = when (prismarineBlockType) {
                PrismarineBlockType.BRICKS -> BlockPrismarineBricks.Companion.PROPERTIES.getDefaultState()
                PrismarineBlockType.DARK -> BlockDarkPrismarine.properties.defaultState
                PrismarineBlockType.DEFAULT -> Companion.properties.defaultState
            }
        }

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun toItem(): Item? {
        return when (this.prismarineBlockType) {
            PrismarineBlockType.BRICKS -> ItemBlock(
                BlockPrismarineBricks.Companion.PROPERTIES.getDefaultState().toBlock()
            )

            PrismarineBlockType.DARK -> ItemBlock(BlockDarkPrismarine.properties.defaultState.toBlock())
            PrismarineBlockType.DEFAULT -> ItemBlock(properties.defaultState.toBlock())
        }
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.PRISMARINE)

    }
}
