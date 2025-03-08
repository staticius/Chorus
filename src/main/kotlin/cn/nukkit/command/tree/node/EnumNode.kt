package cn.nukkit.command.tree.node

import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.tree.ParamList
import com.google.common.collect.Sets

/**
 * 解析为[String]值
 *
 *
 * 所有命令枚举类型如果没有手动指定[IParamNode],则会默认使用这个解析
 */
open class EnumNode : ParamNode<String>() {
    protected var commandEnum: CommandEnum? = null
    protected var enums: Set<String?>? = null

    override fun fill(arg: String) {
        if (commandEnum!!.isSoft) {
            this.value = arg
            return
        }
        if (enums!!.contains(arg)) this.value = arg
        else this.error()
    }

    override fun init(
        parent: ParamList?,
        name: String?,
        optional: Boolean,
        type: CommandParamType?,
        enumData: CommandEnum?,
        postFix: String?
    ): IParamNode<String> {
        this.paramList = parent
        this.commandEnum = enumData
        this.enums = Sets.newHashSet(commandEnum!!.values)
        this.optional = optional
        return this
    }
}
