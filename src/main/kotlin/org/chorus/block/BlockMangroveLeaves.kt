package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.enums.WoodType
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.item.enchantment.Enchantment
import org.chorus.math.BlockFace
import java.util.concurrent.ThreadLocalRandom

class BlockMangroveLeaves : BlockLeaves {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val type: WoodType
        get() = null

    override val name: String
        get() = "Mangrove Leaves"

    override fun getDrops(item: Item): Array<Item?>? {
        if (item.isShears) {
            return arrayOf(
                toItem()
            )
        }

        val drops: MutableList<Item> = ArrayList(1)
        val fortuneEnchantment = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING)

        val fortune = fortuneEnchantment?.level ?: 0
        val stickOdds = when (fortune) {
            0 -> 50
            1 -> 45
            2 -> 40
            else -> 30
        }
        val random = ThreadLocalRandom.current()
        if (random.nextInt(stickOdds) == 0) {
            drops.add(Item.get(ItemID.STICK))
        }
        return drops.toArray(Item.EMPTY_ARRAY)
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        //todo: 实现红树树叶催化
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(
BlockID.BlockID.MANGROVE_LEAVES,
            CommonBlockProperties.PERSISTENT_BIT,
            CommonBlockProperties.UPDATE_BIT
        )

    }
}
