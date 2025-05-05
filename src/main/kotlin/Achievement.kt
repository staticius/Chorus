package org.chorus_oss.chorus

import org.chorus_oss.chorus.config.ServerPropertiesKeys
import org.chorus_oss.chorus.utils.TextFormat

class Achievement(val message: String, vararg requires: String) {
    val requires: Array<String> = arrayOf(*requires)

    fun broadcast(player: Player) {
        val translation: String = Server.instance.baseLang
            .tr("chat.type.achievement", player.displayName, TextFormat.GREEN.toString() + this.message)

        if (Server.instance.properties
                .get(ServerPropertiesKeys.ANNOUNCE_PLAYER_ACHIEVEMENTS, true)
        ) {
            Server.instance.broadcastMessage(translation)
        } else {
            player.sendMessage(translation)
        }
    }

    companion object {
        val achievements: HashMap<String, Achievement> = object : HashMap<String, Achievement>() {
            init {
                put("mineWood", Achievement("Getting Wood"))
                put("buildWorkBench", Achievement("Benchmarking", "mineWood"))
                put("buildPickaxe", Achievement("Time to Mine!", "buildWorkBench"))
                put("buildFurnace", Achievement("Hot Topic", "buildPickaxe"))
                put("acquireIron", Achievement("Acquire hardware", "buildFurnace"))
                put("buildHoe", Achievement("Time to Farm!", "buildWorkBench"))
                put("makeBread", Achievement("Bake Bread", "buildHoe"))
                put("bakeCake", Achievement("The Lie", "buildHoe"))
                put("buildBetterPickaxe", Achievement("Getting an Upgrade", "buildPickaxe"))
                put("buildSword", Achievement("Time to Strike!", "buildWorkBench"))
                put("diamonds", Achievement("DIAMONDS!", "acquireIron"))
            }
        }

        fun broadcast(player: Player, achievementId: String): Boolean {
            if (!achievements.containsKey(achievementId)) {
                return false
            }
            val translation: String = Server.instance.baseLang.tr(
                "chat.type.achievement",
                player.displayName,
                TextFormat.GREEN.toString() + achievements[achievementId]!!.message + TextFormat.RESET
            )

            if (Server.instance.properties
                    .get(ServerPropertiesKeys.ANNOUNCE_PLAYER_ACHIEVEMENTS, true)
            ) {
                Server.instance.broadcastMessage(translation)
            } else {
                player.sendMessage(translation)
            }
            return true
        }

        fun add(name: String, achievement: Achievement): Boolean {
            if (achievements.containsKey(name)) {
                return false
            }

            achievements[name] = achievement
            return true
        }
    }
}
