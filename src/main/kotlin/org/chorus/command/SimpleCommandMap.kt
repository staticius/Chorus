package org.chorus.command

import io.netty.util.internal.EmptyArrays
import org.chorus.Server
import org.chorus.command.data.CommandParameter
import org.chorus.command.defaults.*
import org.chorus.command.simple.*
import org.chorus.command.utils.CommandLogger
import org.chorus.lang.CommandOutputContainer
import org.chorus.lang.TranslationContainer
import org.chorus.plugin.InternalPlugin
import org.chorus.utils.TextFormat
import org.chorus.utils.Utils
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set


class SimpleCommandMap(private val server: Server) : CommandMap {
    protected val knownCommands: MutableMap<String?, Command> = HashMap()

    init {
        this.setDefaultCommands()
    }

    private fun setDefaultCommands() {
        this.register("nukkit", ExecuteCommand("execute"))
        this.register("nukkit", CameraCommand("camera"))
        this.register("nukkit", FogCommand("fog"))
        this.register("nukkit", ExecuteCommandOld("executeold"))
        this.register("nukkit", PlayAnimationCommand("playanimation"))
        this.register("nukkit", WorldCommand("world"))
        this.register("nukkit", TpsCommand("tps"))
        this.register("nukkit", TickingAreaCommand("tickingarea"))
        this.register("nukkit", TellrawCommand("tellraw"))
        this.register("nukkit", TitlerawCommand("titleraw"))
        this.register("nukkit", FunctionCommand("function"))
        this.register("nukkit", ReplaceItemCommand("replaceitem"))
        this.register("nukkit", SummonCommand("summon"))
        this.register("nukkit", DamageCommand("damage"))
        this.register("nukkit", ClearSpawnPointCommand("clearspawnpoint"))
        this.register("nukkit", AbilityCommand("ability"))
        this.register("nukkit", ScoreboardCommand("scoreboard"))
        this.register("nukkit", CameraShakeCommand("camerashake"))
        this.register("nukkit", TagCommand("tag"))
        this.register("nukkit", TestForCommand("testfor"))
        this.register("nukkit", TestForBlockCommand("testforblock"))
        this.register("nukkit", TestForBlocksCommand("testforblocks"))
        this.register("nukkit", SpreadPlayersCommand("spreadplayers"))
        this.register("nukkit", SetMaxPlayersCommand("setmaxplayers"))
        this.register("nukkit", PlaySoundCommand("playsound"))
        this.register("nukkit", StopSoundCommand("stopsound"))
        this.register("nukkit", FillCommand("fill"))
        this.register("nukkit", DayLockCommand("daylock"))
        this.register("nukkit", ClearCommand("clear"))
        this.register("nukkit", CloneCommand("clone"))
        this.register("nukkit", VersionCommand("version"))
        this.register("nukkit", PluginsCommand("plugins"))
        this.register("nukkit", SeedCommand("seed"))
        this.register("nukkit", HelpCommand("help"))
        this.register("nukkit", StopCommand("stop"))
        this.register("nukkit", TellCommand("tell"))
        this.register("nukkit", DefaultGamemodeCommand("defaultgamemode"))
        this.register("nukkit", BanCommand("ban"))
        this.register("nukkit", BanIpCommand("ban-ip"))
        this.register("nukkit", BanListCommand("banlist"))
        this.register("nukkit", PardonCommand("pardon"))
        this.register("nukkit", PardonIpCommand("pardon-ip"))
        this.register("nukkit", SayCommand("say"))
        this.register("nukkit", MeCommand("me"))
        this.register("nukkit", ListCommand("list"))
        this.register("nukkit", DifficultyCommand("difficulty"))
        this.register("nukkit", KickCommand("kick"))
        this.register("nukkit", OpCommand("op"))
        this.register("nukkit", DeOpCommand("deop"))
        this.register("nukkit", WhitelistCommand("whitelist"))
        this.register("nukkit", SaveOnCommand("save-on"))
        this.register("nukkit", SaveOffCommand("save-off"))
        this.register("nukkit", SaveCommand("save-all"))
        this.register("nukkit", GiveCommand("give"))
        this.register("nukkit", EffectCommand("effect"))
        this.register("nukkit", EnchantCommand("enchant"))
        this.register("nukkit", ParticleCommand("particle"))
        this.register("nukkit", GamemodeCommand("gamemode"))
        this.register("nukkit", GameruleCommand("gamerule"))
        this.register("nukkit", KillCommand("kill"))
        this.register("nukkit", SpawnpointCommand("spawnpoint"))
        this.register("nukkit", SetWorldSpawnCommand("setworldspawn"))
        this.register("nukkit", TeleportCommand("tp"))
        this.register("nukkit", TimeCommand("time"))
        this.register("nukkit", TitleCommand("title"))
        this.register("nukkit", ReloadCommand("reload"))
        this.register("nukkit", WeatherCommand("weather"))
        this.register("nukkit", XpCommand("xp"))
        this.register("nukkit", SetBlockCommand("setblock"))
        this.register("nukkit", HudCommand("hud"))

        this.register("nukkit", StatusCommand("status"))
        this.register("nukkit", GarbageCollectorCommand("gc"))
        if (server.settings.debugSettings().command()) {
            this.register("nukkit", DebugCommand("debug"))
        }
    }

