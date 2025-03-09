package org.chorus.command.data

import cn.nukkit.command.data.GenericParameter.CommandParameterSupplier
import cn.nukkit.command.tree.node.ChainedCommandNode
import cn.nukkit.command.tree.node.ItemNode

interface GenericParameter {
    fun interface CommandParameterSupplier<T> {
        fun get(optional: Boolean): T
    }

    companion object {
        val OBJECTIVES: CommandParameterSupplier<CommandParameter> =
            CommandParameterSupplier<CommandParameter> { optional: Boolean ->
                CommandParameter.Companion.newEnum(
                    "objective",
                    optional,
                    CommandEnum.Companion.SCOREBOARD_OBJECTIVES
                )
            }
        val TARGET_OBJECTIVES: CommandParameterSupplier<CommandParameter> =
            CommandParameterSupplier<CommandParameter> { optional: Boolean ->
                CommandParameter.Companion.newEnum(
                    "targetObjective",
                    optional,
                    CommandEnum.Companion.SCOREBOARD_OBJECTIVES
                )
            }
        val ITEM_NAME: CommandParameterSupplier<CommandParameter> =
            CommandParameterSupplier<CommandParameter> { optional: Boolean ->
                CommandParameter.Companion.newEnum(
                    "itemName",
                    optional,
                    CommandEnum.Companion.ENUM_ITEM,
                    ItemNode()
                )
            }
        val CHAINED_COMMAND: CommandParameterSupplier<CommandParameter> =
            CommandParameterSupplier<CommandParameter> { optional: Boolean ->
                CommandParameter.Companion.newEnum(
                    "chainedCommand",
                    optional,
                    CommandEnum.Companion.CHAINED_COMMAND_ENUM,
                    ChainedCommandNode(),
                    CommandParamOption.ENUM_AS_CHAINED_COMMAND
                )
            }
        val ORIGIN: CommandParameterSupplier<CommandParameter> =
            CommandParameterSupplier<CommandParameter> { optional: Boolean ->
                CommandParameter.Companion.newType(
                    "origin",
                    optional,
                    CommandParamType.TARGET
                )
            }
    }
}
