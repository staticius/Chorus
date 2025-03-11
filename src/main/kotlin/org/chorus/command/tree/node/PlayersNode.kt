package org.chorus.command.tree.node

import org.chorus.Player
import org.chorus.Server
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.command.selector.EntitySelectorAPI
import org.chorus.entity.Entity
import java.util.stream.Collectors

/**
 * 解析为`List<Player>`值
 *
 *
 * 不会默认使用，需要手动指定
 */
class PlayersNode : TargetNode<Player?>() {
    //todo 支持uuid 或者 xuid
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
                entities.stream().filter { entity: Entity? -> entity is Player }
                    .map { entity: Entity -> entity as Player }
                    .collect(Collectors.toList())
            this.value = result
        } else {
            this.value = listOf(Server.instance.getPlayer(arg))
        }
    }
}
