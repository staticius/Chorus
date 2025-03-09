package org.chorus.command.defaults

import org.chorus.block.Block
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.level.Locator
import org.chorus.math.*
import org.chorus.utils.Utils
import kotlin.math.max
import kotlin.math.min

class CloneCommand(name: String) : VanillaCommand(name, "commands.clone.description") {
    init {
        this.permission = "nukkit.command.clone"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newType("begin", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newType("end", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newType("destination", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newEnum("maskMode", true, arrayOf<String?>("masked", "replace")),
                CommandParameter.Companion.newEnum("cloneMode", true, arrayOf<String?>("force", "move", "normal"))
            )
        )
        this.addCommandParameters(
            "filtered", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newType("begin", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newType("end", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newType("destination", false, CommandParamType.BLOCK_POSITION),
                CommandParameter.Companion.newEnum("maskMode", false, arrayOf<String?>("filtered")),
                CommandParameter.Companion.newEnum("cloneMode", false, arrayOf<String?>("force", "move", "normal")),
                CommandParameter.Companion.newType("tileId", false, CommandParamType.INT),
                CommandParameter.Companion.newType("tileData", false, CommandParamType.INT)
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
        val begin = list!!.getResult<Locator>(0)
        val end = list.getResult<Locator>(1)
        val destination = list.getResult<Locator>(2)
        var maskMode = MaskMode.REPLACE
        var cloneMode = CloneMode.NORMAL
        var tileId: String? = BlockID.AIR
        var tileData = 0
        when (result.key) {
            "default" -> {
                if (list.hasResult(3)) {
                    val str = list.getResult<String>(3)
                    maskMode = MaskMode.valueOf(str!!.uppercase())
                }
                if (list.hasResult(4)) {
                    val str = list.getResult<String>(4)
                    cloneMode = CloneMode.valueOf(str!!.uppercase())
                }
            }

            "filtered" -> {
                maskMode = MaskMode.FILTERED
                val str = list.getResult<String>(4)
                cloneMode = CloneMode.valueOf(str!!.uppercase())
                tileId = list.getResult(5)
                tileData = list.getResult(6)!!
            }

            else -> {
                return 0
            }
        }
        val blocksAABB: AxisAlignedBB = SimpleAxisAlignedBB(
            min(begin!!.x, end!!.x), min(
                begin.y, end.y
            ), min(begin.z, end.z), max(begin.x, end.x), max(
                begin.y, end.y
            ), max(begin.z, end.z)
        )
        val size =
            NukkitMath.floorDouble((blocksAABB.maxX - blocksAABB.minX + 1) * (blocksAABB.maxY - blocksAABB.minY + 1) * (blocksAABB.maxZ - blocksAABB.minZ + 1))

        if (size > 16 * 16 * 256 * 8) {
            log.addError("commands.clone.tooManyBlocks", size.toString(), (16 * 16 * 256 * 8).toString())
            log.addError("Operation will continue, but too many blocks may cause stuttering")
            log.output()
        }

        val to = Vector3(
            destination!!.x + (blocksAABB.maxX - blocksAABB.minX),
            destination.y + (blocksAABB.maxY - blocksAABB.minY),
            destination.z + (blocksAABB.maxZ - blocksAABB.minZ)
        )
        val destinationAABB: AxisAlignedBB = SimpleAxisAlignedBB(
            min(destination.x, to.getX()), min(
                destination.y, to.getY()
            ), min(destination.z, to.getZ()), max(
                destination.x, to.getX()
            ), max(destination.y, to.getY()), max(
                destination.z, to.getZ()
            )
        )

        if (blocksAABB.minY < -64 || blocksAABB.maxY > 320 || destinationAABB.minY < -64 || destinationAABB.maxY > 320) {
            log.addOutOfWorld().output()
            return 0
        }
        if (blocksAABB.intersectsWith(destinationAABB) && cloneMode != CloneMode.FORCE) {
            log.addError("commands.clone.noOverlap").output()
            return 0
        }

        val level = begin.level

        var sourceChunkX = NukkitMath.floorDouble(blocksAABB.minX) shr 4
        var destinationChunkX = NukkitMath.floorDouble(destinationAABB.minX) shr 4
        while (sourceChunkX <= NukkitMath.floorDouble(blocksAABB.maxX) shr 4) {
            var sourceChunkZ = NukkitMath.floorDouble(blocksAABB.minZ) shr 4
            var destinationChunkZ = NukkitMath.floorDouble(destinationAABB.minZ) shr 4
            while (sourceChunkZ <= NukkitMath.floorDouble(blocksAABB.maxZ) shr 4) {
                if (level.getChunkIfLoaded(sourceChunkX, sourceChunkZ) == null) {
                    log.addOutOfWorld().output()
                    return 0
                }
                if (level.getChunkIfLoaded(destinationChunkX, destinationChunkZ) == null) {
                    log.addOutOfWorld().output()
                    return 0
                }
                sourceChunkZ++
                destinationChunkZ++
            }
            sourceChunkX++
            destinationChunkX++
        }

        val blocks = Utils.getLevelBlocks(level, blocksAABB)
        val destinationBlocks = Utils.getLevelBlocks(level, destinationAABB)
        var count = 0

        val move = cloneMode == CloneMode.MOVE
        when (maskMode) {
            MaskMode.REPLACE -> {
                for (i in blocks.indices) {
                    val block = blocks[i]
                    val destinationBlock = destinationBlocks[i]

                    block.cloneTo(destinationBlock)

                    ++count

                    if (move) {
                        level.setBlock(block.position, Block.get(Block.AIR))
                    }
                }
            }

            MaskMode.MASKED -> {
                for (i in blocks.indices) {
                    val block = blocks[i]
                    val destinationBlock = destinationBlocks[i]

                    if (block.id !== Block.AIR) {
                        block.cloneTo(destinationBlock)
                        ++count

                        if (move) {
                            level.setBlock(block.position, Block.get(Block.AIR))
                        }
                    }
                }
            }

            MaskMode.FILTERED -> {
                for (i in blocks.indices) {
                    val block = blocks[i]
                    val destinationBlock = destinationBlocks[i]

                    if (block.id === tileId && block.blockState.specialValue().toInt() == tileData) {
                        block.cloneTo(destinationBlock)
                        ++count

                        if (move) {
                            level.setBlock(block.position, Block.get(Block.AIR))
                        }
                    }
                }
            }
        }

        if (count == 0) {
            log.addError("commands.clone.failed").output()
        } else {
            log.addSuccess("commands.clone.success", count.toString()).output()
        }
        return 1
    }

    private enum class MaskMode {
        REPLACE,
        MASKED,
        FILTERED
    }

    private enum class CloneMode {
        NORMAL,
        FORCE,
        MOVE
    }
}
