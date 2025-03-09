package org.chorus.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.block.Block
import cn.nukkit.block.BlockID
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntityCommandBlock
import cn.nukkit.blockentity.ICommandBlock
import cn.nukkit.level.Level
import cn.nukkit.math.Vector3
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.CommandBlockUpdatePacket
import cn.nukkit.network.protocol.ProtocolInfo
import java.util.function.Consumer

class CommandBlockUpdateProcessor : DataPacketProcessor<CommandBlockUpdatePacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: CommandBlockUpdatePacket) {
        if (playerHandle.player.isOp && playerHandle.player.isCreative) {
            if (pk.isBlock) {
                val blockEntity = playerHandle.player.level!!.getBlockEntity(
                    Vector3(
                        pk.x.toDouble(),
                        pk.y.toDouble(),
                        pk.z.toDouble()
                    )
                )
                if (blockEntity is BlockEntityCommandBlock) {
                    var cmdBlock = blockEntity.levelBlock

                    //change commandblock type
                    when (pk.commandBlockMode) {
                        ICommandBlock.MODE_REPEATING -> if (cmdBlock!!.id !== BlockID.REPEATING_COMMAND_BLOCK) {
                            cmdBlock = Block.get(BlockID.REPEATING_COMMAND_BLOCK).setPropertyValues(
                                cmdBlock!!.propertyValues
                            )
                            blockEntity.scheduleUpdate()
                        }

                        ICommandBlock.MODE_CHAIN -> if (cmdBlock!!.id !== BlockID.CHAIN_COMMAND_BLOCK) {
                            cmdBlock =
                                Block.get(BlockID.CHAIN_COMMAND_BLOCK).setPropertyValues(cmdBlock!!.propertyValues)
                        }

                        ICommandBlock.MODE_NORMAL -> if (cmdBlock!!.id !== BlockID.COMMAND_BLOCK) {
                            cmdBlock = Block.get(BlockID.COMMAND_BLOCK).setPropertyValues(cmdBlock!!.propertyValues)
                        }

                        else -> if (cmdBlock!!.id !== BlockID.COMMAND_BLOCK) {
                            cmdBlock = Block.get(BlockID.COMMAND_BLOCK).setPropertyValues(cmdBlock!!.propertyValues)
                        }
                    }

                    val conditional = pk.isConditional
                    cmdBlock!!.setPropertyValue(CommonBlockProperties.CONDITIONAL_BIT, conditional)

                    blockEntity.setCommand(pk.command)
                    blockEntity.setName(pk.name)
                    blockEntity.setTrackOutput(pk.shouldTrackOutput)
                    blockEntity.isConditional = conditional
                    blockEntity.tickDelay = pk.tickDelay
                    blockEntity.isExecutingOnFirstTick = pk.executingOnFirstTick

                    //redstone mode / auto
                    val isRedstoneMode = pk.isRedstoneMode
                    blockEntity.isAuto = !isRedstoneMode
                    if (!isRedstoneMode && pk.commandBlockMode == ICommandBlock.MODE_NORMAL) {
                        blockEntity.trigger()
                    }
                    blockEntity.levelBlockAround.forEach(Consumer { b: Block -> b.onUpdate(Level.BLOCK_UPDATE_REDSTONE) }) //update redstone
                    playerHandle.player.level!!.setBlock(blockEntity.position, cmdBlock, true)
                }
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.COMMAND_BLOCK_UPDATE_PACKET
}
