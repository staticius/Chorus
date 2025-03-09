package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.CommonPropertyMap
import cn.nukkit.entity.mob.EntitySnowGolem.Companion.checkAndSpawnGolem
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace
import cn.nukkit.utils.Faceable

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
                position.add(0.5, 0.5, 0.5)!!,
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
            get() = Companion.field
    }
}
