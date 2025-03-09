/**
 * 与计分板相关的一些类.
 *
 *
 * Classes relevant to score board.
 */
package org.chorus.scoreboard

import cn.nukkit.entity.EntityHuman.getUniqueId
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.entity.Entity.getId
import cn.nukkit.plugin.PluginManager.callEvent
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.command.data.CommandEnum.updateSoftEnum
import cn.nukkit.scoreboard.data.ScorerType
import cn.nukkit.scoreboard.IScoreboard
import cn.nukkit.scoreboard.IScoreboardLine
import cn.nukkit.network.protocol.SetScorePacket.ScoreInfo
import cn.nukkit.scoreboard.scorer.IScorer
import cn.nukkit.scoreboard.scorer.FakeScorer
import cn.nukkit.scoreboard.scorer.EntityScorer
import cn.nukkit.scoreboard.scorer.PlayerScorer
import cn.nukkit.scoreboard.storage.IScoreboardStorage
import cn.nukkit.scoreboard.manager.IScoreboardManager
import java.util.HashMap
import cn.nukkit.scoreboard.data.DisplaySlot
import cn.nukkit.scoreboard.displayer.IScoreboardViewer
import java.util.HashSet
import cn.nukkit.event.scoreboard.ScoreboardObjectiveChangeEvent
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.network.protocol.UpdateSoftEnumPacket
import cn.nukkit.entity.EntityLiving
import java.io.IOException
import cn.nukkit.scoreboard.Scoreboard
import cn.nukkit.scoreboard.ScoreboardLine
import cn.nukkit.event.scoreboard.ScoreboardLineChangeEvent
import java.util.concurrent.atomic.AtomicInteger

