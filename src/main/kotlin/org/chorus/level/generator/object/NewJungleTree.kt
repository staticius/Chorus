package org.chorus.level.generator.`object`

import cn.nukkit.block.*
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.math.BlockVector3
import cn.nukkit.math.Vector3

/**
 * @author CreeperFace
 * @since 26. 10. 2016
 */
class NewJungleTree(
    /**
     * The minimum height of a generated tree.
     */
    private val minTreeHeight: Int, private val maxTreeHeight: Int
) : TreeGenerator() {
    /**
     * The metadata value of the wood to use in tree generation.
     */
    private val metaWood: BlockState =
        BlockJungleLog.properties.getBlockState(CommonBlockProperties.PILLAR_AXIS.createValue(BlockFace.Axis.Y))

    /**
     * The metadata value of the leaves to use in tree generation.
     */
    private val metaLeaves: BlockState = BlockJungleLeaves.properties.getDefaultState()

    override fun generate(level: BlockManager, rand: RandomSourceProvider, vectorPosition: Vector3): Boolean {
        val position = BlockVector3(vectorPosition.floorX, vectorPosition.floorY, vectorPosition.floorZ)

        val i: Int = rand.nextInt(maxTreeHeight) + this.minTreeHeight
        var flag = true

        //Check the height of the tree and if exist block in it
        if (position.y >= level.minHeight && position.y + i + 1 < level.maxHeight) {
            for (j in position.y..position.y + 1 + i) {
                var k = 1

                if (j == position.y) {
                    k = 0
                }

                if (j >= position.y + 1 + i - 2) {
                    k = 2
                }

                val pos2 = BlockVector3()

                var l = position.x - k
                while (l <= position.x + k && flag) {
                    var i1 = position.z - k
                    while (i1 <= position.z + k && flag) {
                        if (j >= level.minHeight && j < level.maxHeight) {
                            pos2.setComponents(l, j, i1)
                            if (!this.canGrowInto(level.getBlockIdAt(pos2.x, pos2.y, pos2.z))) {
                                flag = false
                            }
                        } else {
                            flag = false
                        }
                        ++i1
                    }
                    ++l
                }
            }

            if (!flag) {
                return false
            } else {
                val down = position.down()
                val block = level.getBlockIdAt(down.x, down.y, down.z)

                if ((block == GRASS_BLOCK || block == DIRT || block == FARMLAND) && position.y < level.maxHeight - i - 1) {
                    this.setDirtAt(level, down)

                    //Add leaves
                    for (i3 in position.y - 3 + i..position.y + i) {
                        val i4 = i3 - (position.y + i)
                        val j1 = 1 - i4 / 2

                        for (k1 in position.x - j1..position.x + j1) {
                            val l1 = k1 - position.x

                            for (i2 in position.z - j1..position.z + j1) {
                                val j2 = i2 - position.z

                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i4 != 0) {
                                    val blockpos = BlockVector3(k1, i3, i2)
                                    val id = level.getBlockAt(blockpos.x, blockpos.y, blockpos.z)

                                    if (id!!.id == AIR || id is BlockLeaves || id.id == VINE) {
                                        level.setBlockStateAt(blockpos, metaLeaves)
                                    }
                                }
                            }
                        }
                    }

                    //Add vine
                    for (j3 in 0..<i) {
                        val up = position.up(j3)
                        val b = level.getBlockAt(up.x, up.y, up.z)
                        val id = b!!.id

                        if (id == AIR || b is BlockLeaves || id == VINE) {
                            //Add tree trunks
                            level.setBlockStateAt(up, metaWood)
                            if (j3 > 0) {
                                if (rand.nextInt(3) > 0 && isAirBlock(level, position.add(-1, j3, 0))) {
                                    this.addVine(level, position.add(-1, j3, 0), 8)
                                }

                                if (rand.nextInt(3) > 0 && isAirBlock(level, position.add(1, j3, 0))) {
                                    this.addVine(level, position.add(1, j3, 0), 2)
                                }

                                if (rand.nextInt(3) > 0 && isAirBlock(level, position.add(0, j3, -1))) {
                                    this.addVine(level, position.add(0, j3, -1), 1)
                                }

                                if (rand.nextInt(3) > 0 && isAirBlock(level, position.add(0, j3, 1))) {
                                    this.addVine(level, position.add(0, j3, 1), 4)
                                }
                            }
                        }
                    }

                    for (k3 in position.y - 3 + i..position.y + i) {
                        val j4 = k3 - (position.y + i)
                        val k4 = 2 - j4 / 2
                        val pos2 = BlockVector3()

                        for (l4 in position.x - k4..position.x + k4) {
                            for (i5 in position.z - k4..position.z + k4) {
                                pos2.setComponents(l4, k3, i5)

                                if (level.getBlockAt(pos2.x, pos2.y, pos2.z) is BlockLeaves) {
                                    val blockpos2 = pos2.west()
                                    val blockpos3 = pos2.east()
                                    val blockpos4 = pos2.north()
                                    val blockpos1 = pos2.south()

                                    if (rand.nextInt(4) == 0 && level.getBlockIdAt(
                                            blockpos2.x,
                                            blockpos2.y,
                                            blockpos2.z
                                        ) == AIR
                                    ) {
                                        this.addHangingVine(level, blockpos2, 8)
                                    }

                                    if (rand.nextInt(4) == 0 && level.getBlockIdAt(
                                            blockpos3.x,
                                            blockpos3.y,
                                            blockpos3.z
                                        ) == AIR
                                    ) {
                                        this.addHangingVine(level, blockpos3, 2)
                                    }

                                    if (rand.nextInt(4) == 0 && level.getBlockIdAt(
                                            blockpos4.x,
                                            blockpos4.y,
                                            blockpos4.z
                                        ) == AIR
                                    ) {
                                        this.addHangingVine(level, blockpos4, 1)
                                    }

                                    if (rand.nextInt(4) == 0 && level.getBlockIdAt(
                                            blockpos1.x,
                                            blockpos1.y,
                                            blockpos1.z
                                        ) == AIR
                                    ) {
                                        this.addHangingVine(level, blockpos1, 4)
                                    }
                                }
                            }
                        }
                    }

                    //Add cocoa beans
                    if (rand.nextInt(5) == 0 && i > 5) {
                        for (l3 in 0..1) {
                            for (enumfacing in BlockFace.Plane.HORIZONTAL) {
                                if (rand.nextInt(4 - l3) == 0) {
                                    val enumfacing1: BlockFace = enumfacing.getOpposite()
                                    this.placeCocoa(
                                        level,
                                        rand.nextInt(3),
                                        position.add(enumfacing1.getXOffset(), i - 5 + l3, enumfacing1.getZOffset()),
                                        enumfacing
                                    )
                                }
                            }
                        }
                    }

                    return true
                } else {
                    return false
                }
            }
        } else {
            return false
        }
    }

    private fun placeCocoa(level: BlockManager, age: Int, pos: BlockVector3, side: BlockFace) {
        val blockState: BlockState = BlockCocoa.properties.getBlockState(
            CommonBlockProperties.AGE_3.createValue(age),
            CommonBlockProperties.DIRECTION.createValue(side.getHorizontalIndex())
        )
        level.setBlockStateAt(pos, blockState)
    }

    private fun addVine(level: BlockManager, pos: BlockVector3, meta: Int) {
        val blockState: BlockState =
            BlockVine.properties.getBlockState<Int, IntPropertyType>(CommonBlockProperties.VINE_DIRECTION_BITS, meta)
        level.setBlockStateAt(pos, blockState)
    }

    private fun addHangingVine(level: BlockManager, pos: BlockVector3, meta: Int) {
        var pos = pos
        this.addVine(level, pos, meta)
        var i = 4

        pos = pos.down()
        while (i > 0 && level.getBlockIdAt(pos.x, pos.y, pos.z) == AIR) {
            this.addVine(level, pos, meta)
            pos = pos.down()
            --i
        }
    }

    private fun isAirBlock(level: BlockManager, v: BlockVector3): Boolean {
        return level.getBlockIdAt(v.x, v.y, v.z) == AIR
    }
}
