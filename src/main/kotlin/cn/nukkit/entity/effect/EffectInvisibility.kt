package cn.nukkit.entity.effect

import cn.nukkit.entity.*
import cn.nukkit.entity.data.EntityFlag
import java.awt.Color

class EffectInvisibility : Effect(EffectType.Companion.INVISIBILITY, "%potion.invisibility", Color(246, 246, 246)) {
    override fun add(entity: Entity) {
        entity.setDataFlag(EntityFlag.INVISIBLE, true)
        entity.setNameTagVisible(false)
    }

    override fun remove(entity: Entity) {
        entity.setDataFlag(EntityFlag.INVISIBLE, false)
        entity.setNameTagVisible(true)
    }
}
