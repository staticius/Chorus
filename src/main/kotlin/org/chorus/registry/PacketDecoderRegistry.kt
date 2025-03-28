package org.chorus.registry

import org.chorus.network.protocol.*
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
        this.register(ProtocolInfo.CLIENT_TO_SERVER_HANDSHAKE_PACKET, ClientToServerHandshakePacket)
        this.register(ProtocolInfo.ANIMATE_PACKET, AnimatePacket)
        this.register(ProtocolInfo.ANVIL_DAMAGE_PACKET, AnvilDamagePacket)
        this.register(ProtocolInfo.BLOCK_ACTOR_DATA_PACKET, BlockActorDataPacket)
        this.register(ProtocolInfo.BLOCK_PICK_REQUEST_PACKET, BlockPickRequestPacket)
        this.register(ProtocolInfo.ACTOR_PICK_REQUEST_PACKET, ActorPickRequestPacket)
        this.register(ProtocolInfo.BOOK_EDIT_PACKET, BookEditPacket)
        this.register(ProtocolInfo.BOSS_EVENT_PACKET, BossEventPacket)
        this.register(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatedPacket)
        this.register(ProtocolInfo.COMMAND_REQUEST_PACKET, CommandRequestPacket)
        this.register(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket)

        this.register(ProtocolInfo.FULL_CHUNK_DATA_PACKET, LevelChunkPacket)
        this.register(ProtocolInfo.GAME_RULES_CHANGED_PACKET, GameRulesChangedPacket)
        this.register(ProtocolInfo.HURT_ARMOR_PACKET, HurtArmorPacket)
        this.register(ProtocolInfo.INTERACT_PACKET, InteractPacket)
        this.register(ProtocolInfo.INVENTORY_CONTENT_PACKET, InventoryContentPacket)
        this.register(ProtocolInfo.INVENTORY_SLOT_PACKET, InventorySlotPacket)
        this.register(ProtocolInfo.INVENTORY_TRANSACTION_PACKET, InventoryTransactionPacket)
        this.register(ProtocolInfo.LEVEL_EVENT_PACKET, LevelEventPacket)
        this.register(ProtocolInfo.LOGIN_PACKET, LoginPacket)
        this.register(ProtocolInfo.MAP_INFO_REQUEST_PACKET, MapInfoRequestPacket)
        this.register(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket)
        this.register(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket)
        this.register(ProtocolInfo.MODAL_FORM_REQUEST_PACKET, ModalFormRequestPacket)
        this.register(ProtocolInfo.MODAL_FORM_RESPONSE_PACKET, ModalFormResponsePacket)
        this.register(ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET, MoveEntityAbsolutePacket)
        this.register(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket)
        this.register(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket)
        this.register(ProtocolInfo.PLAYER_INPUT_PACKET, PlayerInputPacket)
        this.register(ProtocolInfo.PLAYER_LIST_PACKET, PlayerListPacket)
        this.register(ProtocolInfo.PLAYER_HOTBAR_PACKET, PlayerHotbarPacket)
        this.register(ProtocolInfo.PLAY_SOUND_PACKET, PlaySoundPacket)
        this.register(ProtocolInfo.PLAY_STATUS_PACKET, PlayStatusPacket)
        this.register(ProtocolInfo.REMOVE_ENTITY_PACKET, RemoveEntityPacket)
        this.register(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket)
        this.register(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, ResourcePacksInfoPacket)
        this.register(ProtocolInfo.RESOURCE_PACK_STACK_PACKET, ResourcePackStackPacket)
        this.register(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket)
        this.register(ProtocolInfo.RESOURCE_PACK_DATA_INFO_PACKET, ResourcePackDataInfoPacket)
        this.register(ProtocolInfo.RESOURCE_PACK_CHUNK_DATA_PACKET, ResourcePackChunkDataPacket)
        this.register(ProtocolInfo.RESOURCE_PACK_CHUNK_REQUEST_PACKET, ResourcePackChunkRequestPacket)
        this.register(ProtocolInfo.PLAYER_SKIN_PACKET, PlayerSkinPacket)
        this.register(ProtocolInfo.RESPAWN_PACKET, RespawnPacket)
        this.register(ProtocolInfo.RIDER_JUMP_PACKET, RiderJumpPacket)
        this.register(ProtocolInfo.SET_COMMANDS_ENABLED_PACKET, SetCommandsEnabledPacket)
        this.register(ProtocolInfo.SET_DIFFICULTY_PACKET, SetDifficultyPacket)
        this.register(ProtocolInfo.SET_ENTITY_DATA_PACKET, SetEntityDataPacket)
        this.register(ProtocolInfo.SET_ENTITY_LINK_PACKET, SetEntityLinkPacket)
        this.register(ProtocolInfo.SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket)
        this.register(ProtocolInfo.SET_HEALTH_PACKET, SetHealthPacket)
        this.register(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket)
        this.register(ProtocolInfo.SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket)
        this.register(ProtocolInfo.SET_TITLE_PACKET, SetTitlePacket)
        this.register(ProtocolInfo.SET_TIME_PACKET, SetTimePacket)
        this.register(ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET, ServerSettingsRequestPacket)
        this.register(ProtocolInfo.SERVER_SETTINGS_RESPONSE_PACKET, ServerSettingsResponsePacket)
        this.register(ProtocolInfo.SHOW_CREDITS_PACKET, ShowCreditsPacket)

        this.register(ProtocolInfo.START_GAME_PACKET, StartGamePacket)
        this.register(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket)
        this.register(ProtocolInfo.TEXT_PACKET, TextPacket)
        this.register(ProtocolInfo.SERVER_POST_MOVE_POSITION, ServerPostMovePositionPacket)
        this.register(ProtocolInfo.UPDATE_ATTRIBUTES_PACKET, UpdateAttributesPacket)
        this.register(ProtocolInfo.UPDATE_BLOCK_PACKET, UpdateBlockPacket)
        this.register(ProtocolInfo.UPDATE_TRADE_PACKET, UpdateTradePacket)
        this.register(ProtocolInfo.MOVE_ENTITY_DELTA_PACKET, MoveEntityDeltaPacket)
        this.register(
            ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET,
            SetLocalPlayerAsInitializedPacket
        )
        this.register(ProtocolInfo.UPDATE_SOFT_ENUM_PACKET, UpdateSoftEnumPacket)
        this.register(
            ProtocolInfo.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET,
            NetworkChunkPublisherUpdatePacket
        )
        this.register(ProtocolInfo.AVAILABLE_ENTITY_IDENTIFIERS_PACKET, AvailableEntityIdentifiersPacket)
        this.register(ProtocolInfo.SPAWN_PARTICLE_EFFECT_PACKET, SpawnParticleEffectPacket)
        this.register(ProtocolInfo.BIOME_DEFINITION_LIST_PACKET, BiomeDefinitionListPacket)
        this.register(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket)
        this.register(ProtocolInfo.LEVEL_EVENT_GENERIC_PACKET, LevelEventGenericPacket)
        this.register(ProtocolInfo.LECTERN_UPDATE_PACKET, LecternUpdatePacket)
        this.register(ProtocolInfo.CLIENT_CACHE_STATUS_PACKET, ClientCacheStatusPacket)
        this.register(ProtocolInfo.MAP_CREATE_LOCKED_COPY_PACKET, MapCreateLockedCopyPacket)
        this.register(ProtocolInfo.EMOTE_PACKET, EmotePacket)
        this.register(ProtocolInfo.ON_SCREEN_TEXTURE_ANIMATION_PACKET, OnScreenTextureAnimationPacket)
        this.register(ProtocolInfo.COMPLETED_USING_ITEM_PACKET, CompletedUsingItemPacket)
        this.register(ProtocolInfo.CODE_BUILDER_PACKET, CodeBuilderPacket)
        this.register(ProtocolInfo.PLAYER_AUTH_INPUT_PACKET, PlayerAuthInputPacket)
        this.register(ProtocolInfo.CREATIVE_CONTENT_PACKET, CreativeContentPacket)
        this.register(ProtocolInfo.DEBUG_INFO_PACKET, DebugInfoPacket)
        this.register(ProtocolInfo.EMOTE_LIST_PACKET, EmoteListPacket)
        this.register(ProtocolInfo.ITEM_STACK_REQUEST_PACKET, ItemStackRequestPacket)
        this.register(ProtocolInfo.ITEM_STACK_RESPONSE_PACKET, ItemStackResponsePacket)
        this.register(ProtocolInfo.PACKET_VIOLATION_WARNING_PACKET, PacketViolationWarningPacket)
        this.register(ProtocolInfo.PLAYER_ARMOR_DAMAGE_PACKET, PlayerArmorDamagePacket)
        this.register(ProtocolInfo.PLAYER_ENCHANT_OPTIONS_PACKET, PlayerEnchantOptionsPacket)
        this.register(
            ProtocolInfo.POS_TRACKING_CLIENT_REQUEST_PACKET,
            PositionTrackingDBClientRequestPacket
        )
        this.register(
            ProtocolInfo.POS_TRACKING_SERVER_BROADCAST_PACKET,
            PositionTrackingDBServerBroadcastPacket
        )
        this.register(ProtocolInfo.UPDATE_PLAYER_GAME_TYPE_PACKET, UpdatePlayerGameTypePacket)
        this.register(ProtocolInfo.TOAST_REQUEST_PACKET, ToastRequestPacket)
        this.register(ProtocolInfo.ITEM_REGISTRY_PACKET, ItemRegistryPacket)
        this.register(ProtocolInfo.ADD_VOLUME_ENTITY_PACKET, AddVolumeEntityPacket)
        this.register(ProtocolInfo.REMOVE_VOLUME_ENTITY_PACKET, RemoveVolumeEntityPacket)
        this.register(ProtocolInfo.SYNC_ENTITY_PROPERTY_PACKET, SyncEntityPropertyPacket)
        this.register(ProtocolInfo.ANIMATE_ENTITY_PACKET, AnimateEntityPacket)
        this.register(ProtocolInfo.NPC_DIALOGUE_PACKET, NPCDialoguePacket)
        this.register(ProtocolInfo.NPC_REQUEST_PACKET, NPCRequestPacket)
        this.register(ProtocolInfo.SIMULATION_TYPE_PACKET, SimulationTypePacket)
        this.register(ProtocolInfo.SCRIPT_MESSAGE_PACKET, ScriptMessagePacket)
        this.register(ProtocolInfo.PLAYER_START_ITEM_COOL_DOWN_PACKET, PlayerStartItemCoolDownPacket)
        this.register(ProtocolInfo.CODE_BUILDER_SOURCE_PACKET, CodeBuilderSourcePacket)
        this.register(ProtocolInfo.UPDATE_SUB_CHUNK_BLOCKS_PACKET, UpdateSubChunkBlocksPacket)

        this.register(ProtocolInfo.REQUEST_PERMISSIONS_PACKET, RequestPermissionsPacket)
        this.register(ProtocolInfo.COMMAND_BLOCK_UPDATE_PACKET, CommandBlockUpdatePacket)
        this.register(ProtocolInfo.SET_SCORE_PACKET, SetScorePacket)
        this.register(ProtocolInfo.SET_DISPLAY_OBJECTIVE_PACKET, SetDisplayObjectivePacket)
        this.register(ProtocolInfo.REMOVE_OBJECTIVE_PACKET, RemoveObjectivePacket)
        this.register(ProtocolInfo.SET_SCOREBOARD_IDENTITY_PACKET, SetScoreboardIdentityPacket)
        this.register(ProtocolInfo.CAMERA_SHAKE_PACKET, CameraShakePacket)
        this.register(ProtocolInfo.DEATH_INFO_PACKET, DeathInfoPacket)
        this.register(ProtocolInfo.AGENT_ACTION_EVENT_PACKET, AgentActionEventPacket)
        this.register(ProtocolInfo.CHANGE_MOB_PROPERTY_PACKET, ChangeMobPropertyPacket)
        this.register(ProtocolInfo.DIMENSION_DATA_PACKET, DimensionDataPacket)
        this.register(ProtocolInfo.TICKING_AREAS_LOAD_STATUS_PACKET, TickingAreasLoadStatusPacket)
        this.register(ProtocolInfo.LAB_TABLE_PACKET, LabTablePacket)
        this.register(ProtocolInfo.UPDATE_BLOCK_SYNCED_PACKET, UpdateBlockSyncedPacket)
        this.register(ProtocolInfo.EDU_URI_RESOURCE_PACKET, EduUriResourcePacket)
        this.register(ProtocolInfo.CREATE_PHOTO_PACKET, CreatePhotoPacket)
        this.register(ProtocolInfo.LESSON_PROGRESS_PACKET, LessonProgressPacket)
        this.register(ProtocolInfo.REQUEST_ABILITY_PACKET, RequestAbilityPacket)
        this.register(ProtocolInfo.UPDATE_ABILITIES_PACKET, UpdateAbilitiesPacket)
        this.register(ProtocolInfo.REQUEST_NETWORK_SETTINGS_PACKET, RequestNetworkSettingsPacket)
        this.register(ProtocolInfo.NETWORK_SETTINGS_PACKET, NetworkSettingsPacket)
        this.register(ProtocolInfo.UPDATE_ADVENTURE_SETTINGS_PACKET, UpdateAdventureSettingsPacket)
        this.register(ProtocolInfo.UPDATE_CLIENT_INPUT_LOCKS, UpdateClientInputLocksPacket)
        this.register(ProtocolInfo.PLAYER_FOG_PACKET, PlayerFogPacket)
        this.register(ProtocolInfo.SET_DEFAULT_GAME_TYPE_PACKET, SetDefaultGameTypePacket)
        this.register(ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET, StructureBlockUpdatePacket)
        this.register(ProtocolInfo.CAMERA_PRESETS_PACKET, CameraPresetsPacket)
        this.register(ProtocolInfo.UNLOCKED_RECIPES_PACKET, UnlockedRecipesPacket)
        this.register(ProtocolInfo.CAMERA_INSTRUCTION_PACKET, CameraInstructionPacket)
        this.register(ProtocolInfo.COMPRESSED_BIOME_DEFINITIONS_LIST, CompressedBiomeDefinitionListPacket)
        this.register(ProtocolInfo.TRIM_DATA, TrimDataPacket)
        this.register(ProtocolInfo.OPEN_SIGN, OpenSignPacket)
        this.register(ProtocolInfo.AGENT_ANIMATION, AgentAnimationPacket)
        this.register(ProtocolInfo.TOGGLE_CRAFTER_SLOT_REQUEST, ToggleCrafterSlotRequestPacket)
        this.register(ProtocolInfo.SET_PLAYER_INVENTORY_OPTIONS_PACKET, SetPlayerInventoryOptionsPacket)
        this.register(ProtocolInfo.SET_HUD, SetHudPacket)
        this.register(ProtocolInfo.SETTINGS_COMMAND_PACKET, SettingsCommandPacket)
        this.register(ProtocolInfo.CLIENTBOUND_CLOSE_FORM_PACKET, ClientboundCloseFormPacket)
        this.register(ProtocolInfo.SERVERBOUND_LOADING_SCREEN_PACKET, ServerboundLoadingScreenPacket)
        this.register(ProtocolInfo.SERVERBOUND_DIAGNOSTICS_PACKET, ServerboundDiagnosticsPacket)
        this.register(ProtocolInfo.CAMERA_AIM_ASSIST_PRESETS_PACKET, CameraAimAssistPresetsPacket)
        this.register(ProtocolInfo.CLIENT_CAMERA_AIM_ASSIST_PACKET, ClientCameraAimAssistPacket)
    }

    companion object {
        private val isLoad = AtomicBoolean(false)
    }
}
