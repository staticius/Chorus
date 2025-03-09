package org.chorus.level.format

/**
 * Allay Project 2023/9/10
 *
 * @author daoge_cmd
 */
enum class ChunkState {
    NEW,
    GENERATED,
    POPULATED,
    FINISHED;

    fun canSend(): Boolean {
        return this.ordinal >= 2
    }
}
