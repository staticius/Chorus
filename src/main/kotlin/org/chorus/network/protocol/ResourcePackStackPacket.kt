package org.chorus.network.protocol

import org.chorus.entity.Attribute.getName
import org.chorus.nbt.tag.ListTag.size
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.resourcepacks.ResourcePack
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.*






class ResourcePackStackPacket : DataPacket() {
    var mustAccept: Boolean = false
    var behaviourPackStack: Array<ResourcePack> = ResourcePack.EMPTY_ARRAY
    var resourcePackStack: Array<ResourcePack> = ResourcePack.EMPTY_ARRAY
    val experiments: List<ExperimentData> = ObjectArrayList()
    var gameVersion: String = "*"
    var isHasEditorPacks: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(this.mustAccept)

        byteBuf.writeUnsignedVarInt(behaviourPackStack.size)
        for (entry in this.behaviourPackStack) {
            byteBuf.writeString(entry.packId.toString())
            byteBuf.writeString(entry.packVersion)
            byteBuf.writeString("") //TODO: subpack name
        }

        byteBuf.writeUnsignedVarInt(resourcePackStack.size)
        for (entry in this.resourcePackStack) {
            byteBuf.writeString(entry.packId.toString())
            byteBuf.writeString(entry.packVersion)
            byteBuf.writeString("") //TODO: subpack name
        }

        byteBuf.writeString(this.gameVersion)
        byteBuf.writeIntLE(experiments.size()) // Experiments length
        for (experimentData in this.experiments) {
            byteBuf.writeString(experimentData.getName())
            byteBuf.writeBoolean(experimentData.isEnabled())
        }
        byteBuf.writeBoolean(true) // Were experiments previously toggled
        byteBuf.writeBoolean(isHasEditorPacks)
    }

    
    class ExperimentData {
        var name: String? = null
        var enabled: Boolean = false
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.RESOURCE_PACK_STACK_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
