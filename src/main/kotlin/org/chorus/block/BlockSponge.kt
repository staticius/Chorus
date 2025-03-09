package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.item.ItemBlock
import cn.nukkit.item.ItemTool
import cn.nukkit.level.Locator.x
import cn.nukkit.level.Locator.y
import cn.nukkit.level.Locator.z
import cn.nukkit.math.BlockFace
import java.util.*

class BlockSponge @JvmOverloads constructor(state: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(state) {
    override val hardness: Double
        get() = 0.6

    override val resistance: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

    override val name: String
        get() = "Sponge"

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
        if ((block is BlockFlowingWater || block.levelBlockAround.stream()
                .anyMatch { b: Block? -> b is BlockFlowingWater }) && performWaterAbsorb(block)
        ) {
            level.setBlock(block.position, BlockWetSponge(), true, true)

            val packet: LevelEventPacket = LevelEventPacket()
            packet.evid = LevelEventPacket.EVENT_PARTICLE_DESTROY_BLOCK
            packet.x = block.x.toFloat() + 0.5f
            packet.y = block.y.toFloat() + 1f
            packet.z = block.z.toFloat() + 0.5f
            packet.data = get(BlockID.FLOWING_WATER).blockState!!.blockStateHash()

            for (i in 0..3) {
                level.addChunkPacket(position.chunkX, position.chunkZ, packet)
            }

            return true
        }

        return super.place(item, block, target, face, fx, fy, fz, player)
    }

    override fun toItem(): Item? {
        return ItemBlock(BlockSponge())
    }

    private fun performWaterAbsorb(block: Block): Boolean {
        val entries: Queue<Entry> = ArrayDeque()

        entries.add(Entry(block, 0))

        var entry: Entry
        var waterRemoved = 0
        while (waterRemoved < 64 && (entries.poll().also { entry = it }) != null) {
            for (face in BlockFace.entries) {
                val layer0 = entry.block.getSideAtLayer(0, face)
                val layer1 = layer0!!.getLevelBlockAtLayer(1)

                if (layer0 is BlockFlowingWater) {
                    level.setBlockStateAt(
                        layer0.position.floorX,
                        layer0.position.floorY,
                        layer0.position.floorZ,
                        BlockAir.properties.getDefaultState()
                    )
                    level.updateAround(layer0.position)
                    waterRemoved++
                    if (entry.distance < 6) {
                        entries.add(Entry(layer0, entry.distance + 1))
                    }
                } else if (layer1 is BlockFlowingWater) {
                    if (BlockID.KELP == layer0.id ||
                        BlockID.SEAGRASS == layer0.id ||
                        BlockID.SEA_PICKLE == layer0.id || layer0 is BlockCoralFan
                    ) {
                        layer0.level.useBreakOn(layer0.position)
                    }
                    level.setBlockStateAt(
                        layer1.position.floorX,
                        layer1.position.floorY,
                        layer1.position.floorZ,
                        1,
                        BlockAir.properties.getDefaultState()
                    )
                    level.updateAround(layer1.position)
                    waterRemoved++
                    if (entry.distance < 6) {
                        entries.add(Entry(layer1, entry.distance + 1))
                    }
                }
            }
        }

        return waterRemoved > 0
    }

    @JvmRecord
    private data class Entry(val block: Block, val distance: Int)
    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SPONGE)
            get() = Companion.field
    }
}
