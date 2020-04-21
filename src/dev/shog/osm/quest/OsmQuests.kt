package dev.shog.osm.quest

import dev.shog.osm.quest.handle.SqlHandler
import dev.shog.osm.quest.handle.commands.*
import dev.shog.osm.quest.handle.parser.QuestParser
import dev.shog.osm.quest.handle.quests.BalanceRewardingQuest
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.XpRewardingQuest
import dev.shog.osm.quest.handle.quests.task.type.WolfTameTask
import dev.shog.osm.quest.handle.quests.task.type.block.BlockBreakTask
import dev.shog.osm.quest.handle.quests.task.type.entity.EntityKillTask
import dev.shog.osm.quest.handle.quests.task.type.entity.EntityType
import dev.shog.osm.quest.handle.quests.task.type.move.BoatMoveTask
import dev.shog.osm.quest.handle.quests.task.type.move.MoveTask
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

class OsmQuests : JavaPlugin() {
    lateinit var quests: LinkedList<Quest>
    val timer = Timer()

    override fun onEnable() {
        getCommand("xp").executor = VIEW_XP
        getCommand("quests").executor = VIEW_QUESTS.invoke(this)
        getCommand("rankup").executor = RANK_UP
        getCommand("rank").executor = RANK
        getCommand("osmq_debug").executor = DEBUG_COMMAND

        quests = QuestParser.getAllQuests(this)
        saveQuests()

        SqlHandler.username = configuration.getString("username")
        SqlHandler.password = configuration.getString("password")
        SqlHandler.url = configuration.getString("url")

        // autosave
        timer.schedule(timerTask {
          saveQuests()
        }, 0, TimeUnit.HOURS.toMillis(1))

        println("OSMQuests: Loaded ${quests.size} quests.")
    }

    override fun onDisable() {
        quests
            .forEach { quest -> QuestParser.saveQuest(quest) }

        println("OSMQuests: Exiting...")
    }

    fun saveQuests() {
        println("OSMQuests: Saving quests...")

        quests
            .forEach { quest -> QuestParser.saveQuest(quest) }

        println("OSMQuests: Completed quest saving!")
    }
}