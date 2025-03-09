package org.chorus.command.defaults

import cn.nukkit.block.Block
import cn.nukkit.block.BlockState
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.level.Locator
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.NukkitMath
import cn.nukkit.math.SimpleAxisAlignedBB
import cn.nukkit.utils.Utils
import kotlin.math.max
import kotlin.math.min

class FillCommand(name: String) : VanillaCommand(name, "commands.fill.description") {
    init {
        this.permission = "nukkit.command.fill"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newType("from", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newType("to", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newEnum("tileName", false, CommandEnum.Companion.ENUM_BLOCK),
                CommandParameter.Companion.newType("blockStates", true, CommandParamType.BLOCK_STATES),
                CommandParameter.Companion.newEnum(
                    "oldBlockHandling",
                    true,
                    arrayOf<String?>("destroy", "hollow", "keep", "outline", "replace")
                ),
            )
        )
        this.addCommandParameters(
            "replace", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newType("from", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newType("to", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newEnum("tileName", false, CommandEnum.Companion.ENUM_BLOCK),
                CommandParameter.Companion.newType("blockStates", false, CommandParamType.BLOCK_STATES),
                CommandParameter.Companion.newEnum("oldBlockHandling", false, arrayOf<String?>("replace")),
                CommandParameter.Companion.newEnum("replaceTileName", false, CommandEnum.Companion.ENUM_BLOCK),
                CommandParameter.Companion.newType("blockStates", true, CommandParamType.BLOCK_STATES)
            )
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val from = list!!.getResult<Locator>(0)
        val to = list.getResult<Locator>(1)
        val b = list.getResult<Block>(2)
        var tileState = b!!.properties.defaultState
        var oldBlockHandling = FillMode.REPLACE
        var replaceState: BlockState? = null

        val aabb: AxisAlignedBB = SimpleAxisAlignedBB(
            min(from!!.x, to!!.x), min(from.y, to.y), min(
                from.z, to.z
            ), max(from.x, to.x), max(from.y, to.y), max(
                from.z, to.z
            )
        )
        if (aabb.minY < -64 || aabb.maxY > 320) {
            log.addError("commands.fill.outOfWorld").output()
            return 0
        }

        val size =
            NukkitMath.floorDouble((aabb.maxX - aabb.minX + 1) * (aabb.maxY - aabb.minY + 1) * (aabb.maxZ - aabb.minZ + 1))
        if (size > 16 * 16 * 16 * 8) {
            log.addError("commands.fill.tooManyBlocks", size.toString(), (16 * 16 * 16 * 8).toString())
            log.addError("Operation will continue, but too many blocks may cause stuttering")
        }

        val level = from.level

        for (chunkX in NukkitMath.floorDouble(aabb.minX) shr 4..(NukkitMath.floorDouble(aabb.maxX) shr 4)) {
            for (chunkZ in NukkitMath.floorDouble(aabb.minZ) shr 4..(NukkitMath.floorDouble(aabb.maxZ) shr 4)) {
                if (level.getChunkIfLoaded(chunkX, chunkZ) == null) {
                    log.addError("commands.fill.failed").output()
                    return 0
                }
            }
        }
        val blocks: Array<Block>
        var count = 0

        val blockManager: BlockManager = BlockManager(level)
        when (result.key) {
            "default" -> {
                if (list.hasResult(3)) tileState = list.getResult(3)
                if (list.hasResult(4)) {
                    val str = list.getResult<String>(4)
                    oldBlockHandling = FillMode.valueOf(str!!.uppercase())
                }
                when (oldBlockHandling) {
                    FillMode.OUTLINE -> {
                        for (x in NukkitMath.floorDouble(aabb.minX)..NukkitMath.floorDouble(aabb.maxX)) {
                            for (z in NukkitMath.floorDouble(aabb.minZ)..NukkitMath.floorDouble(aabb.maxZ)) {
                                for (y in NukkitMath.floorDouble(aabb.minY)..NukkitMath.floorDouble(aabb.maxY)) {
                                    val isBorderX =
                                        x == NukkitMath.floorDouble(from.position.x) || x == NukkitMath.floorDouble(
                                            to.position.x
                                        )
                                    val isBorderZ =
                                        z == NukkitMath.floorDouble(from.position.z) || z == NukkitMath.floorDouble(
                                            to.position.z
                                        )
                                    val isBorderY =
                                        y == NukkitMath.floorDouble(from.position.y) || y == NukkitMath.floorDouble(
                                            to.position.y
                                        )

                                    if (isBorderX || isBorderZ || isBorderY) {
                                        blockManager.setBlockStateAt(x, y, z, tileState)
                                        ++count
                                    }
                                }
                            }
                        }
                    }

                    FillMode.HOLLOW -> {
                        for (x in NukkitMath.floorDouble(aabb.minX)..NukkitMath.floorDouble(aabb.maxX)) {
                            for (z in NukkitMath.floorDouble(aabb.minZ)..NukkitMath.floorDouble(aabb.maxZ)) {
                                for (y in NukkitMath.floorDouble(aabb.minY)..NukkitMath.floorDouble(aabb.maxY)) {
                                    val block: Block
                                    val isBorderX =
                                        x == NukkitMath.floorDouble(from.position.x) || x == NukkitMath.floorDouble(
                                            to.position.x
                                        )
                                    val isBorderZ =
                                        z == NukkitMath.floorDouble(from.position.z) || z == NukkitMath.floorDouble(
                                            to.position.z
                                        )
                                    val isBorderY =
                                        y == NukkitMath.floorDouble(from.position.y) || y == NukkitMath.floorDouble(
                                            to.position.y
                                        )

                                    block = if (isBorderX || isBorderZ || isBorderY) {
                                        tileState!!.toBlock()
                                    } else {
                                        Block.get(Block.AIR)
                                    }

                                    blockManager.setBlockStateAt(x, y, z, block.blockState)
                                    ++count
                                }
                            }
                        }
                    }

                    FillMode.REPLACE -> {
                        blocks = Utils.getLevelBlocks(level, aabb)
                        for (block in blocks) {
                            blockManager.setBlockStateAt(
                                block.position.floorX,
                                block.position.floorY,
                                block.position.floorZ,
                                tileState
                            )
                            ++count
                        }
                    }

                    FillMode.DESTROY -> {
                        blocks = Utils.getLevelBlocks(level, aabb)
                        for (block in blocks) {
                            val players =
                                level.getChunkPlayers(block.position.x.toInt() shr 4, block.position.z.toInt() shr 4)
                            level.addParticle(DestroyBlockParticle(block.position.add(0.5), block), players.values)
                            blockManager.setBlockStateAt(
                                block.position.floorX,
                                block.position.floorY,
                                block.position.floorZ,
                                tileState
                            )
                            ++count
                        }
                    }

                    FillMode.KEEP -> {
                        blocks = Utils.getLevelBlocks(level, aabb)
                        for (block in blocks) {
                            if (block.isAir) {
                                blockManager.setBlockStateAt(
                                    block.position.floorX,
                                    block.position.floorY,
                                    block.position.floorZ,
                                    tileState
                                )
                                ++count
                            }
                        }
                    }
                }
            }

            "replace" -> {
                val replaceBlock = list.getResult<Block>(5)
                replaceState = if (list.hasResult(6)) {
                    list.getResult(6)
                } else {
                    replaceBlock!!.properties.defaultState
                }
                blocks = Utils.getLevelBlocks(level, aabb)
                for (block in blocks) {
                    if (block.id == replaceBlock!!.id) {
                        blockManager.setBlockStateAt(
                            block.position.floorX,
                            block.position.floorY,
                            block.position.floorZ,
                            replaceState
                        )
                        ++count
                    }
                }
            }

            else -> {
                return 0
            }
        }

        if (count == 0) {
            log.addError("commands.fill.failed")
            return 0
        } else {
            blockManager.applySubChunkUpdate()
            log.addSuccess("commands.fill.success", count.toString())
            return 1
        }
    }

    private enum class FillMode {
        REPLACE,
        OUTLINE,
        HOLLOW,
        DESTROY,
        KEEP
    }
}
