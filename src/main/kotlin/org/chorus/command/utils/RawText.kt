package org.chorus.command.utils

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.CommandSender
import cn.nukkit.command.selector.EntitySelectorAPI
import cn.nukkit.entity.Entity
import cn.nukkit.scoreboard.scorer.EntityScorer
import cn.nukkit.scoreboard.scorer.FakeScorer
import cn.nukkit.scoreboard.scorer.IScorer
import cn.nukkit.scoreboard.scorer.PlayerScorer
import cn.nukkit.utils.JSONUtils
import com.google.gson.annotations.SerializedName
import lombok.Getter
import lombok.Setter
import lombok.SneakyThrows
import java.util.stream.Collectors

@Getter
class RawText private constructor(base: org.chorus.command.utils.RawText.Component) {
    private var base: org.chorus.command.utils.RawText.Component? = null

    init {
        this.base = base
    }

    fun preParse(sender: CommandSender) {
        org.chorus.command.utils.RawText.Companion.preParse(sender, base!!)
    }

    fun toRawText(): String {
        return JSONUtils.to(base)
    }

    @Getter
    @Setter
    class Component {
        @SerializedName("text")
        private val component_text: String? = null

        @SerializedName("selector")
        val component_selector: String? = null

        @SerializedName("translate")
        private val component_translate: String? = null

        @SerializedName("with")
        var component_translate_with: Any? = null

        @SerializedName("score")
        private val component_score: org.chorus.command.utils.RawText.Component.ScoreComponent? = null

        @SerializedName("rawtext")
        val component_rawtext: MutableList<org.chorus.command.utils.RawText.Component>? = null

        private class ScoreComponent {
            @SerializedName("name")
            val name: String? = null

            @SerializedName("objective")
            val objective: String? = null

            @SerializedName("value")
            val value: Int? = null
        }

        enum class ComponentType {
            TEXT,
            SELECTOR,
            TRANSLATE,
            TRANSLATE_WITH,
            SCORE,
            RAWTEXT
        }

        val type: org.chorus.command.utils.RawText.Component.ComponentType?
            get() {
                if (component_text != null) {
                    return org.chorus.command.utils.RawText.Component.ComponentType.TEXT
                }
                if (component_selector != null) {
                    return org.chorus.command.utils.RawText.Component.ComponentType.SELECTOR
                }
                if (component_translate != null) {
                    if (component_translate_with != null) {
                        return org.chorus.command.utils.RawText.Component.ComponentType.TRANSLATE_WITH
                    }
                    return org.chorus.command.utils.RawText.Component.ComponentType.TRANSLATE
                }
                if (component_score != null) {
                    if (component_score.name != null && component_score.objective != null) {
                        return org.chorus.command.utils.RawText.Component.ComponentType.SCORE
                    }
                }
                if (component_rawtext != null) {
                    return org.chorus.command.utils.RawText.Component.ComponentType.RAWTEXT
                }
                return null
            }
    }

    override fun toString(): String {
        return JSONUtils.to(this.base)
    }

    companion object {
        fun fromRawText(rawText: String?): org.chorus.command.utils.RawText {
            val base = JSONUtils.from(
                rawText,
                org.chorus.command.utils.RawText.Component::class.java
            )
            return org.chorus.command.utils.RawText(base)
        }

        private fun preParse(sender: CommandSender, cps: org.chorus.command.utils.RawText.Component) {
            if (cps.type != org.chorus.command.utils.RawText.Component.ComponentType.RAWTEXT) return
            val components = cps.component_rawtext
            for (component in components!!.toTypedArray<org.chorus.command.utils.RawText.Component>()) {
                if (component.type == org.chorus.command.utils.RawText.Component.ComponentType.SCORE) {
                    val newComponent = org.chorus.command.utils.RawText.Companion.preParseScore(component, sender)
                    if (newComponent != null) components[components.indexOf(component)] = newComponent
                    else components.remove(component)
                }
                if (component.type == org.chorus.command.utils.RawText.Component.ComponentType.SELECTOR) {
                    val newComponent = org.chorus.command.utils.RawText.Companion.preParseSelector(component, sender)
                    if (newComponent != null) components[components.indexOf(component)] = newComponent
                    else components.remove(component)
                }
                if (component.type == org.chorus.command.utils.RawText.Component.ComponentType.RAWTEXT) {
                    org.chorus.command.utils.RawText.Companion.preParse(sender, component)
                }
                if (component.type == org.chorus.command.utils.RawText.Component.ComponentType.TRANSLATE_WITH) {
                    if (component.component_translate_with is Map<*, *>) {
                        val cp = JSONUtils.from(
                            JSONUtils.to(component.component_translate_with),
                            org.chorus.command.utils.RawText.Component::class.java
                        )
                        org.chorus.command.utils.RawText.Companion.preParse(sender, cp)
                        component.component_translate_with = cp
                    }
                }
            }
        }

        @SneakyThrows
        private fun preParseScore(component: org.chorus.command.utils.RawText.Component, sender: CommandSender): org.chorus.command.utils.RawText.Component? {
            val scoreboard = Server.getInstance().scoreboardManager.getScoreboard(component.component_score!!.objective)
                ?: return null
            val name_str = component.component_score.name
            var scorer: IScorer? = null
            var value = component.component_score.value

            if (name_str == "*") {
                if (!sender.isEntity) return null
                scorer = if (sender.isPlayer) PlayerScorer(sender.asPlayer()) else EntityScorer(sender.asEntity())
            } else if (EntitySelectorAPI.Companion.getAPI().checkValid(name_str)) {
                val scorers: List<IScorer> =
                    EntitySelectorAPI.Companion.getAPI().matchEntities(sender, name_str).stream()
                        .map<IScorer> { t: Entity? ->
                            if (t is Player) PlayerScorer(
                                t
                            ) else EntityScorer(t)
                        }.toList()
                if (scorers.isEmpty()) return null
                scorer = scorers[0]
            } else if (Server.getInstance().getPlayer(name_str) != null) {
                scorer = PlayerScorer(Server.getInstance().getPlayer(name_str))
            } else {
                scorer = FakeScorer(name_str)
            }

            if (scorer == null) return null
            if (value == null) value = scoreboard.getLine(scorer)!!.score
            val newComponent = org.chorus.command.utils.RawText.Component()
            newComponent.setComponent_text(value.toString())
            return newComponent
        }

        private fun preParseSelector(component: org.chorus.command.utils.RawText.Component, sender: CommandSender): org.chorus.command.utils.RawText.Component? {
            val entities: List<Entity>
            try {
                entities = EntitySelectorAPI.Companion.getAPI().matchEntities(sender, component.component_selector)
            } catch (e: Exception) {
                return null
            }
            if (entities.isEmpty()) return null
            val entities_str = entities.stream().map { obj: Entity -> obj.name }.collect(Collectors.joining(", "))
            val newComponent = org.chorus.command.utils.RawText.Component()
            newComponent.setComponent_text(entities_str)
            return newComponent
        }
    }
}