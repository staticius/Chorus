package cn.nukkit.blockentity

import cn.nukkit.Player
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.BlockEntityDataPacket

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class BlockEntitySpawnable(chunk: IChunk, nbt: CompoundTag) : BlockEntity(chunk, nbt) {
    override fun initBlockEntity() {
        super.initBlockEntity()
        this.spawnToAll()
    }

    open val spawnCompound: CompoundTag
        get() = CompoundTag()
            .putString(
                BlockEntity.Companion.TAG_ID,
                namedTag.getString(BlockEntity.Companion.TAG_ID)
            )
            .putInt(BlockEntity.Companion.TAG_X, floorX)
            .putInt(BlockEntity.Companion.TAG_Y, floorY)
            .putInt(BlockEntity.Companion.TAG_Z, floorZ)

    fun spawnTo(player: Player) {
        if (this.closed) {
            return
        }

        player.dataPacket(spawnPacket)
    }

    val spawnPacket: BlockEntityDataPacket
        get() = getSpawnPacket(null)

    fun getSpawnPacket(nbt: CompoundTag?): BlockEntityDataPacket {
        var nbt = nbt
        if (nbt == null) {
            nbt = this.spawnCompound
        }

        val pk = BlockEntityDataPacket()
        pk.x = position.floorX
        pk.y = position.floorY
        pk.z = position.floorZ
        pk.namedTag = nbt

        return pk
    }

    open fun spawnToAll() {
        if (this.closed) {
            return
        }

        for (player in level.getChunkPlayers(
            chunk!!.x,
            chunk!!.z
        ).values) {
            if (player.spawned) {
                this.spawnTo(player)
            }
        }
    }

    /**
     * Called when a player updates a block entity's NBT data
     * for example when writing on a sign.
     *
     * @param nbt    tag
     * @param player player
     * @return bool indication of success, will respawn the tile to the player if false.
     */
    open fun updateCompoundTag(nbt: CompoundTag, player: Player): Boolean {
        return false
    }
}
