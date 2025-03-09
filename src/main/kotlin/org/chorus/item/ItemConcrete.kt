package org.chorus.item

import org.chorus.block.*

class ItemConcrete @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.CONCRETE, meta, count) {
    init {
        adjustName()
        adjustBlock()
    }

    private fun adjustBlock() {
        when (damage) {
            0 -> {
                block = Block.get(BlockID.WHITE_CONCRETE)
                return
            }

            1 -> {
                block = Block.get(BlockID.ORANGE_CONCRETE)
                return
            }

            2 -> {
                block = Block.get(BlockID.MAGENTA_CONCRETE)
                return
            }

            3 -> {
                block = Block.get(BlockID.LIGHT_BLUE_CONCRETE)
                return
            }

            4 -> {
                block = Block.get(BlockID.YELLOW_CONCRETE)
                return
            }

            5 -> {
                block = Block.get(BlockID.LIME_CONCRETE)
                return
            }

            6 -> {
                block = Block.get(BlockID.PINK_CONCRETE)
                return
            }

            7 -> {
                block = Block.get(BlockID.GRAY_CONCRETE)
                return
            }

            8 -> {
                block = Block.get(BlockID.LIGHT_GRAY_CONCRETE)
                return
            }

            9 -> {
                block = Block.get(BlockID.CYAN_CONCRETE)
                return
            }

            10 -> {
                block = Block.get(BlockID.PURPLE_CONCRETE)
                return
            }

            11 -> {
                block = Block.get(BlockID.BLUE_CONCRETE)
                return
            }

            12 -> {
                block = Block.get(BlockID.BROWN_CONCRETE)
                return
            }

            13 -> {
                block = Block.get(BlockID.GREEN_CONCRETE)
                return
            }

            14 -> {
                block = Block.get(BlockID.RED_CONCRETE)
                return
            }

            15 -> block = Block.get(BlockID.BLACK_CONCRETE)
        }
    }

    private fun adjustName() {
        when (damage) {
            0 -> {
                name = "White Concrete"
                return
            }

            1 -> {
                name = "Orange Concrete"
                return
            }

            2 -> {
                name = "Magenta Concrete"
                return
            }

            3 -> {
                name = "Light Blue Concrete"
                return
            }

            4 -> {
                name = "Yellow Concrete"
                return
            }

            5 -> {
                name = "Lime Concrete"
                return
            }

            6 -> {
                name = "Pink Concrete"
                return
            }

            7 -> {
                name = "Gray Concrete"
                return
            }

            8 -> {
                name = "Light Gray Concrete"
                return
            }

            9 -> {
                name = "Cyan Concrete"
                return
            }

            10 -> {
                name = "Purple Concrete"
                return
            }

            11 -> {
                name = "Blue Concrete"
                return
            }

            12 -> {
                name = "Brown Concrete"
                return
            }

            13 -> {
                name = "Green Concrete"
                return
            }

            14 -> {
                name = "Red Concrete"
                return
            }

            15 -> name = "Black Concrete"
        }
    }
}