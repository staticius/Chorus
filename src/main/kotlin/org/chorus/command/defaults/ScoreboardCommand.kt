package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.Server
import org.chorus.camera.instruction.impl.ClearInstruction.get
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamOption
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.command.selector.EntitySelectorAPI
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.WildcardIntNode
import org.chorus.command.utils.CommandLogger
import org.chorus.entity.Entity
import org.chorus.scoreboard.IScoreboard
import org.chorus.scoreboard.data.SortOrder
import org.chorus.scoreboard.scorer.EntityScorer
import org.chorus.scoreboard.scorer.FakeScorer
import org.chorus.scoreboard.scorer.IScorer
import org.chorus.scoreboard.scorer.PlayerScorer
import org.chorus.utils.TextFormat
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import kotlin.math.max
import kotlin.math.min

class ScoreboardCommand(name: String) :
    VanillaCommand(name, "commands.scoreboard.description", "commands.scoreboard.usage") {
    init {
        this.permission = "nukkit.command.scoreboard"
        commandParameters.clear()
        commandParameters["objectives-add"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum(
                    "ScoreboardObjectivesCategory",
                    listOf<String>("objectives"),
                    false
                )
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardAddAction", listOf<String>("add"), false)
            ),
            GenericParameter.Companion.OBJECTIVES.get(false),
            CommandParameter.Companion.newEnum(
                "criteria",
                false,
                CommandEnum("ScoreboardCriteria", listOf<String>("dummy"), false)
            ),
            CommandParameter.Companion.newType("displayName", true, CommandParamType.STRING)
        )
        commandParameters["objectives-list"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum(
                    "ScoreboardObjectivesCategory",
                    listOf<String>("objectives"),
                    false
                )
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardListAction", listOf<String>("list"), false)
            ),
        )
        commandParameters["objectives-remove"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum(
                    "ScoreboardObjectivesCategory",
                    listOf<String>("objectives"),
                    false
                )
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardRemoveAction", listOf<String>("remove"), false)
            ),
            GenericParameter.Companion.OBJECTIVES.get(false),
        )
        commandParameters["objectives-setdisplay-list-sidebar"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum(
                    "ScoreboardObjectivesCategory",
                    listOf<String>("objectives"),
                    false
                )
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardSetDisplayAction", listOf<String>("setdisplay"), false)
            ),
            CommandParameter.Companion.newEnum(
                "displaySlot",
                false,
                CommandEnum(
                    "ScoreboardDisplaySlotSortable",
                    listOf<String>("list", "sidebar"),
                    false
                ),
                CommandParamOption.SUPPRESS_ENUM_AUTOCOMPLETION
            ),
            GenericParameter.Companion.OBJECTIVES.get(true),
            CommandParameter.Companion.newEnum(
                "sortOrder",
                true,
                CommandEnum(
                    "ScoreboardSortOrder",
                    listOf<String>("ascending", "descending"),
                    false
                ),
                CommandParamOption.SUPPRESS_ENUM_AUTOCOMPLETION
            ),
        )
        commandParameters["objectives-setdisplay-belowname"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum(
                    "ScoreboardObjectivesCategory",
                    listOf<String>("objectives"),
                    false
                )
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardSetDisplayAction", listOf<String>("setdisplay"), false)
            ),
            CommandParameter.Companion.newEnum(
                "displaySlot",
                false,
                CommandEnum(
                    "ScoreboardDisplaySlotNonSortable",
                    listOf<String>("belowname"),
                    false
                )
            ),
            GenericParameter.Companion.OBJECTIVES.get(true)
        )
        commandParameters["players-add-remove-set"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum("ScoreboardPlayersCategory", listOf<String>("players"), false)
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum(
                    "ScoreboardPlayersNumAction",
                    listOf<String>("add", "remove", "set"),
                    false
                )
            ),
            CommandParameter.Companion.newType("player", false, CommandParamType.WILDCARD_TARGET),  //allow *
            GenericParameter.Companion.TARGET_OBJECTIVES.get(false),
            CommandParameter.Companion.newType("count", CommandParamType.INT)
        )
        commandParameters["players-list"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum("ScoreboardPlayersCategory", listOf<String>("players"), false)
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardListAction", listOf<String>("list"), false)
            ),
            CommandParameter.Companion.newType("playername", true, CommandParamType.WILDCARD_TARGET) //allow *
        )
        commandParameters["players-operation"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum("ScoreboardPlayersCategory", listOf<String>("players"), false)
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardOperationAction", listOf<String>("operation"), false)
            ),
            CommandParameter.Companion.newType("targetName", CommandParamType.WILDCARD_TARGET),  //allow *
            GenericParameter.Companion.TARGET_OBJECTIVES.get(false),
            CommandParameter.Companion.newType("operation", CommandParamType.OPERATOR),
            CommandParameter.Companion.newType("selector", CommandParamType.WILDCARD_TARGET),
            GenericParameter.Companion.OBJECTIVES.get(false),
        )
        commandParameters["players-random"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum("ScoreboardPlayersCategory", listOf<String>("players"), false)
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardRandomAction", listOf<String>("random"), false)
            ),
            CommandParameter.Companion.newType("player", false, CommandParamType.WILDCARD_TARGET),  //allow *
            GenericParameter.Companion.OBJECTIVES.get(false),
            CommandParameter.Companion.newType(
                "min",
                false,
                CommandParamType.WILDCARD_INT,
                WildcardIntNode(Int.MIN_VALUE)
            ),
            CommandParameter.Companion.newType(
                "max",
                false,
                CommandParamType.WILDCARD_INT,
                WildcardIntNode(Int.MAX_VALUE)
            )
        )
        commandParameters["players-reset"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum("ScoreboardPlayersCategory", listOf<String>("players"), false)
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardResetAction", listOf<String>("reset"), false)
            ),
            CommandParameter.Companion.newType("player", false, CommandParamType.WILDCARD_TARGET),  //allow *
            GenericParameter.Companion.OBJECTIVES.get(true),
        )
        commandParameters["players-test"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "category",
                false,
                CommandEnum("ScoreboardPlayersCategory", listOf<String>("players"), false)
            ),
            CommandParameter.Companion.newEnum(
                "action",
                false,
                CommandEnum("ScoreboardTestAction", listOf<String>("test"), false)
            ),
            CommandParameter.Companion.newType("player", false, CommandParamType.WILDCARD_TARGET),  //allow *
            GenericParameter.Companion.OBJECTIVES.get(false),
            CommandParameter.Companion.newType(
                "min",
                false,
                CommandParamType.WILDCARD_INT,
                WildcardIntNode(Int.MIN_VALUE)
            ),
            CommandParameter.Companion.newType(
                "max",
                true,
                CommandParamType.WILDCARD_INT,
                WildcardIntNode(Int.MAX_VALUE)
            )
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
        val manager: IScoreboardManager = Server.getInstance().scoreboardManager
        try {
            when (result.key) {
                "objectives-add" -> {
                    val objectiveName = list.getResult<String>(2)
                    if (manager.containScoreboard(objectiveName)) {
                        log.addError("commands.scoreboard.objectives.add.alreadyExists", objectiveName).output()
                        return 0
                    }
                    val criteriaName = list.getResult<String>(3)
                    if (list.hasResult(4)) {
                        manager.addScoreboard(
                            Scoreboard(
                                objectiveName,
                                list.getResult<String>(4),
                                criteriaName,
                                SortOrder.ASCENDING
                            )
                        )
                    } else {
                        manager.addScoreboard(
                            Scoreboard(
                                objectiveName,
                                objectiveName,
                                criteriaName,
                                SortOrder.ASCENDING
                            )
                        )
                    }
                    log.addSuccess("commands.scoreboard.objectives.add.success", objectiveName).output()
                    return 1
                }

                "objectives-list" -> {
                    log.addSuccess(
                        TextFormat.GREEN.toString() + "%commands.scoreboard.objectives.list.count",
                        manager.getScoreboards().size.toString()
                    )
                    for (scoreboard in manager.getScoreboards().values) {
                        log.addSuccess(
                            "commands.scoreboard.objectives.list.entry",
                            scoreboard.objectiveName,
                            scoreboard.displayName,
                            scoreboard.criteriaName
                        )
                    }
                    log.output(true)
                    return 1
                }

                "objectives-remove" -> {
                    val objectiveName = list.getResult<String>(2)
                    if (!manager.containScoreboard(objectiveName)) {
                        log.addError("commands.scoreboard.objectiveNotFound", objectiveName).output()
                        return 0
                    }
                    if (manager.removeScoreboard(objectiveName)) {
                        log.addSuccess("commands.scoreboard.objectives.remove.success", objectiveName).output()
                    }
                    return 1
                }

                "objectives-setdisplay-list-sidebar" -> {
                    val slotName = list.getResult<String>(2)
                    val slot: DisplaySlot = when (slotName) {
                        "sidebar" -> DisplaySlot.SIDEBAR
                        "list" -> DisplaySlot.LIST
                        else -> DisplaySlot.SIDEBAR
                    }

                    if (!list.hasResult(3)) {
                        manager.setDisplay(slot, null)
                        log.addSuccess("commands.scoreboard.objectives.setdisplay.successCleared", slot.getSlotName())
                            .output()
                    } else {
                        val objectiveName = list.getResult<String>(3)
                        if (!manager.containScoreboard(objectiveName)) {
                            log.addError("commands.scoreboard.objectiveNotFound", objectiveName).output()
                            return 0
                        }
                        val scoreboard: IScoreboard = manager.getScoreboards().get(objectiveName)
                        val orderName = list.getResult<String>(4)
                        val order = if (list.hasResult(4)) when (orderName) {
                            "ascending" -> SortOrder.ASCENDING
                            "descending" -> SortOrder.DESCENDING
                            else -> SortOrder.ASCENDING
                        } else SortOrder.ASCENDING
                        scoreboard.sortOrder = order
                        manager.setDisplay(slot, scoreboard)
                        log.addSuccess(
                            "commands.scoreboard.objectives.setdisplay.successSet",
                            slot.getSlotName(),
                            objectiveName
                        ).output()
                    }
                    return 1
                }

                "objectives-setdisplay-belowname" -> {
                    if (!list.hasResult(3)) {
                        manager.setDisplay(DisplaySlot.BELOW_NAME, null)
                        log.addSuccess(
                            "commands.scoreboard.objectives.setdisplay.successCleared",
                            DisplaySlot.BELOW_NAME.getSlotName()
                        ).output()
                        return 1
                    } else {
                        val objectiveName = list.getResult<String>(3)
                        if (!manager.containScoreboard(objectiveName)) {
                            log.addError("commands.scoreboard.objectiveNotFound", objectiveName).output()
                            return 0
                        }
                        manager.setDisplay(DisplaySlot.BELOW_NAME, manager.getScoreboard(objectiveName))
                        log.addSuccess(
                            "commands.scoreboard.objectives.setdisplay.successSet",
                            DisplaySlot.BELOW_NAME.getSlotName(),
                            objectiveName
                        ).output()
                        return 1
                    }
                }

                "players-add-remove-set" -> {
                    return this.playersCRUD(list, sender, manager, log)
                }

                "players-list" -> {
                    if (manager.getScoreboards().isEmpty()) {
                        log.addError("commands.scoreboard.players.list.empty").output()
                        return 0
                    }
                    if (list.hasResult(2)) {
                        val wildcard_target_str = list.getResult<String>(2)
                        val scorers = parseScorers(sender, wildcard_target_str!!)
                        if (scorers.isEmpty()) {
                            log.addError("commands.scoreboard.players.list.empty").output()
                            return 0
                        }
                        for (scorer in scorers) {
                            var find = false
                            var count = 0
                            for (scoreboard in manager.getScoreboards().values) {
                                if (scoreboard.lines.containsKey(scorer)) {
                                    find = true
                                    count++
                                }
                            }
                            if (!find) {
                                log.addError("commands.scoreboard.players.list.player.empty", scorer.name).output()
                                return 0
                            }
                            log.addSuccess(
                                TextFormat.GREEN.toString() + "%commands.scoreboard.players.list.player.count",
                                count.toString(),
                                scorer.name
                            )
                            for (scoreboard in manager.getScoreboards().values) {
                                if (scoreboard.lines.containsKey(scorer)) {
                                    log.addSuccess(
                                        "commands.scoreboard.players.list.player.entry", scoreboard.lines[scorer]!!
                                            .score.toString(), scoreboard.displayName, scoreboard.objectiveName
                                    )
                                }
                            }
                            log.output()
                        }
                        return 1
                    } else {
                        val scorerNames: MutableSet<String> = LinkedHashSet()
                        manager.getScoreboards().values.forEach(
                            Consumer<IScoreboard> { scoreboard: IScoreboard ->
                                scoreboard.lines.values.forEach(
                                    Consumer<IScoreboardLine> { line: IScoreboardLine ->
                                        scorerNames.add(
                                            TextFormat.WHITE.toString() + line.getScorer().getName()
                                        )
                                    }
                                )
                            }
                        )
                        log.addSuccess(
                            TextFormat.GREEN.toString() + "%commands.scoreboard.players.list.count",
                            scorerNames.size.toString()
                        )
                        val join = StringJoiner(",")
                        scorerNames.forEach(Consumer { newElement: String? -> join.add(newElement) })
                        log.addSuccess(join.toString()).output()
                    }
                }

                "players-operation" -> {
                    return this.playersOperate(list, sender, manager, log)
                }

                "players-random" -> {
                    val wildcard_target_str = list.getResult<String>(2)
                    val scorers = parseScorers(sender, wildcard_target_str!!)
                    if (scorers.isEmpty()) {
                        log.addNoTargetMatch().output()
                        return 0
                    }
                    val objectiveName = list.getResult<String>(3)
                    if (!manager.containScoreboard(objectiveName)) {
                        log.addError("commands.scoreboard.objectiveNotFound", objectiveName).output()
                        return 0
                    }
                    val scoreboard: IScoreboard = manager.getScoreboards().get(objectiveName)
                    val min = list.getResult<Long>(4)!!
                    val max = list.getResult<Long>(5)!!
                    if (min > max) {
                        log.addError("commands.scoreboard.players.random.invalidRange", min.toString(), max.toString())
                            .output()
                        return 0
                    }
                    val random = Random()
                    for (scorer in scorers) {
                        val score =
                            (min + random.nextLong(max - min + 1)).toInt() //avoid "java.lang.IllegalArgumentException: bound must be positive"
                        if (!scoreboard.lines.containsKey(scorer)) {
                            scoreboard.addLine(ScoreboardLine(scoreboard, scorer, score))
                        }
                        scoreboard.lines[scorer]!!.setScore(score)
                        log.addMessage(
                            "commands.scoreboard.players.set.success",
                            objectiveName,
                            scorer.name,
                            score.toString()
                        )
                    }
                    log.output()
                    return 1
                }

                "players-reset" -> {
                    val wildcard_target_str = list.getResult<String>(2)
                    val scorers = parseScorers(sender, wildcard_target_str!!)
                    if (scorers.isEmpty()) {
                        log.addNoTargetMatch().output()
                        return 0
                    }
                    if (list.hasResult(3)) {
                        val objectiveName = list.getResult<String>(3)
                        if (!manager.containScoreboard(objectiveName)) {
                            log.addError("commands.scoreboard.objectiveNotFound", objectiveName).output()
                            return 0
                        }
                        val scoreboard: IScoreboard = manager.getScoreboards().get(objectiveName)
                        for (scorer in scorers) {
                            if (scoreboard.containLine(scorer)) {
                                scoreboard.removeLine(scorer)
                                log.addMessage(
                                    "commands.scoreboard.players.resetscore.success",
                                    scoreboard.objectiveName,
                                    scorer.name
                                )
                            }
                        }
                        log.output()
                        return 1
                    } else {
                        for (scoreboard in manager.getScoreboards().values) {
                            for (scorer in scorers) {
                                if (scoreboard.containLine(scorer)) {
                                    scoreboard.removeLine(scorer)
                                    log.addMessage(
                                        "commands.scoreboard.players.resetscore.success",
                                        scoreboard.objectiveName,
                                        scorer.name
                                    )
                                }
                            }
                        }
                        log.output()
                        return 1
                    }
                }

                "players-test" -> {
                    val wildcard_target_str = list.getResult<String>(2)
                    val scorers = parseScorers(sender, wildcard_target_str!!)
                    if (scorers.isEmpty()) {
                        log.addNoTargetMatch().output()
                        return 0
                    }
                    val objectiveName = list.getResult<String>(3)
                    if (!manager.containScoreboard(objectiveName)) {
                        log.addError("commands.scoreboard.objectiveNotFound", objectiveName).output()
                        return 0
                    }
                    val scoreboard: IScoreboard = manager.getScoreboards().get(objectiveName)
                    val min = list.getResult<Int>(4)!!
                    var max = Int.MAX_VALUE
                    if (list.hasResult(5)) {
                        max = list.getResult(5)!!
                    }
                    for (scorer in scorers) {
                        val line: IScoreboardLine? = scoreboard.getLine(scorer)
                        if (line == null) {
                            log.addError("commands.scoreboard.players.score.notFound", objectiveName, scorer.name)
                                .output()
                            return 0
                        }
                        val score: Int = line.getScore()
                        if (score < min || score > max) {
                            log.addError(
                                "commands.scoreboard.players.test.failed",
                                score.toString(),
                                min.toString(),
                                max.toString()
                            ).output()
                            return 0
                        }
                        log.addMessage(
                            "commands.scoreboard.players.test.success",
                            score.toString(),
                            min.toString(),
                            max.toString()
                        )
                    }
                    log.output()
                    return 1
                }
            }
        } catch (e: SelectorSyntaxException) {
            log.addError(e.message)
            log.output()
        }
        return 0
    }

    @Throws(SelectorSyntaxException::class)
    private fun playersCRUD(
        list: ParamList,
        sender: CommandSender,
        manager: IScoreboardManager,
        log: CommandLogger
    ): Int {
        val ars = list.getResult<String>(1)
        val objectiveName = list.getResult<String>(3)
        if (!manager.containScoreboard(objectiveName)) {
            log.addError("commands.scoreboard.objectiveNotFound", objectiveName).output()
            return 0
        }
        val scoreboard: IScoreboard = manager.getScoreboards().get(objectiveName)
        val wildcard_target_str = list.getResult<String>(2)
        val scorers = parseScorers(sender, wildcard_target_str!!, scoreboard)
        if (scorers.isEmpty()) {
            log.addError("commands.scoreboard.players.list.empty").output()
            return 0
        }
        val score = list.getResult<Int>(4)!!
        val count = scorers.size
        when (ars) {
            "add" -> {
                for (scorer in scorers) {
                    if (!scoreboard.lines.containsKey(scorer)) {
                        scoreboard.addLine(ScoreboardLine(scoreboard, scorer, score))
                    } else {
                        scoreboard.lines[scorer]!!.addScore(score)
                    }
                }
                if (count == 1) {
                    val scorer = scorers.iterator().next()
                    log.addSuccess(
                        "commands.scoreboard.players.add.success",
                        score.toString(),
                        objectiveName,
                        scorer.name,
                        scoreboard.lines[scorer]!!
                            .score.toString()
                    )
                } else {
                    log.addSuccess(
                        "commands.scoreboard.players.add.multiple.success",
                        score.toString(),
                        objectiveName,
                        count.toString()
                    )
                }
                log.output()
                return 1
            }

            "remove" -> {
                for (scorer in scorers) {
                    if (!scoreboard.lines.containsKey(scorer)) {
                        scoreboard.addLine(ScoreboardLine(scoreboard, scorer, -score))
                    }
                    scoreboard.lines[scorer]!!.removeScore(score)
                }
                if (count == 1) {
                    val scorer = scorers.iterator().next()
                    log.addSuccess(
                        "commands.scoreboard.players.remove.success",
                        score.toString(),
                        objectiveName,
                        scorer.name,
                        scoreboard.lines[scorer]!!
                            .score.toString()
                    )
                } else {
                    log.addSuccess(
                        "commands.scoreboard.players.remove.multiple.success",
                        score.toString(),
                        objectiveName,
                        count.toString()
                    )
                }
                log.output()
                return 1
            }

            "set" -> {
                for (scorer in scorers) {
                    if (!scoreboard.lines.containsKey(scorer)) {
                        scoreboard.addLine(ScoreboardLine(scoreboard, scorer, score))
                    }
                    scoreboard.lines[scorer]!!.setScore(score)
                }
                if (count == 1) {
                    val scorer = scorers.iterator().next()
                    log.addSuccess(
                        "commands.scoreboard.players.set.success",
                        objectiveName,
                        scorer.name,
                        score.toString()
                    )
                } else {
                    log.addSuccess(
                        "commands.scoreboard.players.set.multiple.success",
                        objectiveName,
                        count.toString(),
                        score.toString()
                    )
                }
                log.output()
                return 1
            }
        }
        return 0
    }

    @Throws(SelectorSyntaxException::class)
    private fun playersOperate(
        list: ParamList,
        sender: CommandSender,
        manager: IScoreboardManager,
        log: CommandLogger
    ): Int {
        val targetObjectiveName = list.getResult<String>(3)
        if (!manager.containScoreboard(targetObjectiveName)) {
            log.addError("commands.scoreboard.objectiveNotFound", targetObjectiveName).output()
            return 0
        }
        val targetScoreboard: IScoreboard = manager.getScoreboards().get(targetObjectiveName)

        val wildcard_target_str = list.getResult<String>(2)
        val targetScorers = parseScorers(
            sender,
            wildcard_target_str!!, targetScoreboard
        )
        if (targetScorers.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }

        val operation = list.getResult<String>(4)


        val selectorObjectiveName = list.getResult<String>(6)
        if (!manager.containScoreboard(selectorObjectiveName)) {
            log.addError("commands.scoreboard.objectiveNotFound", selectorObjectiveName).output()
            return 0
        }
        val selectorScoreboard: IScoreboard = manager.getScoreboards().get(targetObjectiveName)

        val selector_str = list.getResult<String>(5)
        val selectorScorers = parseScorers(sender, selector_str!!, selectorScoreboard)
        if (selectorScorers.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }

        for (targetScorer in targetScorers) {
            for (selectorScorer in selectorScorers) {
                if (!targetScoreboard.lines.containsKey(targetScorer)) {
                    log.addError(
                        "commands.scoreboard.players.operation.notFound",
                        targetObjectiveName,
                        targetScorer.name
                    ).output()
                    return 0
                }
                if (!selectorScoreboard.lines.containsKey(selectorScorer)) {
                    log.addError(
                        "commands.scoreboard.players.operation.notFound",
                        selectorObjectiveName,
                        selectorScorer.name
                    ).output()
                    return 0
                }
                val targetScore = targetScoreboard.lines[targetScorer]!!.score
                val selectorScore = selectorScoreboard.lines[selectorScorer]!!.score
                var changedScore = -1
                when (operation) {
                    "+=" -> {
                        changedScore = targetScore + selectorScore
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                    }

                    "-=" -> {
                        changedScore = targetScore - selectorScore
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                    }

                    "*=" -> {
                        changedScore = targetScore * selectorScore
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                    }

                    "/=" -> {
                        changedScore = targetScore / selectorScore
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                    }

                    "%=" -> {
                        changedScore = targetScore % selectorScore
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                    }

                    "=" -> {
                        changedScore = selectorScore
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                    }

                    "<" -> {
                        changedScore = min(targetScore.toDouble(), selectorScore.toDouble()).toInt()
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                    }

                    ">" -> {
                        changedScore = max(targetScore.toDouble(), selectorScore.toDouble()).toInt()
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                    }

                    "><" -> {
                        changedScore = selectorScore
                        targetScoreboard.lines[targetScorer]!!.setScore(changedScore)
                        selectorScoreboard.lines[selectorScorer]!!.setScore(targetScore)
                    }
                }
                log.addMessage(
                    "commands.scoreboard.players.operation.success",
                    changedScore.toString(),
                    targetObjectiveName
                )
            }
        }
        log.output()
        return 1
    }

    @Throws(SelectorSyntaxException::class)
    private fun parseScorers(sender: CommandSender, wildcardTargetStr: String): Set<IScorer> {
        return parseScorers(sender, wildcardTargetStr, null)
    }

    @Throws(SelectorSyntaxException::class)
    private fun parseScorers(
        sender: CommandSender,
        wildcardTargetStr: String,
        wildcardScoreboard: IScoreboard?
    ): Set<IScorer> {
        val manager: IScoreboardManager = Server.getInstance().scoreboardManager
        var scorers: MutableSet<IScorer> = HashSet()
        if (wildcardTargetStr == "*") {
            if (wildcardScoreboard != null) {
                scorers.addAll(wildcardScoreboard.lines.keys)
            } else {
                for (scoreboard in manager.getScoreboards().values) {
                    scorers.addAll(scoreboard.lines.keys)
                }
            }
        } else if (EntitySelectorAPI.Companion.getAPI().checkValid(wildcardTargetStr)) {
            scorers = EntitySelectorAPI.Companion.getAPI().matchEntities(sender, wildcardTargetStr).stream()
                .map<IScorer> { t: Entity? ->
                    if (t is Player) PlayerScorer(
                        t
                    ) else EntityScorer(t)
                }.collect<MutableSet<IScorer>, Any>(Collectors.toSet<IScorer>())
        } else if (Server.getInstance().getPlayer(wildcardTargetStr) != null) {
            scorers.add(PlayerScorer(Server.getInstance().getPlayer(wildcardTargetStr)))
        } else {
            scorers.add(FakeScorer(wildcardTargetStr))
        }
        return scorers
    }
}
