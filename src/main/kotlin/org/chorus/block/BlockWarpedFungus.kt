package org.chorus.block

import org.chorus.Player
import org.chorus.event.Event.isCancelled
import org.chorus.event.level.StructureGrowEvent.blockList
import org.chorus.level.generator.`object`.BlockManager.applySubChunkUpdate
import org.chorus.level.generator.`object`.BlockManager.blocks
import org.chorus.level.generator.`object`.legacytree.LegacyNetherTree.placeObject

class BlockWarpedFungus @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFungus(blockstate) {
    private val feature: LegacyWarpedTree = LegacyWarpedTree()

    override val name: String
        get() = "Warped Fungus"

    override fun canGrowOn(support: Block): Boolean {
        if (support.id == BlockID.WARPED_NYLIUM) {
            for (i in 1..feature.getTreeHeight()) {
                if (!up(i)!!.isAir) {
                    return false
                }
            }
            return true
        }
        return false
    }

    override fun grow(cause: Player?): Boolean {
        val nukkitRandom: NukkitRandom = NukkitRandom()
        val blockManager: BlockManager = BlockManager(this.level)
        feature.placeObject(
            blockManager,
            position.floorX, position.floorY, position.floorZ, nukkitRandom
        )
        val ev: StructureGrowEvent = StructureGrowEvent(this, blockManager.blocks)
        level.server.pluginManager.callEvent(ev)
        if (ev.isCancelled) {
            return false
        }
        blockManager.applySubChunkUpdate(ev.blockList)
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WARPED_FUNGUS)
            get() = Companion.field
    }
}