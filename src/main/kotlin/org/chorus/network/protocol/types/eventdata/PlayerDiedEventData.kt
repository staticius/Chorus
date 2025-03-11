package org.chorus.network.protocol.types.eventdata

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.EventData
import org.chorus.network.protocol.types.EventDataType


/**
 * Used to control that pop up on the player's respawn screen
 */

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