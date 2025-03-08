package cn.nukkit.level.generator.`object`.legacytree

/**
 * @author MagicDroidX (Nukkit Project)
 */
class LegacyJungleTree : LegacyTreeGenerator() {
    override val type: WoodType
        get() = WoodType.JUNGLE

    override fun placeObject(level: BlockManager, x: Int, y: Int, z: Int, random: RandomSourceProvider) {
        this.treeHeight = random.nextInt(6) + 4
        super.placeObject(level, x, y, z, random)
    }
}
