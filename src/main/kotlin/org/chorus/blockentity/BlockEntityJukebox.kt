package org.chorus.blockentity

import org.chorus.block.Block
import org.chorus.item.Item
import org.chorus.item.ItemMusicDisc
import org.chorus.level.format.IChunk
import org.chorus.nbt.NBTIO
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.PlaySoundPacket
import org.chorus.network.protocol.StopSoundPacket
import java.util.*

/**
 * @author CreeperFace
 */
class BlockEntityJukebox(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt) {
    private var recordItem: Item? = null

    override fun loadNBT() {
        super.loadNBT()
        if (namedTag.contains("RecordItem")) {
            this.recordItem = NBTIO.getItemHelper(namedTag.getCompound("RecordItem"))
        } else {
            this.recordItem = Item.AIR
        }
    }

    override val isBlockEntityValid: Boolean
        get() = level
            .getBlockIdAt(floorX, floorY, floorZ) === Block.JUKEBOX

    fun setRecordItem(recordItem: Item) {
        Objects.requireNonNull(recordItem, "Record item cannot be null")
        this.recordItem = recordItem
    }

    fun getRecordItem(): Item? {
        return recordItem
    }


    fun play() {
        if (recordItem is ItemMusicDisc) {
            val packet = PlaySoundPacket()
            packet.name = recordItem.soundId
            packet.volume = 1f
            packet.pitch = 1f
            packet.x = position.floorX
            packet.y = position.floorY
            packet.z = position.floorZ
            level.addChunkPacket(
                position.floorX shr 4,
                position.floorZ shr 4, packet
            )
        }
    }

    //TODO: Transfer the stop sound to the new sound method
    fun stop() {
        if (recordItem is ItemMusicDisc) {
            val packet = StopSoundPacket()
            packet.name = recordItem.soundId
            packet.stopAll = false
            level.addChunkPacket(
                position.floorX shr 4,
                position.floorZ shr 4, packet
            )
        }
    }

    fun dropItem() {
        if (!recordItem!!.isNull) {
            stop()
            level.dropItem(position.up(), this.recordItem)
            this.recordItem = Item.AIR
        }
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag.putCompound("RecordItem", NBTIO.putItemHelper(this.recordItem))
    }

    override val spawnCompound: CompoundTag
        get() = super.spawnCompound
            .putCompound("RecordItem", NBTIO.putItemHelper(this.recordItem))

    override fun onBreak(isSilkTouch: Boolean) {
        this.dropItem()
    }
}
