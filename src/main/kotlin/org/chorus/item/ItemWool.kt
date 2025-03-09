package org.chorus.item

import cn.nukkit.block.*

class ItemWool @JvmOverloads constructor(aux: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.WOOL, aux, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                this.name = "White Wool"
                blockUnsafe = Block.get(BlockID.WHITE_WOOL)
                this.name = "Orange Wool"
                blockUnsafe = Block.get(BlockID.ORANGE_WOOL)
                this.name = "Magenta Wool"
                blockUnsafe = Block.get(BlockID.MAGENTA_WOOL)
                this.name = "Light Blue Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_WOOL)
                this.name = "Yellow Wool"
                blockUnsafe = Block.get(BlockID.YELLOW_WOOL)
                this.name = "Lime Wool"
                blockUnsafe = Block.get(BlockID.LIME_WOOL)
                this.name = "Pink Wool"
                blockUnsafe = Block.get(BlockID.PINK_WOOL)
                this.name = "Gray Wool"
                blockUnsafe = Block.get(BlockID.GRAY_WOOL)
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            1 -> {
                this.name = "Orange Wool"
                blockUnsafe = Block.get(BlockID.ORANGE_WOOL)
                this.name = "Magenta Wool"
                blockUnsafe = Block.get(BlockID.MAGENTA_WOOL)
                this.name = "Light Blue Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_WOOL)
                this.name = "Yellow Wool"
                blockUnsafe = Block.get(BlockID.YELLOW_WOOL)
                this.name = "Lime Wool"
                blockUnsafe = Block.get(BlockID.LIME_WOOL)
                this.name = "Pink Wool"
                blockUnsafe = Block.get(BlockID.PINK_WOOL)
                this.name = "Gray Wool"
                blockUnsafe = Block.get(BlockID.GRAY_WOOL)
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            2 -> {
                this.name = "Magenta Wool"
                blockUnsafe = Block.get(BlockID.MAGENTA_WOOL)
                this.name = "Light Blue Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_WOOL)
                this.name = "Yellow Wool"
                blockUnsafe = Block.get(BlockID.YELLOW_WOOL)
                this.name = "Lime Wool"
                blockUnsafe = Block.get(BlockID.LIME_WOOL)
                this.name = "Pink Wool"
                blockUnsafe = Block.get(BlockID.PINK_WOOL)
                this.name = "Gray Wool"
                blockUnsafe = Block.get(BlockID.GRAY_WOOL)
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            3 -> {
                this.name = "Light Blue Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_WOOL)
                this.name = "Yellow Wool"
                blockUnsafe = Block.get(BlockID.YELLOW_WOOL)
                this.name = "Lime Wool"
                blockUnsafe = Block.get(BlockID.LIME_WOOL)
                this.name = "Pink Wool"
                blockUnsafe = Block.get(BlockID.PINK_WOOL)
                this.name = "Gray Wool"
                blockUnsafe = Block.get(BlockID.GRAY_WOOL)
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            4 -> {
                this.name = "Yellow Wool"
                blockUnsafe = Block.get(BlockID.YELLOW_WOOL)
                this.name = "Lime Wool"
                blockUnsafe = Block.get(BlockID.LIME_WOOL)
                this.name = "Pink Wool"
                blockUnsafe = Block.get(BlockID.PINK_WOOL)
                this.name = "Gray Wool"
                blockUnsafe = Block.get(BlockID.GRAY_WOOL)
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            5 -> {
                this.name = "Lime Wool"
                blockUnsafe = Block.get(BlockID.LIME_WOOL)
                this.name = "Pink Wool"
                blockUnsafe = Block.get(BlockID.PINK_WOOL)
                this.name = "Gray Wool"
                blockUnsafe = Block.get(BlockID.GRAY_WOOL)
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            6 -> {
                this.name = "Pink Wool"
                blockUnsafe = Block.get(BlockID.PINK_WOOL)
                this.name = "Gray Wool"
                blockUnsafe = Block.get(BlockID.GRAY_WOOL)
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            7 -> {
                this.name = "Gray Wool"
                blockUnsafe = Block.get(BlockID.GRAY_WOOL)
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            8 -> {
                this.name = "Light Gray Wool"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_WOOL)
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            9 -> {
                this.name = "Cyan Wool"
                blockUnsafe = Block.get(BlockID.CYAN_WOOL)
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            10 -> {
                this.name = "Purple Wool"
                blockUnsafe = Block.get(BlockID.PURPLE_WOOL)
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            11 -> {
                this.name = "Blue Wool"
                blockUnsafe = Block.get(BlockID.BLUE_WOOL)
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            12 -> {
                this.name = "Brown Wool"
                blockUnsafe = Block.get(BlockID.BROWN_WOOL)
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            13 -> {
                this.name = "Green Wool"
                blockUnsafe = Block.get(BlockID.GREEN_WOOL)
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            14 -> {
                this.name = "Red Wool"
                blockUnsafe = Block.get(BlockID.RED_WOOL)
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }

            15 -> {
                this.name = "Black Wool"
                blockUnsafe = Block.get(BlockID.BLACK_WOOL)
            }
        }
        this.meta = 0
    }
}