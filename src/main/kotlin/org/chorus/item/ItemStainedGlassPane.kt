package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemStainedGlassPane @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.STAINED_GLASS_PANE, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                this.name = "White Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.WHITE_STAINED_GLASS_PANE)
            }

            1 -> {
                this.name = "Orange Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.ORANGE_STAINED_GLASS_PANE)
            }

            2 -> {
                this.name = "Magenta Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.MAGENTA_STAINED_GLASS_PANE)
            }

            3 -> {
                this.name = "Light Blue Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_STAINED_GLASS_PANE)
            }

            4 -> {
                this.name = "Yellow Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.YELLOW_STAINED_GLASS_PANE)
            }

            5 -> {
                this.name = "Lime Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.LIME_STAINED_GLASS_PANE)
            }

            6 -> {
                this.name = "Pink Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.PINK_STAINED_GLASS_PANE)
            }

            7 -> {
                this.name = "Gray Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS_PANE)
            }

            8 -> {
                this.name = "Light Gray Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS_PANE)
            }

            9 -> {
                this.name = "Cyan Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS_PANE)
            }

            10 -> {
                this.name = "Purple Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS_PANE)
            }

            11 -> {
                this.name = "Blue Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS_PANE)
            }

            12 -> {
                this.name = "Brown Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS_PANE)
            }

            13 -> {
                this.name = "Green Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS_PANE)
            }

            14 -> {
                this.name = "Red Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS_PANE)
            }

            15 -> {
                this.name = "Black Stained Glass Pane"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS_PANE)
            }
        }
        this.meta = 0
    }
}