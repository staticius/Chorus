package org.chorus.inventory.fake

import org.chorus.Player
import org.chorus.block.BlockStructureBlock
import org.chorus.blockentity.*
import org.chorus.math.BlockVector3
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.BlockActorDataPacket
import org.chorus.network.protocol.UpdateBlockPacket
import java.util.function.Consumer

class FakeStructBlock : SingleFakeBlock(BlockStructureBlock(), BlockEntity.STRUCTURE_BLOCK) {
    override fun getOffset(player: Player): Vector3 {
        val floorX = player.position.floorX
        val floorZ = player.position.floorZ
        return Vector3(floorX.toDouble(), (player.level!!.minHeight + 1).toDouble(), floorZ.toDouble())
    }

    override fun create(player: Player) {
        val pos: BlockVector3 = player.getLocator().position.floor().asBlockVector3()
        create(pos, pos, player)
    }

    fun create(targetStart: BlockVector3, targetEnd: BlockVector3, player: Player) {
        createAndGetLastPositions(player).add(this.getOffset(player))
        lastPositions[player]!!.forEach(Consumer { position ->
            val updateBlockPacket = UpdateBlockPacket()
            updateBlockPacket.blockRuntimeId = block.runtimeId
            updateBlockPacket.flags = UpdateBlockPacket.FLAG_NETWORK
            updateBlockPacket.x = position.floorX
            updateBlockPacket.y = position.floorY
            updateBlockPacket.z = position.floorZ
            player.dataPacket(updateBlockPacket)

            val blockActorDataPacket = BlockActorDataPacket(
                blockPosition = position.asBlockVector3(),
                actorDataTags = this.getBlockEntityDataAt(position, targetStart, targetEnd)
            )
            player.dataPacket(blockActorDataPacket)
        })
    }

    override fun remove(player: Player) {
        lastPositions.getOrDefault(player, HashSet()).forEach(Consumer { position: Vector3 ->
            val packet = UpdateBlockPacket()
            packet.blockRuntimeId = player.level!!.getBlock(position).runtimeId
            packet.flags = UpdateBlockPacket.FLAG_NETWORK
            packet.x = position.floorX
            packet.y = position.floorY
            packet.z = position.floorZ
            player.dataPacket(packet)
        })
        lastPositions.clear()
    }

    private fun getBlockEntityDataAt(base: Vector3, targetStart: BlockVector3, targetEnd: BlockVector3): CompoundTag {
        var offsetX = targetStart.x - base.floorX
        var offsetY = targetStart.y - base.floorY
        var offsetZ = targetStart.z - base.floorZ
        val sizeX: Int
        val sizeY: Int
        val sizeZ: Int
        if (targetEnd.x - targetStart.x < 0) {
            offsetX += targetEnd.x - targetStart.x
            sizeX = targetStart.x - targetEnd.x + 1
        } else {
            sizeX = targetEnd.x - targetStart.x + 1
        }
        if (targetEnd.y - targetStart.y < 0) {
            offsetY += targetEnd.y - targetStart.y
            sizeY = targetStart.y - targetEnd.y + 1
        } else {
            sizeY = targetEnd.y - targetStart.y + 1
        }
        if (targetEnd.z - targetStart.z < 0) {
            offsetZ += targetEnd.z - targetStart.z
            sizeZ = targetStart.z - targetEnd.z + 1
        } else {
            sizeZ = targetEnd.z - targetStart.z + 1
        }
        return CompoundTag()
            .putString("id", tileId!!)
            .putInt("x", base.floorX)
            .putInt("y", base.floorY)
            .putInt("z", base.floorZ)
            .putBoolean(IStructBlock.TAG_SHOW_BOUNDING_BOX, true)
            .putInt(IStructBlock.TAG_X_STRUCTURE_OFFSET, offsetX)
            .putInt(IStructBlock.TAG_Y_STRUCTURE_OFFSET, offsetY)
            .putInt(IStructBlock.TAG_Z_STRUCTURE_OFFSET, offsetZ)
            .putInt(IStructBlock.TAG_X_STRUCTURE_SIZE, sizeX)
            .putInt(IStructBlock.TAG_Y_STRUCTURE_SIZE, sizeY)
            .putInt(IStructBlock.TAG_Z_STRUCTURE_SIZE, sizeZ)
    }
}
