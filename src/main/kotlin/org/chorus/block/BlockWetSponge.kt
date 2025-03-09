package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.math.BlockFace
import java.util.concurrent.ThreadLocalRandom

class BlockWetSponge @JvmOverloads constructor(state: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(state) {
    override val hardness: Double
        get() = 0.6

    override val resistance: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

    override val name: String
        get() = "Wet Sponge"

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
        if (level.dimension != Level.DIMENSION_NETHER) return true

        level.setBlock(block.position, BlockSponge(), true, true)
        level.addLevelEvent(block.position.add(0.5, 0.875, 0.5)!!, LevelEventPacket.EVENT_CAULDRON_EXPLODE)
        val random = ThreadLocalRandom.current()

        for (i in 0..7) {
            level.addParticle(CloudParticle(block.position.add(random.nextDouble(), 1.0, random.nextDouble())))
        }

        return true
    }

    override fun toItem(): Item? {
        return ItemBlock(BlockWetSponge())
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WET_SPONGE)
            get() = Companion.field
    }
}
