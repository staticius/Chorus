package org.chorus.network.protocol

import org.chorus.block.property.enums.StructureBlockType
import org.chorus.math.BlockVector3
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.*


class StructureBlockUpdatePacket : DataPacket() {
    var blockPosition: BlockVector3? = null
    var editorData: StructureEditorData? = null
    var powered: Boolean = false
    var waterlogged: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.blockPosition = byteBuf.readBlockVector3()
        this.editorData = readEditorData(byteBuf)
        this.powered = byteBuf.readBoolean()
        this.waterlogged = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBlockVector3(blockPosition!!)
        this.writeEditorData(byteBuf, editorData!!)
        byteBuf.writeBoolean(powered)
        byteBuf.writeBoolean(waterlogged)
    }

    private fun readEditorData(byteBuf: HandleByteBuf): StructureEditorData {
        val name = byteBuf.readString()
        val filteredName = byteBuf.readString()
        val dataField = byteBuf.readString()
        val isIncludingPlayers = byteBuf.readBoolean()
        val isBoundingBoxVisible = byteBuf.readBoolean()
        val type = byteBuf.readVarInt()
        val structureSettings = readStructureSettings(byteBuf)
        val redstoneSaveMode = byteBuf.readVarInt()
        return StructureEditorData(
            name,
            filteredName,
            dataField,
            isIncludingPlayers,
            isBoundingBoxVisible,
            StructureBlockType.from(type),
            structureSettings,
            StructureRedstoneSaveMode.Companion.from(redstoneSaveMode)
        )
    }

    private fun readStructureSettings(byteBuf: HandleByteBuf): StructureSettings {
        val paletteName = byteBuf.readString()
        val isIgnoringEntities = byteBuf.readBoolean()
        val isIgnoringBlocks = byteBuf.readBoolean()
        val isNonTickingPlayersAndTickingAreasEnabled = byteBuf.readBoolean()
        val size = byteBuf.readBlockVector3()
        val offset = byteBuf.readBlockVector3()
        val lastEditedByEntityId = byteBuf.readVarLong()
        val rotation = byteBuf.readByte()
        val mirror = byteBuf.readByte()
        val animationMode = byteBuf.readByte()
        val animationSeconds = byteBuf.readFloatLE()
        val integrityValue = byteBuf.readFloatLE()
        val integritySeed = byteBuf.readIntLE()
        val pivot = byteBuf.readVector3f()
        return StructureSettings(
            paletteName,
            isIgnoringEntities,
            isIgnoringBlocks,
            isNonTickingPlayersAndTickingAreasEnabled,
            size,
            offset,
            lastEditedByEntityId,
            StructureRotation.Companion.from(rotation.toInt()),
            StructureMirror.Companion.from(mirror.toInt()),
            StructureAnimationMode.Companion.from(animationMode.toInt()),
            animationSeconds,
            integrityValue,
            integritySeed,
            pivot
        )
    }

    private fun writeEditorData(byteBuf: HandleByteBuf, editorData: StructureEditorData) {
        byteBuf.writeString(editorData.name)
        byteBuf.writeString(editorData.filteredName)
        byteBuf.writeString(editorData.dataField)
        byteBuf.writeBoolean(editorData.isIncludingPlayers)
        byteBuf.writeBoolean(editorData.isBoundingBoxVisible)
        byteBuf.writeVarInt(editorData.type.ordinal())
        writeStructureSettings(byteBuf, editorData.settings)
        byteBuf.writeVarInt(editorData.redstoneSaveMode.ordinal())
    }

    private fun writeStructureSettings(byteBuf: HandleByteBuf, settings: StructureSettings) {
        byteBuf.writeString(settings.paletteName)
        byteBuf.writeBoolean(settings.isIgnoringEntities)
        byteBuf.writeBoolean(settings.isIgnoringBlocks)
        byteBuf.writeBoolean(settings.isNonTickingPlayersAndTickingAreasEnabled)
        byteBuf.writeBlockVector3(settings.size)
        byteBuf.writeBlockVector3(settings.offset)
        byteBuf.writeVarLong(settings.lastEditedByEntityId)
        byteBuf.writeByte(settings.rotation.ordinal().toByte().toInt())
        byteBuf.writeByte(settings.mirror.ordinal().toByte().toInt())
        byteBuf.writeByte(settings.animationMode.ordinal().toByte().toInt())
        byteBuf.writeFloatLE(settings.animationSeconds)
        byteBuf.writeFloatLE(settings.integrityValue)
        byteBuf.writeIntLE(settings.integritySeed)
        byteBuf.writeVector3f(settings.pivot)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.STRUCTURE_BLOCK_UPDATE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
