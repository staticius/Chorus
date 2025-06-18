package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.utils.SemVersion

interface ProtocolInfo {
    companion object {
        const val PROTOCOL_VERSION = 818

        const val GAME_VERSION_NET = "1.21.90"
        const val GAME_VERSION_STR = "v$GAME_VERSION_NET"

        val GAME_VERSION = SemVersion(1, 21, 9, 0, 0)

        val BLOCK_STATE_VERSION_NO_REVISION =
            (GAME_VERSION.major shl 24) or (GAME_VERSION.minor shl 16) or (GAME_VERSION.patch shl 8)

        const val LOGIN_PACKET = 1
        const val PLAY_STATUS_PACKET = 2
        const val SERVER_TO_CLIENT_HANDSHAKE_PACKET = 3
        const val CLIENT_TO_SERVER_HANDSHAKE_PACKET = 4
        const val DISCONNECT_PACKET = 5
        const val RESOURCE_PACKS_INFO_PACKET = 6
        const val RESOURCE_PACK_STACK_PACKET = 7
        const val RESOURCE_PACK_CLIENT_RESPONSE_PACKET = 8
        const val TEXT_PACKET = 9
        const val SET_TIME_PACKET = 10
        const val START_GAME_PACKET = 11
        const val ADD_PLAYER_PACKET = 12
        const val ADD_ACTOR_PACKET = 13
        const val REMOVE_ACTOR_PACKET = 14
        const val ADD_ITEM_ACTOR_PACKET = 15
        const val SERVER_POST_MOVE_POSITION = 16
        const val TAKE_ITEM_ENTITY_PACKET = 17
        const val MOVE_ENTITY_ABSOLUTE_PACKET = 18
        const val MOVE_PLAYER_PACKET = 19

        // UNUSED                                           = 20
        const val UPDATE_BLOCK_PACKET = 21
        const val ADD_PAINTING_PACKET = 22

        // UNUSED                                           = 23
        // UNUSED                                           = 24
        const val LEVEL_EVENT_PACKET = 25
        const val BLOCK_EVENT_PACKET = 26
        const val ENTITY_EVENT_PACKET = 27
        const val MOB_EFFECT_PACKET = 28
        const val UPDATE_ATTRIBUTES_PACKET = 29
        const val INVENTORY_TRANSACTION_PACKET = 30
        const val MOB_EQUIPMENT_PACKET = 31
        const val MOB_ARMOR_EQUIPMENT_PACKET = 32
        const val INTERACT_PACKET = 33
        const val BLOCK_PICK_REQUEST_PACKET = 34
        const val ACTOR_PICK_REQUEST_PACKET = 35
        const val PLAYER_ACTION_PACKET = 36

        // UNUSED                                           = 37
        const val HURT_ARMOR_PACKET = 38
        const val SET_ENTITY_DATA_PACKET = 39
        const val SET_ENTITY_MOTION_PACKET = 40
        const val SET_ENTITY_LINK_PACKET = 41
        const val SET_HEALTH_PACKET = 42
        const val SET_SPAWN_POSITION_PACKET = 43
        const val ANIMATE_PACKET = 44
        const val RESPAWN_PACKET = 45
        const val CONTAINER_OPEN_PACKET = 46
        const val CONTAINER_CLOSE_PACKET = 47
        const val PLAYER_HOTBAR_PACKET = 48
        const val INVENTORY_CONTENT_PACKET = 49
        const val INVENTORY_SLOT_PACKET = 50
        const val CONTAINER_SET_DATA_PACKET = 51
        const val CRAFTING_DATA_PACKET = 52

        // UNUSED                                           = 53
        const val GUI_DATA_PICK_ITEM_PACKET = 54

        // UNUSED                                           = 55
        const val BLOCK_ACTOR_DATA_PACKET = 56

        // UNUSED                                           = 57
        const val LEVEL_CHUNK_PACKET = 58
        const val SET_COMMANDS_ENABLED_PACKET = 59
        const val SET_DIFFICULTY_PACKET = 60
        const val CHANGE_DIMENSION_PACKET = 61
        const val SET_PLAYER_GAME_TYPE_PACKET = 62
        const val PLAYER_LIST_PACKET = 63
        const val SIMPLE_EVENT_PACKET = 64

        // DEPRECATED                                       = 65
        // DEPRECATED                                       = 66
        const val CLIENTBOUND_MAP_ITEM_DATA_PACKET = 67
        const val MAP_INFO_REQUEST_PACKET = 68
        const val REQUEST_CHUNK_RADIUS_PACKET = 69
        const val CHUNK_RADIUS_UPDATED_PACKET = 70

        // UNUSED                                           = 71
        const val GAME_RULES_CHANGED_PACKET = 72
        const val CAMERA_PACKET = 73
        const val BOSS_EVENT_PACKET = 74
        const val SHOW_CREDITS_PACKET = 75
        const val AVAILABLE_COMMANDS_PACKET = 76
        const val COMMAND_REQUEST_PACKET = 77
        const val COMMAND_BLOCK_UPDATE_PACKET = 78
        const val COMMAND_OUTPUT_PACKET = 79
        const val UPDATE_TRADE_PACKET = 80
        const val UPDATE_EQUIPMENT_PACKET = 81
        const val RESOURCE_PACK_DATA_INFO_PACKET = 82
        const val RESOURCE_PACK_CHUNK_DATA_PACKET = 83
        const val RESOURCE_PACK_CHUNK_REQUEST_PACKET = 84
        const val TRANSFER_PACKET = 85
        const val PLAY_SOUND_PACKET = 86
        const val STOP_SOUND_PACKET = 87
        const val SET_TITLE_PACKET = 88
        const val ADD_BEHAVIOR_TREE_PACKET = 89
        const val STRUCTURE_BLOCK_UPDATE_PACKET = 90
        const val SHOW_STORE_OFFER_PACKET = 91
        const val PURCHASE_RECEIPT_PACKET = 92
        const val PLAYER_SKIN_PACKET = 93
        const val SUB_CLIENT_LOGIN_PACKET = 94
        const val INITIATE_WEB_SOCKET_CONNECTION_PACKET = 95
        const val SET_LAST_HURT_BY_PACKET = 96
        const val BOOK_EDIT_PACKET = 97
        const val NPC_REQUEST_PACKET = 98
        const val PHOTO_TRANSFER_PACKET = 99
        const val MODAL_FORM_REQUEST_PACKET = 100
        const val MODAL_FORM_RESPONSE_PACKET = 101
        const val SERVER_SETTINGS_REQUEST_PACKET = 102
        const val SERVER_SETTINGS_RESPONSE_PACKET = 103
        const val SHOW_PROFILE_PACKET = 104
        const val SET_DEFAULT_GAME_TYPE_PACKET = 105
        const val REMOVE_OBJECTIVE_PACKET = 106
        const val SET_DISPLAY_OBJECTIVE_PACKET = 107
        const val SET_SCORE_PACKET = 108
        const val LAB_TABLE_PACKET = 109
        const val UPDATE_BLOCK_SYNCED_PACKET = 110
        const val MOVE_ENTITY_DELTA_PACKET = 111
        const val SET_SCOREBOARD_IDENTITY_PACKET = 112
        const val SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET = 113
        const val UPDATE_SOFT_ENUM_PACKET = 114

        // DEPRECATED                                       = 115
        // UNUSED                                           = 116
        // UNUSED                                           = 117
        const val SPAWN_PARTICLE_EFFECT_PACKET = 118
        const val AVAILABLE_ACTOR_IDENTIFIERS_PACKET = 119

        // UNUSED                                           = 120
        const val NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET = 121
        const val BIOME_DEFINITION_LIST_PACKET = 122
        const val LEVEL_SOUND_EVENT_PACKET = 123
        const val LEVEL_EVENT_GENERIC_PACKET = 124
        const val LECTERN_UPDATE_PACKET = 125

        // UNUSED                                           = 126
        // UNUSED                                           = 127
        // UNUSED                                           = 128
        const val CLIENT_CACHE_STATUS_PACKET = 129
        const val ON_SCREEN_TEXTURE_ANIMATION_PACKET = 130
        const val MAP_CREATE_LOCKED_COPY_PACKET = 131
        const val STRUCTURE_TEMPLATE_DATA_EXPORT_REQUEST = 132
        const val STRUCTURE_TEMPLATE_DATA_EXPORT_RESPONSE = 133

        // UNUSED                                           = 134
        const val CLIENT_CACHE_BLOB_STATUS_PACKET = 135
        const val CLIENT_CACHE_MISS_RESPONSE_PACKET = 136
        const val EDUCATION_SETTINGS_PACKET = 137
        const val EMOTE_PACKET = 138
        const val MULTIPLAYER_SETTINGS_PACKET = 139
        const val SETTINGS_COMMAND_PACKET = 140
        const val ANVIL_DAMAGE_PACKET = 141
        const val COMPLETED_USING_ITEM_PACKET = 142
        const val NETWORK_SETTINGS_PACKET = 143
        const val PLAYER_AUTH_INPUT_PACKET = 144
        const val CREATIVE_CONTENT_PACKET = 145
        const val PLAYER_ENCHANT_OPTIONS_PACKET = 146
        const val ITEM_STACK_REQUEST_PACKET = 147
        const val ITEM_STACK_RESPONSE_PACKET = 148
        const val PLAYER_ARMOR_DAMAGE_PACKET = 149
        const val CODE_BUILDER_PACKET = 150
        const val UPDATE_PLAYER_GAME_TYPE_PACKET = 151
        const val EMOTE_LIST_PACKET = 152
        const val POS_TRACKING_SERVER_BROADCAST_PACKET = 153
        const val POS_TRACKING_CLIENT_REQUEST_PACKET = 154
        const val DEBUG_INFO_PACKET = 155
        const val PACKET_VIOLATION_WARNING_PACKET = 156
        const val MOTION_PREDICTION_HINTS_PACKET = 157
        const val ANIMATE_ENTITY_PACKET = 158
        const val CAMERA_SHAKE_PACKET = 159
        const val PLAYER_FOG_PACKET = 160
        const val CORRECT_PLAYER_MOVE_PREDICTION_PACKET = 161
        const val ITEM_REGISTRY_PACKET = 162

        // UNUSED                                           = 163
        const val CLIENTBOUND_DEBUG_RENDERER_PACKET = 164
        const val SYNC_ENTITY_PROPERTY_PACKET = 165
        const val ADD_VOLUME_ENTITY_PACKET = 166
        const val REMOVE_VOLUME_ENTITY_PACKET = 167
        const val SIMULATION_TYPE_PACKET = 168
        const val NPC_DIALOGUE_PACKET = 169
        const val EDU_URI_RESOURCE_PACKET = 170
        const val CREATE_PHOTO_PACKET = 171
        const val UPDATE_SUB_CHUNK_BLOCKS_PACKET = 172

        // UNUSED                                           = 173
        const val SUB_CHUNK_PACKET = 174
        const val SUB_CHUNK_REQUEST_PACKET = 175
        const val PLAYER_START_ITEM_COOL_DOWN_PACKET = 176
        const val SCRIPT_MESSAGE_PACKET = 177
        const val CODE_BUILDER_SOURCE_PACKET = 178
        const val TICKING_AREAS_LOAD_STATUS_PACKET = 179
        const val DIMENSION_DATA_PACKET = 180
        const val AGENT_ACTION_EVENT_PACKET = 181
        const val CHANGE_MOB_PROPERTY_PACKET = 182
        const val LESSON_PROGRESS_PACKET = 183
        const val REQUEST_ABILITY_PACKET = 184
        const val REQUEST_PERMISSIONS_PACKET = 185
        const val TOAST_REQUEST_PACKET = 186
        const val UPDATE_ABILITIES_PACKET = 187
        const val UPDATE_ADVENTURE_SETTINGS_PACKET = 188
        const val DEATH_INFO_PACKET = 189
        const val EDITOR_NETWORK_PACKET = 190
        const val FEATURE_REGISTRY_PACKET = 191
        const val SERVER_STATS_PACKET = 192
        const val REQUEST_NETWORK_SETTINGS_PACKET = 193
        const val GAME_TEST_REQUEST_PACKET = 194
        const val GAME_TEST_RESULTS_PACKET = 195
        const val UPDATE_CLIENT_INPUT_LOCKS = 196

        // UNUSED                                           = 197
        const val CAMERA_PRESETS_PACKET = 198
        const val UNLOCKED_RECIPES_PACKET = 199

        // UNUSED                                           = 200
        // UNUSED                                           = ...
        // UNUSED                                           = 299
        const val CAMERA_INSTRUCTION_PACKET = 300

        // UNUSED                                           = 301
        const val TRIM_DATA = 302
        const val OPEN_SIGN = 303
        const val AGENT_ANIMATION = 304
        const val REFRESH_ENTITLEMENTS = 305
        const val TOGGLE_CRAFTER_SLOT_REQUEST = 306
        const val SET_PLAYER_INVENTORY_OPTIONS_PACKET = 307
        const val SET_HUD = 308
        const val AWARD_ACHIEVEMENT_PACKET = 309
        const val CLIENTBOUND_CLOSE_FORM_PACKET = 310

        // UNUSED                                           = 311
        const val SERVERBOUND_LOADING_SCREEN_PACKET = 312
        const val JIGSAW_STRUCTURE_DATA_PACKET = 313
        const val CURRENT_STRUCTURE_FEATURE_PACKET = 314
        const val SERVERBOUND_DIAGNOSTICS_PACKET = 315
        const val CAMERA_AIM_ASSIST_PACKET = 316
        const val CONTAINER_REGISTRY_CLEANUP_PACKET = 317
        const val MOVEMENT_EFFECT_PACKET = 318
        // UNUSED = 319
        const val CAMERA_AIM_ASSIST_PRESETS_PACKET = 320
        const val CLIENT_CAMERA_AIM_ASSIST_PACKET = 321
        const val CLIENT_MOVEMENT_PREDICTION_SYNC_PACKET = 322
        const val UPDATE_CLIENT_OPTIONS_PACKET = 323
        const val PLAYER_VIDEO_CAPTURE_PACKET = 324
        const val PLAYER_UPDATE_ENTITY_OVERRIDES_PACKET = 325
        const val PLAYER_LOCATION_PACKET = 326
        const val CLIENTBOUND_CONTROL_SCHEME_SET_PACKET = 327
        const val SERVER_SCRIPT_DEBUG_DRAWER_PACKET = 328
    }
}
