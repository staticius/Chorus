package org.chorus.network.protocol

import org.chorus.Player
import org.chorus.Server
import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.PlayerAbility
import org.chorus.network.protocol.types.PlayerPermission


class RequestPermissionsPacket : DataPacket() {
    var uniqueEntityId: Long = 0
    var permissions: PlayerPermission? = null

    //Serialized capability list
    //It is an 8-bit binary number, each bit corresponds to an ability
    var customPermissions: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
        this.uniqueEntityId = byteBuf.readLongLE()
        this.permissions = PlayerPermission.entries[byteBuf.readByte() / 2]
        this.customPermissions = byteBuf.readShortLE().toInt()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        throw UnsupportedOperationException()
    }

    fun parseCustomPermissions(): Set<PlayerAbility> {
        val abilities = HashSet<PlayerAbility>()
        for (controllableAbility in CONTROLLABLE_ABILITIES) {
            if ((this.customPermissions and controllableAbility.bit) != 0) abilities.add(controllableAbility)
        }
        return abilities
    }

    val targetPlayer: Player?
        get() {
            for (player in Server.instance.onlinePlayers.values()) {
                if (player.getId() == this.uniqueEntityId) return player
            }
            return null
        }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REQUEST_PERMISSIONS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        //Controllable capabilities in the permission list
        @JvmField
        val CONTROLLABLE_ABILITIES: Array<PlayerAbility> = arrayOf(
            PlayerAbility.BUILD,
            PlayerAbility.MINE,
            PlayerAbility.DOORS_AND_SWITCHES,
            PlayerAbility.OPEN_CONTAINERS,
            PlayerAbility.ATTACK_PLAYERS,
            PlayerAbility.ATTACK_MOBS,
            PlayerAbility.OPERATOR_COMMANDS,
            PlayerAbility.TELEPORT
        )
    }
}
