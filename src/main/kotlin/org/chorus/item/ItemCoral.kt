package org.chorus.item

import org.chorus.block.Block
import org.chorus.block.BlockID

class ItemCoral : Item {
    constructor() : super(ItemID.Companion.CORAL)

    @JvmOverloads
    constructor(meta: Int, count: Int = 1) : super(ItemID.Companion.CONCRETE, meta, count) {
        adjustName()
        adjustBlock()
    }

    private fun adjustBlock() {
        when (damage) {
            0 -> {
                this.block = Block.get(BlockID.TUBE_CORAL)
                return
            }

            1 -> {
                this.block = Block.get(BlockID.BRAIN_CORAL)
                return
            }

            2 -> {
                this.block = Block.get(BlockID.BUBBLE_CORAL)
                return
            }

            3 -> {
                this.block = Block.get(BlockID.FIRE_CORAL)
                return
            }

            4 -> {
                this.block = Block.get(BlockID.HORN_CORAL)
                return
            }

            8 -> {
                this.block = Block.get(BlockID.DEAD_TUBE_CORAL)
                return
            }

            9 -> {
                this.block = Block.get(BlockID.DEAD_BRAIN_CORAL)
                return
            }

            10 -> {
                this.block = Block.get(BlockID.DEAD_BUBBLE_CORAL)
                return
            }

            11 -> {
                this.block = Block.get(BlockID.DEAD_FIRE_CORAL)
                return
            }

            12 -> {
                this.block = Block.get(BlockID.DEAD_HORN_CORAL)
                return
            }

            else -> this.name = "Coral"
        }
    }

    private fun adjustName() {
        when (damage) {
            0 -> {
                this.name = "Tube Coral"
                return
            }

            1 -> {
                this.name = "Brain Coral"
                return
            }

            2 -> {
                this.name = "Bubble Coral"
                return
            }

            3 -> {
                this.name = "Fire Coral"
                return
            }

            4 -> {
                this.name = "Horn Coral"
                return
            }

            8 -> {
                this.name = "Dead Tube Coral"
                return
            }

            9 -> {
                this.name = "Dead Brain Coral"
                return
            }

            10 -> {
                this.name = "Dead Bubble Coral"
                return
            }

            11 -> {
                this.name = "Dead Fire Coral"
                return
            }

            12 -> {
                this.name = "Dead Horn Coral"
                return
            }

            else -> this.name = "Coral"
        }
    }
}