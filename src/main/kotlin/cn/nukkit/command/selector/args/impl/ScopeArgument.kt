package cn.nukkit.command.selector.args.impl

import cn.nukkit.command.CommandSender

/**
 * args like dx,dy,dz.
 */
abstract class ScopeArgument : ISelectorArgument {
    val priority: Int
        get() = 2

    override fun getDefaultValue(
        values: Map<String?, List<String?>?>,
        selectorType: SelectorType,
        sender: CommandSender?
    ): String? {
        if (values.containsKey("dx") || values.containsKey("dy") || values.containsKey("dz")) return "0"
        return null
    }
}
