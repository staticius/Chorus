package org.chorus_oss.chorus.network.chorus

import dev.whyoleg.cryptography.operations.Cipher
import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.Source
import org.chorus_oss.protocol.core.ProtoCodec

data class EncryptionWrapper(
    val raw: Buffer,
    val cipher: Cipher
) {
    companion object : ProtoCodec<EncryptionWrapper> {
        override fun serialize(value: EncryptionWrapper, stream: Sink) {
            TODO("Not yet implemented")
        }

        override fun deserialize(stream: Source): EncryptionWrapper {
            TODO("Not yet implemented")
        }
    }
}
