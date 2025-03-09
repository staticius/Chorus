package org.chorus.network.protocol.types.itemstack.request

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap

enum class TextProcessingEventOrigin(val id: Int) {
    SERVER_CHAT_PUBLIC(0),
    SERVER_CHAT_WHISPER(1),
    SIGN_TEXT(2),
    ANVIL_TEXT(3),
    BOOK_AND_QUILL_TEXT(4),
    COMMAND_BLOCK_TEXT(5),
    BLOCK_ENTITY_DATA_TEXT(6),
    JOIN_EVENT_TEXT(7),
    LEAVE_EVENT_TEXT(8),
    SLASH_COMMAND_TEXT(9),
    CARTOGRAPHY_TEXT(10),
    SLASH_COMMAND_NON_CHAT(11),
    SCOREBOARD_TEXT(12),
    TICKING_AREA_TEXT(13);


    companion object {
        private val VALUES = Int2ObjectArrayMap<TextProcessingEventOrigin>()

        init {
            for (v in entries) {
                VALUES.put(v.id, v)
            }
        }

        fun fromId(id: Int): TextProcessingEventOrigin {
            return VALUES[id]
        }
    }
}