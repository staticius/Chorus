package org.chorus.command.tree.node

import org.chorus.IPlayer
import org.chorus.Server
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.command.selector.EntitySelectorAPI
import org.chorus.entity.Entity
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
            val player = Server.instance.getOfflinePlayer(arg)
            if (player != null) {
                this.value = listOf(player)
            } else error("commands.generic.player.notFound")
        }
    }
}
