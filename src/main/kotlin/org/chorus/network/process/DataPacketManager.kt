package org.chorus.network.process

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.chorus.PlayerHandle
import org.chorus.entity.EntityHuman.getName
import org.chorus.network.process.processor.*
import org.chorus.network.protocol.DataPacket

/**
 * DataPacketManager is a static class to manage DataPacketProcessors and process DataPackets.
 */
class DataPacketManager {
    private val PROCESSORS = Int2ObjectOpenHashMap<DataPacketProcessor<*>>(300)

    init {
        registerDefaultProcessors()
    }

    fun registerProcessor(vararg processors: DataPacketProcessor<*>) {
        for (processor in processors) {
            PROCESSORS.put(processor.packetId, processor)
        }
        PROCESSORS.trim()
    }

    fun canProcess(packetId: Int): Boolean {
        return PROCESSORS.containsKey(packetId)
    }

    fun processPacket(playerHandle: PlayerHandle, packet: DataPacket) {
        val processor = PROCESSORS[packet.pid()]
        if (processor != null) {
            processor.handle(playerHandle, packet)
        } else {
            throw UnsupportedOperationException(
                "No processor found for packet " + packet.getClass().getName() + " with id " + packet.pid() + "."
            )
        }
    }

    fun registerDefaultProcessors() {
        registerProcessor(
            LoginProcessor(),
            InventoryTransactionProcessor(),
            PlayerSkinProcessor(),
            PacketViolationWarningProcessor(),
            EmoteProcessor(),
            PlayerInputProcessor(),
            MovePlayerProcessor(),
            PlayerAuthInputProcessor(),
            RequestAbilityProcessor(),
            MobEquipmentProcessor(),
            PlayerActionProcessor(),
            ModalFormResponseProcessor(),
            NPCRequestProcessor(),
            InteractProcessor(),
            BlockPickRequestProcessor(),
            AnimateProcessor(),
            EntityEventProcessor(),
            CommandRequestProcessor(),
            CommandBlockUpdateProcessor(),
            StructureBlockUpdateProcessor(),
            TextProcessor(),
            ContainerCloseProcessor(),
            CraftingEventProcessor(),
            BlockEntityDataProcessor(),
            SetPlayerGameTypeProcessor(),
            LecternUpdateProcessor(),
            MapInfoRequestProcessor(),  /*
                 * Minecraft doesn't really use this packet any more, and the client can send it and play the music it wants,
                 * even though it's of no interest to the gameplay.
                 * The client isn't supposed to be able to broadcast a sound to the whole server.
                 * @Zwuiix
                new LevelSoundEventProcessor(),
                new LevelSoundEventProcessorV1(),
                new LevelSoundEventProcessorV2(),
                */
            //new PlayerHotbarProcessor(),
            ServerSettingsRequestProcessor(),
            RespawnProcessor(),
            BookEditProcessor(),
            SetDifficultyProcessor(),
            SettingsCommandProcessor(),
            PositionTrackingDBClientRequestProcessor(),
            ShowCreditsProcessor(),
            TickSyncProcessor(),
            RequestPermissionsProcessor(),
            RiderJumpProcessor(),
            ItemStackRequestPacketProcessor(),
            SetLocalPlayerAsInitializedPacketProcessor()
        )
    }
}
