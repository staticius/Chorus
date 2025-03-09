package org.chorus.resourcepacks

import java.util.*

/**
 * 描述一个资源包的接口
 */
interface ResourcePack {
    /**
     * @return 此资源包的名称
     */
    val packName: String?

    /**
     * @return 此资源包的UUID
     */
    val packId: UUID

    /**
     * @return 此资源包的版本号
     */
    val packVersion: String

    /**
     * @return 此资源包的文件大小
     */
    val packSize: Int

    /**
     * @return 资源包文件的SHA-256值
     */
    val sha256: ByteArray

    /**
     * @param off 偏移值
     * @param len 长度
     * @return 资源包文件的指定分块
     */
    fun getPackChunk(off: Int, len: Int): ByteArray

    val isAddonPack: Boolean

    fun cdnUrl(): String

    val encryptionKey: String
        /**
         * @return 资源包密钥（若加密）
         */
        get() = ""

    companion object {
        val EMPTY_ARRAY: Array<ResourcePack?> = arrayOfNulls(0)
    }
}
