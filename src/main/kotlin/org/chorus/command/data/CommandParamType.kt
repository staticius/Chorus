package org.chorus.command.data

import org.chorus.network.protocol.AvailableCommandsPacket

/**
 * @author CreeperFace
 */
enum class CommandParamType(@JvmField val id: Int) {
    INT(AvailableCommandsPacket.ARG_TYPE_INT),
    FLOAT(AvailableCommandsPacket.ARG_TYPE_FLOAT),
    VALUE(AvailableCommandsPacket.ARG_TYPE_VALUE),
    WILDCARD_INT(AvailableCommandsPacket.ARG_TYPE_WILDCARD_INT),
    TARGET(AvailableCommandsPacket.ARG_TYPE_TARGET),
    WILDCARD_TARGET(AvailableCommandsPacket.ARG_TYPE_WILDCARD_TARGET),
    EQUIPMENT_SLOT(AvailableCommandsPacket.ARG_TYPE_EQUIPMENT_SLOT),
    STRING(AvailableCommandsPacket.ARG_TYPE_STRING),
    BLOCK_POSITION(AvailableCommandsPacket.ARG_TYPE_BLOCK_POSITION),
    POSITION(AvailableCommandsPacket.ARG_TYPE_POSITION),
    MESSAGE(AvailableCommandsPacket.ARG_TYPE_MESSAGE),
    RAWTEXT(AvailableCommandsPacket.ARG_TYPE_RAWTEXT),
    JSON(AvailableCommandsPacket.ARG_TYPE_JSON),
    TEXT(AvailableCommandsPacket.ARG_TYPE_RAWTEXT),  // backwards compatibility
    COMMAND(AvailableCommandsPacket.ARG_TYPE_COMMAND),
    FILE_PATH(AvailableCommandsPacket.ARG_TYPE_FILE_PATH),
    OPERATOR(AvailableCommandsPacket.ARG_TYPE_OPERATOR),
    COMPARE_OPERATOR(AvailableCommandsPacket.ARG_TYPE_COMPARE_OPERATOR),
    FULL_INTEGER_RANGE(AvailableCommandsPacket.ARG_TYPE_FULL_INTEGER_RANGE),
    BLOCK_STATES(AvailableCommandsPacket.ARG_TYPE_BLOCK_STATES)
}
