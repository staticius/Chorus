package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.camera.data.CameraPreset.Companion.getPreset
import org.chorus.camera.data.Ease
import org.chorus.camera.data.EaseType
import org.chorus.camera.data.Time
import org.chorus.camera.instruction.impl.ClearInstruction
import org.chorus.camera.instruction.impl.FadeInstruction
import org.chorus.camera.instruction.impl.SetInstruction
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.*
import org.chorus.command.utils.CommandLogger
import org.chorus.level.Locator
import org.chorus.math.Vector2f
import org.chorus.math.Vector3f
import org.chorus.network.protocol.CameraInstructionPacket
import java.awt.Color
import java.util.*

/**
 * TODO: The multilingual text of this command does not seem to work properly
 */
class CameraCommand(name: String) : VanillaCommand(name, "commands.camera.description") {
    init {
        this.permission = "chorus.command.camera"
        commandParameters.clear()
        commandParameters["clear"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("clear", false, arrayOf("clear"))
        )
        commandParameters["fade"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("fade", false, arrayOf("fade"))
        )
        commandParameters["fade-color"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("fade", false, arrayOf("fade")),
            CommandParameter.Companion.newEnum("color", false, arrayOf("color")),
            CommandParameter.Companion.newType("red", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("green", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("blue", false, CommandParamType.FLOAT)
        )
        commandParameters["fade-time-color"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("fade", false, arrayOf("fade")),
            CommandParameter.Companion.newEnum("time", false, arrayOf("time")),
            CommandParameter.Companion.newType("fadeInSeconds", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("holdSeconds", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("fadeOutSeconds", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newEnum("color", false, arrayOf("color")),
            CommandParameter.Companion.newType("red", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("green", false, CommandParamType.FLOAT),
            CommandParameter.Companion.newType("blue", false, CommandParamType.FLOAT)
        )
        commandParameters["set-default"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("default", true, arrayOf("default"))
        )
        commandParameters["set-rot"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("rot", false, arrayOf("rot")),
            CommandParameter.Companion.newType("xRot", false, CommandParamType.VALUE, RelativeFloatNode()),
            CommandParameter.Companion.newType("yRot", false, CommandParamType.VALUE, RelativeFloatNode())
        )
        commandParameters["set-pos"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("pos", false, arrayOf("pos")),
            CommandParameter.Companion.newType("position", false, CommandParamType.POSITION),
        )
        commandParameters["set-pos-rot"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("pos", false, arrayOf("pos")),
            CommandParameter.Companion.newType("position", false, CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("rot", false, arrayOf("rot")),
            CommandParameter.Companion.newType("xRot", false, CommandParamType.VALUE, RelativeFloatNode()),
            CommandParameter.Companion.newType("yRot", false, CommandParamType.VALUE, RelativeFloatNode())
        )
        commandParameters["set-ease-default"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("ease", false, arrayOf("ease")),
            CommandParameter.Companion.newType(
                "easeTime",
                false,
                CommandParamType.FLOAT,
                FloatNode()
            ),
            CommandParameter.Companion.newEnum("easeType", false, EASE_TYPES),
            CommandParameter.Companion.newEnum("default", true, arrayOf("default"))
        )
        commandParameters["set-ease-rot"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("ease", false, arrayOf("ease")),
            CommandParameter.Companion.newType(
                "easeTime",
                false,
                CommandParamType.FLOAT,
                FloatNode()
            ),
            CommandParameter.Companion.newEnum("easeType", false, EASE_TYPES),
            CommandParameter.Companion.newEnum("rot", false, arrayOf("rot")),
            CommandParameter.Companion.newType("xRot", false, CommandParamType.VALUE, RelativeFloatNode()),
            CommandParameter.Companion.newType("yRot", false, CommandParamType.VALUE, RelativeFloatNode())
        )
        commandParameters["set-ease-pos"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("ease", false, arrayOf("ease")),
            CommandParameter.Companion.newType(
                "easeTime",
                false,
                CommandParamType.FLOAT,
                FloatNode()
            ),
            CommandParameter.Companion.newEnum("easeType", false, EASE_TYPES),
            CommandParameter.Companion.newEnum("pos", false, arrayOf("pos")),
            CommandParameter.Companion.newType("position", false, CommandParamType.POSITION),
        )
        commandParameters["set-ease-pos-rot"] = arrayOf(
            CommandParameter.Companion.newType("players", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum("set", false, arrayOf("set")),
            CommandParameter.Companion.newEnum("preset", false, CommandEnum.Companion.CAMERA_PRESETS),
            CommandParameter.Companion.newEnum("ease", false, arrayOf("ease")),
            CommandParameter.Companion.newType(
                "easeTime",
                false,
                CommandParamType.FLOAT,
                FloatNode()
            ),
            CommandParameter.Companion.newEnum("easeType", false, EASE_TYPES),
            CommandParameter.Companion.newEnum("pos", false, arrayOf("pos")),
            CommandParameter.Companion.newType("position", false, CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("rot", false, arrayOf("rot")),
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
            players.stream().map { obj: Player -> obj.getName() }.reduce { a: String, b: String -> "$a $b" }.orElse("")
        val pk: CameraInstructionPacket = CameraInstructionPacket()
        val senderLocation = sender.getTransform()
        when (result.key) {
            "clear" -> {
                pk.setInstruction(ClearInstruction)
            }

            "fade" -> {
                pk.setInstruction(FadeInstruction())
            }

            "fade-color" -> {
                pk.setInstruction(
                    FadeInstruction(
                        Color(getFloat(list, 3), getFloat(list, 4), getFloat(list, 5))
                    )
                )
            }

            "fade-time-color" -> {
                pk.setInstruction(
                    FadeInstruction(
                        time = Time(list[3]!!.get()!!, list[4]!!.get()!!, list[5]!!.get()!!),
                        color = Color(getFloat(list, 7), getFloat(list, 8), getFloat(list, 9))
                    )
                )
            }

            "set-default" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                pk.setInstruction(SetInstruction(preset = preset))
            }

            "set-rot" -> {
                val preset = getPreset(list[2]!!.get()!!)
                if (preset == null) {
                    log.addError("commands.camera.invalid-preset").output()
                    return 0
                }
                pk.setInstruction(
                    SetInstruction(
                        preset = preset,
                        rot = Vector2f(
                            (list[4] as RelativeFloatNode).get(senderLocation.pitch.toFloat()),
                            (list[5] as RelativeFloatNode).get(senderLocation.yaw.toFloat())
                        )
                    )
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
                    SetInstruction(
                        preset = preset,
                        pos = Vector3f(locator!!.x.toFloat(), locator.y.toFloat(), locator.z.toFloat())
                    )
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
                    SetInstruction(
                        preset = preset,
                        pos = Vector3f(locator!!.x.toFloat(), locator.y.toFloat(), locator.z.toFloat()),
                        rot = Vector2f(
                            (list[6] as RelativeFloatNode).get(senderLocation.pitch.toFloat()),
                            (list[7] as RelativeFloatNode).get(senderLocation.yaw.toFloat())
                        )
                    )
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
                    SetInstruction(
                        preset = preset,
                        ease = Ease(easeTime, easeType)
                    )
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
                    SetInstruction(
                        preset = preset,
                        ease = Ease(easeTime, easeType),
                        rot = Vector2f(
                            (list[7] as RelativeFloatNode).get(senderLocation.pitch.toFloat()),
                            (list[8] as RelativeFloatNode).get(senderLocation.yaw.toFloat())
                        )
                    )
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
                    SetInstruction(
                        preset = preset,
                        ease = Ease(easeTime, easeType),
                        pos = Vector3f(locator!!.x.toFloat(), locator.y.toFloat(), locator.z.toFloat())
                    )
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
                    SetInstruction(
                        preset = preset,
                        ease = Ease(easeTime, easeType),
                        pos = Vector3f(locator!!.x.toFloat(), locator.y.toFloat(), locator.z.toFloat()),
                        rot = Vector2f(
                            (list[9] as RelativeFloatNode).get(senderLocation.pitch.toFloat()),
                            (list[10] as RelativeFloatNode).get(senderLocation.yaw.toFloat())
                        )
                    )
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
        val EASE_TYPES = EaseType.entries.map(EaseType::type).toTypedArray()

        private fun getFloat(list: ParamList, index: Int): Float {
            return list[index]!!.get()!!
        }
    }
}
