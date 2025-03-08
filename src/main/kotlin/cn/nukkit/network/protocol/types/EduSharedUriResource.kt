package cn.nukkit.network.protocol.types


@JvmRecord
data class EduSharedUriResource(buttonName: String, linkUri: String) {
    val buttonName: String = buttonName
    val linkUri: String = linkUri

    companion object {
        val EMPTY: EduSharedUriResource = EduSharedUriResource("", "")
    }
}
