package dev.shog.osm.quest

import dev.shog.osm.quest.handle.DiscordWebhook
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
    companion object {
        const val VERSION_STR = "1.0.0-B"
    }

    lateinit var quests: LinkedList<Quest>
    lateinit var webhook: DiscordWebhook

    var lastQuestSave = System.currentTimeMillis()
    private val timer = Timer()

    override fun onEnable() {
        if (arrayOf("username", "password", "url", "webhook").any { !configuration.all.containsKey(it) }) {
            println("You are missing configuraiton options for OSM quests!")
            server.pluginManager.disablePlugin(this)
            return
        }

        webhook = DiscordWebhook(configuration.getString("webhook"))

        getCommand("xp").executor = VIEW_XP
        getCommand("quests").executor = VIEW_QUESTS.invoke(this)
        getCommand("rankup").executor = RANK_UP
        getCommand("rank").executor = RANK
        getCommand("osmq_debug").executor = DEBUG_COMMAND.invoke(this)

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

    /**
     * Save quests to file.
     */
    fun saveQuests() {
        println("OSMQuests: Saving quests...")

        quests
            .forEach { quest -> QuestParser.saveQuest(quest) }

        lastQuestSave = System.currentTimeMillis()
        webhook.sendMessage("Quests have been saved.")
        println("OSMQuests: Completed quest saving!")
    }
}