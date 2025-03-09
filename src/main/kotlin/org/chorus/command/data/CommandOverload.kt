package org.chorus.command.data

class CommandOverload {
    @JvmField
    var input: CommandInput = CommandInput()

    @JvmField
    var chaining: Boolean = false
}
