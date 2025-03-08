package cn.nukkit.command.defaults

import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.entity.Entity
import cn.nukkit.level.Locator
import cn.nukkit.level.Transform
import cn.nukkit.math.*
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author Pub4Game and milkice
 * @since 2015/11/12
 */
class TeleportCommand(name: String) :
    VanillaCommand(name, "commands.tp.description", "commands.tp.usage", arrayOf<String>("teleport")) {
    init {
        this.permission = "nukkit.command.teleport"
        commandParameters.clear()
        commandParameters["->Entity"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("destination", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["Entity->Entity"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET),
            CommandParameter.Companion.newType("destination", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["Entity->Pos"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET),
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newType("yRot", true, CommandParamType.VALUE),
            CommandParameter.Companion.newType("xRot", true, CommandParamType.VALUE),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["Entity->Pos(FacingPos)"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET),
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("facing", false, arrayOf<String?>("facing")),
            CommandParameter.Companion.newType("lookAtPosition", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["Entity->Pos(FacingEntity)"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET),
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("facing", false, arrayOf<String?>("facing")),
            CommandParameter.Companion.newType("lookAtEntity", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["->Pos"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newType("yRot", true, CommandParamType.VALUE),
            CommandParameter.Companion.newType("xRot", true, CommandParamType.VALUE),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["->Pos(FacingPos)"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("facing", false, arrayOf<String?>("facing")),
            CommandParameter.Companion.newType("lookAtPosition", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["->Pos(FacingEntity)"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("facing", false, arrayOf<String?>("facing")),
            CommandParameter.Companion.newType("lookAtEntity", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        this.enableParamTree()
    }

    override fun testPermissionSilent(target: CommandSender): Boolean {
        if (target.isPlayer && target.asPlayer()!!.adventureSettings.get(PlayerAbility.TELEPORT)) return true
        return super.testPermissionSilent(target)
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        when (result.key) {
            "->Entity" -> {
                if (!sender.isEntity) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val destination = list!!.getResult<List<Entity>>(0)!!
                if (destination.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                if (destination.size > 1) {
                    log.addError("commands.generic.tooManyTargets").output()
                    return 0
                }
                val victim = sender.transform
                val entity = destination[0]
                entity.rotation.yaw = (victim.yaw)
                entity.rotation.pitch = (victim.pitch)
                val target = entity.transform
                var checkForBlocks = false
                if (list.hasResult(1)) {
                    checkForBlocks = list.getResult(1)!!
                }
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        sender.asEntity()!!.teleport(target)
                        log.addSuccess("commands.tp.successVictim", destination[0].name).output()
                    } else {
                        log.addError("commands.tp.safeTeleportFail", sender.asEntity()!!.name, destination[0].name)
                            .output()
                        return 0
                    }
                } else {
                    sender.asEntity()!!.teleport(target)
                    log.addSuccess("commands.tp.successVictim", destination[0].name).output()
                }
                return 1
            }

            "Entity->Entity" -> {
                val victims = list!!.getResult<List<Entity>>(0)!!
                if (victims.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val destination = list.getResult<List<Entity>>(1)!!
                if (destination.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                if (destination.size > 1) {
                    log.addError("commands.generic.tooManyTargets").output()
                    return 0
                }
                val target = destination[0]
                var checkForBlocks = false
                if (list.hasResult(3)) {
                    checkForBlocks = list.getResult(3)!!
                }
                val sb = StringBuilder()
                for (victim in victims) {
                    sb.append(victim.name).append(" ")
                }
                if (checkForBlocks) {
                    if (!target.locator.levelBlock.isSolid && !target.locator.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        for (victim in victims) {
                            victim.teleport(
                                target.transform.setYaw(victim.rotation.yaw).setPitch(victim.rotation.pitch)
                            )
                        }
                        log.addSuccess("commands.tp.success", sb.toString(), target.name)
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sb.toString(), target.name).output()
                        return 0
                    }
                } else {
                    for (victim in victims) {
                        victim.teleport(target.transform.setYaw(victim.rotation.yaw).setPitch(victim.rotation.pitch))
                    }
                    log.addSuccess("commands.tp.success", sb.toString(), target.name)
                }
                log.output()
                return victims.size
            }

            "Entity->Pos" -> {
                val victims = list!!.getResult<List<Entity>>(0)!!
                if (victims.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val pos = list.getResult<Locator>(1)
                var yRot = sender.transform.rotation.pitch
                if (list.hasResult(2)) {
                    yRot = list.getResult(2)!!
                }
                var xRot = sender.transform.rotation.yaw
                if (list.hasResult(3)) {
                    xRot = list.getResult(3)!!
                }
                var checkForBlocks = false
                if (list.hasResult(4)) {
                    checkForBlocks = list.getResult(4)!!
                }
                val sb = StringBuilder()
                for (victim in victims) {
                    sb.append(victim.name).append(" ")
                }
                val target = Transform.fromObject(pos!!.position, pos.level, xRot, yRot)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        for (victim in victims) {
                            victim.teleport(target)
                        }
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sb.toString(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sb.toString(), target.toString()).output()
                        return 0
                    }
                } else {
                    for (victim in victims) {
                        victim.teleport(target)
                    }
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sb.toString(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                return 1
            }

            "Entity->Pos(FacingPos)" -> {
                val victims = list!!.getResult<List<Entity>>(0)!!
                if (victims.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val pos = list.getResult<Locator>(1)
                val lookAtLocator = list.getResult<Locator>(3)
                var checkForBlocks = false
                if (list.hasResult(4)) {
                    checkForBlocks = list.getResult(4)!!
                }
                val sb = StringBuilder()
                for (victim in victims) {
                    sb.append(victim.name).append(" ")
                }
                val bv = BVector3.fromPos(
                    Vector3(
                        lookAtLocator!!.position.south - pos!!.position.south,
                        lookAtLocator.position.up - pos.position.up,
                        lookAtLocator.position.west - pos.position.west
                    )
                )
                val target = Transform.fromObject(pos.position, pos.level, bv.yaw, bv.pitch)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        for (victim in victims) {
                            victim.teleport(target)
                        }
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sb.toString(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError(
                            "commands.tp.safeTeleportFail ",
                            sb.toString(),
                            target.position.floorX.toString() + " " + target.position.floorY + " " + target.position.floorZ
                        ).output()
                        return 0
                    }
                } else {
                    for (victim in victims) {
                        victim.teleport(target)
                    }
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sb.toString(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            "Entity->Pos(FacingEntity)" -> {
                val victims = list!!.getResult<List<Entity>>(0)!!
                if (victims.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val pos = list.getResult<Locator>(1)
                val lookAtEntity = list.getResult<List<Entity>>(3)!!
                if (lookAtEntity.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                if (lookAtEntity.size > 1) {
                    log.addTooManyTargets().output()
                    return 0
                }
                val lookAtPosition = lookAtEntity[0].position
                var checkForBlocks = false
                if (list.hasResult(4)) {
                    checkForBlocks = list.getResult(4)!!
                }
                val sb = StringBuilder()
                for (victim in victims) {
                    sb.append(victim.name).append(" ")
                }
                val bv = BVector3.fromPos(lookAtPosition.subtract(pos!!.position))
                val target = Transform.fromObject(pos.position, pos.level, bv.yaw, bv.pitch)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        for (victim in victims) {
                            victim.teleport(target)
                        }
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sb.toString(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sb.toString(), target.toString()).output()
                        return 0
                    }
                } else {
                    for (victim in victims) {
                        victim.teleport(target)
                    }
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sb.toString(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            "->Pos" -> {
                if (!sender.isEntity) {
                    log.addError("commands.generic.noTargetMatch").output()
                    return 0
                }
                val pos = list!!.getResult<Locator>(0)
                var yRot = sender.transform.rotation.pitch
                if (list.hasResult(1)) {
                    yRot = list.getResult(1)!!
                }
                var xRot = sender.transform.rotation.yaw
                if (list.hasResult(2)) {
                    xRot = list.getResult(2)!!
                }
                var checkForBlocks = false
                if (list.hasResult(3)) {
                    checkForBlocks = list.getResult(3)!!
                }
                val target = Transform.fromObject(pos!!.position, pos.level, xRot, yRot)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        sender.asEntity()!!.teleport(target)
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sender.name,
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sender.name, target.toString()).output()
                        return 0
                    }
                } else {
                    sender.asEntity()!!.teleport(target)
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sender.name,
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            "->Pos(FacingPos)" -> {
                if (!sender.isEntity) {
                    log.addError("commands.generic.noTargetMatch").output()
                    return 0
                }
                val pos = list!!.getResult<Locator>(0)
                val lookAtLocator = list.getResult<Locator>(2)
                var checkForBlocks = false
                if (list.hasResult(3)) {
                    checkForBlocks = list.getResult(3)!!
                }
                val bv = BVector3.fromPos(
                    Vector3(
                        lookAtLocator!!.position.south - pos!!.position.south,
                        lookAtLocator.position.up - pos.position.up,
                        lookAtLocator.position.west - pos.position.west
                    )
                )
                val target = Transform.fromObject(pos.position, pos.level, bv.yaw, bv.pitch)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        sender.asEntity()!!.teleport(target)
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sender.asEntity()!!.name,
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sender.asEntity()!!.name, target.toString())
                            .output()
                        return 0
                    }
                } else {
                    sender.asEntity()!!.teleport(target)
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sender.asEntity()!!.name,
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            "->Pos(FacingEntity)" -> {
                if (!sender.isEntity) {
                    log.addError("commands.generic.noTargetMatch").output()
                    return 0
                }
                val pos = list!!.getResult<Locator>(0)
                val lookAtEntity = list.getResult<List<Entity>>(2)!!
                if (lookAtEntity.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                if (lookAtEntity.size > 1) {
                    log.addTooManyTargets().output()
                    return 0
                }
                val lookAtPosition = lookAtEntity[0].position
                var checkForBlocks = false
                if (list.hasResult(3)) {
                    checkForBlocks = list.getResult(3)!!
                }
                val bv = BVector3.fromPos(lookAtPosition.subtract(pos!!.position))
                val target = Transform.fromObject(pos.position, pos.level, bv.yaw, bv.pitch)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        sender.asEntity()!!.teleport(target)
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sender.asEntity()!!.name,
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail", sender.asEntity()!!.name, target.toString())
                            .output()
                        return 0
                    }
                } else {
                    sender.asEntity()!!.teleport(target)
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sender.asEntity()!!.name,
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            else -> {
                return 0
            }
        }
    }
}
