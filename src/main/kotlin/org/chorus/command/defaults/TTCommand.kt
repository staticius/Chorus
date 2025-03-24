package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.PlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.inventory.fake.FakeStructBlock
import kotlin.collections.set

class TTCommand(name: String) : TestCommand(name, "tt") {
    var fakeStructBlock: FakeStructBlock? = null

    init {
        commandParameters.clear()
        commandParameters["default"] = arrayOf(
            CommandParameter.Companion.newEnum("sub", arrayOf<String?>("get", "set")),
            CommandParameter.Companion.newType("name", false, CommandParamType.TARGET, PlayersNode()),
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val value = result.value
        val s = value!!.getResult<String>(0)

        if (sender.isOp) {
            val p = sender.asPlayer()
            when (s) {
                "get" -> {
                    val itemInHand = p!!.inventory.itemInHand
                    itemInHand.damage = 0
                    p.inventory.setItemInHand(itemInHand)
                }

                "set" -> {
                }
            }
            return 1
        } else return 0
    }
}
