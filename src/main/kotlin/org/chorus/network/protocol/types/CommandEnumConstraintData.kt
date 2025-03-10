package org.chorus.network.protocol.types

import org.chorus.command.data.CommandEnum


/**
 * CommandEnumConstraintData is sent in the AvailableCommandsPacket to limit what values of an enum may be used
 * taking in account things such as whether cheats are enabled.
 */

class CommandEnumConstraintData {
    // The option in an enum that the constraints should be applied to.
    var option: String? = null

    // The name of the enum of which the option above should be constrained.
    var enumData: CommandEnum? = null

    // List of constraints that should be applied to the enum option.
    var constraints: Array<CommandEnumConstraintType>
}