    override fun registerAll(fallbackPrefix: String, commands: List<Command>) {
        for (command in commands) {
            this.register(fallbackPrefix, command)
        }
    }

    override fun register(fallbackPrefix: String, command: Command): Boolean {
        return this.register(fallbackPrefix, command, null)
    }

    override fun register(fallbackPrefix: String, command: Command, label: String?): Boolean {
        var fallbackPrefix = fallbackPrefix
        var label = label
        if (label == null) {
            label = command.name
        }
        label = label.trim { it <= ' ' }.lowercase()
        fallbackPrefix = fallbackPrefix.trim { it <= ' ' }.lowercase()

        val registered = this.registerAlias(command, false, fallbackPrefix, label)

        val aliases: MutableList<String?> = ArrayList(Arrays.asList(*command.aliases))

        val iterator = aliases.iterator()
        while (iterator.hasNext()) {
            val alias = iterator.next()
            if (!this.registerAlias(command, true, fallbackPrefix, alias)) {
                iterator.remove()
            }
        }
        command.aliases = aliases.toArray(EmptyArrays.EMPTY_STRINGS)

        if (!registered) {
            command.setLabel("$fallbackPrefix:$label")
        }

        command.register(this)

        return registered
    }

    override fun registerSimpleCommands(`object`: Any) {
        for (method in `object`.javaClass.declaredMethods) {
            val def = method.getAnnotation(org.chorus.command.simple.Command::class.java)
            if (def != null) {
                val sc = SimpleCommand(`object`, method, def.name, def.description, def.usageMessage, def.aliases)

                val args = method.getAnnotation(
                    Arguments::class.java
                )
                if (args != null) {
                    sc.setMaxArgs(args.max)
                    sc.setMinArgs(args.min)
                }

                val perm = method.getAnnotation(
                    CommandPermission::class.java
                )
                if (perm != null) {
                    sc.permission = perm.value
                }

                if (method.isAnnotationPresent(ForbidConsole::class.java)) {
                    sc.setForbidConsole(true)
                }

                val commandParameters: CommandParameters =
                    method.getAnnotation<CommandParameters>(CommandParameters::class.java)
                if (commandParameters != null) {
                    val map = Arrays.stream<Parameters>(commandParameters.parameters)
                        .collect(
                            Collectors.toMap<Parameters, String, Array<CommandParameter?>?>(
                                Function<Parameters, String> { obj: Parameters -> obj.name },
                                Function<Parameters, Array<CommandParameter?>?> { parameters: Parameters ->
                                    Arrays.stream<Parameter>(parameters.parameters)
                                        .map<CommandParameter> { parameter: Parameter ->
                                            CommandParameter.Companion.newType(
                                                parameter.name,
                                                parameter.optional,
                                                parameter.type
                                            )
                                        }
                                        .distinct()
                                        .toArray<CommandParameter?> { _Dummy_.__Array__() }
                                })
                        )

                    sc.commandParameters.putAll(map)
                }

                this.register(def.name, sc)
            }
        }
    }

    private fun registerAlias(command: Command, isAlias: Boolean, fallbackPrefix: String, label: String?): Boolean {
        knownCommands["$fallbackPrefix:$label"] = command

        //if you're registering a command alias that is already registered, then return false
        val alreadyRegistered = knownCommands.containsKey(label)
        val existingCommand = knownCommands[label]
        val existingCommandIsNotVanilla = alreadyRegistered && existingCommand !is VanillaCommand
        //basically, if we're an alias and it's already registered, or we're a vanilla command, then we can't override it
        if ((command is VanillaCommand || isAlias) && alreadyRegistered && existingCommandIsNotVanilla) {
            return false
        }

        //if you're registering a name (alias or label) which is identical to another command who's primary name is the same
        //so basically we can't override the main name of a command, but we can override aliases if we're not an alias

        //added the last statement which will allow us to override a VanillaCommand unconditionally
        if (alreadyRegistered && existingCommand.getLabel() != null && existingCommand.getLabel() == label && existingCommandIsNotVanilla) {
            return false
        }

        //you can now assume that the command is either uniquely named, or overriding another command's alias (and is not itself, an alias)
        if (!isAlias) {
            command.setLabel(label)
        }

        // Then we need to check if there isn't any command conflicts with vanilla commands
        val toRemove = ArrayList<String?>()

        for ((key, cmd) in knownCommands) {
            if (cmd.label.equals(
                    command.label,
                    ignoreCase = true
                ) && cmd != command
            ) { // If the new command conflicts... (But if it isn't the same command)
                if (cmd is VanillaCommand) { // And if the old command is a vanilla command...
                    // Remove it!
                    toRemove.add(key)
                }
            }
        }

        // Now we loop the toRemove list to remove the command conflicts from the knownCommands map
        for (cmd in toRemove) {
            knownCommands.remove(cmd)
        }

        knownCommands[label] = command

        return true
    }

