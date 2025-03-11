package org.chorus.utils


import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SHAUtil {
    fun SHA256(strText: String?): String? {
        return SHA(strText, "SHA-256")
    }

    fun SHA512(strText: String?): String? {
        return SHA(strText, "SHA-512")
    }

    fun MD5(strText: String?): String? {
        return SHA(strText, "MD5")
    }

    private fun SHA(strText: String?, strType: String): String? {
        // 返回值
        var strResult: String? = null

        // 是否是有效字符串
        if (!strText.isNullOrEmpty()) {
            try {
                val messageDigest = MessageDigest.getInstance(strType)
                messageDigest.update(strText.toByteArray())
                val byteBuffer = messageDigest.digest()

                val strHexString = StringBuilder()
                for (b in byteBuffer) {
                    val hex = Integer.toHexString(0xff and b.toInt())
                    if (hex.length == 1) {
                        strHexString.append('0')
                    }
                    strHexString.append(hex)
                }
                // 得到返回結果
                strResult = strHexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return strResult
    }
}
