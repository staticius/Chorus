package org.chorus_oss.chorus.network.chorus

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.algorithms.SHA256
import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.Source
import org.chorus_oss.protocol.core.ProtoCodec

data class EncryptionWrapper(
    val raw: Buffer,
    val cipher: AES.CTR.Key
) {
    companion object : ProtoCodec<EncryptionWrapper> {
        val digest = CryptographyProvider.Default.get(SHA256)
        val aes = CryptographyProvider.Default.get(AES.CTR)

        override fun serialize(value: EncryptionWrapper, stream: Sink) {
            TODO("Not yet implemented")
        }

        override fun deserialize(stream: Source): EncryptionWrapper {
            TODO("Not yet implemented")
        }
    }
}
