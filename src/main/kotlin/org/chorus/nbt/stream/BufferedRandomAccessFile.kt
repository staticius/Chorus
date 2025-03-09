/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chorus.nbt.stream

import java.io.*
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * A `BufferedRandomAccessFile` is like a
 * `RandomAccessFile`, but it uses a private buffer so that most
 * operations do not require a disk access.
 * <P>
 *
 * Note: The operations on this class are unmonitored. Also, the correct
 * functioning of the `RandomAccessFile` methods that are not
 * overridden here relies on the implementation of those methods in the
 * superclass.
 * @author Avinash Lakshman ( alakshman@facebook.com) &amp; Prashant Malik ( pmalik@facebook.com )
</P> */
class BufferedRandomAccessFile : RandomAccessFile {
    /*
     * This implementation is based on the buffer implementation in Modula-3's
     * "Rd", "Wr", "RdClass", and "WrClass" interfaces.
     */
    private var dirty_ = false // true iff unflushed bytes exist
    private var closed_ = false // true iff the file is closed
    private var curr_: Long = 0 // current position in file
    private var lo_: Long = 0
    private var hi_: Long = 0 // bounds on characters in "buff"
    private var buff_: ByteArray // local buffer
    private var maxHi_: Long = 0 // this.lo + this.buff.length
    private var hitEOF_ = false // buffer contains last file block?
    private var diskPos_: Long = 0 // disk position

    /*
     * To describe the above fields, we introduce the following abstractions for
     * the file "f":
     *
     * len(f) the length of the file curr(f) the current position in the file
     * c(f) the abstract contents of the file disk(f) the contents of f's
     * backing disk file closed(f) true iff the file is closed
     *
     * "curr(f)" is an index in the closed interval [0, len(f)]. "c(f)" is a
     * character sequence of length "len(f)". "c(f)" and "disk(f)" may differ if
     * "c(f)" contains unflushed writes not reflected in "disk(f)". The flush
     * operation has the effect of making "disk(f)" identical to "c(f)".
     *
     * A file is said to be *valid* if the following conditions hold:
     *
     * V1. The "closed" and "curr" fields are correct:
     *
     * f.closed == closed(f) f.curr == curr(f)
     *
     * V2. The current position is either contained in the buffer, or just past
     * the buffer:
     *
     * f.lo <= f.curr <= f.hi
     *
     * V3. Any (possibly) unflushed characters are stored in "f.buff":
     *
     * (forall i in [f.lo, f.curr): c(f)[i] == f.buff[i - f.lo])
     *
     * V4. For all characters not covered by V3, c(f) and disk(f) agree:
     *
     * (forall i in [f.lo, len(f)): i not in [f.lo, f.curr) => c(f)[i] ==
     * disk(f)[i])
     *
     * V5. "f.dirty" is true iff the buffer contains bytes that should be
     * flushed to the file; by V3 and V4, only part of the buffer can be dirty.
     *
     * f.dirty == (exists i in [f.lo, f.curr): c(f)[i] != f.buff[i - f.lo])
     *
     * V6. this.maxHi == this.lo + this.buff.length
     *
     * Note that "f.buff" can be "null" in a valid file, since the range of
     * characters in V3 is empty when "f.lo == f.curr".
     *
     * A file is said to be *ready* if the buffer contains the current position,
     * i.e., when:
     *
     * R1. !f.closed && f.buff != null && f.lo <= f.curr && f.curr < f.hi
     *
     * When a file is ready, reading or writing a single byte can be performed
     * by reading or writing the in-memory buffer without performing a disk
     * operation.
     */
    /**
     * Open a new `BufferedRandomAccessFile` on `file`
     * in mode `mode`, which should be "r" for reading only, or
     * "rw" for reading and writing.
     */
    constructor(file: File, mode: String) : super(file, mode) {
        this.init(0)
    }

    constructor(file: File, mode: String, size: Int) : super(file, mode) {
        this.init(size)
    }

    /**
     * Open a new `BufferedRandomAccessFile` on the file named
     * `name` in mode `mode`, which should be "r" for
     * reading only, or "rw" for reading and writing.
     */
    constructor(name: String?, mode: String) : super(name, mode) {
        this.init(0)
    }

    constructor(name: String?, mode: String, size: Int) : super(name, mode) {
        this.init(size)
    }

