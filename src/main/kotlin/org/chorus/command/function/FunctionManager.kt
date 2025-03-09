package org.chorus.command.function

import cn.nukkit.command.data.CommandEnum
import lombok.Getter
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import kotlin.collections.HashMap
import kotlin.collections.MutableMap
import kotlin.collections.set

@Getter
class FunctionManager(private val rootPath: Path) {
    private val functions: MutableMap<String, Function> = HashMap()

    init {
        try {
            if (!Files.exists(this.rootPath)) {
                Files.createDirectories(this.rootPath)
            }
            loadFunctions()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    constructor(rootPath: String) : this(Path.of(rootPath))

    fun loadFunctions() {
        try {
            Files.walkFileTree(this.rootPath, object : SimpleFileVisitor<Path?>() {
                @Throws(IOException::class)
                override fun visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (path.toString().endsWith(".mcfunction")) {
                        functions[path.toString().replace(rootPath.toString() + "\\", "").replace("\\\\".toRegex(), "/")
                            .replace(".mcfunction", "")] = Function.Companion.fromPath(path)
                    }
                    return FileVisitResult.CONTINUE
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun reload() {
        functions.clear()
        loadFunctions()
        CommandEnum.Companion.FUNCTION_FILE.updateSoftEnum()
    }

    fun containFunction(name: String): Boolean {
        return functions.containsKey(name)
    }

    fun getFunction(name: String): Function? {
        return functions[name]
    }
}
