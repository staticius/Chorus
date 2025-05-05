package org.chorus_oss.chorus.network.process.processor

import org.chorus_oss.chorus.PlayerHandle
import org.chorus_oss.chorus.network.process.DataPacketProcessor
import org.chorus_oss.chorus.network.protocol.PacketViolationWarningPacket
import org.chorus_oss.chorus.network.protocol.ProtocolInfo
import org.chorus_oss.chorus.utils.Loggable
import java.lang.reflect.Field
import java.util.*


class PacketViolationWarningProcessor : DataPacketProcessor<PacketViolationWarningPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: PacketViolationWarningPacket) {
        val packetName = Arrays.stream(
            ProtocolInfo::class.java.declaredFields
        )
            .filter { field: Field -> field.type == java.lang.Byte.TYPE }
            .filter { field: Field ->
                try {
                    return@filter field.getByte(null).toInt() == pk.packetId
                } catch (e: IllegalAccessException) {
                    return@filter false
                }
            }.map { obj: Field -> obj.name }.findFirst()
        PacketViolationWarningProcessor.log.warn(
            "Violation warning from {}{}",
            playerHandle.player.getEntityName(),
            packetName.map<String> { name: String -> " for packet $name" }
                .orElse("") + ": " + pk)
    }

    override val packetId: Int
        get() = ProtocolInfo.PACKET_VIOLATION_WARNING_PACKET

    companion object : Loggable
}
