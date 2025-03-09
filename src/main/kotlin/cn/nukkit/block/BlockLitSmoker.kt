package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.*
import cn.nukkit.item.*

open class BlockLitSmoker @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLitFurnace(blockstate) {
    override val name: String
        get() = "Burning Smoker"

    override val blockEntityType: String
        get() = BlockEntity.SMOKER

    override val blockEntityClass: Class<out BlockEntityFurnace>
        get() = BlockEntitySmoker::class.java

    override fun toItem(): Item? {
        return ItemBlock(BlockSmoker())
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIT_SMOKER, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)
            get() = Companion.field
    }
}