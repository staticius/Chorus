/**
 * 与计分板相关的一些类.
 *
 *
 * Classes relevant to score board.
 */
package org.chorus.scoreboard

import org.chorus.entity.EntityHuman.getUniqueId
import org.chorus.entity.EntityHuman.getName
import org.chorus.entity.Entity.getId
import org.chorus.plugin.PluginManager.callEvent
import org.chorus.event.Event.isCancelled
import org.chorus.command.data.CommandEnum.updateSoftEnum
import org.chorus.scoreboard.data.ScorerType
import org.chorus.scoreboard.IScoreboard
import org.chorus.scoreboard.IScoreboardLine
import org.chorus.network.protocol.SetScorePacket.ScoreInfo
import org.chorus.scoreboard.scorer.IScorer
import org.chorus.scoreboard.scorer.FakeScorer
import org.chorus.scoreboard.scorer.EntityScorer
import org.chorus.scoreboard.scorer.PlayerScorer
import org.chorus.scoreboard.storage.IScoreboardStorage
import org.chorus.scoreboard.manager.IScoreboardManager
import java.util.HashMap
import org.chorus.scoreboard.data.DisplaySlot
import org.chorus.scoreboard.displayer.IScoreboardViewer
import java.util.HashSet
import org.chorus.event.scoreboard.ScoreboardObjectiveChangeEvent
import org.chorus.command.data.CommandEnum
import org.chorus.network.protocol.UpdateSoftEnumPacket
import org.chorus.entity.EntityLiving
import java.io.IOException
import org.chorus.scoreboard.Scoreboard
import org.chorus.scoreboard.ScoreboardLine
import org.chorus.event.scoreboard.ScoreboardLineChangeEvent
import java.util.concurrent.atomic.AtomicInteger

