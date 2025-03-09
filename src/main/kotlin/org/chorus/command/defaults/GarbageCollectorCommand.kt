package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.math.NukkitMath
import org.chorus.utils.TextFormat

/**
 * @author xtypr
 * @since 2015/11/11
 */
class GarbageCollectorCommand(name: String) :
    TestCommand(name, "%nukkit.command.gc.description", "%nukkit.command.gc.usage"),
    CoreCommand {
    init {
        this.permission = "nukkit.command.gc"
        commandParameters.clear()
    }

    override fun execute(sender: CommandSender, commandLabel: String?, args: Array<String?>): Boolean {
        if (!this.testPermission(sender)) {
            return false
        }

        var chunksCollected = 0
        var entitiesCollected = 0
        var tilesCollected = 0
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()

        for (level in sender.server.levels.values) {
            val chunksCount = level.chunks.size
            val entitiesCount = level.entities.size
            val tilesCount = level.blockEntities.size
            level.doLevelGarbageCollection(true)
            chunksCollected += chunksCount - level.chunks.size
            entitiesCollected += entitiesCount - level.entities.size
            tilesCollected += tilesCount - level.blockEntities.size
        }

        ThreadCache.clean()
        System.gc()

        val freedMemory = usedMemory - (runtime.totalMemory() - runtime.freeMemory())

        sender.sendMessage(TextFormat.GREEN.toString() + "---- " + TextFormat.WHITE + "Garbage collection result" + TextFormat.GREEN + " ----")
        sender.sendMessage(TextFormat.GOLD.toString() + "Chunks: " + TextFormat.RED + chunksCollected)
        sender.sendMessage(TextFormat.GOLD.toString() + "Entities: " + TextFormat.RED + entitiesCollected)
        sender.sendMessage(TextFormat.GOLD.toString() + "Block Entities: " + TextFormat.RED + tilesCollected)
        sender.sendMessage(
            TextFormat.GOLD.toString() + "Memory freed: " + TextFormat.RED + NukkitMath.round(
                (freedMemory / 1024.0 / 1024.0),
                2
            ) + " MB"
        )
        return true
    }
}
