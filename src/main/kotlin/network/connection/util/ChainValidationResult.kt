package org.chorus_oss.chorus.network.connection.util

import com.google.gson.reflect.TypeToken
import org.chorus_oss.chorus.utils.JSONUtils
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.util.*

class ChainValidationResult(private val signed: Boolean, parsedPayload: Map<String, Any?>?) {
    private val parsedPayload: Map<String, Any?> = parsedPayload!!
    private var identityClaims: IdentityClaims? = null


    constructor(signed: Boolean, rawPayload: String?) : this(
        signed,
        JSONUtils.from<Map<String, Any?>>(rawPayload, object : TypeToken<Map<String, Any?>>() {})
    )

    fun signed(): Boolean {
        return signed
    }

    fun rawIdentityClaims(): Map<String?, Any?> {
        return HashMap(parsedPayload)
    }

    @Throws(IllegalStateException::class)
    fun identityClaims(): IdentityClaims {
        if (identityClaims == null) {
            val identityPublicKey = JSONUtils.childAsType(
                parsedPayload, "identityPublicKey",
                String::class.java
            )
            val extraData = JSONUtils.childAsType(
                parsedPayload, "extraData",
                Map::class.java
            )

            val displayName = JSONUtils.childAsType(extraData, "displayName", String::class.java)
            val identityString = JSONUtils.childAsType(extraData, "identity", String::class.java)
            val xuid = JSONUtils.childAsType(extraData, "XUID", String::class.java)
            val titleId = extraData["titleId"]

            val identity: UUID
            try {
                identity = UUID.fromString(identityString)
            } catch (exception: Exception) {
                throw IllegalStateException("identity node is an invalid UUID")
            }

            identityClaims = IdentityClaims(
                IdentityData(displayName, identity, xuid, titleId as String?),
                identityPublicKey
            )
        }
        return identityClaims!!
    }

    class IdentityClaims(val extraData: IdentityData, val identityPublicKey: String) {
        private var parsedIdentityPublicKey: PublicKey? = null

        @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
        fun parsedIdentityPublicKey(): PublicKey? {
            if (parsedIdentityPublicKey == null) {
                parsedIdentityPublicKey = EncryptionUtils.parseKey(identityPublicKey)
            }
            return parsedIdentityPublicKey
        }
    }

    class IdentityData(val displayName: String, val identity: UUID, val xuid: String, val titleId: String?)
}
