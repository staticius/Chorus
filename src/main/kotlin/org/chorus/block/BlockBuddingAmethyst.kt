package org.chorus.block

import org.chorus.item.*
import org.chorus.level.*
import org.chorus.math.BlockFace.Companion.fromIndex
import org.chorus.utils.random.NukkitRandom.nextInt

class BlockBuddingAmethyst : BlockSolid {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "Budding Amethyst"

    override val resistance: Double
        get() = 1.5

    override val hardness: Double
        get() = 1.5

    override fun breaksWhenMoved(): Boolean {
        return true
    }

    override fun sticksToPiston(): Boolean {
        return false
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (RANDOM.nextInt(5) == 1) {
                tryGrow(0)
            }
            return type
        }
        return 0
    }

    fun tryGrow(time: Int) {
        if (time > 6) {
            return
        }
        val face = fromIndex(RANDOM.nextInt(6))
        val side = this.getSide(face!!)
        val tmp: BlockAmethystBud
        if (side!!.canBeReplaced()) {
            tmp = BlockSmallAmethystBud()
            tmp.blockFace = face
            level.setBlock(side.position, tmp, true, true)
        } else if (side is BlockSmallAmethystBud) {
            tmp = BlockMediumAmethystBud()
            tmp.blockFace = face
            level.setBlock(side.position, tmp, true, true)
        } else if (side is BlockMediumAmethystBud) {
            tmp = BlockLargeAmethystBud()
            tmp.blockFace = face
            level.setBlock(side.position, tmp, true, true)
        } else if (side is BlockLargeAmethystBud) {
            tmp = BlockAmethystCluster()
            tmp.blockFace = face
            level.setBlock(side.position, tmp, true, true)
        } else {
            tryGrow(time + 1)
        }
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BUDDING_AMETHYST)
            get() = Companion.field
        private val RANDOM: NukkitRandom = NukkitRandom()
    }
}