    override fun executeCommand(sender: CommandSender, cmdLine: String): Int {
        val parsed = parseArguments(cmdLine)
        if (parsed.isEmpty()) {
            return -1
        }

        val sentCommandLabel = parsed.removeAt(0).lowercase() //command name
        val args = parsed.toArray(EmptyArrays.EMPTY_STRINGS)
        val target = this.getCommand(sentCommandLabel)

        if (target == null) {
            sender.sendCommandOutput(
                CommandOutputContainer(
                    TextFormat.RED.toString() + "%commands.generic.unknown",
                    arrayOf(sentCommandLabel),
                    0
                )
            )
            return -1
        }
        var output: Int
        try {
            if (target.hasParamTree()) {
                val plugin = if (target is PluginCommand<*>) target.plugin else InternalPlugin.INSTANCE
                val result = target.getParamTree().matchAndParse(sender, sentCommandLabel, args)
                if (result == null) output = 0
                else if (target.testPermissionSilent(sender)) {
                    try {
                        output = target.execute(
                            sender,
                            sentCommandLabel,
                            result,
                            CommandLogger(target, sender, sentCommandLabel, args, result.value.messageContainer, plugin)
                        )
                    } catch (e: UnsupportedOperationException) {
                        SimpleCommandMap.log.error("If you use paramtree, you must override execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) method to run the command!")
                        output = 0
                    }
                } else {
                    val log = CommandLogger(target, sender, sentCommandLabel, args, plugin)
                    if (target.permissionMessage == null) {
                        log.addMessage("nukkit.command.generic.permission").output()
                    } else if (target.permissionMessage != "") {
                        log.addError(target.permissionMessage.replace("<permission>", target.permission)).output()
                    }
                    output = 0
                }
            } else {
                output = if (target.execute(sender, sentCommandLabel, args)) 1 else 0
            }
        } catch (e: Exception) {
            SimpleCommandMap.log.error(
                server.baseLang.tr(
                    "nukkit.command.exception",
                    cmdLine,
                    target.toString(),
                    Utils.getExceptionMessage(e)
                ), e
            )
            sender.sendMessage(TranslationContainer(TextFormat.RED.toString() + "%commands.generic.exception"))
            output = 0
        }

        return output
    }

    override fun clearCommands() {
        for (command in knownCommands.values) {
            command.unregister(this)
        }
        knownCommands.clear()
        this.setDefaultCommands()
    }

    override fun getCommand(name: String): Command? {
        var name = name
        name = name.lowercase()
        if (knownCommands.containsKey(name)) {
            return knownCommands[name]
        }
        return null
    }

    val commands: Map<String?, Command>
        /**
         * 获取[.knownCommands]的未克隆实例
         *
         * @return the commands
         */
        get() = knownCommands

    companion object {
        /**
         * 解析给定文本，从中分割参数
         *
         * @param cmdLine the cmd line
         * @return 参数数组
         */
        fun parseArguments(cmdLine: String): ArrayList<String> {
            val sb = StringBuilder(cmdLine)
            val args = ArrayList<String>()
            var notQuoted = true
            var curlyBraceCount = 0
            var start = 0

            var i = 0
            while (i < sb.length) {
                if ((sb[i] == '{' && curlyBraceCount >= 1) || (sb[i] == '{' && sb[i - 1] == ' ' && curlyBraceCount == 0)) {
                    curlyBraceCount++
                } else if (sb[i] == '}' && curlyBraceCount > 0) {
                    curlyBraceCount--
                    if (curlyBraceCount == 0) {
                        args.add(sb.substring(start, i + 1))
                        start = i + 1
                    }
                }
                if (curlyBraceCount == 0) {
                    if (sb[i] == ' ' && notQuoted) {
                        val arg = sb.substring(start, i)
                        if (!arg.isEmpty()) {
                            args.add(arg)
                        }
                        start = i + 1
                    } else if (sb[i] == '"') {
                        sb.deleteCharAt(i)
                        --i
                        notQuoted = !notQuoted
                    }
                }
                i++
            }

            val arg = sb.substring(start)
            if (!arg.isEmpty()) {
                args.add(arg)
            }
            return args
        }
    }
}
