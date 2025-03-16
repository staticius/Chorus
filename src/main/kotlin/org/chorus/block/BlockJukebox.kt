package org.chorus.block

import org.chorus.Player
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntityJukebox
import org.chorus.item.*
import org.chorus.math.BlockFace

class BlockJukebox @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate), BlockEntityHolder<BlockEntityJukebox?> {
    override val name: String
        get() = "Jukebox"

    override val blockEntityClass: Class<out BlockEntityJukebox>
        get() = BlockEntityJukebox::class.java

    override fun getBlockEntityType(): String {
        return BlockEntity.JUKEBOX

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun toItem(): Item {
        return ItemBlock(this, 0)
    }

    override val hardness: Double
        get() = 1.0

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        val jukebox = getOrCreateBlockEntity()!!
        if (!jukebox.getRecordItem()!!.isNothing) {
            jukebox.dropItem()
            return true
        }

        if (!item.isNothing && item is ItemMusicDisc) {
            val record: Item = item.clone()
            record.count = 1
            item.count--
            jukebox.setRecordItem(record)
            jukebox.play()
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
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.Companion.JUKEBOX)

    }
}
