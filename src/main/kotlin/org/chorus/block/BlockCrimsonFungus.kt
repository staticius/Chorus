package org.chorus.block

import org.chorus.Player
import org.chorus.Server
import org.chorus.event.level.StructureGrowEvent
import org.chorus.level.generator.`object`.BlockManager
import org.chorus.level.generator.`object`.legacytree.LegacyCrimsonTree
import org.chorus.utils.ChorusRandom

class BlockCrimsonFungus @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFungus(blockstate) {
    private val feature = LegacyCrimsonTree()

    override val name: String
        get() = "Crimson Fungus"

    override fun canGrowOn(support: Block?): Boolean {
        if (support != null) {
            if (support.id == BlockID.CRIMSON_NYLIUM) {
                for (i in 1..feature.getTreeHeight()) {
                    if (!up(i)!!.isAir) {
                        return false
                    }
                }
                return true
            }
        }
        return false
    }

    override fun grow(cause: Player?): Boolean {
        val chorusRandom = ChorusRandom()
        val blockManager = BlockManager(this.level)
        feature.placeObject(
            blockManager,
            position.floorX, position.floorY, position.floorZ, chorusRandom
        )
        val ev = StructureGrowEvent(this, blockManager.blocks)
        Server.instance.pluginManager.callEvent(ev)
        if (ev.isCancelled) {
            return false
        }
        blockManager.applySubChunkUpdate(ev.blockList)
        return true
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.CRIMSON_FUNGUS)
    }
}