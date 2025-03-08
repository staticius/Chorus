package cn.nukkit.command.data

class CommandOverload {
    @JvmField
    var input: CommandInput = CommandInput()

    @JvmField
    var chaining: Boolean = false
}
