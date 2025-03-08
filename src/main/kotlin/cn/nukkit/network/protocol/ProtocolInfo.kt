package cn.nukkit.network.protocol

import cn.nukkit.utils.SemVersion
import cn.nukkit.utils.Utils

/**
 * @author MagicDroidX &amp; iNevet (Nukkit Project)
 */
interface ProtocolInfo {
    companion object {
        /**
         * Actual Minecraft: PE protocol version
         */
        @JvmField
        val CURRENT_PROTOCOL: Int = Utils.dynamic(776)

        val MINECRAFT_VERSION_NETWORK: String = Utils.dynamic("1.21.60")

        val MINECRAFT_SEMVERSION: SemVersion = SemVersion(1, 21, 6, 0, 0)

        @JvmField
        val BLOCK_STATE_VERSION_NO_REVISION: Int = (MINECRAFT_SEMVERSION.major shl 24) or  //major
                (MINECRAFT_SEMVERSION.minor shl 16) or  //minor
                (MINECRAFT_SEMVERSION.patch shl 8) //patch

        @JvmField
        val MINECRAFT_VERSION: String = 'v'.toString() + MINECRAFT_VERSION_NETWORK

        const val LOGIN_PACKET: Int = 0x01
        const val PLAY_STATUS_PACKET: Int = 0x02
        const val SERVER_TO_CLIENT_HANDSHAKE_PACKET: Int = 0x03
        const val CLIENT_TO_SERVER_HANDSHAKE_PACKET: Int = 0x04
        const val DISCONNECT_PACKET: Int = 0x05
        const val RESOURCE_PACKS_INFO_PACKET: Int = 0x06
        const val RESOURCE_PACK_STACK_PACKET: Int = 0x07
        const val RESOURCE_PACK_CLIENT_RESPONSE_PACKET: Int = 0x08
        const val TEXT_PACKET: Int = 0x09
        const val SERVER_POST_MOVE_POSITION: Int = 0x10
        const val SET_TIME_PACKET: Int = 0x0a
        const val START_GAME_PACKET: Int = 0x0b
        const val ADD_PLAYER_PACKET: Int = 0x0c
        const val ADD_ENTITY_PACKET: Int = 0x0d
        const val REMOVE_ENTITY_PACKET: Int = 0x0e
        const val ADD_ITEM_ENTITY_PACKET: Int = 0x0f
        const val TAKE_ITEM_ENTITY_PACKET: Int = 0x11
        const val MOVE_ENTITY_ABSOLUTE_PACKET: Int = 0x12
        const val MOVE_PLAYER_PACKET: Int = 0x13
        const val RIDER_JUMP_PACKET: Int = 0x14
        const val UPDATE_BLOCK_PACKET: Int = 0x15
        const val ADD_PAINTING_PACKET: Int = 0x16
        const val TICK_SYNC_PACKET: Int = 0x17
        const val LEVEL_SOUND_EVENT_PACKET_V1: Int = 0x18
        const val LEVEL_EVENT_PACKET: Int = 0x19
        const val BLOCK_EVENT_PACKET: Int = 0x1a
        const val ENTITY_EVENT_PACKET: Int = 0x1b
        const val MOB_EFFECT_PACKET: Int = 0x1c
        const val UPDATE_ATTRIBUTES_PACKET: Int = 0x1d
        const val INVENTORY_TRANSACTION_PACKET: Int = 0x1e
        const val MOB_EQUIPMENT_PACKET: Int = 0x1f
        const val MOB_ARMOR_EQUIPMENT_PACKET: Int = 0x20
        const val INTERACT_PACKET: Int = 0x21
        const val BLOCK_PICK_REQUEST_PACKET: Int = 0x22
        const val ENTITY_PICK_REQUEST_PACKET: Int = 0x23
        const val PLAYER_ACTION_PACKET: Int = 0x24
        const val ENTITY_FALL_PACKET: Int = 0x25
        const val HURT_ARMOR_PACKET: Int = 0x26
        const val SET_ENTITY_DATA_PACKET: Int = 0x27
        const val SET_ENTITY_MOTION_PACKET: Int = 0x28
        const val SET_ENTITY_LINK_PACKET: Int = 0x29
        const val SET_HEALTH_PACKET: Int = 0x2a
        const val SET_SPAWN_POSITION_PACKET: Int = 0x2b
        const val ANIMATE_PACKET: Int = 0x2c
        const val RESPAWN_PACKET: Int = 0x2d
        const val CONTAINER_OPEN_PACKET: Int = 0x2e
        const val CONTAINER_CLOSE_PACKET: Int = 0x2f
        const val PLAYER_HOTBAR_PACKET: Int = 0x30
        const val INVENTORY_CONTENT_PACKET: Int = 0x31
        const val INVENTORY_SLOT_PACKET: Int = 0x32
        const val CONTAINER_SET_DATA_PACKET: Int = 0x33
        const val CRAFTING_DATA_PACKET: Int = 0x34
        const val CRAFTING_EVENT_PACKET: Int = 0x35
        const val GUI_DATA_PICK_ITEM_PACKET: Int = 0x36
        const val ADVENTURE_SETTINGS_PACKET: Int = 0x37
        const val BLOCK_ENTITY_DATA_PACKET: Int = 0x38
        const val PLAYER_INPUT_PACKET: Int = 0x39
        const val FULL_CHUNK_DATA_PACKET: Int = 0x3a
        const val SET_COMMANDS_ENABLED_PACKET: Int = 0x3b
        const val SET_DIFFICULTY_PACKET: Int = 0x3c
        const val CHANGE_DIMENSION_PACKET: Int = 0x3d
        const val SET_PLAYER_GAME_TYPE_PACKET: Int = 0x3e
        const val PLAYER_LIST_PACKET: Int = 0x3f
        const val SIMPLE_EVENT_PACKET: Int = 0x40
        const val EVENT_PACKET: Int = 0x41
        const val SPAWN_EXPERIENCE_ORB_PACKET: Int = 0x42
        const val CLIENTBOUND_MAP_ITEM_DATA_PACKET: Int = 0x43
        const val MAP_INFO_REQUEST_PACKET: Int = 0x44
        const val REQUEST_CHUNK_RADIUS_PACKET: Int = 0x45
        const val CHUNK_RADIUS_UPDATED_PACKET: Int = 0x46
        const val ITEM_FRAME_DROP_ITEM_PACKET: Int = 0x47
        const val GAME_RULES_CHANGED_PACKET: Int = 0x48
        const val CAMERA_PACKET: Int = 0x49
        const val BOSS_EVENT_PACKET: Int = 0x4a
        const val SHOW_CREDITS_PACKET: Int = 0x4b
        const val AVAILABLE_COMMANDS_PACKET: Int = 0x4c
        const val COMMAND_REQUEST_PACKET: Int = 0x4d
        const val COMMAND_BLOCK_UPDATE_PACKET: Int = 0x4e
        const val COMMAND_OUTPUT_PACKET: Int = 0x4f
        const val UPDATE_TRADE_PACKET: Int = 0x50
        const val UPDATE_EQUIPMENT_PACKET: Int = 0x51
        const val RESOURCE_PACK_DATA_INFO_PACKET: Int = 0x52
        const val RESOURCE_PACK_CHUNK_DATA_PACKET: Int = 0x53
        const val RESOURCE_PACK_CHUNK_REQUEST_PACKET: Int = 0x54
        const val TRANSFER_PACKET: Int = 0x55
        const val PLAY_SOUND_PACKET: Int = 0x56
        const val STOP_SOUND_PACKET: Int = 0x57
        const val SET_TITLE_PACKET: Int = 0x58
        const val ADD_BEHAVIOR_TREE_PACKET: Int = 0x59
        const val STRUCTURE_BLOCK_UPDATE_PACKET: Int = 0x5a
        const val SHOW_STORE_OFFER_PACKET: Int = 0x5b
        const val PURCHASE_RECEIPT_PACKET: Int = 0x5c
        const val PLAYER_SKIN_PACKET: Int = 0x5d
        const val SUB_CLIENT_LOGIN_PACKET: Int = 0x5e
        const val INITIATE_WEB_SOCKET_CONNECTION_PACKET: Int = 0x5f
        const val SET_LAST_HURT_BY_PACKET: Int = 0x60
        const val BOOK_EDIT_PACKET: Int = 0x61
        const val NPC_REQUEST_PACKET: Int = 0x62
        const val PHOTO_TRANSFER_PACKET: Int = 0x63
        const val MODAL_FORM_REQUEST_PACKET: Int = 0x64
        const val MODAL_FORM_RESPONSE_PACKET: Int = 0x65
        const val SERVER_SETTINGS_REQUEST_PACKET: Int = 0x66
        const val SERVER_SETTINGS_RESPONSE_PACKET: Int = 0x67
        const val SHOW_PROFILE_PACKET: Int = 0x68
        const val SET_DEFAULT_GAME_TYPE_PACKET: Int = 0x69
        const val REMOVE_OBJECTIVE_PACKET: Int = 0x6a
        const val SET_DISPLAY_OBJECTIVE_PACKET: Int = 0x6b
        const val SET_SCORE_PACKET: Int = 0x6c
        const val LAB_TABLE_PACKET: Int = 0x6d
        const val UPDATE_BLOCK_SYNCED_PACKET: Int = 0x6e
        const val MOVE_ENTITY_DELTA_PACKET: Int = 0x6f
        const val SET_SCOREBOARD_IDENTITY_PACKET: Int = 0x70
        const val SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET: Int = 0x71
        const val UPDATE_SOFT_ENUM_PACKET: Int = 0x72
        const val NETWORK_STACK_LATENCY_PACKET: Int = 0x73
        const val SCRIPT_CUSTOM_EVENT_PACKET: Int = 0x75
        const val SPAWN_PARTICLE_EFFECT_PACKET: Int = 0x76
        const val AVAILABLE_ENTITY_IDENTIFIERS_PACKET: Int = 0x77
        const val LEVEL_SOUND_EVENT_PACKET_V2: Int = 0x78
        const val NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET: Int = 0x79
        const val BIOME_DEFINITION_LIST_PACKET: Int = 0x7a
        const val LEVEL_SOUND_EVENT_PACKET: Int = 0x7b
        const val LEVEL_EVENT_GENERIC_PACKET: Int = 0x7c
        const val LECTERN_UPDATE_PACKET: Int = 0x7d

        //int ADD_ENTITY_PACKET = 0x7f;
        //int REMOVE_ENTITY_PACKET = 0x80;
        const val CLIENT_CACHE_STATUS_PACKET: Int = 0x81
        const val ON_SCREEN_TEXTURE_ANIMATION_PACKET: Int = 0x82
        const val MAP_CREATE_LOCKED_COPY_PACKET: Int = 0x83
        const val STRUCTURE_TEMPLATE_DATA_EXPORT_REQUEST: Int = 0x84
        const val STRUCTURE_TEMPLATE_DATA_EXPORT_RESPONSE: Int = 0x85
        const val UPDATE_BLOCK_PROPERTIES: Int = 0x86
        const val CLIENT_CACHE_BLOB_STATUS_PACKET: Int = 0x87
        const val CLIENT_CACHE_MISS_RESPONSE_PACKET: Int = 0x88
        const val EDUCATION_SETTINGS_PACKET: Int = 0x89
        const val EMOTE_PACKET: Int = 0x8a
        const val MULTIPLAYER_SETTINGS_PACKET: Int = 0x8b
        const val SETTINGS_COMMAND_PACKET: Int = 140
        const val ANVIL_DAMAGE_PACKET: Int = 0x8d
        const val COMPLETED_USING_ITEM_PACKET: Int = 0x8e
        const val NETWORK_SETTINGS_PACKET: Int = 0x8f
        const val PLAYER_AUTH_INPUT_PACKET: Int = 0x90
        const val CREATIVE_CONTENT_PACKET: Int = 0x91
        const val PLAYER_ENCHANT_OPTIONS_PACKET: Int = 0x92
        const val ITEM_STACK_REQUEST_PACKET: Int = 0x93
        const val ITEM_STACK_RESPONSE_PACKET: Int = 0x94
        const val PLAYER_ARMOR_DAMAGE_PACKET: Int = 0x95
        const val CODE_BUILDER_PACKET: Int = 0x96
        const val UPDATE_PLAYER_GAME_TYPE_PACKET: Int = 0x97
        const val EMOTE_LIST_PACKET: Int = 0x98
        const val POS_TRACKING_SERVER_BROADCAST_PACKET: Int = 0x99
        const val POS_TRACKING_CLIENT_REQUEST_PACKET: Int = 0x9a
        const val DEBUG_INFO_PACKET: Int = 0x9b
        const val PACKET_VIOLATION_WARNING_PACKET: Int = 0x9c
        const val MOTION_PREDICTION_HINTS_PACKET: Int = 0x9d
        const val ANIMATE_ENTITY_PACKET: Int = 0x9e
        const val CAMERA_SHAKE_PACKET: Int = 0x9f
        const val PLAYER_FOG_PACKET: Int = 0xa0
        const val CORRECT_PLAYER_MOVE_PREDICTION_PACKET: Int = 0xa1
        const val ITEM_REGISTRY_PACKET: Int = 0xa2
        const val FILTER_TEXT_PACKET: Int = 0xa3
        const val CLIENTBOUND_DEBUG_RENDERER_PACKET: Int = 0xa4
        const val SYNC_ENTITY_PROPERTY_PACKET: Int = 0xa5
        const val ADD_VOLUME_ENTITY_PACKET: Int = 0xa6
        const val REMOVE_VOLUME_ENTITY_PACKET: Int = 0xa7
        const val SIMULATION_TYPE_PACKET: Int = 0xa8
        const val NPC_DIALOGUE_PACKET: Int = 0xa9
        const val EDU_URI_RESOURCE_PACKET: Int = 0xaa
        const val CREATE_PHOTO_PACKET: Int = 0xab
        const val UPDATE_SUB_CHUNK_BLOCKS_PACKET: Int = 0xac
        const val PHOTO_INFO_REQUEST_PACKET: Int = 0xad
        const val SUB_CHUNK_PACKET: Int = 0xae
        const val SUB_CHUNK_REQUEST_PACKET: Int = 0xaf
        const val PLAYER_START_ITEM_COOL_DOWN_PACKET: Int = 0xb0
        const val SCRIPT_MESSAGE_PACKET: Int = 0xb1
        const val CODE_BUILDER_SOURCE_PACKET: Int = 0xb2
        const val AGENT_ACTION_EVENT_PACKET: Int = 0xb3
        const val CHANGE_MOB_PROPERTY_PACKET: Int = 0xb4
        const val DIMENSION_DATA_PACKET: Int = 0xb5
        const val TICKING_AREAS_LOAD_STATUS_PACKET: Int = 0xb6
        const val LESSON_PROGRESS_PACKET: Int = 0xb7
        const val REQUEST_ABILITY_PACKET: Int = 0xb8
        const val REQUEST_PERMISSIONS_PACKET: Int = 0xb9
        const val TOAST_REQUEST_PACKET: Int = 0xba
        const val UPDATE_ABILITIES_PACKET: Int = 0xbb
        const val UPDATE_ADVENTURE_SETTINGS_PACKET: Int = 0xbc
        const val DEATH_INFO_PACKET: Int = 0xbd
        const val EDITOR_NETWORK_PACKET: Int = 0xbe
        const val FEATURE_REGISTRY_PACKET: Int = 0xbf
        const val SERVER_STATS_PACKET: Int = 0xc0
        const val REQUEST_NETWORK_SETTINGS_PACKET: Int = 0xc1
        const val GAME_TEST_REQUEST_PACKET: Int = 0xc2
        const val GAME_TEST_RESULTS_PACKET: Int = 0xc3
        const val UPDATE_CLIENT_INPUT_LOCKS: Int = 0xc4
        const val CLIENT_CHEAT_ABILITY_PACKET: Int = 0xc5
        const val CAMERA_PRESETS_PACKET: Int = 0xc6
        const val UNLOCKED_RECIPES_PACKET: Int = 0xc7
        const val CAMERA_INSTRUCTION_PACKET: Int = 300
        const val COMPRESSED_BIOME_DEFINITIONS_LIST: Int = 301
        const val TRIM_DATA: Int = 302
        const val OPEN_SIGN: Int = 303
        const val AGENT_ANIMATION: Int = 304
        const val REFRESH_ENTITLEMENTS: Int = 305
        const val TOGGLE_CRAFTER_SLOT_REQUEST: Int = 306
        const val SET_PLAYER_INVENTORY_OPTIONS_PACKET: Int = 307
        const val SET_HUD: Int = 308
        const val CLIENTBOUND_CLOSE_FORM_PACKET: Int = 310
        const val SERVERBOUND_LOADING_SCREEN_PACKET: Int = 312
        const val SERVERBOUND_DIAGNOSTICS_PACKET: Int = 315
        const val CAMERA_AIM_ASSIST_PACKET: Int = 316
        const val CONTAINER_REGISTRY_CLEANUP_PACKET: Int = 317
        const val MOVEMENT_EFFECT_PACKET: Int = 318
        const val CAMERA_AIM_ASSIST_PRESETS_PACKET: Int = 320
        const val CLIENT_CAMERA_AIM_ASSIST_PACKET: Int = 321
        const val CLIENT_MOVEMENT_PREDICTION_SYNC_PACKET: Int = 322
    }
}
