package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.block.Block
import org.chorus_oss.chorus.block.BlockID
import org.chorus_oss.chorus.block.property.CommonBlockProperties
import org.chorus_oss.chorus.blockentity.BlockEntityCommandBlock
import org.chorus_oss.chorus.level.Level
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.CommandBlockUpdatePacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.types.CommandBlockMode
import java.util.function.Consumer

class CommandBlockUpdateProcessor : DataPacketProcessor<CommandBlockUpdatePacket>() {
    override fun handle(player: Player, pk: CommandBlockUpdatePacket) {
        if (player.player.isOp && player.player.isCreative) {
            if (pk.isBlock) {
                val commandBlockData = pk.commandBlockHolderData as CommandBlockUpdatePacket.CommandBlockData

                val blockEntity = player.player.level!!.getBlockEntity(
                    commandBlockData.blockPosition
                )
                if (blockEntity is BlockEntityCommandBlock) {
                    var cmdBlock = blockEntity.levelBlock

                    //change commandblock type
                    when (commandBlockData.commandBlockMode) {
                        CommandBlockMode.REPEATING -> if (cmdBlock.id !== BlockID.REPEATING_COMMAND_BLOCK) {
                            cmdBlock = Block.get(BlockID.REPEATING_COMMAND_BLOCK).setPropertyValues(
                                cmdBlock.propertyValues
                            )
                            blockEntity.scheduleUpdate()
                        }

                        CommandBlockMode.CHAIN -> if (cmdBlock.id !== BlockID.CHAIN_COMMAND_BLOCK) {
                            cmdBlock =
                                Block.get(BlockID.CHAIN_COMMAND_BLOCK).setPropertyValues(cmdBlock.propertyValues)
                        }

                        CommandBlockMode.NORMAL -> if (cmdBlock.id !== BlockID.COMMAND_BLOCK) {
                            cmdBlock = Block.get(BlockID.COMMAND_BLOCK).setPropertyValues(cmdBlock.propertyValues)
                        }
                    }

                    val conditional = commandBlockData.isConditional
                    cmdBlock.setPropertyValue(CommonBlockProperties.CONDITIONAL_BIT, conditional)

                    blockEntity.command = pk.command
                    blockEntity.senderName = (pk.name)
                    blockEntity.setTrackOutput(pk.trackOutput)
                    blockEntity.isConditional = conditional
                    blockEntity.tickDelay = pk.tickDelay
                    blockEntity.isExecutingOnFirstTick = pk.shouldExecuteOnFirstTick

                    //redstone mode / auto
                    val isRedstoneMode = commandBlockData.redstoneMode
                    blockEntity.isAuto = !isRedstoneMode
                    if (!isRedstoneMode && commandBlockData.commandBlockMode == CommandBlockMode.NORMAL) {
                        blockEntity.trigger()
                    }
                    blockEntity.levelBlockAround.forEach(Consumer { b: Block -> b.onUpdate(Level.BLOCK_UPDATE_REDSTONE) }) //update redstone
                    player.player.level!!.setBlock(blockEntity.position, cmdBlock, true)
                }
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.COMMAND_BLOCK_UPDATE_PACKET
}
