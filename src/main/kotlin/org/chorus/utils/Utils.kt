package org.chorus.utils

import org.chorus.block.Block
import org.chorus.level.Level
import org.chorus.math.*
import org.chorus.math.NukkitMath.ceilDouble
import org.chorus.math.NukkitMath.floorDouble
import io.netty.buffer.ByteBuf
import lombok.extern.slf4j.Slf4j
import java.io.*
import java.lang.management.ManagementFactory
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.math.max
import kotlin.math.min

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Slf4j
object Utils {
    val EMPTY_INTEGERS: Array<Int?> = arrayOfNulls(0)


    val random: SplittableRandom = SplittableRandom()


    @Throws(IOException::class)
    fun safeWrite(currentFile: File, operation: Consumer<File?>) {
        val parent = currentFile.parentFile
        val newFile = File(parent, currentFile.name + "_new")
        val oldFile = File(parent, currentFile.name + "_old")
        val olderFile = File(parent, currentFile.name + "_older")

        if (olderFile.isFile && !olderFile.delete()) {
            Utils.log.error("Could not delete the file {}", olderFile.absolutePath)
        }

        if (newFile.isFile && !newFile.delete()) {
            Utils.log.error("Could not delete the file {}", newFile.absolutePath)
        }

        try {
            operation.accept(newFile)
        } catch (e: Exception) {
            throw IOException(e)
        }

        if (oldFile.isFile) {
            if (olderFile.isFile) {
                copyFile(oldFile, olderFile)
            } else if (!oldFile.renameTo(olderFile)) {
                throw IOException("Could not rename the $oldFile to $olderFile")
            }
        }

        if (currentFile.isFile && !currentFile.renameTo(oldFile)) {
            throw IOException("Could not rename the $currentFile to $oldFile")
        }

        if (!newFile.renameTo(currentFile)) {
            throw IOException("Could not rename the $newFile to $currentFile")
        }
    }

