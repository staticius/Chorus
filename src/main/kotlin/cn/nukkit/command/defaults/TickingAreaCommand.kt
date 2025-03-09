package cn.nukkit.command.defaults

import cn.nukkit.Server
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.level.Locator
import cn.nukkit.math.*
import cn.nukkit.utils.TextFormat
import java.util.stream.Collectors
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.Set
import kotlin.collections.set
import kotlin.math.max
import kotlin.math.min

class TickingAreaCommand(name: String) : VanillaCommand(name, "commands.tickingarea.description") {
    init {
        this.permission = "nukkit.command.tickingarea"
        commandParameters.clear()
        commandParameters["add-pos"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("add", arrayOf<String?>("add")),
            CommandParameter.Companion.newType("from", CommandParamType.POSITION),
            CommandParameter.Companion.newType("to", CommandParamType.POSITION),
            CommandParameter.Companion.newType("name", true, CommandParamType.STRING)
        )
        commandParameters["add-circle"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("add", arrayOf<String?>("add")),
            CommandParameter.Companion.newEnum("circle", arrayOf<String?>("circle")),
            CommandParameter.Companion.newType("center", CommandParamType.POSITION),
            CommandParameter.Companion.newType("radius", CommandParamType.INT),
            CommandParameter.Companion.newType("name", true, CommandParamType.STRING)
        )
        commandParameters["remove-pos"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("remove", arrayOf<String?>("remove")),
            CommandParameter.Companion.newType("position", CommandParamType.POSITION)
        )
        commandParameters["remove-name"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("remove", arrayOf<String?>("remove")),
            CommandParameter.Companion.newType("name", CommandParamType.STRING)
        )
        commandParameters["remove-all"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("remove-all", arrayOf<String?>("remove-all"))
        )
        commandParameters["list"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("list", arrayOf<String?>("list")),
            CommandParameter.Companion.newEnum("all-dimensions", true, arrayOf<String?>("all-dimensions"))
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val manager: TickingAreaManager = Server.getInstance().tickingAreaManager
        val level = sender.locator.level
        when (result.key) {
            "add-pos" -> {
                val from = list!!.getResult<Locator>(1)
                val to = list.getResult<Locator>(2)
                var name: String? = "" //will auto generate name if not set, like "Area0"
                if (list.hasResult(3)) name = list.getResult(3)
                if (manager.containTickingArea(name)) {
                    log.addError("commands.tickingarea-add.conflictingname", name).output()
                    return 0
                }
                val area: TickingArea = TickingArea(name, level.name)
                for (chunkX in min(from!!.position.chunkX.toDouble(), to!!.position.chunkX.toDouble())..max(
                    from.position.chunkX.toDouble(), to.position.chunkX.toDouble()
                )) {
                    for (chunkZ in min(from.position.chunkZ.toDouble(), to.position.chunkZ.toDouble())..max(
                        from.position.chunkZ.toDouble(), to.position.chunkZ.toDouble()
                    )) {
                        area.addChunk(ChunkPos(chunkX, chunkZ))
                    }
                }
                manager.addTickingArea(area)
                log.addSuccess(
                    "commands.tickingarea-add-bounds.success",
                    from.position.x.toInt().toString() + "," + from.position.y.toInt() + "," + from.position.z.toInt(),
                    to.position.x.toInt().toString() + "," + to.position.y.toInt() + "," + to.position.z.toInt()
                ).output()
                return 1
            }

            "add-circle" -> {
                val center = list!!.getResult<Locator>(2)
                val radius = list.getResult<Int>(3)!!
                var name: String? = "" //will auto generate name if not set, like "Area0"
                if (list.hasResult(4)) name = list.getResult(4)
                if (manager.containTickingArea(name)) {
                    log.addError("commands.tickingarea-add.conflictingname", name).output()
                    return 0
                }
                //计算出哪些区块和圆重合
                val area: TickingArea = TickingArea(name, level.name)
                val centerVec2 = Vector2(center!!.position.chunkX.toDouble(), center.position.chunkZ.toDouble())
                val radiusSquared = radius * radius
                for (chunkX in center.position.chunkX - radius..center.position.chunkX + radius) {
                    for (chunkZ in center.position.chunkZ - radius..center.position.chunkZ + radius) {
                        val distanceSquared = Vector2(chunkX.toDouble(), chunkZ.toDouble()).distanceSquared(centerVec2)
                        if (distanceSquared <= radiusSquared) {
                            area.addChunk(ChunkPos(chunkX, chunkZ))
                        }
                    }
                }
                manager.addTickingArea(area)
                log.addSuccess(
                    "commands.tickingarea-add-circle.success",
                    center.position.x.toInt()
                        .toString() + "," + center.position.y.toInt() + "," + center.position.z.toInt(),
                    radius.toString()
                ).output()
                return 1
            }

            "remove-pos" -> {
                val pos = list!!.getResult<Locator>(1)
                if (manager.getTickingAreaByPos(pos) == null) {
                    log.addSuccess(
                        "commands.tickingarea-remove.failure",
                        pos!!.position.x.toInt().toString(),
                        pos.position.y.toInt().toString(),
                        pos.position.z.toInt().toString()
                    ).output()
                    return 0
                }
                manager.removeTickingArea(manager.getTickingAreaByPos(pos).getName())
                log.addSuccess("commands.tickingarea-remove.success").output()
                return 1
            }

            "remove-name" -> {
                val name = list!!.getResult<String>(1)
                if (!manager.containTickingArea(name)) {
                    log.addSuccess("commands.tickingarea-remove.byname.failure", name).output()
                    return 0
                }
                manager.removeTickingArea(name)
                log.addSuccess("commands.tickingarea-remove.success").output()
                return 1
            }

            "remove-all" -> {
                if (manager.getAllTickingArea().isEmpty()) {
                    log.addSuccess("commands.tickingarea-list.failure.allDimensions").output()
                    return 0
                }
                manager.removeAllTickingArea()
                log.addSuccess("commands.tickingarea-remove_all.success").output()
                return 1
            }

            "list" -> {
                var areas: Set<TickingArea> = manager.getAllTickingArea()
                val showAll = list!!.hasResult(1)
                if (!showAll) {
                    areas = areas.stream().filter { area: TickingArea -> area.getLevelName() == level.name }.collect(
                        Collectors.toSet<TickingArea>()
                    )
                    if (areas.isEmpty()) {
                        log.addError("commands.tickingarea-remove_all.failure").output()
                        return 0
                    }
                    log.addSuccess(TextFormat.GREEN.toString() + "%commands.tickingarea-list.success.currentDimension")
                        .output()
                    for (area in areas) {
                        val minAndMax: List<ChunkPos> = area.minAndMaxChunkPos()
                        val min: ChunkPos = minAndMax[0]
                        val max: ChunkPos = minAndMax[1]
                        log.addSuccess(" - " + area.getName() + ": " + min.x + " " + min.z + " %commands.tickingarea-list.to " + max.x + " " + max.z)
                            .output()
                    }
                    log.addSuccess("commands.tickingarea.inuse", areas.size.toString(), "∞").output()
                } else {
                    if (areas.isEmpty()) {
                        log.addError("commands.tickingarea-list.failure.allDimensions").output()
                        return 0
                    }
                    log.addSuccess(TextFormat.GREEN.toString() + "%commands.tickingarea-list.success.allDimensions")
                        .output()
                    for (area in areas) {
                        val minAndMax: List<ChunkPos> = area.minAndMaxChunkPos()
                        val min: ChunkPos = minAndMax[0]
                        val max: ChunkPos = minAndMax[1]
                        log.addSuccess(" - " + area.getName() + ": " + min.x + " " + min.z + " %commands.tickingarea-list.to " + max.x + " " + max.z)
                            .output()
                    }
                    log.addSuccess("commands.tickingarea.inuse", areas.size.toString(), "∞").output()
                }
                return 1
            }

            else -> {
                return 0
            }
        }
    }
}
