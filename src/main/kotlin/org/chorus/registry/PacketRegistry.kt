package org.chorus.registry

import cn.nukkit.network.protocol.*
import cn.nukkit.registry.RegisterException
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import me.sunlan.fastreflection.FastConstructor
import java.util.concurrent.atomic.AtomicBoolean
import javax.annotation.Nonnegative

class PacketRegistry : IRegistry<Int?, DataPacket?, Class<out DataPacket?>> {
    private val PACKET_POOL = Int2ObjectOpenHashMap<FastConstructor<out DataPacket>?>(256)
    override fun init() {
        if (isLoad.getAndSet(true)) return
        registerPackets()
    }

    override fun get(key: Int?): DataPacket? {
        val fastConstructor = PACKET_POOL[key]
        return if (fastConstructor == null) {
            null
        } else {
            try {
                fastConstructor.invoke() as DataPacket
            } catch (e: Throwable) {
                throw RuntimeException(e)
            }
        }
    }

    fun get(key: Int): DataPacket? {
        val fastConstructor = PACKET_POOL[key]
        return if (fastConstructor == null) {
            null
        } else {
            try {
                fastConstructor.invoke() as DataPacket
            } catch (e: Throwable) {
                throw RuntimeException(e)
            }
        }
    }

    override fun trim() {
        PACKET_POOL.trim()
    }

    override fun reload() {
        isLoad.set(false)
        PACKET_POOL.clear()
        init()
    }

    /**
     * Register a packet to the pool. Using from 1.19.70.
     *
     * @param id    The packet id, non-negative int
     * @param clazz The packet class
     */
    @Throws(RegisterException::class)
    override fun register(id: Int?, clazz: Class<out DataPacket?>) {
        try {
            if (PACKET_POOL.putIfAbsent(id, FastConstructor.create(clazz.getConstructor())) != null) {
                throw RegisterException("The packet has been registered!")
            }
        } catch (e: NoSuchMethodException) {
            throw RegisterException(e)
        }
    }

    private fun register0(@Nonnegative id: Int, clazz: Class<out DataPacket>) {
        try {
            if (PACKET_POOL.putIfAbsent(id, FastConstructor.create(clazz.getConstructor())) != null) {
                throw RegisterException("The packet has been registered!")
            }
        } catch (ignored: NoSuchMethodException) {
        } catch (ignored: RegisterException) {
        }
    }

