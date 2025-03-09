package org.chorus.command.defaults

import cn.nukkit.camera.instruction.impl.ClearInstruction.get
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.math.NukkitMath
import cn.nukkit.utils.TextFormat
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import oshi.SystemInfo
import java.io.File
import java.lang.management.ManagementFactory
import java.nio.file.Files
import java.util.concurrent.TimeUnit

/**
 * @author xtypr
 * @since 2015/11/11
 */
class StatusCommand(name: String) :
    TestCommand(name, "%nukkit.command.status.description", "%nukkit.command.status.usage"),
    CoreCommand {
    private val systemInfo = SystemInfo()

    init {
        this.permission = "nukkit.command.status"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newEnum("mode", true, arrayOf<String?>("full", "simple"))
            )
        )
    }

    override fun execute(sender: CommandSender, commandLabel: String?, args: Array<String?>): Boolean {
        if (!this.testPermission(sender)) {
            return false
        }

        val simpleMode = args.size == 0 || !args[0].equals("full", ignoreCase = true)
        val server = sender.server

        if (simpleMode) {
            sender.sendMessage(TextFormat.GREEN.toString() + "---- " + TextFormat.WHITE + "Server status" + TextFormat.GREEN + " ----")

            val time: Long = System.currentTimeMillis() - Nukkit.START_TIME

            sender.sendMessage(TextFormat.GOLD.toString() + "Uptime: " + formatUptime(time))

            var tpsColor = TextFormat.GREEN
            val tps = server!!.ticksPerSecond
            if (tps < 12) {
                tpsColor = TextFormat.RED
            } else if (tps < 17) {
                tpsColor = TextFormat.GOLD
            }

            sender.sendMessage(
                TextFormat.GOLD.toString() + "Current TPS: " + tpsColor + NukkitMath.round(
                    tps.toDouble(),
                    2
                )
            )

            sender.sendMessage(TextFormat.GOLD.toString() + "Load: " + tpsColor + server.tickUsage + "%")


            val runtime = Runtime.getRuntime()
            val totalMB = NukkitMath.round((runtime.totalMemory().toDouble()) / 1024 / 1024, 2)
            val usedMB = NukkitMath.round((runtime.totalMemory() - runtime.freeMemory()).toDouble() / 1024 / 1024, 2)
            val maxMB = NukkitMath.round((runtime.maxMemory().toDouble()) / 1024 / 1024, 2)
            val usage = usedMB / maxMB * 100
            var usageColor = TextFormat.GREEN

            if (usage > 85) {
                usageColor = TextFormat.GOLD
            }

            sender.sendMessage(
                TextFormat.GOLD.toString() + "Used VM memory: " + usageColor + usedMB + " MB. (" + NukkitMath.round(
                    usage,
                    2
                ) + "%)"
            )

            sender.sendMessage(TextFormat.GOLD.toString() + "Total VM memory: " + TextFormat.RED + totalMB + " MB.")


            var playerColor = TextFormat.GREEN
            if ((server.onlinePlayers.size.toFloat() / server.maxPlayers.toFloat()) > 0.85) {
                playerColor = TextFormat.GOLD
            }

            sender.sendMessage(
                TextFormat.GOLD.toString() + "Players: " + playerColor + server.onlinePlayers.size + TextFormat.GREEN + " online, " +
                        TextFormat.RED + server.maxPlayers + TextFormat.GREEN + " max. "
            )

            for (level in server.levels.values) {
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "World \"" + level.folderName + "\"" + (if (level.folderName != level.name) " (" + level.name + ")" else "") + ": " +
                            TextFormat.RED + level.chunks.size + TextFormat.GREEN + " chunks, " +
                            TextFormat.RED + level.entities.size + TextFormat.GREEN + " entities, " +
                            TextFormat.RED + level.blockEntities.size + TextFormat.GREEN + " blockEntities." +
                            " Time " + (if (level.tickRate > 1 || level.tickRateTime > 40) TextFormat.RED else TextFormat.YELLOW) + NukkitMath.round(
                        level.tickRateTime.toDouble(),
                        2
                    ) + "ms" +
                            (" [delayOpt " + (level.tickRateOptDelay - 1) + "]") +
                            (if (level.tickRate > 1) " (tick rate " + (19 - level.tickRate) + ")" else "") +
                            (if (level.baseTickGameLoop.isRunning) " (" + (if (level.baseTickGameLoop.tps >= 19) TextFormat.GREEN else (if (level.baseTickGameLoop.tps < 5) TextFormat.RED else TextFormat.YELLOW)) + level.baseTickGameLoop.tps + " TPS, " + level.baseTickGameLoop.mspt + " MSPT)" else "")
                )
            }
        } else {
            // 完整模式
            sender.sendMessage(TextFormat.GREEN.toString() + "---- " + TextFormat.WHITE + "Server status" + TextFormat.GREEN + " ----")

            // PNX服务器信息
            run {
                sender.sendMessage(TextFormat.YELLOW.toString() + ">>> " + TextFormat.WHITE + "PNX Server Info" + TextFormat.YELLOW + " <<<" + TextFormat.RESET)
                // 运行时间
                val time: Long = System.currentTimeMillis() - Nukkit.START_TIME
                sender.sendMessage(TextFormat.GOLD.toString() + "Uptime: " + formatUptime(time))
                // TPS
                var tpsColor = TextFormat.GREEN
                val tps = server!!.ticksPerSecond
                if (tps < 12) {
                    tpsColor = TextFormat.RED
                } else if (tps < 17) {
                    tpsColor = TextFormat.GOLD
                }
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "Current TPS: " + tpsColor + NukkitMath.round(
                        tps.toDouble(),
                        2
                    )
                )
                // 游戏刻负载
                sender.sendMessage(TextFormat.GOLD.toString() + "Tick Load: " + tpsColor + server.tickUsage + "%")
                // 在线玩家情况
                var playerColor = TextFormat.GREEN
                if ((server.onlinePlayers.size.toFloat() / server.maxPlayers.toFloat()) > 0.85) {
                    playerColor = TextFormat.GOLD
                }
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "Players: " + playerColor + server.onlinePlayers.size + TextFormat.GREEN + " online, " +
                            TextFormat.RED + server.maxPlayers + TextFormat.GREEN + " max. "
                )
                // 各个世界的情况
                for (level in server.levels.values) {
                    sender.sendMessage(
                        TextFormat.GOLD.toString() + "World \"" + level.folderName + "\"" + (if (level.folderName != level.name) " (" + level.name + ")" else "") + ": " +
                                TextFormat.RED + level.chunks.size + TextFormat.GREEN + " chunks, " +
                                TextFormat.RED + level.entities.size + TextFormat.GREEN + " entities, " +
                                TextFormat.RED + level.blockEntities.size + TextFormat.GREEN + " blockEntities." +
                                " Time " + (if (level.tickRate > 1 || level.tickRateTime > 40) TextFormat.RED else TextFormat.YELLOW) + NukkitMath.round(
                            level.tickRateTime.toDouble(),
                            2
                        ) + "ms" +
                                (" [delayOpt " + (level.tickRateOptDelay - 1) + "]") +
                                (if (level.tickRate > 1) " (tick rate " + (19 - level.tickRate) + ")" else "")
                    )
                }
                sender.sendMessage("")
            }
            // 操作系统&JVM信息
            run {
                val os = systemInfo.operatingSystem
                val mxBean = ManagementFactory.getRuntimeMXBean()
                sender.sendMessage(TextFormat.YELLOW.toString() + ">>> " + TextFormat.WHITE + "OS & JVM Info" + TextFormat.YELLOW + " <<<" + TextFormat.RESET)
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "OS: " + TextFormat.AQUA + os.family + " " + os.manufacturer + " " +
                            os.versionInfo.version + " " + os.versionInfo.codeName + " " + os.bitness + "bit, " +
                            "build " + os.versionInfo.buildNumber
                )
                sender.sendMessage(TextFormat.GOLD.toString() + "JVM: " + TextFormat.AQUA + mxBean.vmName + " " + mxBean.vmVendor + " " + mxBean.vmVersion)
                try {
                    val vm = isInVM(systemInfo.hardware)
                    if (vm == null) {
                        sender.sendMessage(TextFormat.GOLD.toString() + "Virtual environment: " + TextFormat.GREEN + "no")
                    } else {
                        sender.sendMessage(TextFormat.GOLD.toString() + "Virtual environment: " + TextFormat.YELLOW + "yes (" + vm + ")")
                    }
                } catch (ignore: Exception) {
                }
                sender.sendMessage("")
            }
            // 网络信息
            try {
                val network = server!!.network
                if (network.hardWareNetworkInterfaces != null) {
                    sender.sendMessage(TextFormat.YELLOW.toString() + ">>> " + TextFormat.WHITE + "Network Info" + TextFormat.YELLOW + " <<<" + TextFormat.RESET)
                    sender.sendMessage(
                        TextFormat.GOLD.toString() + "Network upload: " + TextFormat.GREEN + formatKB(
                            network.upload
                        ) + "/s"
                    )
                    sender.sendMessage(
                        TextFormat.GOLD.toString() + "Network download: " + TextFormat.GREEN + formatKB(
                            network.download
                        ) + "/s"
                    )
                    sender.sendMessage(TextFormat.GOLD.toString() + "Network hardware list: ")
                    var list: ObjectArrayList<String?>
                    for (each in network.hardWareNetworkInterfaces!!) {
                        list = ObjectArrayList(each.iPv4addr.size + each.iPv6addr.size)
                        list.addElements(0, each.iPv4addr)
                        list.addElements(list.size, each.iPv6addr)
                        sender.sendMessage(TextFormat.AQUA.toString() + "  " + each.displayName)
                        sender.sendMessage(
                            TextFormat.RESET.toString() + "    " + formatKB(each.speed) + "/s " + TextFormat.GRAY + java.lang.String.join(
                                ", ",
                                list
                            )
                        )
                    }
                    sender.sendMessage("")
                }
            } catch (ignored: Exception) {
                sender.sendMessage(TextFormat.RED.toString() + "    Failed to get network info.")
            }
            // CPU信息
            run {
                val cpu: CentralProcessor = systemInfo.hardware.processor
                sender.sendMessage(TextFormat.YELLOW.toString() + ">>> " + TextFormat.WHITE + "CPU Info" + TextFormat.YELLOW + " <<<" + TextFormat.RESET)
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "CPU: " + TextFormat.AQUA + cpu.getProcessorIdentifier()
                        .getName() + TextFormat.GRAY +
                            " (" + formatFreq(cpu.getMaxFreq()) + " baseline; " + cpu.getPhysicalProcessorCount() + " cores, " + cpu.getLogicalProcessorCount() + " logical cores)"
                )
                sender.sendMessage(TextFormat.GOLD.toString() + "Thread count: " + TextFormat.GREEN + Thread.getAllStackTraces().size)
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "CPU Features: " + TextFormat.RESET + (if (cpu.getProcessorIdentifier()
                            .isCpu64bit()
                    ) "64bit, " else "32bit, ") +
                            cpu.getProcessorIdentifier().getModel() + ", micro-arch: " + cpu.getProcessorIdentifier()
                        .getMicroarchitecture()
                )
                sender.sendMessage("")
            }
            // 内存信息
            run {
                val globalMemory: GlobalMemory = systemInfo.hardware.memory
                val physicalMemories: List<PhysicalMemory> = globalMemory.getPhysicalMemory()
                val virtualMemory: VirtualMemory = globalMemory.getVirtualMemory()
                val allPhysicalMemory: Long = globalMemory.getTotal() / 1000
                val usedPhysicalMemory: Long = (globalMemory.getTotal() - globalMemory.getAvailable()) / 1000
                val allVirtualMemory: Long = virtualMemory.getVirtualMax() / 1000
                val usedVirtualMemory: Long = virtualMemory.getVirtualInUse() / 1000
                sender.sendMessage(TextFormat.YELLOW.toString() + ">>> " + TextFormat.WHITE + "Memory Info" + TextFormat.YELLOW + " <<<" + TextFormat.RESET)
                //JVM内存
                val runtime = Runtime.getRuntime()
                val totalMB = NukkitMath.round((runtime.totalMemory().toDouble()) / 1024 / 1024, 2)
                val usedMB =
                    NukkitMath.round((runtime.totalMemory() - runtime.freeMemory()).toDouble() / 1024 / 1024, 2)
                val maxMB = NukkitMath.round((runtime.maxMemory().toDouble()) / 1024 / 1024, 2)
                var usage = usedMB / maxMB * 100
                var usageColor = TextFormat.GREEN
                if (usage > 85) {
                    usageColor = TextFormat.GOLD
                }
                sender.sendMessage(TextFormat.GOLD.toString() + "JVM memory: ")
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "  Used JVM memory: " + usageColor + usedMB + " MB. (" + NukkitMath.round(
                        usage,
                        2
                    ) + "%)"
                )
                sender.sendMessage(TextFormat.GOLD.toString() + "  Total JVM memory: " + TextFormat.RED + totalMB + " MB.")
                sender.sendMessage(TextFormat.GOLD.toString() + "  Maximum JVM memory: " + TextFormat.RED + maxMB + " MB.")
                // 操作系统内存
                usage = usedPhysicalMemory.toDouble() / allPhysicalMemory * 100
                usageColor = TextFormat.GREEN
                if (usage > 85) {
                    usageColor = TextFormat.GOLD
                }
                sender.sendMessage(TextFormat.GOLD.toString() + "OS memory: ")
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "  Physical memory: " + TextFormat.GREEN + usageColor + formatMB(
                        usedPhysicalMemory
                    ) + " / " + formatMB(allPhysicalMemory) + ". (" + NukkitMath.round(usage, 2) + "%)"
                )
                usage = usedVirtualMemory.toDouble() / allVirtualMemory * 100
                usageColor = TextFormat.GREEN
                if (usage > 85) {
                    usageColor = TextFormat.GOLD
                }
                sender.sendMessage(
                    TextFormat.GOLD.toString() + "  Virtual memory: " + TextFormat.GREEN + usageColor + formatMB(
                        usedVirtualMemory
                    ) + " / " + formatMB(allVirtualMemory) + ". (" + NukkitMath.round(usage, 2) + "%)"
                )
                if (physicalMemories.size > 0) sender.sendMessage(TextFormat.GOLD.toString() + "  Hardware list: ")
                for (each in physicalMemories) {
                    sender.sendMessage(
                        TextFormat.AQUA.toString() + "    " + each.getBankLabel() + " @ " + formatFreq(
                            each.getClockSpeed()
                        ) + TextFormat.WHITE + " " + formatMB(each.getCapacity() / 1000)
                    )
                    sender.sendMessage(TextFormat.GRAY.toString() + "      " + each.getMemoryType() + ", " + each.getManufacturer())
                }
                sender.sendMessage("")
            }
        }

        return true
    }

    enum class ComputerSystemEntry {
        HYPERVISORPRESENT
    }

    companion object {
        private val UPTIME_FORMAT = TextFormat.RED.toString() + "%d" + TextFormat.GOLD + " days " +
                TextFormat.RED + "%d" + TextFormat.GOLD + " hours " +
                TextFormat.RED + "%d" + TextFormat.GOLD + " minutes " +
                TextFormat.RED + "%d" + TextFormat.GOLD + " seconds"
        private val vmVendor: MutableMap<String, String> = HashMap(10, 0.99f)
        private val vmMac: MutableMap<String, String> = HashMap(10, 0.99f)
        private val vmModelArray = arrayOf(
            "Linux KVM", "Linux lguest", "OpenVZ", "Qemu",
            "Microsoft Virtual PC", "VMWare", "linux-vserver", "Xen", "FreeBSD Jail", "VirtualBox", "Parallels",
            "Linux Containers", "LXC", "Bochs"
        )

        init {
            vmVendor["bhyve"] = "bhyve"
            vmVendor["KVM"] = "KVM"
            vmVendor["TCG"] = "QEMU"
            vmVendor["Microsoft Hv"] =
                "Microsoft Hyper-V or Windows Virtual PC"
            vmVendor["lrpepyh vr"] = "Parallels"
            vmVendor["VMware"] = "VMware"
            vmVendor["XenVM"] = "Xen HVM"
            vmVendor["ACRN"] = "Project ACRN"
            vmVendor["QNXQVMBSQG"] = "QNX Hypervisor"
        }

        init {
            vmMac["00:50:56"] = "VMware ESX 3"
            vmMac["00:0C:29"] = "VMware ESX 3"
            vmMac["00:05:69"] = "VMware ESX 3"
            vmMac["00:03:FF"] = "Microsoft Hyper-V"
            vmMac["00:1C:42"] = "Parallels Desktop"
            vmMac["00:0F:4B"] = "Virtual Iron 4"
            vmMac["00:16:3E"] = "Xen or Oracle VM"
            vmMac["08:00:27"] = "VirtualBox"
            vmMac["02:42:AC"] = "Docker Container"
        }

        private fun formatKB(bytes: Double): String {
            return NukkitMath.round((bytes / 1024 * 1000), 2).toString() + " KB"
        }

        private fun formatKB(bytes: Long): String {
            return NukkitMath.round((bytes / 1024.0 * 1000), 2).toString() + " KB"
        }

        private fun formatMB(bytes: Double): String {
            return NukkitMath.round((bytes / 1024 / 1024 * 1000), 2).toString() + " MB"
        }

        private fun formatMB(bytes: Long): String {
            return NukkitMath.round((bytes / 1024.0 / 1024 * 1000), 2).toString() + " MB"
        }

        private fun formatFreq(hz: Long): String {
            return if (hz >= 1000000000) {
                String.format("%.2fGHz", hz / 1000000000.0)
            } else if (hz >= 1000 * 1000) {
                String.format("%.2fMHz", hz / 1000000.0)
            } else if (hz >= 1000) {
                String.format("%.2fKHz", hz / 1000.0)
            } else {
                String.format("%dHz", hz)
            }
        }

        private fun formatUptime(uptime: Long): String {
            var uptime = uptime
            val days = TimeUnit.MILLISECONDS.toDays(uptime)
            uptime -= TimeUnit.DAYS.toMillis(days)
            val hours = TimeUnit.MILLISECONDS.toHours(uptime)
            uptime -= TimeUnit.HOURS.toMillis(hours)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(uptime)
            uptime -= TimeUnit.MINUTES.toMillis(minutes)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(uptime)
            return String.format(UPTIME_FORMAT, days, hours, minutes, seconds)
        }

        private fun isInVM(hardware: HardwareAbstractionLayer): String? {
            // CPU型号检测
            val vendor: String = hardware.getProcessor().getProcessorIdentifier().getVendor().trim { it <= ' ' }
            if (vmVendor.containsKey(vendor)) {
                return vmVendor[vendor]
            }

            // MAC地址检测
            val nifs: List<NetworkIF> = hardware.getNetworkIFs()
            for (nif in nifs) {
                val mac: String = nif.getMacaddr().uppercase()
                val oui = if (mac.length > 7) mac.substring(0, 8) else mac
                if (vmMac.containsKey(oui)) {
                    return vmMac[oui]
                }
            }

            // 模型检测
            val model: String = hardware.getComputerSystem().getModel()
            for (vm in vmModelArray) {
                if (model.contains(vm)) {
                    return vm
                }
            }
            val manufacturer: String = hardware.getComputerSystem().getManufacturer()
            if ("Microsoft Corporation" == manufacturer && "Virtual Machine" == model) {
                return "Microsoft Hyper-V"
            }

            //内存型号检测
            if (hardware.getMemory().getPhysicalMemory().get(0).getManufacturer() == "QEMU") {
                return "QEMU"
            }

            //检查Windows系统参数
            //Wmi虚拟机查询只能在Windows上使用，Linux上不执行这个部分即可
            if (System.getProperties().getProperty("os.name").uppercase().contains("WINDOWS")) {
                val computerSystemQuery: WmiQuery<ComputerSystemProperty> = WmiQuery<Any?>(
                    "Win32_ComputerSystem",
                    ComputerSystemEntry::class.java
                )
                val result: WmiResult<*> =
                    WmiQueryHandler.createInstance().queryWMI<ComputerSystemProperty>(computerSystemQuery)
                val tmp: Any = result.getValue(ComputerSystemEntry.HYPERVISORPRESENT, 0)
                if (tmp != null && tmp.toString() == "true") {
                    return "Hyper-V"
                }
            } else {
                val file = File("/.dockerenv")
                if (file.exists()) {
                    return "Docker Container"
                }
                val cgroupFile = File("/proc/1/cgroup")
                if (cgroupFile.exists()) {
                    try {
                        Files.lines(cgroupFile.toPath()).use { lineStream ->
                            val searchResult =
                                lineStream.filter { line: String -> line.contains("docker") || line.contains("lxc") }
                            if (searchResult.findAny().isPresent) {
                                return "Docker Container"
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            return null
        }
    }
}
