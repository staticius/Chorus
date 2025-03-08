package cn.nukkit.command.data

class CommandInput {
    @JvmField
    var parameters: Array<CommandParameter?> = CommandParameter.Companion.EMPTY_ARRAY
}