package cn.nukkit.plugin

import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.logging.Logger

/**
 * @since 15-12-13
 */
object LibraryLoader {
    val baseFolder: File = File("./libraries")
    private val LOGGER: Logger = Logger.getLogger("LibraryLoader")
    private const val SUFFIX = ".jar"

    init {
        if (baseFolder.mkdir()) {
            LOGGER.info("Created libraries folder.")
        }
    }

    fun load(library: String) {
        val split = library.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(split.size == 3) { library }
        load(object : Library {
            override val groupId: String
                get() = split[0]

            override val artifactId: String
                get() = split[1]

            override val version: String
                get() = split[2]
        })
    }

    fun load(library: Library) {
        val filePath = library.groupId.replace('.', '/') + '/' + library.artifactId + '/' + library.version
        val fileName = library.artifactId + '-' + library.version + SUFFIX

        val folder = File(baseFolder, filePath)
        if (folder.mkdirs()) {
            LOGGER.info("Created " + folder.path + '.')
        }

        val file = File(folder, fileName)
        if (!file.isFile) try {
            val url = URL("https://repo1.maven.org/maven2/$filePath/$fileName")
            LOGGER.info("Get library from $url.")
            Files.copy(url.openStream(), file.toPath())
            LOGGER.info("Get library $fileName done!")
        } catch (e: IOException) {
            throw LibraryLoadException(library)
        }

        try {
            val method = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
            val accessible = method.isAccessible
            if (!accessible) {
                method.isAccessible = true
            }
            val classLoader = Thread.currentThread().contextClassLoader as URLClassLoader
            val url = file.toURI().toURL()
            method.invoke(classLoader, url)
            method.isAccessible = accessible
        } catch (e: NoSuchMethodException) {
            throw LibraryLoadException(library)
        } catch (e: MalformedURLException) {
            throw LibraryLoadException(library)
        } catch (e: IllegalAccessException) {
            throw LibraryLoadException(library)
        } catch (e: InvocationTargetException) {
            throw LibraryLoadException(library)
        }

        LOGGER.info("Load library $fileName done!")
    }
}
