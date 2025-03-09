package org.chorus.network.process.handler

import org.chorus.network.connection.BedrockSession
import org.chorus.network.process.SessionState
import org.chorus.network.protocol.*
import org.chorus.network.protocol.ResourcePackStackPacket.ExperimentData
import org.chorus.utils.version.Version
import lombok.extern.slf4j.Slf4j
import java.util.*
import kotlin.math.ceil


class ResourcePackHandler(session: BedrockSession) : BedrockSessionPacketHandler(session) {
    init {
        val infoPacket = ResourcePacksInfoPacket()
        infoPacket.resourcePackEntries = session.server.resourcePackManager.resourceStack
        infoPacket.isForcedToAccept = session.server.forceResources
        infoPacket.worldTemplateId = UUID.randomUUID()
        infoPacket.worldTemplateVersion = ""
        session.sendPacket(infoPacket)
    }

    override fun handle(pk: ResourcePackClientResponsePacket) {
        val server = session.server
        when (pk.responseStatus) {
            ResourcePackClientResponsePacket.STATUS_REFUSED -> {
                ResourcePackHandler.log.debug("ResourcePackClientResponsePacket STATUS_REFUSED")
                session.close("disconnectionScreen.noReason")
            }

            ResourcePackClientResponsePacket.STATUS_SEND_PACKS -> {
                ResourcePackHandler.log.debug("ResourcePackClientResponsePacket STATUS_SEND_PACKS")
                for (entry in pk.packEntries) {
                    val resourcePack = server.resourcePackManager.getPackById(entry.uuid)
                    if (resourcePack == null) {
                        session.close("disconnectionScreen.resourcePack")
                        return
                    }

                    val dataInfoPacket = ResourcePackDataInfoPacket()
                    dataInfoPacket.packId = resourcePack.packId
                    dataInfoPacket.packVersion = Version(resourcePack.packVersion)
                    dataInfoPacket.maxChunkSize = server.resourcePackManager.maxChunkSize
                    dataInfoPacket.chunkCount =
                        ceil(resourcePack.packSize / dataInfoPacket.maxChunkSize.toDouble()).toInt()
                    dataInfoPacket.compressedPackSize = resourcePack.packSize.toLong()
                    dataInfoPacket.sha256 = resourcePack.sha256
                    session.sendPacket(dataInfoPacket)
                }
            }

            ResourcePackClientResponsePacket.STATUS_HAVE_ALL_PACKS -> {
                ResourcePackHandler.log.debug("ResourcePackClientResponsePacket STATUS_HAVE_ALL_PACKS")
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
                ResourcePackHandler.log.debug("ResourcePackClientResponsePacket STATUS_COMPLETED")
                session.machine.fire(SessionState.PRE_SPAWN)
            }
        }
    }

    override fun handle(pk: ResourcePackChunkRequestPacket) {
        // TODO: Pack version check
        val mgr = session.server.resourcePackManager
        val resourcePack = mgr.getPackById(pk.packId)
        if (resourcePack == null) {
            session.close("disconnectionScreen.resourcePack")
            return
        }
        val maxChunkSize = mgr.maxChunkSize
        val dataPacket = ResourcePackChunkDataPacket()
        dataPacket.packId = resourcePack.packId
        dataPacket.packVersion = Version(resourcePack.packVersion)
        dataPacket.chunkIndex = pk.chunkIndex
        dataPacket.data = resourcePack.getPackChunk(maxChunkSize * pk.chunkIndex, maxChunkSize)
        dataPacket.progress = maxChunkSize * pk.chunkIndex.toLong()
        session.sendPacket(dataPacket)
    }
}
