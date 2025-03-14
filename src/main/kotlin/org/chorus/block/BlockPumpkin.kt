package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.CommonPropertyMap
import org.chorus.entity.mob.EntitySnowGolem.Companion.checkAndSpawnGolem
import org.chorus.item.Item
import org.chorus.item.ItemBlock
import org.chorus.item.ItemID
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import org.chorus.utils.Faceable

open class BlockPumpkin : BlockSolid, Faceable, Natural {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockstate: BlockState?) : super(blockstate)

    override val name: String
        get() = "Pumpkin"

    override val hardness: Double
        get() = 1.0

    override val resistance: Double
        get() = 1.0

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override fun toItem(): Item? {
        return ItemBlock(this, 0)
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
        if (item.isShears) {
            val carvedPumpkin = BlockCarvedPumpkin()
            // TODO: Use the activated block face not the player direction
            if (player == null) {
                carvedPumpkin.blockFace = BlockFace.SOUTH
            } else {
                carvedPumpkin.blockFace = player.getDirection()!!.getOpposite()
            }
            item.useOn(this)
            level.setBlock(this.position, carvedPumpkin, true, true)
            level.dropItem(
                position.add(0.5, 0.5, 0.5),
                Item.get(ItemID.PUMPKIN_SEEDS)
            ) // TODO: Get correct drop item position
            return true
        }
        return false
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        blockFace = if (player == null) {
            BlockFace.SOUTH
        } else {
            player.getDirection()!!.getOpposite()
        }
        level.setBlock(block.position, this, true, true)
        checkAndSpawnGolem(this)
        return true
    }

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    override var blockFace: BlockFace?
        get() = CommonPropertyMap.CARDINAL_BLOCKFACE[getPropertyValue(
            CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION
        )]
        set(face) {
            this.setPropertyValue(
                CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION,
                CommonPropertyMap.CARDINAL_BLOCKFACE.inverse()[face]
            )
        }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PUMPKIN, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)

    }
}
