package org.chorus.level.generator.`object`.legacytree

import org.chorus.block.*
import org.chorus.math.Vector3

object LegacyTallGrass {
    private val places = arrayOf<BlockState>(
        //total 106
        BlockShortGrass.properties.getDefaultState(),  //50
        BlockTallGrass.properties.getDefaultState(),  // 30
        BlockDandelion.properties.getDefaultState(),  // 10
        BlockPoppy.properties.getDefaultState(),  // 10
        BlockAzureBluet.properties.getDefaultState(),  // 5
        BlockOxeyeDaisy.properties.getDefaultState(),  // 5
        BlockAllium.properties.getDefaultState(),  // 2
        BlockCornflower.properties.getDefaultState(),  // 2
        BlockBlueOrchid.properties.getDefaultState(),  // 2
        BlockLilyOfTheValley.properties.getDefaultState(),  // 2
        BlockRedTulip.properties.getDefaultState(),  // 2
        BlockOrangeTulip.properties.getDefaultState(),  // 2
        BlockPinkTulip.properties.getDefaultState(),  // 2
        BlockWhiteTulip.properties.getDefaultState(),  // 2
    )

    fun growGrass(level: BlockManager, pos: Vector3, random: RandomSourceProvider) {
        val y = pos.floorY + 1
        val minx = pos.floorX - 2
        val minz = pos.floorZ - 2
        val maxx = pos.floorX + 2
        val maxz = pos.floorZ + 2
        for (x in minx..maxx) {
            for (z in minz..maxz) {
                val newY: Int = y + random.nextInt(2) * (if (random.nextBoolean()) -1 else 1)
                if (random.nextBoolean()) {
                    if (level.getBlockIdAt(x, newY, z) == BlockID.AIR && level.getBlockIdAt(
                            x,
                            newY - 1,
                            z
                        ) == Block.GRASS_BLOCK
                    ) {
                        val ranNumber = Math.round(random.nextGaussian() * 1000).toInt()
                        val absRn = Math.abs(ranNumber)
                        if (-300 <= ranNumber && ranNumber <= 300) {
                            level.setBlockStateAt(x, newY, z, places[0])
                        } else if (300 <= absRn && absRn <= 500) { //-300 ~ -500 + 300 ~ 50
                            level.setBlockStateAt(x, newY, z, places[1])
                            val block: BlockTallGrass = BlockTallGrass()
                            block.setTopHalf(true)
                            level.getLevel().setBlock(x, newY + 1, z, block, false, false)
                        } else if (500 <= ranNumber && ranNumber < 600) {
                            level.setBlockStateAt(x, newY, z, places[2])
                        } else if (-600 <= ranNumber && ranNumber <= -500) {
                            level.setBlockStateAt(x, newY, z, places[3])
                        } else if (600 <= ranNumber && ranNumber < 700) {
                            level.setBlockStateAt(x, newY, z, places[4])
                        } else if (-700 <= ranNumber && ranNumber < -600) {
                            level.setBlockStateAt(x, newY, z, places[5])
                        } else if (-750 <= ranNumber && ranNumber < -700) {
                            level.setBlockStateAt(x, newY, z, places[6])
                        } else if (-800 <= ranNumber && ranNumber < -750) {
                            level.setBlockStateAt(x, newY, z, places[7])
                        } else if (-850 <= ranNumber && ranNumber < -800) {
                            level.setBlockStateAt(x, newY, z, places[8])
                        } else if (-900 <= ranNumber && ranNumber < -850) {
                            level.setBlockStateAt(x, newY, z, places[9])
                        } else if (-1000 <= ranNumber && ranNumber < -900) {
                            level.setBlockStateAt(x, newY, z, places[10])
                        } else if (700 <= ranNumber && ranNumber < 800) {
                            level.setBlockStateAt(x, newY, z, places[11])
                        } else if (800 <= ranNumber && ranNumber < 900) {
                            level.setBlockStateAt(x, newY, z, places[12])
                        } else if (900 <= ranNumber && ranNumber < 1000) {
                            level.setBlockStateAt(x, newY, z, places[13])
                        }
                    }
                }
            }
        }
    }
}

