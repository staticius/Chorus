package org.chorus.level.tickingarea.storage

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import com.google.gson.reflect.TypeToken
import org.chorus.Server
import org.chorus.entity.EntityHuman.getName
import org.chorus.utils.JSONUtils
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.collections.set

class JSONTickingAreaStorage(path: String) : TickingAreaStorage {
    /**
     * 存储常加载区域的根目录
     */
    protected var filePath: Path = Paths.get(path)

    //               row     column
    //            LevelName AreaName
    protected var areaMap: Table<String?, String, TickingArea> = HashBasedTable.create<String?, String, TickingArea>()

    init {
        try {
            if (!Files.exists(this.filePath)) {
                Files.createDirectories(this.filePath)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun addTickingArea(area: TickingArea) {
        areaMap.put(area.getLevelName(), area.getName(), area)
        save()
    }

    override fun addTickingArea(vararg areas: TickingArea?) {
        for (each in areas) {
            areaMap.put(each.getLevelName(), each.getName(), each)
        }
        save()
    }

    override fun readTickingArea(): Map<String, TickingArea> {
        val rootDir = File(filePath.toString())
        val aMap: HashMap<String, TickingArea> = HashMap<String, TickingArea>()
        for (each in Objects.requireNonNull<Array<File>>(rootDir.listFiles())) {
            val jsonFile = File(each, "tickingarea.json")
            if (jsonFile.exists()) {
                try {
                    FileReader(jsonFile).use { fr ->
                        val areas: Set<TickingArea> = JSONUtils.from<HashSet<TickingArea>>(fr, type)
                        for (area in areas) {
                            areaMap.put(area.getLevelName(), area.getName(), area)
                            aMap[area.getName()] = area
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return aMap
    }

    override fun removeTickingArea(name: String) {
        areaMap.columnMap().remove(name)
        save()
    }

    override fun removeAllTickingArea() {
        areaMap.clear()
        save()
    }

    override fun containTickingArea(name: String?): Boolean {
        return areaMap.containsColumn(name)
    }

    private fun save() {
        try {
            for (level in Server.instance.levels.values()) {
                if (areaMap.containsRow(level.name)) {
                    Files.writeString(
                        Path.of(filePath.toString(), level.folderName, "tickingarea.json"),
                        JSONUtils.toPretty<Array<Any>>(
                            areaMap.rowMap()[level.name]!!.values().toArray()
                        )
                    )
                } else {
                    Files.deleteIfExists(Path.of(filePath.toString(), level.folderName, "tickingarea.json"))
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val type: TypeToken<HashSet<TickingArea>> = object : TypeToken<HashSet<TickingArea?>?>() {
        }
    }
}
