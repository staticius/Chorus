package org.chorus.item

import cn.nukkit.block.*

class ItemStainedGlass @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    Item(ItemID.Companion.STAINED_GLASS, meta, count) {
    override fun internalAdjust() {
        when (damage) {
            0 -> {
                this.name = "White Stained Glass"
                blockUnsafe = Block.get(BlockID.WHITE_STAINED_GLASS)
                this.name = "Orange Stained Glass"
                blockUnsafe = Block.get(BlockID.ORANGE_STAINED_GLASS)
                this.name = "Magenta Stained Glass"
                blockUnsafe = Block.get(BlockID.MAGENTA_STAINED_GLASS)
                this.name = "Light Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_STAINED_GLASS)
                this.name = "Yellow Stained Glass"
                blockUnsafe = Block.get(BlockID.YELLOW_STAINED_GLASS)
                this.name = "Lime Stained Glass"
                blockUnsafe = Block.get(BlockID.LIME_STAINED_GLASS)
                this.name = "Pink Stained Glass"
                blockUnsafe = Block.get(BlockID.PINK_STAINED_GLASS)
                this.name = "Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS)
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            1 -> {
                this.name = "Orange Stained Glass"
                blockUnsafe = Block.get(BlockID.ORANGE_STAINED_GLASS)
                this.name = "Magenta Stained Glass"
                blockUnsafe = Block.get(BlockID.MAGENTA_STAINED_GLASS)
                this.name = "Light Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_STAINED_GLASS)
                this.name = "Yellow Stained Glass"
                blockUnsafe = Block.get(BlockID.YELLOW_STAINED_GLASS)
                this.name = "Lime Stained Glass"
                blockUnsafe = Block.get(BlockID.LIME_STAINED_GLASS)
                this.name = "Pink Stained Glass"
                blockUnsafe = Block.get(BlockID.PINK_STAINED_GLASS)
                this.name = "Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS)
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            2 -> {
                this.name = "Magenta Stained Glass"
                blockUnsafe = Block.get(BlockID.MAGENTA_STAINED_GLASS)
                this.name = "Light Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_STAINED_GLASS)
                this.name = "Yellow Stained Glass"
                blockUnsafe = Block.get(BlockID.YELLOW_STAINED_GLASS)
                this.name = "Lime Stained Glass"
                blockUnsafe = Block.get(BlockID.LIME_STAINED_GLASS)
                this.name = "Pink Stained Glass"
                blockUnsafe = Block.get(BlockID.PINK_STAINED_GLASS)
                this.name = "Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS)
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            3 -> {
                this.name = "Light Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_BLUE_STAINED_GLASS)
                this.name = "Yellow Stained Glass"
                blockUnsafe = Block.get(BlockID.YELLOW_STAINED_GLASS)
                this.name = "Lime Stained Glass"
                blockUnsafe = Block.get(BlockID.LIME_STAINED_GLASS)
                this.name = "Pink Stained Glass"
                blockUnsafe = Block.get(BlockID.PINK_STAINED_GLASS)
                this.name = "Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS)
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            4 -> {
                this.name = "Yellow Stained Glass"
                blockUnsafe = Block.get(BlockID.YELLOW_STAINED_GLASS)
                this.name = "Lime Stained Glass"
                blockUnsafe = Block.get(BlockID.LIME_STAINED_GLASS)
                this.name = "Pink Stained Glass"
                blockUnsafe = Block.get(BlockID.PINK_STAINED_GLASS)
                this.name = "Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS)
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            5 -> {
                this.name = "Lime Stained Glass"
                blockUnsafe = Block.get(BlockID.LIME_STAINED_GLASS)
                this.name = "Pink Stained Glass"
                blockUnsafe = Block.get(BlockID.PINK_STAINED_GLASS)
                this.name = "Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS)
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            6 -> {
                this.name = "Pink Stained Glass"
                blockUnsafe = Block.get(BlockID.PINK_STAINED_GLASS)
                this.name = "Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS)
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            7 -> {
                this.name = "Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.GRAY_STAINED_GLASS)
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            8 -> {
                this.name = "Light Gray Stained Glass"
                blockUnsafe = Block.get(BlockID.LIGHT_GRAY_STAINED_GLASS)
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            9 -> {
                this.name = "Cyan Stained Glass"
                blockUnsafe = Block.get(BlockID.CYAN_STAINED_GLASS)
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            10 -> {
                this.name = "Purple Stained Glass"
                blockUnsafe = Block.get(BlockID.PURPLE_STAINED_GLASS)
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            11 -> {
                this.name = "Blue Stained Glass"
                blockUnsafe = Block.get(BlockID.BLUE_STAINED_GLASS)
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            12 -> {
                this.name = "Brown Stained Glass"
                blockUnsafe = Block.get(BlockID.BROWN_STAINED_GLASS)
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            13 -> {
                this.name = "Green Stained Glass"
                blockUnsafe = Block.get(BlockID.GREEN_STAINED_GLASS)
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            14 -> {
                this.name = "Red Stained Glass"
                blockUnsafe = Block.get(BlockID.RED_STAINED_GLASS)
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }

            15 -> {
                this.name = "Black Stained Glass"
                blockUnsafe = Block.get(BlockID.BLACK_STAINED_GLASS)
            }
        }
        this.meta = 0
    }
}