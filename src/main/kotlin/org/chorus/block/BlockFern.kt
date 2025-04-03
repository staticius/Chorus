package org.chorus.block

import org.chorus.Player
import org.chorus.block.BlockFlowerPot.FlowerPotBlock
import org.chorus.item.Item
import org.chorus.item.Item.Companion.get
import org.chorus.item.ItemID
import org.chorus.item.ItemTool
import org.chorus.item.enchantment.Enchantment
import org.chorus.level.Level
import org.chorus.level.particle.BoneMealParticle
import org.chorus.math.BlockFace
import java.util.concurrent.ThreadLocalRandom

class BlockFern : BlockFlowable, FlowerPotBlock {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState) : super(blockstate)

    override fun canBeReplaced(): Boolean {
        return true
    }

    override val burnChance: Int
        get() = 60

    override val burnAbility: Int
        get() = 100

    override val isFertilizable: Boolean
        get() = true

    override fun place(
        item: Item?,
        block: Block,
        target: Block?,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        if (BlockSweetBerryBush.isSupportValid(down())) {
            level.setBlock(block.position, this, true)
            return true
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!BlockSweetBerryBush.isSupportValid(down(1, 0))) {
                level.useBreakOn(this.position)
                return Level.BLOCK_UPDATE_NORMAL
            }
        }
        return 0
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (item.isFertilizer) {
            val up = this.up()

            if (up.isAir) {
                if (player != null && !player.isCreative) {
                    item.count--
                }

                val doublePlant = BlockLargeFern()
                doublePlant.isTopHalf = false

                level.addParticle(BoneMealParticle(this.position))
                level.setBlock(this.position, doublePlant, true, false)

                doublePlant.isTopHalf = true
                level.setBlock(up.position, doublePlant, true)
                level.updateAround(this.position)
            }

            return true
        }

        return false
    }

    override fun getDrops(item: Item): Array<Item> {
        // https://minecraft.wiki/w/Fortune#Grass_and_ferns
        val drops: MutableList<Item> = ArrayList(2)
        if (item.isShears) {
            drops.add(toItem())
        }

        val random = ThreadLocalRandom.current()
        if (random.nextInt(8) == 0) {
            val fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING)
            val fortuneLevel = fortune?.level ?: 0
            val amount = if (fortuneLevel == 0) 1 else 1 + random.nextInt(fortuneLevel * 2)
            drops.add(get(ItemID.WHEAT_SEEDS, 0, amount))
        }

        return drops.toTypedArray()
    }

    override val toolType: Int
        get() = ItemTool.TYPE_SHEARS

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.FERN)
    }
}