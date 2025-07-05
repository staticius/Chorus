package org.chorus_oss.chorus.network.process.handler

import kotlinx.io.bytestring.ByteString
import org.chorus_oss.chorus.experimental.network.MigrationPacket
import org.chorus_oss.chorus.experimental.network.protocol.utils.invoke
import org.chorus_oss.chorus.network.connection.BedrockSession
import org.chorus_oss.chorus.network.process.SessionState
import org.chorus_oss.chorus.network.protocol.ResourcePackClientResponsePacket
import org.chorus_oss.chorus.network.protocol.ResourcePackStackPacket
import org.chorus_oss.chorus.network.protocol.ResourcePackStackPacket.ExperimentData
import org.chorus_oss.chorus.utils.Loggable
import org.chorus_oss.protocol.packets.ResourcePackChunkDataPacket
import org.chorus_oss.protocol.packets.ResourcePackDataInfoPacket
import org.chorus_oss.protocol.types.TexturePackInfo
import java.util.*
import kotlin.math.ceil
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
class ResourcePackHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    init {
        val infoPacket = org.chorus_oss.protocol.packets.ResourcePacksInfoPacket(
            texturePackRequired = session.server.forceResources,
            hasAddons = false,
            hasScripts = false,
            forceDisableVibrantVisuals = false,
            worldTemplateUuid = Uuid.random(),
            worldTemplateVersion = "",
            texturePacks = session.server.resourcePackManager.resourceStack.map(TexturePackInfo::invoke)
        )
        session.sendPacket(infoPacket)
    }

    override fun handle(pk: ResourcePackClientResponsePacket) {
        val server = session.server
        when (pk.responseStatus) {
            ResourcePackClientResponsePacket.STATUS_REFUSED -> {
                log.debug("ResourcePackClientResponsePacket STATUS_REFUSED")
                session.close("disconnectionScreen.noReason")
            }

            ResourcePackClientResponsePacket.STATUS_SEND_PACKS -> {
                log.debug("ResourcePackClientResponsePacket STATUS_SEND_PACKS")
                for (entry in pk.packEntries) {
                    val resourcePack = server.resourcePackManager.getPackById(entry.uuid)
                    if (resourcePack == null) {
                        session.close("disconnectionScreen.resourcePack")
                        return
                    }

                    val dataInfoPacket = ResourcePackDataInfoPacket(
                        resourceName = "${resourcePack.packId}_${resourcePack.packVersion}",
                        chunkSize = server.resourcePackManager.maxChunkSize.toUInt(),
                        chunkCount = ceil(resourcePack.packSize / server.resourcePackManager.maxChunkSize.toDouble()).toUInt(),
                        fileSize = resourcePack.packSize.toULong(),
                        fileHash = ByteString(resourcePack.sha256),
                        premium = false,
                        type = ResourcePackDataInfoPacket.Companion.Type.Resource
                    )
                    session.sendPacket(dataInfoPacket)
                }
            }

            ResourcePackClientResponsePacket.STATUS_HAVE_ALL_PACKS -> {
                log.debug("ResourcePackClientResponsePacket STATUS_HAVE_ALL_PACKS")
                val stackPacket = ResourcePackStackPacket()
                stackPacket.mustAccept = server.forceResources && !server.forceResourcesAllowOwnPacks
                stackPacket.resourcePackStack = server.resourcePackManager.resourceStack
                stackPacket.experiments.add(
                    ExperimentData("data_driven_items", true)
                )
                stackPacket.experiments.add(
                    ExperimentData("data_driven_biomes", true)
                )
                stackPacket.experiments.add(
                    ExperimentData("upcoming_creator_features", true)
                )
                stackPacket.experiments.add(
                    ExperimentData("gametest", true)
                )
                stackPacket.experiments.add(
                    ExperimentData("experimental_molang_features", true)
                )
                stackPacket.experiments.add(
                    ExperimentData("cameras", true)
                )
                session.sendPacket(stackPacket)
            }

            ResourcePackClientResponsePacket.STATUS_COMPLETED -> {
                log.debug("ResourcePackClientResponsePacket STATUS_COMPLETED")
                session.machine.fire(SessionState.PRE_SPAWN)
            }
        }
    }

    override fun handle(pk: MigrationPacket<*>) {
        val packet = pk.packet
        if (packet !is org.chorus_oss.protocol.packets.ResourcePackChunkRequestPacket) return

        // TODO: Pack version check
        val mgr = session.server.resourcePackManager
        val packID = UUID.fromString(packet.resourceName.split("_", limit = 2)[0])
        val resourcePack = mgr.getPackById(packID)
        if (resourcePack == null) {
            session.close("disconnectionScreen.resourcePack")
            return
        }
        val maxChunkSize = mgr.maxChunkSize
        val dataPacket = ResourcePackChunkDataPacket(
            resourceName = "${resourcePack.packId}_${resourcePack.packVersion}",
            chunkID = packet.chunkID,
            byteOffset = (maxChunkSize.toUInt() * packet.chunkID).toULong(),
            chunkData = ByteString(resourcePack.getPackChunk(maxChunkSize * packet.chunkID.toInt(), maxChunkSize)),
        )
        session.sendPacket(dataPacket)
    }

    companion object : Loggable
}
