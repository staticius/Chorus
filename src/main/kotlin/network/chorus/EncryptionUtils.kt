package org.chorus_oss.chorus.network.chorus

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.EC
import dev.whyoleg.cryptography.algorithms.ECDSA
import dev.whyoleg.cryptography.random.CryptographyRandom
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.encodeToByteString

object EncryptionUtils {
    @Suppress("SpellCheckingInspection")
    val mojangPublicKey: ECDSA.PublicKey = parseKey("MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAECRXueJeTDqNRRgJi/vlRufByu/2G0i2Ebt6YMar5QX/R0DIIyrJMcUpruK4QveTfJSTp3Shlq4Gk34cD/4GUWwkv0DVuzeuB+tXija7HBxii03NHDbPAD0AKnLr2wdAp".encodeToByteString())

    val ecdsa = CryptographyProvider.Default.get(ECDSA)
    val curve = EC.Curve.P384
    val format = EC.PublicKey.Format.DER

    val keyGenerator = ecdsa.keyPairGenerator(curve)
    val publicKeyDecoder = ecdsa.publicKeyDecoder(curve)

    fun parseKey(b64: ByteString): ECDSA.PublicKey {
        return publicKeyDecoder.decodeFromByteStringBlocking(format, b64)
    }

    fun generateKeyPair(): ECDSA.KeyPair {
        return keyGenerator.generateKeyBlocking()
    }

    fun generateRandomToken(): ByteArray {
        return CryptographyRandom.nextBytes(16)
    }
}