    private fun registerPackets() {
        PACKET_POOL.clear()

        this.register0(ProtocolInfo.SERVER_TO_CLIENT_HANDSHAKE_PACKET, ServerToClientHandshakePacket::class.java)
        this.register0(ProtocolInfo.CLIENT_TO_SERVER_HANDSHAKE_PACKET, ClientToServerHandshakePacket::class.java)
        this.register0(ProtocolInfo.ADD_ENTITY_PACKET, AddEntityPacket::class.java)
        this.register0(ProtocolInfo.ADD_ITEM_ENTITY_PACKET, AddItemEntityPacket::class.java)
        this.register0(ProtocolInfo.ADD_PAINTING_PACKET, AddPaintingPacket::class.java)
        this.register0(ProtocolInfo.ADD_PLAYER_PACKET, AddPlayerPacket::class.java)
        this.register0(ProtocolInfo.ADVENTURE_SETTINGS_PACKET, AdventureSettingsPacket::class.java)
        this.register0(ProtocolInfo.ANIMATE_PACKET, AnimatePacket::class.java)
        this.register0(ProtocolInfo.ANVIL_DAMAGE_PACKET, AnvilDamagePacket::class.java)
        this.register0(ProtocolInfo.AVAILABLE_COMMANDS_PACKET, AvailableCommandsPacket::class.java)
        this.register0(ProtocolInfo.BLOCK_ENTITY_DATA_PACKET, BlockEntityDataPacket::class.java)
        this.register0(ProtocolInfo.BLOCK_EVENT_PACKET, BlockEventPacket::class.java)
        this.register0(ProtocolInfo.BLOCK_PICK_REQUEST_PACKET, BlockPickRequestPacket::class.java)
        this.register0(ProtocolInfo.BOOK_EDIT_PACKET, BookEditPacket::class.java)
        this.register0(ProtocolInfo.BOSS_EVENT_PACKET, BossEventPacket::class.java)
        this.register0(ProtocolInfo.CHANGE_DIMENSION_PACKET, ChangeDimensionPacket::class.java)
        this.register0(ProtocolInfo.CHUNK_RADIUS_UPDATED_PACKET, ChunkRadiusUpdatedPacket::class.java)
        this.register0(ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET, ClientboundMapItemDataPacket::class.java)
        this.register0(ProtocolInfo.COMMAND_REQUEST_PACKET, CommandRequestPacket::class.java)
        this.register0(ProtocolInfo.CONTAINER_CLOSE_PACKET, ContainerClosePacket::class.java)
        this.register0(ProtocolInfo.CONTAINER_OPEN_PACKET, ContainerOpenPacket::class.java)
        this.register0(ProtocolInfo.CONTAINER_SET_DATA_PACKET, ContainerSetDataPacket::class.java)
        this.register0(ProtocolInfo.CRAFTING_DATA_PACKET, CraftingDataPacket::class.java)
        this.register0(ProtocolInfo.CRAFTING_EVENT_PACKET, CraftingEventPacket::class.java)
        this.register0(ProtocolInfo.DISCONNECT_PACKET, DisconnectPacket::class.java)
        this.register0(ProtocolInfo.ENTITY_EVENT_PACKET, EntityEventPacket::class.java)
        this.register0(ProtocolInfo.ENTITY_FALL_PACKET, EntityFallPacket::class.java)
        this.register0(ProtocolInfo.FULL_CHUNK_DATA_PACKET, LevelChunkPacket::class.java)
        this.register0(ProtocolInfo.GAME_RULES_CHANGED_PACKET, GameRulesChangedPacket::class.java)
        this.register0(ProtocolInfo.HURT_ARMOR_PACKET, HurtArmorPacket::class.java)
        this.register0(ProtocolInfo.INTERACT_PACKET, InteractPacket::class.java)
        this.register0(ProtocolInfo.INVENTORY_CONTENT_PACKET, InventoryContentPacket::class.java)
        this.register0(ProtocolInfo.INVENTORY_SLOT_PACKET, InventorySlotPacket::class.java)
        this.register0(ProtocolInfo.INVENTORY_TRANSACTION_PACKET, InventoryTransactionPacket::class.java)
        this.register0(ProtocolInfo.ITEM_FRAME_DROP_ITEM_PACKET, ItemFrameDropItemPacket::class.java)
        this.register0(ProtocolInfo.LEVEL_EVENT_PACKET, LevelEventPacket::class.java)
        this.register0(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET_V1, LevelSoundEventPacketV1::class.java)
        this.register0(ProtocolInfo.LOGIN_PACKET, LoginPacket::class.java)
        this.register0(ProtocolInfo.MAP_INFO_REQUEST_PACKET, MapInfoRequestPacket::class.java)
        this.register0(ProtocolInfo.MOB_ARMOR_EQUIPMENT_PACKET, MobArmorEquipmentPacket::class.java)
        this.register0(ProtocolInfo.MOB_EQUIPMENT_PACKET, MobEquipmentPacket::class.java)
        this.register0(ProtocolInfo.MODAL_FORM_REQUEST_PACKET, ModalFormRequestPacket::class.java)
        this.register0(ProtocolInfo.MODAL_FORM_RESPONSE_PACKET, ModalFormResponsePacket::class.java)
        this.register0(ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET, MoveEntityAbsolutePacket::class.java)
        this.register0(ProtocolInfo.MOVE_PLAYER_PACKET, MovePlayerPacket::class.java)
        this.register0(ProtocolInfo.PLAYER_ACTION_PACKET, PlayerActionPacket::class.java)
        this.register0(ProtocolInfo.PLAYER_INPUT_PACKET, PlayerInputPacket::class.java)
        this.register0(ProtocolInfo.PLAYER_LIST_PACKET, PlayerListPacket::class.java)
        this.register0(ProtocolInfo.PLAYER_HOTBAR_PACKET, PlayerHotbarPacket::class.java)
        this.register0(ProtocolInfo.PLAY_SOUND_PACKET, PlaySoundPacket::class.java)
        this.register0(ProtocolInfo.PLAY_STATUS_PACKET, PlayStatusPacket::class.java)
        this.register0(ProtocolInfo.REMOVE_ENTITY_PACKET, RemoveEntityPacket::class.java)
        this.register0(ProtocolInfo.REQUEST_CHUNK_RADIUS_PACKET, RequestChunkRadiusPacket::class.java)
        this.register0(ProtocolInfo.RESOURCE_PACKS_INFO_PACKET, ResourcePacksInfoPacket::class.java)
        this.register0(ProtocolInfo.RESOURCE_PACK_STACK_PACKET, ResourcePackStackPacket::class.java)
        this.register0(ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET, ResourcePackClientResponsePacket::class.java)
        this.register0(ProtocolInfo.RESOURCE_PACK_DATA_INFO_PACKET, ResourcePackDataInfoPacket::class.java)
        this.register0(ProtocolInfo.RESOURCE_PACK_CHUNK_DATA_PACKET, ResourcePackChunkDataPacket::class.java)
        this.register0(ProtocolInfo.RESOURCE_PACK_CHUNK_REQUEST_PACKET, ResourcePackChunkRequestPacket::class.java)
        this.register0(ProtocolInfo.PLAYER_SKIN_PACKET, PlayerSkinPacket::class.java)
        this.register0(ProtocolInfo.RESPAWN_PACKET, RespawnPacket::class.java)
        this.register0(ProtocolInfo.RIDER_JUMP_PACKET, RiderJumpPacket::class.java)
        this.register0(ProtocolInfo.SET_COMMANDS_ENABLED_PACKET, SetCommandsEnabledPacket::class.java)
        this.register0(ProtocolInfo.SET_DIFFICULTY_PACKET, SetDifficultyPacket::class.java)
        this.register0(ProtocolInfo.SET_ENTITY_DATA_PACKET, SetEntityDataPacket::class.java)
        this.register0(ProtocolInfo.SET_ENTITY_LINK_PACKET, SetEntityLinkPacket::class.java)
        this.register0(ProtocolInfo.SET_ENTITY_MOTION_PACKET, SetEntityMotionPacket::class.java)
        this.register0(ProtocolInfo.SET_HEALTH_PACKET, SetHealthPacket::class.java)
        this.register0(ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET, SetPlayerGameTypePacket::class.java)
        this.register0(ProtocolInfo.SET_SPAWN_POSITION_PACKET, SetSpawnPositionPacket::class.java)
        this.register0(ProtocolInfo.SET_TITLE_PACKET, SetTitlePacket::class.java)
        this.register0(ProtocolInfo.SET_TIME_PACKET, SetTimePacket::class.java)
        this.register0(ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET, ServerSettingsRequestPacket::class.java)
        this.register0(ProtocolInfo.SERVER_SETTINGS_RESPONSE_PACKET, ServerSettingsResponsePacket::class.java)
        this.register0(ProtocolInfo.SHOW_CREDITS_PACKET, ShowCreditsPacket::class.java)
        this.register0(ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET, SpawnExperienceOrbPacket::class.java)
        this.register0(ProtocolInfo.START_GAME_PACKET, StartGamePacket::class.java)
        this.register0(ProtocolInfo.TAKE_ITEM_ENTITY_PACKET, TakeItemEntityPacket::class.java)
        this.register0(ProtocolInfo.TEXT_PACKET, TextPacket::class.java)
        this.register0(ProtocolInfo.SERVER_POST_MOVE_POSITION, ServerPostMovePositionPacket::class.java)
        this.register0(ProtocolInfo.UPDATE_ATTRIBUTES_PACKET, UpdateAttributesPacket::class.java)
        this.register0(ProtocolInfo.UPDATE_BLOCK_PACKET, UpdateBlockPacket::class.java)
        this.register0(ProtocolInfo.UPDATE_TRADE_PACKET, UpdateTradePacket::class.java)
        this.register0(ProtocolInfo.MOVE_ENTITY_DELTA_PACKET, MoveEntityDeltaPacket::class.java)
        this.register0(
            ProtocolInfo.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET,
            SetLocalPlayerAsInitializedPacket::class.java
        )
        this.register0(ProtocolInfo.NETWORK_STACK_LATENCY_PACKET, NetworkStackLatencyPacket::class.java)
        this.register0(ProtocolInfo.UPDATE_SOFT_ENUM_PACKET, UpdateSoftEnumPacket::class.java)
        this.register0(
            ProtocolInfo.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET,
            NetworkChunkPublisherUpdatePacket::class.java
        )
        this.register0(ProtocolInfo.AVAILABLE_ENTITY_IDENTIFIERS_PACKET, AvailableEntityIdentifiersPacket::class.java)
        this.register0(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET_V2, LevelSoundEventPacket::class.java)
        //        this.registerPacket(ProtocolInfo.SCRIPT_CUSTOM_EVENT_PACKET, ScriptCustomEventPacket.class); // deprecated since 1.20.10
        this.register0(ProtocolInfo.SPAWN_PARTICLE_EFFECT_PACKET, SpawnParticleEffectPacket::class.java)
        this.register0(ProtocolInfo.BIOME_DEFINITION_LIST_PACKET, BiomeDefinitionListPacket::class.java)
        this.register0(ProtocolInfo.LEVEL_SOUND_EVENT_PACKET, LevelSoundEventPacket::class.java)
        this.register0(ProtocolInfo.LEVEL_EVENT_GENERIC_PACKET, LevelEventGenericPacket::class.java)
        this.register0(ProtocolInfo.LECTERN_UPDATE_PACKET, LecternUpdatePacket::class.java)
        this.register0(ProtocolInfo.CLIENT_CACHE_STATUS_PACKET, ClientCacheStatusPacket::class.java)
        this.register0(ProtocolInfo.MAP_CREATE_LOCKED_COPY_PACKET, MapCreateLockedCopyPacket::class.java)
        this.register0(ProtocolInfo.EMOTE_PACKET, EmotePacket::class.java)
        this.register0(ProtocolInfo.ON_SCREEN_TEXTURE_ANIMATION_PACKET, OnScreenTextureAnimationPacket::class.java)
        this.register0(ProtocolInfo.COMPLETED_USING_ITEM_PACKET, CompletedUsingItemPacket::class.java)
        this.register0(ProtocolInfo.CODE_BUILDER_PACKET, CodeBuilderPacket::class.java)
        this.register0(ProtocolInfo.PLAYER_AUTH_INPUT_PACKET, PlayerAuthInputPacket::class.java)
        this.register0(ProtocolInfo.CREATIVE_CONTENT_PACKET, CreativeContentPacket::class.java)
        this.register0(ProtocolInfo.DEBUG_INFO_PACKET, DebugInfoPacket::class.java)
        this.register0(ProtocolInfo.EMOTE_LIST_PACKET, EmoteListPacket::class.java)
        this.register0(ProtocolInfo.ITEM_STACK_REQUEST_PACKET, ItemStackRequestPacket::class.java)
        this.register0(ProtocolInfo.ITEM_STACK_RESPONSE_PACKET, ItemStackResponsePacket::class.java)
        this.register0(ProtocolInfo.PACKET_VIOLATION_WARNING_PACKET, PacketViolationWarningPacket::class.java)
        this.register0(ProtocolInfo.PLAYER_ARMOR_DAMAGE_PACKET, PlayerArmorDamagePacket::class.java)
        this.register0(ProtocolInfo.PLAYER_ENCHANT_OPTIONS_PACKET, PlayerEnchantOptionsPacket::class.java)
        this.register0(
            ProtocolInfo.POS_TRACKING_CLIENT_REQUEST_PACKET,
            PositionTrackingDBClientRequestPacket::class.java
        )
        this.register0(
            ProtocolInfo.POS_TRACKING_SERVER_BROADCAST_PACKET,
            PositionTrackingDBServerBroadcastPacket::class.java
        )
        this.register0(ProtocolInfo.UPDATE_PLAYER_GAME_TYPE_PACKET, UpdatePlayerGameTypePacket::class.java)
        this.register0(ProtocolInfo.FILTER_TEXT_PACKET, FilterTextPacket::class.java)
        this.register0(ProtocolInfo.TOAST_REQUEST_PACKET, ToastRequestPacket::class.java)
        this.register0(ProtocolInfo.ITEM_REGISTRY_PACKET, ItemRegistryPacket::class.java)
        this.register0(ProtocolInfo.ADD_VOLUME_ENTITY_PACKET, AddVolumeEntityPacket::class.java)
        this.register0(ProtocolInfo.REMOVE_VOLUME_ENTITY_PACKET, RemoveVolumeEntityPacket::class.java)
        this.register0(ProtocolInfo.SYNC_ENTITY_PROPERTY_PACKET, SyncEntityPropertyPacket::class.java)
        this.register0(ProtocolInfo.TICK_SYNC_PACKET, TickSyncPacket::class.java)
        this.register0(ProtocolInfo.ANIMATE_ENTITY_PACKET, AnimateEntityPacket::class.java)
        this.register0(ProtocolInfo.NPC_DIALOGUE_PACKET, NPCDialoguePacket::class.java)
        this.register0(ProtocolInfo.NPC_REQUEST_PACKET, NPCRequestPacket::class.java)
        this.register0(ProtocolInfo.SIMULATION_TYPE_PACKET, SimulationTypePacket::class.java)
        this.register0(ProtocolInfo.SCRIPT_MESSAGE_PACKET, ScriptMessagePacket::class.java)
        this.register0(ProtocolInfo.PLAYER_START_ITEM_COOL_DOWN_PACKET, PlayerStartItemCoolDownPacket::class.java)
        this.register0(ProtocolInfo.CODE_BUILDER_SOURCE_PACKET, CodeBuilderSourcePacket::class.java)
        this.register0(ProtocolInfo.UPDATE_SUB_CHUNK_BLOCKS_PACKET, UpdateSubChunkBlocksPacket::class.java)
        //powernukkitx only
        this.register0(ProtocolInfo.REQUEST_PERMISSIONS_PACKET, RequestPermissionsPacket::class.java)
        this.register0(ProtocolInfo.COMMAND_BLOCK_UPDATE_PACKET, CommandBlockUpdatePacket::class.java)
        this.register0(ProtocolInfo.SET_SCORE_PACKET, SetScorePacket::class.java)
        this.register0(ProtocolInfo.SET_DISPLAY_OBJECTIVE_PACKET, SetDisplayObjectivePacket::class.java)
        this.register0(ProtocolInfo.REMOVE_OBJECTIVE_PACKET, RemoveObjectivePacket::class.java)
        this.register0(ProtocolInfo.SET_SCOREBOARD_IDENTITY_PACKET, SetScoreboardIdentityPacket::class.java)
        this.register0(ProtocolInfo.CAMERA_SHAKE_PACKET, CameraShakePacket::class.java)
        this.register0(ProtocolInfo.DEATH_INFO_PACKET, DeathInfoPacket::class.java)
        this.register0(ProtocolInfo.AGENT_ACTION_EVENT_PACKET, AgentActionEventPacket::class.java)
        this.register0(ProtocolInfo.CHANGE_MOB_PROPERTY_PACKET, ChangeMobPropertyPacket::class.java)
        this.register0(ProtocolInfo.DIMENSION_DATA_PACKET, DimensionDataPacket::class.java)
        this.register0(ProtocolInfo.TICKING_AREAS_LOAD_STATUS_PACKET, TickingAreasLoadStatusPacket::class.java)
        this.register0(ProtocolInfo.LAB_TABLE_PACKET, LabTablePacket::class.java)
        this.register0(ProtocolInfo.UPDATE_BLOCK_SYNCED_PACKET, UpdateBlockSyncedPacket::class.java)
        this.register0(ProtocolInfo.EDU_URI_RESOURCE_PACKET, EduUriResourcePacket::class.java)
        this.register0(ProtocolInfo.CREATE_PHOTO_PACKET, CreatePhotoPacket::class.java)
        this.register0(ProtocolInfo.PHOTO_INFO_REQUEST_PACKET, PhotoInfoRequestPacket::class.java)
        this.register0(ProtocolInfo.LESSON_PROGRESS_PACKET, LessonProgressPacket::class.java)
        this.register0(ProtocolInfo.REQUEST_ABILITY_PACKET, RequestAbilityPacket::class.java)
        this.register0(ProtocolInfo.UPDATE_ABILITIES_PACKET, UpdateAbilitiesPacket::class.java)
        this.register0(ProtocolInfo.REQUEST_NETWORK_SETTINGS_PACKET, RequestNetworkSettingsPacket::class.java)
        this.register0(ProtocolInfo.NETWORK_SETTINGS_PACKET, NetworkSettingsPacket::class.java)
        this.register0(ProtocolInfo.UPDATE_ADVENTURE_SETTINGS_PACKET, UpdateAdventureSettingsPacket::class.java)
        this.register0(ProtocolInfo.UPDATE_CLIENT_INPUT_LOCKS, UpdateClientInputLocksPacket::class.java)
        this.register0(ProtocolInfo.PLAYER_FOG_PACKET, PlayerFogPacket::class.java)
        this.register0(ProtocolInfo.SET_DEFAULT_GAME_TYPE_PACKET, SetDefaultGameTypePacket::class.java)
        this.register0(ProtocolInfo.STRUCTURE_BLOCK_UPDATE_PACKET, StructureBlockUpdatePacket::class.java)
        this.register0(ProtocolInfo.CAMERA_PRESETS_PACKET, CameraPresetsPacket::class.java)
        this.register0(ProtocolInfo.UNLOCKED_RECIPES_PACKET, UnlockedRecipesPacket::class.java)
        this.register0(ProtocolInfo.CAMERA_INSTRUCTION_PACKET, CameraInstructionPacket::class.java)
        this.register0(ProtocolInfo.COMPRESSED_BIOME_DEFINITIONS_LIST, CompressedBiomeDefinitionListPacket::class.java)
        this.register0(ProtocolInfo.TRIM_DATA, TrimDataPacket::class.java)
        this.register0(ProtocolInfo.OPEN_SIGN, OpenSignPacket::class.java)
        this.register0(ProtocolInfo.AGENT_ANIMATION, AgentAnimationPacket::class.java)
        this.register0(ProtocolInfo.TOGGLE_CRAFTER_SLOT_REQUEST, ToggleCrafterSlotRequestPacket::class.java)
        this.register0(ProtocolInfo.SET_PLAYER_INVENTORY_OPTIONS_PACKET, SetPlayerInventoryOptionsPacket::class.java)
        this.register0(ProtocolInfo.SET_HUD, SetHudPacket::class.java)
        this.register0(ProtocolInfo.SETTINGS_COMMAND_PACKET, SettingsCommandPacket::class.java)
        this.register0(ProtocolInfo.CLIENTBOUND_CLOSE_FORM_PACKET, ClientboundCloseFormPacket::class.java)
        this.register0(ProtocolInfo.SERVERBOUND_LOADING_SCREEN_PACKET, ServerboundLoadingScreenPacket::class.java)
        this.register0(ProtocolInfo.SERVERBOUND_DIAGNOSTICS_PACKET, ServerboundDiagnosticsPacket::class.java)
        this.register0(ProtocolInfo.CAMERA_AIM_ASSIST_PRESETS_PACKET, CameraAimAssistPresetsPacket::class.java)
        this.register0(ProtocolInfo.CLIENT_CAMERA_AIM_ASSIST_PACKET, ClientCameraAimAssistPacket::class.java)

        PACKET_POOL.trim()
    }

    companion object {
        private val isLoad = AtomicBoolean(false)
    }
}
