package org.chorus.inventory.fake

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.chorus.Player
import org.chorus.block.Block
import org.chorus.blockentity.BlockEntity.Companion.getDefaultCompound
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.BlockEntityDataPacket
import org.chorus.network.protocol.UpdateBlockPacket
import java.util.function.Consumer

open class SingleFakeBlock : FakeBlock {
    protected val block: Block
    protected val tileId: String?
    protected var lastPositions: Object2ObjectArrayMap<Player, HashSet<Vector3?>> = Object2ObjectArrayMap()

    constructor(blockId: String?) {
        this.block = Block.get(blockId)
        this.tileId = "default"
    }

    constructor(blockId: String?, tileId: String?) {
        this.block = Block.get(blockId)
        this.tileId = tileId
    }

    constructor(block: Block, tileId: String?) {
        this.block = block
        this.tileId = tileId
    }

    override fun create(player: Player) {
        create(player, "default")
    }

    override fun create(player: Player, titleName: String) {
        createAndGetLastPositions(player).addAll(this.getPlacePositions(player))
        lastPositions[player]!!.forEach(Consumer { position: Vector3 ->
            val updateBlockPacket = UpdateBlockPacket()
            updateBlockPacket.blockRuntimeId = block.runtimeId
            updateBlockPacket.flags = UpdateBlockPacket.FLAG_NETWORK
            updateBlockPacket.x = position.floorX
            updateBlockPacket.y = position.floorY
            updateBlockPacket.z = position.floorZ
            player.dataPacket(updateBlockPacket)

            val blockEntityDataPacket = BlockEntityDataPacket()
            blockEntityDataPacket.x = position.floorX
            blockEntityDataPacket.y = position.floorY
            blockEntityDataPacket.z = position.floorZ
            blockEntityDataPacket.namedTag = this.getBlockEntityDataAt(position, titleName)
            player.dataPacket(blockEntityDataPacket)
        })
    }

    override fun remove(player: Player) {
        getLastPositions(player).forEach(Consumer { position: Vector3? ->
            val packet = UpdateBlockPacket()
            packet.blockRuntimeId = player.level!!.getBlock(position).runtimeId
            packet.flags = UpdateBlockPacket.FLAG_NETWORK
            packet.x = position!!.floorX
            packet.y = position.floorY
            packet.z = position.floorZ
            player.dataPacket(packet)
        })
        lastPositions.remove(player)
    }

    protected open fun getBlockEntityDataAt(position: Vector3, title: String): CompoundTag {
        return getDefaultCompound(position, title)
            .putBoolean("isMovable", true)
            .putString("CustomName", title)
    }

    fun createAndGetLastPositions(player: Player): HashSet<Vector3?> {
        if (!lastPositions.containsKey(player)) lastPositions[player] = HashSet()
        return lastPositions[player]!!
    }

    override fun getLastPositions(player: Player): HashSet<Vector3?> {
        return lastPositions.getOrDefault(player, HashSet())
    }
}
