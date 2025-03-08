package cn.nukkit.level.generator.`object`.legacytree

import cn.nukkit.block.*

abstract class LegacyNetherTree @JvmOverloads constructor(
    treeHeight: Int = RandomSourceProvider.create().nextInt(9) + 4
) :
    LegacyTreeGenerator() {
    override var treeHeight: Int

    init {
        this.treeHeight = treeHeight
    }

    override fun getTreeHeight(): Int {
        return treeHeight
    }

    private fun checkY(level: BlockManager, y: Int): Boolean {
        // 防止长出顶部
        if (level.isNether()) {
            return y > 126
        } else if (level.isOverWorld()) {
            return y > 318
        } else if (level.isTheEnd()) {
            return y > 254
        }
        return false
    }

    override fun placeObject(level: BlockManager, x: Int, y: Int, z: Int, random: RandomSourceProvider) {
        if (checkY(level, y)) { // 防止长出下界顶部基岩层
            return
        }

        this.placeTrunk(level, x, y, z, random, this.getTreeHeight())

        val blankArea = -3.0
        val mid = (1 - blankArea / 2).toInt()
        for (yy in y - 3 + treeHeight..y + this.treeHeight - 1) {
            if (checkY(level, yy)) { // 防止长出下界顶部基岩层
                continue
            }

            for (xx in x - mid..x + mid) {
                val xOff = Math.abs(xx - x)
                var zz = z - mid
                while (zz <= z + mid) {
                    val zOff = Math.abs(zz - z)
                    if (xOff == mid && zOff == mid && random.nextInt(2) == 0) {
                        zz += mid * 2
                        continue
                    }
                    val block: Block = level.getBlockAt(xx, yy, zz)
                    if (!block.isSolid) {
                        if (random.nextInt(20) == 0) level.setBlockStateAt(xx, yy, zz, Block.SHROOMLIGHT)
                        else level.setBlockStateAt(xx, yy, zz, this.leafBlockState)
                    }
                    zz += mid * 2
                }
            }

            for (zz in z - mid..z + mid) {
                val zOff = Math.abs(zz - z)
                var xx = x - mid
                while (xx <= x + mid) {
                    val xOff = Math.abs(xx - x)
                    if (xOff == mid && zOff == mid && (random.nextInt(2) == 0)) {
                        xx += mid * 2
                        continue
                    }
                    val block: Block = level.getBlockAt(xx, yy, zz)
                    if (!block.isSolid) {
                        if (random.nextInt(20) == 0) level.setBlockStateAt(xx, yy, zz, Block.SHROOMLIGHT)
                        else level.setBlockStateAt(xx, yy, zz, this.leafBlockState)
                    }
                    xx += mid * 2
                }
            }
        }

        for (yy in y - 4 + treeHeight..y + this.treeHeight - 3) {
            if (checkY(level, yy)) { // 防止长出下界顶部基岩层
                continue
            }

            for (xx in x - mid..x + mid) {
                var zz = z - mid
                while (zz <= z + mid) {
                    val block: Block = level.getBlockAt(xx, yy, zz)
                    if (!block.isSolid) {
                        if (random.nextInt(3) == 0) {
                            for (i in 0..<random.nextInt(5)) {
                                val block2: Block = level.getBlockAt(xx, yy - i, zz)
                                if (!block2.isSolid) level.setBlockStateAt(xx, yy - i, zz, leafBlockState)
                            }
                        }
                    }
                    zz += mid * 2
                }
            }

            for (zz in z - mid..z + mid) {
                var xx = x - mid
                while (xx <= x + mid) {
                    val block: Block = level.getBlockAt(xx, yy, zz)
                    if (!block.isSolid) {
                        if (random.nextInt(3) == 0) {
                            for (i in 0..<random.nextInt(4)) {
                                val block2: Block = level.getBlockAt(xx, yy - i, zz)
                                if (!block2.isSolid) level.setBlockStateAt(xx, yy - i, zz, leafBlockState)
                            }
                        }
                    }
                    xx += mid * 2
                }
            }
        }

        for (xCanopy in x - mid + 1..x + mid - 1) {
            for (zCanopy in z - mid + 1..z + mid - 1) {
                val block: Block = level.getBlockAt(xCanopy, y + treeHeight, zCanopy)
                if (!block.isSolid) level.setBlockStateAt(xCanopy, y + treeHeight, zCanopy, leafBlockState)
            }
        }
    }

    override fun placeTrunk(
        level: BlockManager,
        x: Int,
        y: Int,
        z: Int,
        random: RandomSourceProvider?,
        trunkHeight: Int
    ) {
        level.setBlockStateAt(x, y, z, trunkBlockState)
        for (yy in 0..<trunkHeight) {
            if (checkY(level, y + yy)) { // 防止长出下界顶部基岩层
                continue
            }
            val b: Block = level.getBlockAt(x, y + yy, z)
            if (this.overridable(b)) {
                level.setBlockStateAt(x, y + yy, z, this.trunkBlockState)
            }
        }
    }
}
