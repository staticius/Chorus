package org.chorus.command.tree.node


/**
 * 解析为[String]值
 *
 *
 * 所有命令参数类型为[TEXT][cn.nukkit.command.data.CommandParamType.TEXT] , [STRING][cn.nukkit.command.data.CommandParamType.STRING] ,
 * [FILE_PATH][cn.nukkit.command.data.CommandParamType.FILE_PATH]的
 * 如果没有手动指定[IParamNode],则会默认使用这个解析
 */
open class StringNode : ParamNode<String?>() {
    override fun fill(arg: String) {
        this.value = arg
    }
}
