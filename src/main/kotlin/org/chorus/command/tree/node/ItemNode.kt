package org.chorus.command.tree.node

import org.chorus.item.Item

/**
 * 解析对应参数为[Item]值
 *
 *
 * 所有命令枚举[ENUM_ITEM][cn.nukkit.command.data.CommandEnum.ENUM_ITEM]如果没有手动指定[IParamNode],则会默认使用这个解析
 */
class ItemNode : ParamNode<Item?>() {
    override fun fill(arg: String) {
        var arg = arg
        if (arg.indexOf(':') == -1) {
            arg = "minecraft:$arg"
        }
        val item = Item.get(arg)
        if (item.isNull) {
            error()
            return
        }
        this.value = item
    }
}
