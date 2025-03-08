package cn.nukkit.recipe.descriptor


enum class ItemDescriptorType {
    INVALID,
    DEFAULT,
    MOLANG,
    ITEM_TAG,
    DEFERRED,

    /**
     * @since v575
     */
    COMPLEX_ALIAS
}
