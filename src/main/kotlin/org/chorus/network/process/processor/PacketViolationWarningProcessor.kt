package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.entity.EntityHuman.getName
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.PacketViolationWarningPacket
import org.chorus.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j
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
            playerHandle.player.getName(),
            packetName.map<String> { name: String -> " for packet $name" }
                .orElse("") + ": " + pk)
    }

    override val packetId: Int
        get() = ProtocolInfo.PACKET_VIOLATION_WARNING_PACKET
}