    @Throws(IOException::class)
    fun writeFile(fileName: String, content: String) {
        writeFile(fileName, ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)))
    }

    @Throws(IOException::class)
    fun writeFile(fileName: String, content: InputStream) {
        writeFile(File(fileName), content)
    }

    @Throws(IOException::class)
    fun writeFile(file: File, content: String) {
        writeFile(file, ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)))
    }

    @Throws(IOException::class)
    fun writeFile(file: File, content: InputStream) {
        requireNotNull(content) { "content must not be null" }
        if (!file.exists()) {
            file.createNewFile()
        }
        content.use {
            FileOutputStream(file).use { stream ->
                val buffer = ByteArray(1024)
                var length: Int
                while ((content.read(buffer).also { length = it }) != -1) {
                    stream.write(buffer, 0, length)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun readFile(file: File): String {
        if (!file.exists() || file.isDirectory) {
            throw FileNotFoundException()
        }
        return readFile(FileInputStream(file))
    }

    @Throws(IOException::class)
    fun readFile(filename: String): String {
        val file = File(filename)
        if (!file.exists() || file.isDirectory) {
            throw FileNotFoundException()
        }
        return readFile(FileInputStream(file))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun readFile(inputStream: InputStream): String {
        return readFile(InputStreamReader(inputStream, StandardCharsets.UTF_8))
    }

    @Throws(IOException::class)
    private fun readFile(reader: Reader): String {
        BufferedReader(reader).use { br ->
            var temp: String?
            val stringBuilder = StringBuilder()
            temp = br.readLine()
            while (temp != null) {
                if (stringBuilder.length != 0) {
                    stringBuilder.append("\n")
                }
                stringBuilder.append(temp)
                temp = br.readLine()
            }
            return stringBuilder.toString()
        }
    }

    @Throws(IOException::class)
    fun copyFile(from: File, to: File) {
        if (!from.exists()) {
            throw FileNotFoundException()
        }
        if (from.isDirectory || to.isDirectory) {
            throw FileNotFoundException()
        }
        var fi: FileInputStream? = null
        var `in`: FileChannel? = null
        var fo: FileOutputStream? = null
        var out: FileChannel? = null
        try {
            if (!to.exists()) {
                to.createNewFile()
            }
            fi = FileInputStream(from)
            `in` = fi.channel
            fo = FileOutputStream(to)
            out = fo.channel
            `in`.transferTo(0, `in`.size(), out)
        } finally {
            fi?.close()
            `in`?.close()
            fo?.close()
            out?.close()
        }
    }

    @JvmStatic
    val allThreadDumps: String
        get() {
            val threads =
                ManagementFactory.getThreadMXBean().dumpAllThreads(true, true)
            val builder = StringBuilder()
            for (info in threads) {
                builder.append('\n').append(info)
            }
            return builder.toString()
        }

    @JvmStatic
    fun getExceptionMessage(e: Throwable): String {
        val stringWriter = StringWriter()
        PrintWriter(stringWriter).use { printWriter ->
            e.printStackTrace(printWriter)
            printWriter.flush()
        }
        return stringWriter.toString()
    }

    fun dataToUUID(vararg params: String?): UUID {
        val builder = StringBuilder()
        for (param in params) {
            builder.append(param)
        }
        return UUID.nameUUIDFromBytes(builder.toString().toByteArray(StandardCharsets.UTF_8))
    }

    fun dataToUUID(vararg params: ByteArray): UUID {
        val stream = ByteArrayOutputStream()
        for (param in params) {
            try {
                stream.write(param)
            } catch (e: IOException) {
                break
            }
        }
        return UUID.nameUUIDFromBytes(stream.toByteArray())
    }

    fun rtrim(s: String, character: Char): String {
        var i = s.length - 1
        while (i >= 0 && (s[i]) == character) {
            i--
        }
        return s.substring(0, i + 1)
    }

    fun isByteArrayEmpty(array: ByteArray): Boolean {
        var i = 0
        val len = array.size
        while (i < len) {
            if (array[i].toInt() != 0) {
                return false
            }
            ++i
        }
        return true
    }

    fun toRGB(r: Byte, g: Byte, b: Byte, a: Byte): Long {
        var result = (r.toInt() and 0xff).toLong()
        result = result or ((g.toInt() and 0xff) shl 8).toLong()
        result = result or ((b.toInt() and 0xff) shl 16).toLong()
        result = result or ((a.toInt() and 0xff).toLong() shl 24)
        return result and 0xFFFFFFFFL
    }

    fun toABGR(argb: Int): Long {
        var result = argb.toLong() and 0xFF00FF00L
        result = result or ((argb shl 16).toLong() and 0x00FF0000L) // B to R
        result = result or ((argb ushr 16).toLong() and 0xFFL) // R to B
        return result and 0xFFFFFFFFL
    }

    fun splitArray(arrayToSplit: Array<Any>, chunkSize: Int): Array<Array<Any>>? {
        if (chunkSize <= 0) {
            return null
        }

        val rest = arrayToSplit.size % chunkSize
        val chunks = arrayToSplit.size / chunkSize + (if (rest > 0) 1 else 0)

        val arrays: Array<Array<Any>> = arrayOfNulls(chunks)
        for (i in 0..<(if (rest > 0) chunks - 1 else chunks)) {
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize)
        }
        if (rest > 0) {
            arrays[chunks - 1] =
                Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest)
        }
        return arrays
    }

    fun <T> reverseArray(data: Array<T>) {
        reverseArray<T>(data, false)
    }

    fun <T> concatArray(vararg arrays: Array<T>): Array<T> {
        val list = ArrayList<T>()
        for (array in arrays) list.addAll(Arrays.asList(*array))
        return java.lang.reflect.Array.newInstance(arrays[0][0].javaClass.getComponentType(), list.size) as Array<T>
    }

    fun <T> reverseArray(array: Array<T?>, copy: Boolean): Array<T?> {
        var data = array

        if (copy) {
            data = array.copyOf(array.size)
        }

        var left = 0
        var right = data.size - 1
        while (left < right) {
            // swap the values at the left and right indices
            val temp = data[left]
            data[left] = data[right]
            data[right] = temp
            left++
            right--
        }

        return data
    }

    fun <T> clone2dArray(array: Array<Array<T?>>): Array<Array<T?>> {
        val newArray: Array<Array<T?>> = array.copyOf(array.size)

        for (i in array.indices) {
            newArray[i] = array[i].copyOf(array[i].size)
        }

        return newArray
    }

    fun <T, U, V> getOrCreate(map: MutableMap<T, Map<U, V>?>, key: T): Map<U, V> {
        var existing = map[key]
        if (existing == null) {
            val toPut = ConcurrentHashMap<U, V>()
            existing = map.putIfAbsent(key, toPut)
            if (existing == null) {
                existing = toPut
            }
        }
        return existing
    }

    fun <T, U, V : U?> getOrCreate(map: MutableMap<T, U>, clazz: Class<V>, key: T): U {
        var existing = map[key]
        if (existing != null) {
            return existing
        }
        try {
            val toPut: U = clazz.newInstance()
            existing = map.putIfAbsent(key, toPut)
            if (existing == null) {
                return toPut
            }
            return existing
        } catch (e: InstantiationException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }
    }

    fun toInt(number: Any): Int {
        if (number is Int) {
            return number
        }

        return Math.round(number as Double).toInt()
    }

    fun parseHexBinary(s: String): ByteArray {
        val len = s.length

        // "111" is not a valid hex encoding.
        require(len % 2 == 0) { "hexBinary needs to be even-length: $s" }

        val out = ByteArray(len / 2)

        var i = 0
        while (i < len) {
            val h = hexToBin(s[i])
            val l = hexToBin(s[i + 1])
            require(!(h == -1 || l == -1)) { "contains illegal character for hexBinary: $s" }

            out[i / 2] = (h * 16 + l).toByte()
            i += 2
        }

        return out
    }

    private fun hexToBin(ch: Char): Int {
        if ('0' <= ch && ch <= '9') return ch.code - '0'.code
        if ('A' <= ch && ch <= 'F') return ch.code - 'A'.code + 10
        if ('a' <= ch && ch <= 'f') return ch.code - 'a'.code + 10
        return -1
    }

    /**
     * 返回介于最小值(包含)和最大值(包含)之间的伪随机数
     *
     *
     * Return a random number between the minimum (inclusive) and maximum (inclusive).
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    fun rand(min: Int, max: Int): Int {
        if (min == max) {
            return max
        }
        return random.nextInt(max + 1 - min) + min
    }

    fun rand(min: Float, max: Float): Float {
        if (min == max) {
            return max
        }
        return min + random.nextFloat() * (max - min)
    }

    fun rand(min: Double, max: Double): Double {
        if (min == max) {
            return max
        }
        return min + random.nextDouble() * (max - min)
    }

    fun rand(): Boolean {
        return random.nextBoolean()
    }

    /**
     * A way to tell the java compiler to do not replace the users of a `public static final int` constant
     * with the value defined in it, forcing the JVM to get the value directly from the class, preventing
     * binary incompatible changes.
     *
     * @param value The value to be assigned to the field.
     * @return The same value that was passed as parameter
     */
    @JvmStatic
    fun dynamic(value: Int): Int {
        return value
    }

    /**
     * A way to tell the java compiler to do not replace the users of a `public static final` constant
     * with the value defined in it, forcing the JVM to get the value directly from the class, preventing
     * binary incompatible changes.
     *
     * @param value The value to be assigned to the field.
     * @return The same value that was passed as parameter
     */
    fun <T> dynamic(value: T): T {
        return value
    }

    @Throws(IOException::class)
    fun zipFolder(sourceFolderPath: Path, zipPath: Path) {
        ZipOutputStream(FileOutputStream(zipPath.toFile())).use { zos ->
            Files.walkFileTree(sourceFolderPath, object : SimpleFileVisitor<Path>() {
                @Throws(IOException::class)
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    zos.putNextEntry(ZipEntry(sourceFolderPath.relativize(file).toString()))
                    Files.copy(file, zos)
                    zos.closeEntry()
                    return FileVisitResult.CONTINUE
                }
            })
        }
    }

    fun isInteger(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val length = str.length
        if (length == 0) {
            return false
        }
        var i = 0
        if (str[0] == '-') {
            if (length == 1) {
                return false
            }
            i = 1
        }
        while (i < length) {
            val c = str[i]
            if (c < '0' || c > '9') {
                return false
            }
            i++
        }
        return true
    }


    //used for commands /fill , /clone and so on
    //todo: using other methods instead of this one
    fun getLevelBlocks(level: Level, bb: AxisAlignedBB): Array<Block> {
        val minX = floorDouble(min(bb.minX, bb.maxX))
        val minY = floorDouble(min(bb.minY, bb.maxY))
        val minZ = floorDouble(min(bb.minZ, bb.maxZ))
        val maxX = floorDouble(max(bb.minX, bb.maxX))
        val maxY = floorDouble(max(bb.minY, bb.maxY))
        val maxZ = floorDouble(max(bb.minZ, bb.maxZ))

        val blocks: MutableList<Block?> = ArrayList()
        val vec = Vector3()

        for (z in minZ..maxZ) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    blocks.add(level.getBlock(vec.setComponents(x.toDouble(), y.toDouble(), z.toDouble())!!, false))
                }
            }
        }

        return blocks.toArray(Block.EMPTY_ARRAY)
    }

    const val ACCORDING_X_OBTAIN_Y: Int = 0


    const val ACCORDING_Y_OBTAIN_X: Int = 1


    fun calLinearFunction(pos1: Vector3, pos2: Vector3, element: Double, type: Int): Double {
        if (pos1.floorY != pos2.floorY) return Double.MAX_VALUE
        return if (pos1.getX() === pos2.getX()) {
            if (type == ACCORDING_Y_OBTAIN_X) pos1.getX()
            else Double.MAX_VALUE
        } else if (pos1.getZ() === pos2.getZ()) {
            if (type == ACCORDING_X_OBTAIN_Y) pos1.getZ()
            else Double.MAX_VALUE
        } else {
            if (type == ACCORDING_X_OBTAIN_Y) {
                (element - pos1.getX()) * (pos1.getZ() - pos2.getZ()) / (pos1.getX() - pos2.getX()) + pos1.getZ()
            } else {
                (element - pos1.getZ()) * (pos1.getX() - pos2.getX()) / (pos1.getZ() - pos2.getZ()) + pos1.getX()
            }
        }
    }

    fun hasCollisionBlocks(level: Level, bb: AxisAlignedBB): Boolean {
        val minX = floorDouble(bb.minX)
        val minY = floorDouble(bb.minY)
        val minZ = floorDouble(bb.minZ)
        val maxX = ceilDouble(bb.maxX)
        val maxY = ceilDouble(bb.maxY)
        val maxZ = ceilDouble(bb.maxZ)

        for (z in minZ..maxZ) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    val block = level.getBlock(x, y, z, false)
                    //判断是否和非空气方块有碰撞
                    if (block != null && !block.canPassThrough() && block.collidesWithBB(bb)) {
                        return true
                    }
                }
            }
        }

        return false
    }

    fun hasCollisionTickCachedBlocks(level: Level, bb: AxisAlignedBB): Boolean {
        val minX = floorDouble(bb.minX)
        val minY = floorDouble(bb.minY)
        val minZ = floorDouble(bb.minZ)
        val maxX = ceilDouble(bb.maxX)
        val maxY = ceilDouble(bb.maxY)
        val maxZ = ceilDouble(bb.maxZ)

        for (z in minZ..maxZ) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    val block = level.getTickCachedBlock(x, y, z, false)
                    //判断是否和非空气方块有碰撞
                    if (block != null && block.collidesWithBB(bb) && !block.canPassThrough()) {
                        return true
                    }
                }
            }
        }

        return false
    }

    /**
     * @return 0 if no collision, else a byte in the format of 0b 00 xx yy zz <br></br>
     * if xx is 01, then the block at the minX side of the bb has collision <br></br>
     * if xx is 11, then the block at the maxX side of the bb has collision <br></br>
     * if xx is 00, then xx is not used <br></br>
     * if yy is 01, then the block at the minY side of the bb has collision <br></br>
     * if yy is 11, then the block at the maxY side of the bb has collision <br></br>
     * if yy is 00, then yy is not used <br></br>
     * if zz is 01, then the block at the minZ side of the bb has collision <br></br>
     * if zz is 11, then the block at the maxZ side of the bb has collision <br></br>
     * if zz is 00, then zz is not used <br></br>
     */
    fun hasCollisionTickCachedBlocksWithInfo(level: Level, bb: AxisAlignedBB): Byte {
        val minX = floorDouble(bb.minX)
        val minY = floorDouble(bb.minY)
        val minZ = floorDouble(bb.minZ)
        val maxX = ceilDouble(bb.maxX)
        val maxY = ceilDouble(bb.maxY)
        val maxZ = ceilDouble(bb.maxZ)
        val centerX = (maxX + minX).toFloat() / 2
        val centerY = (maxY + minY).toFloat() / 2
        val centerZ = (maxZ + minZ).toFloat() / 2
        var returnValue: Byte = 0

        for (z in minZ..maxZ) {
            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    val block = level.getTickCachedBlock(x, y, z, false)
                    //判断是否和非空气方块有碰撞
                    if (block != null && block.collidesWithBB(bb) && !block.canPassThrough()) {
                        if (x < centerX) {
                            returnValue = (returnValue.toInt() or 16).toByte()
                        } else if (x > centerX) {
                            returnValue = (returnValue.toInt() or 48).toByte()
                        }
                        if (y < centerY) {
                            returnValue = (returnValue.toInt() or 4).toByte()
                        } else if (y > centerY) {
                            returnValue = (returnValue.toInt() or 12).toByte()
                        }
                        if (z < centerZ) {
                            returnValue = (returnValue.toInt() or 1).toByte()
                        } else if (z > centerZ) {
                            returnValue = (returnValue.toInt() or 3).toByte()
                        }
                        return returnValue
                    }
                }
            }
        }

        return 0
    }

    fun appendBytes(bytes1: ByteArray, vararg bytes2: ByteArray): ByteArray {
        var length = bytes1.size
        for (bytes in bytes2) {
            length += bytes.size
        }

        val appendedBytes = ByteArray(length)
        System.arraycopy(bytes1, 0, appendedBytes, 0, bytes1.size)
        var index = bytes1.size

        for (b in bytes2) {
            System.arraycopy(b, 0, appendedBytes, index, b.size)
            index += b.size
        }
        return appendedBytes
    }

    @JvmStatic
    fun computeRequiredBits(min: Int, max: Int): Byte {
        val value = max - min
        if (value <= 1) return 1
        var bits: Byte = 1
        while (value >= (1 shl bits.toInt())) {
            bits++
        }
        return bits
    }

    fun convertByteBuf2Array(buf: ByteBuf): ByteArray {
        val payload = ByteArray(buf.readableBytes())
        buf.readBytes(payload)
        return payload
    }
}
