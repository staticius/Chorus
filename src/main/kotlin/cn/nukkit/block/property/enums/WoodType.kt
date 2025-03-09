package cn.nukkit.block.property.enums

import lombok.Getter

enum class WoodType(@field:Getter override val name: String) {
    OAK("Oak"),

    SPRUCE("Spruce"),

    BIRCH("Birch"),

    JUNGLE("Jungle"),

    ACACIA("Acacia"),

    DARK_OAK("Dark Oak"),

    CHERRY("Cherry"),

    PALE_OAK("Pale Oak")
}
