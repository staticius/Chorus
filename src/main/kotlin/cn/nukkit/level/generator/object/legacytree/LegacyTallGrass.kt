package cn.nukkit.level.generator.`object`.legacytree

import cn.nukkit.block.*
import cn.nukkit.math.Vector3

object LegacyTallGrass {
    private val places = arrayOf<BlockState>(
        //total 106
        BlockShortGrass.PROPERTIES.getDefaultState(),  //50
        BlockTallGrass.PROPERTIES.getDefaultState(),  // 30
        BlockDandelion.PROPERTIES.getDefaultState(),  // 10
        BlockPoppy.PROPERTIES.getDefaultState(),  // 10
        BlockAzureBluet.PROPERTIES.getDefaultState(),  // 5
        BlockOxeyeDaisy.PROPERTIES.getDefaultState(),  // 5
        BlockAllium.PROPERTIES.getDefaultState(),  // 2
        BlockCornflower.PROPERTIES.getDefaultState(),  // 2
        BlockBlueOrchid.PROPERTIES.getDefaultState(),  // 2
        BlockLilyOfTheValley.PROPERTIES.getDefaultState(),  // 2
        BlockRedTulip.PROPERTIES.getDefaultState(),  // 2
        BlockOrangeTulip.PROPERTIES.getDefaultState(),  // 2
        BlockPinkTulip.PROPERTIES.getDefaultState(),  // 2
        BlockWhiteTulip.PROPERTIES.getDefaultState(),  // 2
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
                    if (level.getBlockIdAt(x, newY, z) == Block.AIR && level.getBlockIdAt(
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

