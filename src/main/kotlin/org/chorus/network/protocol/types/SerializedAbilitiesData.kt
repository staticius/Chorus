package org.chorus.network.protocol.types

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.UpdateAbilitiesPacket

data class SerializedAbilitiesData(
    val targetPlayerRawID: ActorUniqueID,
    val playerPermissions: PlayerPermission,
    val commandPermissions: CommandPermission,
    val layers: Array<AbilityLayer>
) {
    fun write(byteBuf: HandleByteBuf) {
        byteBuf.writeActorUniqueID(this.targetPlayerRawID)
        byteBuf.writeByte(this.playerPermissions.ordinal)
        byteBuf.writeByte(this.commandPermissions.ordinal)
        byteBuf.writeArray(this.layers) {
            UpdateAbilitiesPacket.writeAbilityLayer(byteBuf, it)
        }
    }
}
