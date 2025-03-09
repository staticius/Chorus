package org.chorus.item

import cn.nukkit.block.*

class ItemShulkerBox @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.SHULKER_BOX, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                this.name = "White Shulker Box"
                blockUnsafe = Block.get(BlockID.WHITE_SHULKER_BOX)
            }

            1 -> {
                this.name = "Orange Shulker Box"
                blockUnsafe = Block.get(BlockID.ORANGE_SHULKER_BOX)
            }

            2 -> {
                this.name = "Magenta Shulker Box"
                blockUnsafe = Block.get(BlockID.MAGENTA_SHULKER_BOX)
            }

            3 -> {
                this.name = "Light Blue Shulker Box"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_SHULKER_BOX)
            }

            4 -> {
                this.name = "Yellow Shulker Box"
                blockUnsafe = Block.get(BlockID.YELLOW_SHULKER_BOX)
            }

            5 -> {
                this.name = "Lime Shulker Box"
                blockUnsafe = Block.get(BlockID.LIME_SHULKER_BOX)
            }

            6 -> {
                this.name = "Pink Shulker Box"
                blockUnsafe = Block.get(BlockID.PINK_SHULKER_BOX)
            }

            7 -> {
                this.name = "Gray Shulker Box"
                blockUnsafe = Block.get(BlockID.GRAY_SHULKER_BOX)
            }

            8 -> {
                this.name = "Light Gray Shulker Box"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_SHULKER_BOX)
            }

            9 -> {
                this.name = "Cyan Shulker Box"
                blockUnsafe = Block.get(BlockID.CYAN_SHULKER_BOX)
            }

            10 -> {
                this.name = "Purple Shulker Box"
                blockUnsafe = Block.get(BlockID.PURPLE_SHULKER_BOX)
            }

            11 -> {
                this.name = "Blue Shulker Box"
                blockUnsafe = Block.get(BlockID.BLUE_SHULKER_BOX)
            }

            12 -> {
                this.name = "Brown Shulker Box"
                blockUnsafe = Block.get(BlockID.BROWN_SHULKER_BOX)
            }

            13 -> {
                this.name = "Green Shulker Box"
                blockUnsafe = Block.get(BlockID.GREEN_SHULKER_BOX)
            }

            14 -> {
                this.name = "Red Shulker Box"
                blockUnsafe = Block.get(BlockID.RED_SHULKER_BOX)
            }

            15 -> {
                this.name = "Black Shulker Box"
                blockUnsafe = Block.get(BlockID.BLACK_SHULKER_BOX)
            }
        }
        this.meta = 0
    }
}