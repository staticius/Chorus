package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemConcretePowder @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.CONCRETE_POWDER, meta, count) {
    init {
        adjustName()
        adjustBlock()
    }

    private fun adjustBlock() {
        when (damage) {
            0 -> {
                block = Block.get(BlockID.WHITE_CONCRETE_POWDER)
                return
            }

            1 -> {
                block = Block.get(BlockID.ORANGE_CONCRETE_POWDER)
                return
            }

            2 -> {
                block = Block.get(BlockID.MAGENTA_CONCRETE_POWDER)
                return
            }

            3 -> {
                block = Block.get(BlockID.LIGHT_BLUE_CONCRETE_POWDER)
                return
            }

            4 -> {
                block = Block.get(BlockID.YELLOW_CONCRETE_POWDER)
                return
            }

            5 -> {
                block = Block.get(BlockID.LIME_CONCRETE_POWDER)
                return
            }

            6 -> {
                block = Block.get(BlockID.PINK_CONCRETE_POWDER)
                return
            }

            7 -> {
                block = Block.get(BlockID.GRAY_CONCRETE_POWDER)
                return
            }

            8 -> {
                block = Block.get(BlockID.LIGHT_GRAY_CONCRETE_POWDER)
                return
            }

            9 -> {
                block = Block.get(BlockID.CYAN_CONCRETE_POWDER)
                return
            }

            10 -> {
                block = Block.get(BlockID.PURPLE_CONCRETE_POWDER)
                return
            }

            11 -> {
                block = Block.get(BlockID.BLUE_CONCRETE_POWDER)
                return
            }

            12 -> {
                block = Block.get(BlockID.BROWN_CONCRETE_POWDER)
                return
            }

            13 -> {
                block = Block.get(BlockID.GREEN_CONCRETE_POWDER)
                return
            }

            14 -> {
                block = Block.get(BlockID.RED_CONCRETE_POWDER)
                return
            }

            15 -> block = Block.get(BlockID.BLACK_CONCRETE_POWDER)
        }
    }

    private fun adjustName() {
        when (damage) {
            0 -> {
                name = "White Concrete Powder"
                return
            }

            1 -> {
                name = "Orange Concrete Powder"
                return
            }

            2 -> {
                name = "Magenta Concrete Powder"
                return
            }

            3 -> {
                name = "Light Blue Concrete Powder"
                return
            }

            4 -> {
                name = "Yellow Concrete Powder"
                return
            }

            5 -> {
                name = "Lime Concrete Powder"
                return
            }

            6 -> {
                name = "Pink Concrete Powder"
                return
            }

            7 -> {
                name = "Gray Concrete Powder"
                return
            }

            8 -> {
                name = "Light Gray Concrete Powder"
                return
            }

            9 -> {
                name = "Cyan Concrete Powder"
                return
            }

            10 -> {
                name = "Purple Concrete Powder"
                return
            }

            11 -> {
                name = "Blue Concrete Powder"
                return
            }

            12 -> {
                name = "Brown Concrete Powder"
                return
            }

            13 -> {
                name = "Green Concrete Powder"
                return
            }

            14 -> {
                name = "Red Concrete Powder"
                return
            }

            15 -> name = "Black Concrete Powder"
        }
    }
}