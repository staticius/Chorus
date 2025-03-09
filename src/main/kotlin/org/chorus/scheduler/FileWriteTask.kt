package org.chorus.scheduler

import org.chorus.utils.Utils
import lombok.extern.slf4j.Slf4j
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

/**
 * @author MagicDroidX (Nukkit Project)
 */

class FileWriteTask : AsyncTask {
    private val file: File
    private val contents: InputStream

    constructor(path: String, contents: String) : this(File(path), contents)

    constructor(path: String, contents: ByteArray) : this(File(path), contents)

    constructor(path: String, contents: InputStream) {
        this.file = File(path)
        this.contents = contents
    }

    constructor(file: File, contents: String) {
        this.file = file
        this.contents = ByteArrayInputStream(contents.toByteArray(StandardCharsets.UTF_8))
    }

    constructor(file: File, contents: ByteArray) {
        this.file = file
        this.contents = ByteArrayInputStream(contents)
    }

    constructor(file: File, contents: InputStream) {
        this.file = file
        this.contents = contents
    }

    override fun onRun() {
        try {
            Utils.writeFile(file, contents)
        } catch (e: IOException) {
            FileWriteTask.log.error("An error occurred while writing the file {}", file, e)
        }
    }
}
