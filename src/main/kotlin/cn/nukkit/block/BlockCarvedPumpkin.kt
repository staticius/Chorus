package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.entity.mob.EntityIronGolem.Companion.checkAndSpawnGolem
import cn.nukkit.item.*
import cn.nukkit.math.BlockFace

class BlockCarvedPumpkin @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockPumpkin(blockstate) {
    override val name: String
        get() = "Carved Pumpkin"

    override fun canBeActivated(): Boolean {
        return false
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
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
        if (!super.place(item, block, target, face, fx, fy, fz, player)) return false
        checkAndSpawnGolem(this, player)
        return true
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(CARVED_PUMPKIN, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)
            get() = Companion.field
    }
}
