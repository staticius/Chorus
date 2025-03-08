package cn.nukkit.scoreboard.data

import lombok.Getter

/**
 * 计分板显示槽位枚举
 */
enum class DisplaySlot(@field:Getter private val slotName: String) {
    //玩家屏幕右侧
    SIDEBAR("sidebar"),

    //玩家列表
    LIST("list"),

    //玩家名称标签下方
    BELOW_NAME("belowname")
}
