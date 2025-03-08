package cn.nukkit.command.defaults

import cn.nukkit.Player
import cn.nukkit.camera.data.CameraPreset.Companion.getPreset
import cn.nukkit.camera.data.Time
import cn.nukkit.camera.instruction.impl.ClearInstruction.get
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.*
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.level.Locator
import java.awt.Color
import java.util.*

/**
 * @author daoge_cmd <br></br>
 * Date: 2023/6/11 <br></br>
 * PowerNukkitX Project <br></br>
 * TODO: 此命令的多语言文本似乎不能正常工作
 */
class CameraCommand(name: String) : VanillaCommand(name, "commands.camera.description") {
    init {
        this.permission = "nukkit.command.camera"
        commandParameters.clear()
        commandParameters["clear"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("clear", false, arrayOf<String?>("clear"))
        )
        commandParameters["fade"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("fade", false, arrayOf<String?>("fade"))
        )
        commandParameters["fade-color"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("fade", false, arrayOf<String?>("fade")),
            CommandParameter.Companion.newEnum("color", false, arrayOf<String?>("color")),
            CommandParameter.Companion.newType("red", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("green", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("blue", false, CommandParamType.FLOAT)
        )
        commandParameters["fade-time-color"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("fade", false, arrayOf<String?>("fade")),
            CommandParameter.Companion.newEnum("time", false, arrayOf<String?>("time")),
            CommandParameter.Companion.newType("fadeInSeconds", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("holdSeconds", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("fadeOutSeconds", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newEnum("color", false, arrayOf<String?>("color")),
            CommandParameter.Companion.newType("red", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("green", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("blue", false, CommandParamType.FLOAT)
        )
        commandParameters["set-default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf<String?>("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("default", true, arrayOf<String?>("default"))
        )
        commandParameters["set-rot"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf<String?>("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("rot", false, arrayOf<String?>("rot")),
            CommandParameter.Companion.newType("xRot", false, CommandParamType.VALUE, RelativeFloatNode()),
            CommandParameter.Companion.newType("yRot", false, CommandParamType.VALUE, RelativeFloatNode())
        )
        commandParameters["set-pos"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf<String?>("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("pos", false, arrayOf<String?>("pos")),
            CommandParameter.Companion.newType("position", false, CommandParamType.POSITION),
        )
        commandParameters["set-pos-rot"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf<String?>("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("pos", false, arrayOf<String?>("pos")),
            CommandParameter.Companion.newType("position", false, CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("rot", false, arrayOf<String?>("rot")),
            CommandParameter.Companion.newType("xRot", false, CommandParamType.VALUE, RelativeFloatNode()),
            CommandParameter.Companion.newType("yRot", false, CommandParamType.VALUE, RelativeFloatNode())
        )
        commandParameters["set-ease-default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf<String?>("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("ease", false, arrayOf<String?>("ease")),
            CommandParameter.Companion.newType(
                "easeTime",
                false,
                CommandParamType.FLOAT,
                FloatNode()
            ),
            CommandParameter.Companion.newEnum("easeType", false, EASE_TYPES),
            CommandParameter.Companion.newEnum("default", true, arrayOf<String?>("default"))
        )
        commandParameters["set-ease-rot"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf<String?>("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("ease", false, arrayOf<String?>("ease")),
            CommandParameter.Companion.newType(
                "easeTime",
                false,
                CommandParamType.FLOAT,
                FloatNode()
            ),
            CommandParameter.Companion.newEnum("easeType", false, EASE_TYPES),
            CommandParameter.Companion.newEnum("rot", false, arrayOf<String?>("rot")),
            CommandParameter.Companion.newType("xRot", false, CommandParamType.VALUE, RelativeFloatNode()),
            CommandParameter.Companion.newType("yRot", false, CommandParamType.VALUE, RelativeFloatNode())
        )
        commandParameters["set-ease-pos"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf<String?>("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("ease", false, arrayOf<String?>("ease")),
            CommandParameter.Companion.newType(
                "easeTime",
                false,
                CommandParamType.FLOAT,
                FloatNode()
            ),
            CommandParameter.Companion.newEnum("easeType", false, EASE_TYPES),
            CommandParameter.Companion.newEnum("pos", false, arrayOf<String?>("pos")),
            CommandParameter.Companion.newType("position", false, CommandParamType.POSITION),
        )
        commandParameters["set-ease-pos-rot"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf<String?>("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("ease", false, arrayOf<String?>("ease")),
            CommandParameter.Companion.newType(
                "easeTime",
                false,
                CommandParamType.FLOAT,
                FloatNode()
            ),
            CommandParameter.Companion.newEnum("easeType", false, EASE_TYPES),
            CommandParameter.Companion.newEnum("pos", false, arrayOf<String?>("pos")),
            CommandParameter.Companion.newType("position", false, CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("rot", false, arrayOf<String?>("rot")),
            CommandParameter.Companion.newType("xRot", false, CommandParamType.VALUE, RelativeFloatNode()),
            CommandParameter.Companion.newType("yRot", false, CommandParamType.VALUE, RelativeFloatNode())
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
        val players = list.getResult<List<Player>>(0)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        val playerNames =
            players.stream().map { obj: Player -> obj.name }.reduce { a: String, b: String -> "$a $b" }.orElse("")
        val pk: CameraInstructionPacket = CameraInstructionPacket()
        val senderLocation = sender.transform
        when (result.key) {
            "clear" -> {
                pk.setInstruction(ClearInstruction.get())
            }

            "fade" -> {
                pk.setInstruction(FadeInstruction.builder().build())
            }

            "fade-color" -> {
                pk.setInstruction(
                    FadeInstruction
                        .builder()
                        .color(Color(getFloat(list, 3), getFloat(list, 4), getFloat(list, 5)))
                        .build()
                )
            }

            "fade-time-color" -> {
                pk.setInstruction(
                    FadeInstruction
                        .builder()
                        .time(
                            Time(
                                list[3]!!.get<Float>()!!, list[4]!!.get<Float>()!!, list[5]!!
                                    .get<Float>()!!
                            )
                        )
                        .color(Color(getFloat(list, 7), getFloat(list, 8), getFloat(list, 9)))
                        .build()
                )
            }

            "set-default" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                pk.setInstruction(SetInstruction.builder().preset(preset).build())
            }

            "set-rot" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                pk.setInstruction(
                    SetInstruction.builder()
                        .preset(preset)
                        .rot(
                            Vector2f(
                                (list[4] as RelativeFloatNode).get(senderLocation.pitch.toFloat()),
                                (list[5] as RelativeFloatNode).get(senderLocation.yaw.toFloat())
                            )
                        )
                        .build()
                )
            }

            "set-pos" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                val locator = list[4]!!.get<Locator>()
                pk.setInstruction(
                    SetInstruction.builder()
                        .preset(preset)
                        .pos(Vector3f(locator!!.x.toFloat(), locator.y.toFloat(), locator.z.toFloat()))
                        .build()
                )
            }

            "set-pos-rot" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                val locator = list[4]!!.get<Locator>()
                pk.setInstruction(
                    SetInstruction.builder()
                        .preset(preset)
                        .pos(Vector3f(locator!!.x.toFloat(), locator.y.toFloat(), locator.z.toFloat()))
                        .rot(
                            Vector2f(
                                (list[6] as RelativeFloatNode).get(senderLocation.pitch.toFloat()),
                                (list[7] as RelativeFloatNode).get(senderLocation.yaw.toFloat())
                            )
                        )
                        .build()
                )
            }

            "set-ease-default" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                val easeTime = list[4]!!.get<Float>()!!
                val easeType: EaseType = EaseType.valueOf((list[5]!!.get<Any>() as String).uppercase())
                pk.setInstruction(
                    SetInstruction.builder()
                        .preset(preset)
                        .ease(Ease(easeTime, easeType))
                        .build()
                )
            }

            "set-ease-rot" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                val easeTime = list[4]!!.get<Float>()!!
                val easeType: EaseType = EaseType.valueOf((list[5]!!.get<Any>() as String).uppercase())
                pk.setInstruction(
                    SetInstruction.builder()
                        .preset(preset)
                        .ease(Ease(easeTime, easeType))
                        .rot(
                            Vector2f(
                                (list[7] as RelativeFloatNode).get(senderLocation.pitch.toFloat()),
                                (list[8] as RelativeFloatNode).get(senderLocation.yaw.toFloat())
                            )
                        )
                        .build()
                )
            }

            "set-ease-pos" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                val easeTime = list[4]!!.get<Float>()!!
                val easeType: EaseType = EaseType.valueOf((list[5]!!.get<Any>() as String).uppercase())
                val locator = list[7]!!.get<Locator>()
                pk.setInstruction(
                    SetInstruction.builder()
                        .preset(preset)
                        .ease(Ease(easeTime, easeType))
                        .pos(Vector3f(locator!!.x.toFloat(), locator.y.toFloat(), locator.z.toFloat()))
                        .build()
                )
            }

            "set-ease-pos-rot" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                val easeTime = list[4]!!.get<Float>()!!
                val easeType: EaseType = EaseType.valueOf((list[5]!!.get<Any>() as String).uppercase())
                val locator = list[7]!!.get<Locator>()
                pk.setInstruction(
                    SetInstruction.builder()
                        .preset(preset)
                        .ease(Ease(easeTime, easeType))
                        .pos(Vector3f(locator!!.x.toFloat(), locator.y.toFloat(), locator.z.toFloat()))
                        .rot(
                            Vector2f(
                                (list[9] as RelativeFloatNode).get(senderLocation.pitch.toFloat()),
                                (list[10] as RelativeFloatNode).get(senderLocation.yaw.toFloat())
                            )
                        )
                        .build()
                )
            }

            else -> {
                return 0
            }
        }
        for (player in players) {
            player.dataPacket(pk)
        }
        log.addSuccess("commands.camera.success", playerNames).output()
        return 1
    }

    companion object {
        val EASE_TYPES: Array<String?> =
            Arrays.stream<EaseType>(EaseType.entries.toTypedArray()).map<Any>(EaseType::getType)
                .toArray<String> { _Dummy_.__Array__() }

        private fun getFloat(list: ParamList, index: Int): Float {
            return list[index]!!.get()!!
        }
    }
}
