package org.chorus.config

import org.chorus.Server
import eu.okaeri.configs.postprocessor.ConfigContextManipulator
import eu.okaeri.configs.postprocessor.ConfigLineFilter
import eu.okaeri.configs.postprocessor.ConfigLineInfo
import eu.okaeri.configs.postprocessor.ConfigSectionWalker



import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import kotlin.math.max


class TrConfigPostprocessor {
    private var context: String? = null

    fun write(outputStream: OutputStream): TrConfigPostprocessor {
        writeOutput(outputStream, this.context)
        return this
    }

    fun removeLines(filter: ConfigLineFilter): TrConfigPostprocessor {
        val lines =
            context!!.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val buf = StringBuilder()

        for (line in lines) {
            if (filter.remove(line)) {
                continue
            }
            buf.append(line).append("\n")
        }

        this.context = buf.toString()
        return this
    }

    fun removeLinesUntil(shouldStop: Predicate<String?>): TrConfigPostprocessor {
        val lines =
            context!!.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (i in lines.indices) {
            val line = lines[i]
            if (!shouldStop.test(line)) {
                continue
            }
            val remaining = Arrays.copyOfRange(lines, i, lines.size)
            this.context = java.lang.String.join("\n", *remaining)
            break
        }

        return this
    }

    fun updateLines(manipulator: ConfigContextManipulator): TrConfigPostprocessor {
        val lines =
            context!!.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val buf = StringBuilder()

        for (line in lines) {
            buf.append(manipulator.convert(line)).append("\n")
        }

        this.context = buf.toString()
        return this
    }

    fun updateLinesKeys(walker: ConfigSectionWalker): TrConfigPostprocessor {
        try {
            return this.updateLinesKeys0(walker)
        } catch (exception: Exception) {
            throw RuntimeException(
                """
                    failed to #updateLinesKeys for context:
                    ${context}
                    """.trimIndent(), exception
            )
        }
    }

    private fun updateLinesKeys0(walker: ConfigSectionWalker): TrConfigPostprocessor {
        val lines =
            context!!.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var currentPath: MutableList<ConfigLineInfo?> = ArrayList()
        var lastIndent = 0
        var level = 0
        val newContext = StringBuilder()
        var multilineSkip = false

        for (line in lines) {
            val indent = countIndent(line)
            val change = indent - lastIndent
            val key = walker.readName(line)

            // skip non-keys
            if (!walker.isKey(line)) {
                newContext.append(line).append("\n")
                multilineSkip = false
                continue
            }

            if (currentPath.isEmpty()) {
                currentPath.add(ConfigLineInfo.of(indent, change, key))
            }

            if (change > 0) {
                if (!multilineSkip) {
                    level++
                    currentPath.add(ConfigLineInfo.of(indent, change, key))
                }
            } else {
                if (change != 0) {
                    val lastLineInfo: ConfigLineInfo = currentPath.getLast()
                    val step = lastLineInfo.indent / level
                    level -= ((change * -1) / step)
                    currentPath = currentPath.subList(0, level + 1)
                    multilineSkip = false
                }
                if (!multilineSkip) {
                    currentPath[currentPath.size - 1] = ConfigLineInfo.of(indent, change, key)
                }
            }

            if (multilineSkip) {
                newContext.append(line).append("\n")
                continue
            } else if (walker.isKeyMultilineStart(line)) {
                multilineSkip = true
            }

            lastIndent = indent
            val updatedLine = walker.update(line, currentPath.getLast(), currentPath)
            newContext.append(updatedLine).append("\n")
        }

        this.context = newContext.toString()
        return this
    }

    fun updateContext(manipulator: ConfigContextManipulator): TrConfigPostprocessor {
        this.context = manipulator.convert(this.context)
        return this
    }

    fun prependContextComment(prefix: String?, strings: Array<String?>?): TrConfigPostprocessor {
        if (strings != null) this.context = createComment(prefix, strings) + this.context
        return this
    }

    fun appendContextComment(prefix: String?, strings: Array<String?>?): TrConfigPostprocessor {
        return this.appendContextComment(prefix, "", strings)
    }

    fun appendContextComment(prefix: String?, separator: String, strings: Array<String?>?): TrConfigPostprocessor {
        if (strings != null) this.context += separator + createComment(prefix, strings)
        return this
    }

    companion object {
        fun of(inputStream: InputStream): TrConfigPostprocessor {
            val postprocessor = TrConfigPostprocessor()
            postprocessor.setContext(readInput(inputStream))
            return postprocessor
        }

        fun of(context: String): TrConfigPostprocessor {
            val postprocessor = TrConfigPostprocessor()
            postprocessor.setContext(context)
            return postprocessor
        }

        fun countIndent(line: String): Int {
            var whitespaces = 0
            for (c in line.toCharArray()) {
                if (!Character.isWhitespace(c)) {
                    return whitespaces
                }
                whitespaces++
            }
            return whitespaces
        }

        fun addIndent(line: String, size: Int): String {
            val indent = " ".repeat(max(0.0, size.toDouble()).toInt())

            return (Arrays.stream(line.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .map { part: String -> indent + part }
                .collect(Collectors.joining("\n"))
                    + "\n")
        }

        fun createCommentOrEmpty(commentPrefix: String?, strings: Array<String?>?): String {
            return if (strings == null) "" else createComment(commentPrefix, strings)!!
        }

        fun createComment(commentPrefix: String?, strings: Array<String?>?): String? {
            var commentPrefix = commentPrefix
            if (strings == null) return null
            if (commentPrefix == null) commentPrefix = ""

            val newLines: MutableList<String> = ArrayList()
            for (line in strings) {
                val trLine = Server.instance.baseLang.tr(line)
                var prefix = if (trLine.startsWith(commentPrefix.trim { it <= ' ' })) "" else commentPrefix
                val result = (if (trLine.isEmpty()) "" else prefix) + trLine

                val parts = result.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (parts.size != 0) {
                    for (p in parts) {
                        prefix = if (p.startsWith(commentPrefix.trim { it <= ' ' })) "" else commentPrefix
                        newLines.add((if (p.isEmpty()) "" else prefix) + p)
                    }
                } else {
                    newLines.add(result)
                }
            }

            return java.lang.String.join("\n", newLines) + "\n"
        }

        private fun readInput(inputStream: InputStream): String {
            return BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"))
        }

        @SneakyThrows
        private fun writeOutput(outputStream: OutputStream, text: String?) {
            @Cleanup val out = PrintStream(outputStream, true, StandardCharsets.UTF_8.name())
            out.print(text)
        }
    }
}
