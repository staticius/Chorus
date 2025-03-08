package cn.nukkit.command.tree.node

import cn.nukkit.Server
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.command.selector.EntitySelectorAPI
import cn.nukkit.entity.Entity
import com.google.common.collect.Lists

/**
 * 解析为`List<Entity>`值
 *
 *
 * 所有命令参数类型为[TARGET][cn.nukkit.command.data.CommandParamType.TARGET]如果没有手动指定[IParamNode],则会默认使用这个解析
 */
class EntitiesNode : TargetNode<Entity?>() {
    //todo 支持uuid 或者 xuid
    override fun fill(arg: String) {
        val entities: MutableList<Entity?>
        if (arg.isBlank()) {
            this.error()
        } else if (EntitySelectorAPI.Companion.getAPI().checkValid(arg)) {
            try {
                entities = EntitySelectorAPI.Companion.getAPI().matchEntities(paramList.paramTree.sender, arg)
            } catch (exception: SelectorSyntaxException) {
                error(exception.message)
                return
            }
            this.value = entities
        } else {
            entities = Lists.newArrayList()
            val player = Server.getInstance().getPlayer(arg)
            if (player != null) {
                entities.add(player)
            }
            this.value = entities
        }
    }
}