    private fun init(size: Int) {
        this.closed_ = false
        this.dirty_ = this.closed_
        this.hi_ = 0
        this.curr_ = this.hi_
        this.lo_ = this.curr_
        this.buff_ = if (size > BuffSz_) ByteArray(size) else ByteArray(BuffSz_)
        this.maxHi_ = BuffSz_.toLong()
        this.hitEOF_ = false
        this.diskPos_ = 0L
    }

    @Throws(IOException::class)
    override fun close() {
        this.flush()
        this.closed_ = true
        super.close()
    }

    /**
     * Flush any bytes in the file's buffer that have not yet been written to
     * disk. If the file was created read-only, this method is a no-op.
     */
    @Throws(IOException::class)
    fun flush() {
        this.flushBuffer()
    }

    /* Flush any dirty bytes in the buffer to disk. */
    @Throws(IOException::class)
    private fun flushBuffer() {
        if (this.dirty_) {
            if (this.diskPos_ != this.lo_) super.seek(this.lo_)
            val len = (this.curr_ - this.lo_).toInt()
            super.write(this.buff_, 0, len)
            this.diskPos_ = this.curr_
            this.dirty_ = false
        }
    }

    /*
     * Read at most "this.buff.length" bytes into "this.buff", returning the
     * number of bytes read. If the return result is less than
     * "this.buff.length", then EOF was read.
     */
    @Throws(IOException::class)
    private fun fillBuffer(): Int {
        var cnt = 0
        var rem = buff_.size
        while (rem > 0) {
            val n = super.read(this.buff_, cnt, rem)
            if (n < 0) break
            cnt += n
            rem -= n
        }
        if ((cnt < 0) && ((cnt < buff_.size).also { this.hitEOF_ = it })) {
            // make sure buffer that wasn't read is initialized with -1
            Arrays.fill(this.buff_, cnt, buff_.size, 0xff.toByte())
        }
        this.diskPos_ += cnt.toLong()
        return cnt
    }

    /*
     * This method positions <code>this.curr</code> at position <code>pos</code>.
     * If <code>pos</code> does not fall in the current buffer, it flushes the
     * current buffer and loads the correct one.<p>
     *
     * On exit from this routine <code>this.curr == this.hi</code> iff <code>pos</code>
     * is at or past the end-of-file, which can only happen if the file was
     * opened in read-only mode.
     */
    @Throws(IOException::class)
    override fun seek(pos: Long) {
        if (pos >= this.hi_ || pos < this.lo_) {
            // seeking outside of current buffer -- flush and read
            this.flushBuffer()
            this.lo_ = pos and BuffMask_ // start at BuffSz boundary
            this.maxHi_ = this.lo_ + buff_.size.toLong()
            if (this.diskPos_ != this.lo_) {
                super.seek(this.lo_)
                this.diskPos_ = this.lo_
            }
            val n = this.fillBuffer()
            this.hi_ = this.lo_ + n.toLong()
        } else {
            // seeking inside current buffer -- no read required
            if (pos < this.curr_) {
                // if seeking backwards, we must flush to maintain V4
                this.flushBuffer()
            }
        }
        this.curr_ = pos
    }

    /*
     * Does not maintain V4 (i.e. buffer differs from disk contents if previously written to)
     *  - Assumes no writes were made
     * @param pos
     * @throws IOException
     */
    @Throws(IOException::class)
    fun seekUnsafe(pos: Long) {
        if (pos >= this.hi_ || pos < this.lo_) {
            // seeking outside of current buffer -- flush and read
            this.flushBuffer()
            this.lo_ = pos and BuffMask_ // start at BuffSz boundary
            this.maxHi_ = this.lo_ + buff_.size.toLong()
            if (this.diskPos_ != this.lo_) {
                super.seek(this.lo_)
                this.diskPos_ = this.lo_
            }
            val n = this.fillBuffer()
            this.hi_ = this.lo_ + n.toLong()
        }
        this.curr_ = pos
    }

    override fun getFilePointer(): Long {
        return this.curr_
    }

    @Throws(IOException::class)
    override fun length(): Long {
        return max(curr_.toDouble(), super.length().toDouble()).toLong()
    }

