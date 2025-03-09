package org.chorus.utils

import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.block.BlockID
import cn.nukkit.block.BlockState
import cn.nukkit.level.DimensionData
import cn.nukkit.level.DimensionEnum
import cn.nukkit.level.Level
import cn.nukkit.level.Locator
import cn.nukkit.math.*
import lombok.extern.slf4j.Slf4j
import java.util.function.BiPredicate

@Slf4j
object PortalHelper : BlockID {
    fun spawnPortal(pos: Locator) {
        val lvl = pos.level //TODO: This will generate part of the time, seems to be only when the chunk is populated
        val x = pos.position.floorX
        var y = pos.position.floorY
        var z = pos.position.floorZ

        val air = Block.get(BlockID.AIR)
        val obsidian = Block.get(BlockID.OBSIDIAN)
        val netherPortal = Block.get(BlockID.PORTAL)
        for (xx in -1..3) {
            for (yy in 1..3) {
                for (zz in -1..2) {
                    lvl.setBlock(x + xx, y + yy, z + zz, air, false, true)
                }
            }
        }

        lvl.setBlock(x + 1, y, z, obsidian, false, true)
        lvl.setBlock(x + 2, y, z, obsidian, false, true)

        z++
        lvl.setBlock(x, y, z, obsidian, false, true)
        lvl.setBlock(x + 1, y, z, obsidian, false, true)
        lvl.setBlock(x + 2, y, z, obsidian, false, true)
        lvl.setBlock(x + 3, y, z, obsidian, false, true)

        z++
        lvl.setBlock(x + 1, y, z, obsidian, false, true)
        lvl.setBlock(x + 2, y, z, obsidian, false, true)
        z--

        for (i in 0..2) {
            y++
            lvl.setBlock(x, y, z, obsidian, false, true)
            lvl.setBlock(x + 1, y, z, netherPortal, false, true)
            lvl.setBlock(x + 2, y, z, netherPortal, false, true)
            lvl.setBlock(x + 3, y, z, obsidian, false, true)
        }

        y++
        lvl.setBlock(x, y, z, obsidian, false, true)
        lvl.setBlock(x + 1, y, z, obsidian, false, true)
        lvl.setBlock(x + 2, y, z, obsidian, false, true)
        lvl.setBlock(x + 3, y, z, obsidian, false, true)
    }

    fun getNearestValidPortal(currentPos: Locator): Locator? {
        val axisAlignedBB: AxisAlignedBB = SimpleAxisAlignedBB(
            Vector3(
                currentPos.position.floorX - 128.0,
                currentPos.level.dimensionData.minHeight.toDouble(),
                currentPos.position.floorZ - 128.0
            ),
            Vector3(
                currentPos.position.floorX + 128.0,
                currentPos.level.dimensionData.maxHeight.toDouble(),
                currentPos.position.floorZ + 128.0
            )
        )
        val condition =
            BiPredicate { pos: BlockVector3?, state: BlockState? -> state!!.identifier == BlockID.PORTAL }
        val blocks: List<Block?> = currentPos.level.scanBlocks(axisAlignedBB, condition)

        if (blocks.isEmpty()) {
            return null
        }

        val currentPosV2 = Vector2(currentPos.position.floorX.toDouble(), currentPos.position.floorZ.toDouble())
        val by = currentPos.position.floorY.toDouble()
        val euclideanDistance = Comparator.comparingDouble { block: Block? ->
            currentPosV2.distanceSquared(
                block!!.position.floorX.toDouble(), block.position.floorZ.toDouble()
            )
        }
        val heightDistance = Comparator.comparingDouble { block: Block? ->
            val ey = by - block!!.position.y
            ey * ey
        }

        return blocks.stream()
            .filter { block: Block? -> block!!.down().id != BlockID.PORTAL }
            .min(euclideanDistance.thenComparing(heightDistance))
            .orElse(null)
    }

    fun convertPosBetweenNetherAndOverworld(current: Locator): Locator? {
        val defaultNetherLevel = Server.getInstance().defaultNetherLevel ?: return null
        val dimensionData: DimensionData
        if (current.level.dimension == Level.DIMENSION_OVERWORLD) {
            dimensionData = DimensionEnum.NETHER.dimensionData
            return Locator(
                (current.position.floorX shr 3).toDouble(),
                NukkitMath.clamp(current.position.floorY, dimensionData.minHeight, dimensionData.maxHeight).toDouble(),
                (current.position.floorZ shr 3).toDouble(),
                defaultNetherLevel
            )
        } else if (current.level.dimension == Level.DIMENSION_NETHER) {
            dimensionData = DimensionEnum.OVERWORLD.dimensionData
            return Locator(
                (current.position.floorX shl 3).toDouble(),
                NukkitMath.clamp(current.position.floorY, dimensionData.minHeight, dimensionData.maxHeight).toDouble(),
                (current.position.floorZ shl 3).toDouble(),
                Server.getInstance().defaultLevel
            )
        } else {
            throw IllegalArgumentException("Neither overworld nor nether given!")
        }
    }

    @JvmStatic
    fun moveToTheEnd(current: Locator): Locator? {
        val defaultEndLevel = Server.getInstance().defaultEndLevel ?: return null
        return if (current.level.dimension == Level.DIMENSION_OVERWORLD) {
            Locator(100.0, 49.0, 0.0, defaultEndLevel)
        } else if (current.level.dimension == Level.DIMENSION_THE_END) {
            Server.getInstance().defaultLevel.spawnLocation
        } else {
            throw IllegalArgumentException("Neither overworld nor the end given!")
        }
    }
}
