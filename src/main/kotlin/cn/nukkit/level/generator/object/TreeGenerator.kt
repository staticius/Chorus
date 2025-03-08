package cn.nukkit.level.generator.`object`

import cn.nukkit.math.BlockVector3
import cn.nukkit.math.Vector3
import java.util.*

abstract class TreeGenerator : ObjectGenerator() {
    /*
        * returns whether or not a tree can grow into a block
        * For example, a tree will not grow into stone
        */
    protected fun canGrowInto(id: String): Boolean {
        return when (id) {
            AIR, ACACIA_LEAVES, AZALEA_LEAVES, BIRCH_LEAVES, AZALEA_LEAVES_FLOWERED, CHERRY_LEAVES, DARK_OAK_LEAVES, JUNGLE_LEAVES, MANGROVE_LEAVES, OAK_LEAVES, SPRUCE_LEAVES, PALE_OAK_LEAVES, GRASS_BLOCK, DIRT, ACACIA_LOG, BIRCH_LOG, OAK_LOG, PALE_OAK_LOG, DARK_OAK_LOG, JUNGLE_LOG, MANGROVE_LOG, SPRUCE_LOG, VINE, DIRT_WITH_ROOTS, CHERRY_LOG, MANGROVE_ROOTS, MANGROVE_PROPAGULE, ACACIA_SAPLING, CHERRY_SAPLING, SPRUCE_SAPLING, BAMBOO_SAPLING, OAK_SAPLING, JUNGLE_SAPLING, DARK_OAK_SAPLING, PALE_OAK_SAPLING, BIRCH_SAPLING -> true
            else -> false
        }
    }

    fun generateSaplings(level: BlockManager?, random: Random?, pos: Vector3?) {
    }

    protected fun setDirtAt(level: BlockManager, pos: BlockVector3) {
        setDirtAt(level, Vector3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()))
    }

    /*
     * sets dirt at a specific location if it isn't already dirt
     */
    protected open fun setDirtAt(level: BlockManager, pos: Vector3) {
        level.setBlockStateAt(pos.floorX, pos.floorY, pos.floorZ, DIRT)
    }
}

