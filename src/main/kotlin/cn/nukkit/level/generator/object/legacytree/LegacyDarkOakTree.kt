package cn.nukkit.level.generator.`object`.legacytree

import cn.nukkit.block.*

class LegacyDarkOakTree(private val leafStartHeightMultiplier: Float, private val baseLeafRadius: Int) :
    LegacyTreeGenerator() {
    override val type: WoodType
        get() = WoodType.DARK_OAK

    fun setRandomTreeHeight(random: RandomSourceProvider) {
        this.treeHeight = random.nextInt(15) + 20
    }

    override fun placeObject(level: BlockManager, x: Int, y: Int, z: Int, random: RandomSourceProvider) {
        if (this.treeHeight == 0) {
            this.setRandomTreeHeight(random)
        }

        val topSize = this.treeHeight - (this.treeHeight * leafStartHeightMultiplier).toInt()
        val lRadius: Int = baseLeafRadius + random.nextInt(2)

        this.placeTrunk(level, x, y, z, random, this.getTreeHeight() - random.nextInt(3))

        this.placeLeaves(level, topSize, lRadius, x, y, z, random)
    }

    override fun placeTrunk(
        level: BlockManager,
        x: Int,
        y: Int,
        z: Int,
        random: RandomSourceProvider?,
        trunkHeight: Int
    ) {
        // The base dirt block
        level.setBlockStateAt(x, y - 1, z, Block.DIRT)
        val radius = 2

        for (yy in 0..<trunkHeight) {
            for (xx in 0..<radius) {
                for (zz in 0..<radius) {
                    val b: Block = level.getBlockAt(x, y + yy, z)
                    if (this.overridable(b)) {
                        level.setBlockStateAt(x + xx, y + yy, z + zz, trunkBlockState)
                    }
                }
            }
        }
    }

    fun placeLeaves(
        level: BlockManager,
        topSize: Int,
        lRadius: Int,
        x: Int,
        y: Int,
        z: Int,
        random: RandomSourceProvider
    ) {
        var radius: Int = random.nextInt(2)
        var maxR = 1
        var minR = 0

        for (yy in 0..topSize) {
            val yyy = y + this.treeHeight - yy

            for (xx in x - radius..x + radius) {
                val xOff = Math.abs(xx - x)
                for (zz in z - radius..z + radius) {
                    val zOff = Math.abs(zz - z)
                    if (xOff == radius && zOff == radius && radius > 0) {
                        continue
                    }
                    val solid: Boolean = level.getBlockAt(xx, yyy, zz).isSolid()
                    if (!solid) {
                        level.setBlockStateAt(xx, yyy, zz, leafBlockState)
                    }
                }
            }

            if (radius >= maxR) {
                radius = minR
                minR = 1
                if (++maxR > lRadius) {
                    maxR = lRadius
                }
            } else {
                ++radius
            }
        }
    }
}
