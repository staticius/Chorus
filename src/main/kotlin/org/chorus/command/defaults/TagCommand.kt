package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.entity.Entity
import org.chorus.nbt.tag.StringTag
import java.util.stream.Collectors
import kotlin.collections.set

class TagCommand(name: String) : VanillaCommand(name, "commands.tag.description") {
    init {
        this.permission = "nukkit.command.tag"
        commandParameters.clear()
        commandParameters["add"] = arrayOf(
            CommandParameter.Companion.newType("targets", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("add", arrayOf("add")),
            CommandParameter.Companion.newType("name", CommandParamType.STRING)
        )
        commandParameters["remove"] = arrayOf(
            CommandParameter.Companion.newType("targets", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("remove", arrayOf("remove")),
            CommandParameter.Companion.newType("name", CommandParamType.STRING)
        )
        commandParameters["list"] = arrayOf(
            CommandParameter.Companion.newType("targets", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("list", arrayOf("list")),
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val entities = list.getResult<List<Entity>>(0)!!
        if (entities.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        when (result.key) {
            "add" -> {
                val tag = list.getResult<String>(2)!!
                var success_count = 0
                for (entity in entities) {
                    if (entity.containTag(tag)) continue
                    entity.addTag(tag)
                    success_count++
                }
                if (success_count == 0) {
                    log.addError("commands.tag.add.failed").output()
                    return 0
                }
                if (entities.size == 1) {
                    log.addSuccess("commands.tag.add.success.single", tag, entities[0].getName())
                } else {
                    log.addSuccess("commands.tag.add.success.multiple", tag, entities.size.toString())
                }
                log.output()
                return 1
            }

            "remove" -> {
                val tag = list.getResult<String>(2)!!
                var success_count = 0
                for (entity in entities) {
                    if (!entity.containTag(tag)) continue
                    entity.removeTag(tag)
                    success_count++
                }
                if (success_count == 0) {
                    log.addError("commands.tag.remove.failed").output()
                    return 0
                }
                if (entities.size == 1) {
                    log.addSuccess("commands.tag.remove.success.single", tag, entities[0].getName())
                } else {
                    log.addSuccess("commands.tag.remove.success.multiple", tag, entities.size.toString())
                }
                log.output()
                return 1
            }

            "list" -> {
                val tagSet: MutableSet<String> = HashSet()
                for (entity in entities) {
                    tagSet.addAll(entity.getAllTags().map { t: StringTag -> t.data }.toSet())
                }
                val tagCount = tagSet.size
                val tagStr = tagSet.stream().collect(Collectors.joining(" "))

                if (tagStr.isEmpty()) {
                    if (entities.size == 1) {
                        log.addError("commands.tag.list.single.empty", entities[0].getName())
                    } else {
                        log.addError("commands.tag.list.multiple.empty", entities.size.toString())
                    }
                    log.output()
                    return 0
                } else {
                    if (entities.size == 1) {
                        log.addSuccess(
                            "commands.tag.list.single.success",
                            entities[0].getName(),
                            tagCount.toString(),
                            tagStr
                        )
                    } else {
                        log.addSuccess(
                            "commands.tag.list.multiple.success",
                            entities.size.toString(),
                            tagCount.toString(),
                            tagStr
                        )
                    }
                    log.output()
                    return 1
                }
            }

            else -> {
                return 0
            }
        }
    }
}