    @Throws(IOException::class)
    override fun read(): Int {
        if (this.curr_ >= this.hi_) {
            // test for EOF
            // if (this.hi < this.maxHi) return -1;
            if (this.hitEOF_) return -1

            // slow path -- read another buffer
            this.seek(this.curr_)
            if (this.curr_ == this.hi_) return -1
        }
        val res = buff_[(this.curr_ - this.lo_).toInt()]
        curr_++
        return (res.toInt()) and 0xFF // convert byte -> int
    }

    @Throws(IOException::class)
    fun read1(): Byte {
        if (this.curr_ >= this.hi_) {
            // test for EOF
            // if (this.hi < this.maxHi) return -1;
            if (this.hitEOF_) return -1

            // slow path -- read another buffer
            this.seek(this.curr_)
            if (this.curr_ == this.hi_) return -1
        }
        val res = buff_[(curr_++ - this.lo_).toInt()]
        return res
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray): Int {
        return this.read(b, 0, b.size)
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int {
        var len = len
        if (this.curr_ >= this.hi_) {
            // test for EOF
            // if (this.hi < this.maxHi) return -1;
            if (this.hitEOF_) return -1

            // slow path -- read another buffer
            this.seek(this.curr_)
            if (this.curr_ == this.hi_) return -1
        }
        len = min(len.toDouble(), (this.hi_ - this.curr_).toInt().toDouble()).toInt()
        val buffOff = (this.curr_ - this.lo_).toInt()
        System.arraycopy(this.buff_, buffOff, b, off, len)
        this.curr_ += len.toLong()
        return len
    }

    @Throws(IOException::class)
    fun readCurrent(): Byte {
        if (this.curr_ >= this.hi_) {
            // test for EOF
            // if (this.hi < this.maxHi) return -1;
            if (this.hitEOF_) return -1

            // slow path -- read another buffer
            this.seek(this.curr_)
            if (this.curr_ == this.hi_) return -1
        }
        val res = buff_[(this.curr_ - this.lo_).toInt()]
        return res
    }

    @Throws(IOException::class)
    fun writeCurrent(b: Byte) {
        if (this.curr_ >= this.hi_) {
            if (this.hitEOF_ && this.hi_ < this.maxHi_) {
                // at EOF -- bump "hi"
                hi_++
            } else {
                // slow path -- write current buffer; read next one
                this.seek(this.curr_)
                if (this.curr_ == this.hi_) {
                    // appending to EOF -- bump "hi"
                    hi_++
                }
            }
        }
        buff_[(this.curr_ - this.lo_).toInt()] = b
        this.dirty_ = true
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        if (this.curr_ >= this.hi_) {
            if (this.hitEOF_ && this.hi_ < this.maxHi_) {
                // at EOF -- bump "hi"
                hi_++
            } else {
                // slow path -- write current buffer; read next one
                this.seek(this.curr_)
                if (this.curr_ == this.hi_) {
                    // appending to EOF -- bump "hi"
                    hi_++
                }
            }
        }
        buff_[(this.curr_ - this.lo_).toInt()] = b.toByte()
        curr_++
        this.dirty_ = true
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray) {
        this.write(b, 0, b.size)
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        var off = off
        var len = len
        while (len > 0) {
            val n = this.writeAtMost(b, off, len)
            off += n
            len -= n
            this.dirty_ = true
        }
    }

    /*
     * Write at most "len" bytes to "b" starting at position "off", and return
     * the number of bytes written.
     */
    @Throws(IOException::class)
    private fun writeAtMost(b: ByteArray, off: Int, len: Int): Int {
        var len = len
        if (this.curr_ >= this.hi_) {
            if (this.hitEOF_ && this.hi_ < this.maxHi_) {
                // at EOF -- bump "hi"
                this.hi_ = this.maxHi_
            } else {
                // slow path -- write current buffer; read next one
                this.seek(this.curr_)
                if (this.curr_ == this.hi_) {
                    // appending to EOF -- bump "hi"
                    this.hi_ = this.maxHi_
                }
            }
        }
        len = min(len.toDouble(), (this.hi_ - this.curr_).toInt().toDouble()).toInt()
        val buffOff = (this.curr_ - this.lo_).toInt()
        System.arraycopy(b, off, this.buff_, buffOff, len)
        this.curr_ += len.toLong()
        return len
    }

    companion object {
        const val LogBuffSz_: Int = 16 // 64K buffer
        const val BuffSz_: Int = (1 shl LogBuffSz_)
        const val BuffMask_: Long = -(BuffSz_.toLong())
    }
}
