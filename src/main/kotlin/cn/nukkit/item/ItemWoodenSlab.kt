package cn.nukkit.item

import cn.nukkit.block.*
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.MinecraftVerticalHalf

class ItemWoodenSlab @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.WOODEN_SLAB, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0, 6, 7 -> {
                name = "Oak Slab"
                blockUnsafe = Block.get(BlockID.OAK_SLAB)
            }

            1 -> {
                name = "Spruce Slab"
                blockUnsafe = Block.get(BlockID.SPRUCE_SLAB)
            }

            2 -> {
                name = "Birch Slab"
                blockUnsafe = Block.get(BlockID.BIRCH_SLAB)
            }

            3 -> {
                name = "Jungle Slab"
                blockUnsafe = Block.get(BlockID.JUNGLE_SLAB)
            }

            4 -> {
                name = "Acacia Slab"
                blockUnsafe = Block.get(BlockID.ACACIA_SLAB)
            }

            5 -> {
                name = "Dark Oak Slab"
                blockUnsafe = Block.get(BlockID.DARK_OAK_SLAB)
            }

            8, 14, 15 -> {
                name = "Oak Slab (Top)"
                blockUnsafe =
                    BlockOakSlab.properties.getBlockState(
                        CommonBlockProperties.MINECRAFT_VERTICAL_HALF,
                        MinecraftVerticalHalf.TOP
                    ).toBlock()
            }

            9 -> {
                name = "Spruce Slab (Top)"
                blockUnsafe =
                    BlockSpruceSlab.properties.getBlockState(
                        CommonBlockProperties.MINECRAFT_VERTICAL_HALF,
                        MinecraftVerticalHalf.TOP
                    ).toBlock()
            }

            10 -> {
                name = "Birch Slab (Top)"
                blockUnsafe =
                    BlockBirchSlab.properties.getBlockState(
                        CommonBlockProperties.MINECRAFT_VERTICAL_HALF,
                        MinecraftVerticalHalf.TOP
                    ).toBlock()
            }

            11 -> {
                name = "Jungle Slab (Top)"
                blockUnsafe =
                    BlockJungleSlab.properties.getBlockState(
                        CommonBlockProperties.MINECRAFT_VERTICAL_HALF,
                        MinecraftVerticalHalf.TOP
                    ).toBlock()
            }

            12 -> {
                name = "Acacia Slab (Top)"
                blockUnsafe =
                    BlockAcaciaSlab.properties.getBlockState(
                        CommonBlockProperties.MINECRAFT_VERTICAL_HALF,
                        MinecraftVerticalHalf.TOP
                    ).toBlock()
            }

            13 -> {
                name = "Dark Oak Slab (Top)"
                blockUnsafe =
                    BlockDarkOakSlab.properties.getBlockState(
                        CommonBlockProperties.MINECRAFT_VERTICAL_HALF,
                        MinecraftVerticalHalf.TOP
                    ).toBlock()
            }

            else -> throw IllegalArgumentException("Invalid damage: $damage")
        }
        this.meta = 0
    }
}