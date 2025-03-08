package cn.nukkit.command.selector.args.impl

/**
 * args like x,y,z.
 */
abstract class CoordinateArgument : ISelectorArgument {
    val priority: Int
        get() = 1
}
