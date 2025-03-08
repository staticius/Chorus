package cn.nukkit.command.defaults

import cn.nukkit.Server
import cn.nukkit.command.*
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.scheduler.Task
import cn.nukkit.utils.TextFormat

class TpsCommand(name: String) : Command(name, "get server tps"), CoreCommand {
    init {
        this.permission = "nukkit.tps.status"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newType("count", true, CommandParamType.INT)
            )
        )
    }

    override fun execute(sender: CommandSender, commandLabel: String?, args: Array<String?>): Boolean {
        if (!this.testPermission(sender)) {
            return false
        }

        var count = 1
        if (args.size > 0) {
            count = args[0]!!.toInt()
        }

        if (count == 1) {
            val currentTps = Server.getInstance().ticksPerSecond
            sender.sendMessage(getTpsColor(currentTps).toString() + " Current TPS: " + currentTps)
        } else {
            Server.getInstance().scheduler.scheduleRepeatingTask(TpsTestTask(sender, count), 20)
        }
        return true
    }

    private fun getTpsColor(tps: Float): TextFormat {
        var tpsColor = TextFormat.GREEN
        if (tps < 12) {
            tpsColor = TextFormat.RED
        } else if (tps < 17) {
            tpsColor = TextFormat.GOLD
        }
        return tpsColor
    }

    private inner class TpsTestTask(private val sender: CommandSender, private val count: Int) : Task() {
        private var currentCount = 0
        private var tpsSum = 0f

        override fun onRun(currentTick: Int) {
            currentCount++
            val currentTps = Server.getInstance().ticksPerSecond

            sender.sendMessage("[" + currentCount + "]" + getTpsColor(currentTps) + " Current TPS: " + currentTps)
            tpsSum += currentTps
            if (currentCount >= count) {
                sender.sendMessage("Average TPS: " + (tpsSum / count))
                this.cancel()
            }
        }
    }
}
