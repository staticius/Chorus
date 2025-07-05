package org.chorus_oss.chorus.registry

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.protocol.*
import java.util.concurrent.atomic.AtomicBoolean

class PacketDecoderRegistry : IRegistry<Int, PacketDecoder<out DataPacket>?, PacketDecoder<out DataPacket>> {
    private val packets = HashMap<Int, PacketDecoder<out DataPacket>>(256)

    override fun init() {
        if (isLoad.getAndSet(true)) return
        registerPackets()
    }

    override operator fun get(key: Int): PacketDecoder<out DataPacket>? {
        return packets[key]
    }

    override fun reload() {
        isLoad.set(false)
        packets.clear()
        init()
    }

    @Throws(RegisterException::class)
    override fun register(key: Int, value: PacketDecoder<*>) {
        try {
            if (packets.putIfAbsent(key, value) != null) {
                throw RegisterException("The packet has been registered!")
            }
        } catch (e: Exception) {
            throw RegisterException(e)
        }
    }

    private fun registerPackets() {
        packets.clear()

        // Register all packets that are Client -> Server

        this.register(ProtocolInfo.INVENTORY_TRANSACTION_PACKET, InventoryTransactionPacket) // 30
        this.register(ProtocolInfo.INTERACT_PACKET, InteractPacket) // 33
        this.register(ProtocolInfo.ANIMATE_PACKET, AnimatePacket) // 44

        this.register(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket)

        this.register(ProtocolInfo.LOGIN_PACKET, LoginPacket) // 1
        this.register(ProtocolInfo.MAP_INFO_REQUEST_PACKET, MapInfoRequestPacket) // 68
        this.register(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket) // 32
        this.register(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket) // 31
        this.register(ProtocolInfo.MODAL_FORM_RESPONSE_PACKET, ModalFormResponsePacket) // 101
        this.register(ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET, MoveEntityAbsolutePacket) // 18
        this.register(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket) // 19
        this.register(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket) // 36
        this.register(ProtocolInfo.PLAYER_HOTBAR_PACKET, PlayerHotbarPacket) // 48
        this.register(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket) // 69
        this.register(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket) // 8
        this.register(ProtocolInfo.PLAYER_SKIN_PACKET, PlayerSkinPacket) // 93
        this.register(ProtocolInfo.RESPAWN_PACKET, RespawnPacket) // 45
        this.register(ProtocolInfo.SET_DIFFICULTY_PACKET, SetDifficultyPacket) // 60
        this.register(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket) // 62
        this.register(ProtocolInfo.SET_TITLE_PACKET, SetTitlePacket) // 88
        this.register(ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET, ServerSettingsRequestPacket) // 102
        this.register(ProtocolInfo.SHOW_CREDITS_PACKET, ShowCreditsPacket) // 75
        this.register(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket) // 17
        this.register(ProtocolInfo.TEXT_PACKET, TextPacket) // 9
        this.register(ProtocolInfo.MOVE_ENTITY_DELTA_PACKET, MoveEntityDeltaPacket) // 111
        this.register(ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET, SetLocalPlayerAsInitializedPacket) // 113
        this.register(ProtocolInfo.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET, NetworkChunkPublisherUpdatePacket) // 121
        this.register(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket) // 123
        this.register(ProtocolInfo.LECTERN_UPDATE_PACKET, LecternUpdatePacket) // 125
        this.register(ProtocolInfo.MAP_CREATE_LOCKED_COPY_PACKET, MapCreateLockedCopyPacket) // 131
        this.register(ProtocolInfo.ON_SCREEN_TEXTURE_ANIMATION_PACKET, OnScreenTextureAnimationPacket) // 130
        this.register(ProtocolInfo.PLAYER_AUTH_INPUT_PACKET, PlayerAuthInputPacket) // 144
        this.register(ProtocolInfo.ITEM_STACK_REQUEST_PACKET, ItemStackRequestPacket) // 147
        this.register(ProtocolInfo.PACKET_VIOLATION_WARNING_PACKET, PacketViolationWarningPacket) // 156
        this.register(ProtocolInfo.PLAYER_ARMOR_DAMAGE_PACKET, PlayerArmorDamagePacket) // 149
        this.register(ProtocolInfo.POS_TRACKING_CLIENT_REQUEST_PACKET, PositionTrackingDBClientRequestPacket) // 154
        this.register(ProtocolInfo.POS_TRACKING_SERVER_BROADCAST_PACKET, PositionTrackingDBServerBroadcastPacket) // 153
        this.register(ProtocolInfo.UPDATE_PLAYER_GAME_TYPE_PACKET, UpdatePlayerGameTypePacket) // 151
        this.register(ProtocolInfo.TOAST_REQUEST_PACKET, ToastRequestPacket) // 186
        this.register(ProtocolInfo.REMOVE_VOLUME_ENTITY_PACKET, RemoveVolumeEntityPacket) // 167
        this.register(ProtocolInfo.SYNC_ENTITY_PROPERTY_PACKET, SyncEntityPropertyPacket) // 165
        this.register(ProtocolInfo.NPC_DIALOGUE_PACKET, NPCDialoguePacket) // 169
        this.register(ProtocolInfo.NPC_REQUEST_PACKET, NPCRequestPacket) // 98
        this.register(ProtocolInfo.SIMULATION_TYPE_PACKET, SimulationTypePacket) // 168
        this.register(ProtocolInfo.SCRIPT_MESSAGE_PACKET, ScriptMessagePacket) // 177
        this.register(ProtocolInfo.PLAYER_START_ITEM_COOL_DOWN_PACKET, PlayerStartItemCoolDownPacket) // 176
        this.register(ProtocolInfo.REQUEST_PERMISSIONS_PACKET, RequestPermissionsPacket) // 185
        this.register(ProtocolInfo.LESSON_PROGRESS_PACKET, LessonProgressPacket) // 183
        this.register(ProtocolInfo.REQUEST_ABILITY_PACKET, RequestAbilityPacket) // 184
        this.register(ProtocolInfo.REQUEST_NETWORK_SETTINGS_PACKET, RequestNetworkSettingsPacket) // 193
        this.register(ProtocolInfo.NETWORK_SETTINGS_PACKET, NetworkSettingsPacket) // 143
        this.register(ProtocolInfo.UPDATE_CLIENT_INPUT_LOCKS, UpdateClientInputLocksPacket) // 196
        this.register(ProtocolInfo.SET_DEFAULT_GAME_TYPE_PACKET, SetDefaultGameTypePacket) // 105
        this.register(ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET, StructureBlockUpdatePacket) // 90
        this.register(ProtocolInfo.UNLOCKED_RECIPES_PACKET, UnlockedRecipesPacket) // 199
        this.register(ProtocolInfo.TRIM_DATA, TrimDataPacket) // 302
        this.register(ProtocolInfo.OPEN_SIGN, OpenSignPacket) // 303
        this.register(ProtocolInfo.TOGGLE_CRAFTER_SLOT_REQUEST, ToggleCrafterSlotRequestPacket) // 306
        this.register(ProtocolInfo.SET_PLAYER_INVENTORY_OPTIONS_PACKET, SetPlayerInventoryOptionsPacket) // 307
        this.register(ProtocolInfo.SET_HUD, SetHudPacket) // 308
        this.register(ProtocolInfo.SETTINGS_COMMAND_PACKET, SettingsCommandPacket) // 140
        this.register(ProtocolInfo.SERVERBOUND_LOADING_SCREEN_PACKET, ServerboundLoadingScreenPacket) // 312
        this.register(ProtocolInfo.SERVERBOUND_DIAGNOSTICS_PACKET, ServerboundDiagnosticsPacket) // 315
        this.register(ProtocolInfo.PLAYER_LOCATION_PACKET, PlayerLocationPacket) // 326
    }

    companion object {
        private val isLoad = AtomicBoolean(false)
    }
}
