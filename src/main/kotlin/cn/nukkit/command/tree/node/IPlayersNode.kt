package cn.nukkit.command.tree.node

import cn.nukkit.IPlayer
import cn.nukkit.Server
import cn.nukkit.command.exceptions.SelectorSyntaxException
import cn.nukkit.command.selector.EntitySelectorAPI
import cn.nukkit.entity.Entity
import java.util.stream.Collectors

/**
 * 解析为`List<IPlayer>`值
 *
 *
 * 不会默认使用，需要手动指定
 */
class IPlayersNode : ParamNode<List<IPlayer?>?>() {
    override fun fill(arg: String) {
        if (arg.isBlank()) {
            this.error()
        } else if (EntitySelectorAPI.Companion.getAPI().checkValid(arg)) {
            val entities: List<Entity>
            try {
                entities = EntitySelectorAPI.Companion.getAPI().matchEntities(paramList.paramTree.sender, arg)
            } catch (exception: SelectorSyntaxException) {
                error(exception.message)
                return
            }
            val result =
                entities.stream().filter { entity: Entity? -> entity is IPlayer }
                    .map { entity: Entity -> entity as IPlayer }
                    .collect(Collectors.toList())
            if (!result.isEmpty()) this.value = result
            else error("commands.generic.noTargetMatch")
        } else {
            val player = Server.getInstance().getOfflinePlayer(arg)
            if (player != null) {
                this.value = listOf(player)
            } else error("commands.generic.player.notFound")
        }
    }
}
