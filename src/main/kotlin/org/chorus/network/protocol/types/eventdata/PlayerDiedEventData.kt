package org.chorus.network.protocol.types.eventdata

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.EventData
import cn.nukkit.network.protocol.types.EventDataType
import lombok.Value

/**
 * Used to control that pop up on the player's respawn screen
 */
@Value
class PlayerDiedEventData : EventData {
    private val attackerEntityId = 0
    private val attackerVariant = 0
    private val entityDamageCause = 0
    private val inRaid = false

    override val type: EventDataType
        get() = EventDataType.PLAYER_DIED

    override fun write(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(attackerEntityId)
        byteBuf.writeVarInt(attackerVariant)
        byteBuf.writeVarInt(entityDamageCause)
        byteBuf.writeBoolean(inRaid)
    }
}