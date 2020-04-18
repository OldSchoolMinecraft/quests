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
import dev.shog.osm.quest.handle.quests.task.type.move.MoveTask
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.json.JSONObject
import java.util.*

class OsmQuests : JavaPlugin() {
    lateinit var quests: LinkedList<Quest>

    override fun onEnable() {
        getCommand("xp").executor = VIEW_XP
        getCommand("quests").executor = VIEW_QUESTS.invoke(this)
        getCommand("rankup").executor = RANK_UP
        getCommand("rank").executor = RANK
        getCommand("osmq_debug").executor = DEBUG_COMMAND

        quests = QuestParser.getAllQuests(this)
        quests.add(XpRewardingQuest(
            "Commit Mass Genocide",
            listOf(
                EntityKillTask(EntityType.CREEPER, 15, this, "Creepers", false, JSONObject()),
                EntityKillTask(EntityType.ZOMBIE, 23, this, "Zombies", false, JSONObject()),
                EntityKillTask(EntityType.SPIDER, 14, this, "Spiders", false, JSONObject()),
                EntityKillTask(EntityType.SKELETON, 11, this, "Skeletons", false, JSONObject())
            ),
            this,
            false,
            1500,
            "1,500 XP"
        ))
        quests.add(XpRewardingQuest(
            "Get Some Friends",
            listOf(
                WolfTameTask(2, this, "Tame some friends", true, JSONObject()),
                EntityKillTask(EntityType.COW, 5, this, "Get some food for friends", true, JSONObject())
            ),
            this,
            true,
            1400,
            "1,400 XP"
        ))
        quests.add(XpRewardingQuest(
            "Find Diamonds",
            listOf(
                BlockBreakTask(Material.DIAMOND_ORE, 1, this, "Mine Diamond Ore", false, JSONObject())
            ),
            this,
            false,
            5024,
            "5,024 XP"
        ))
        quests.add(XpRewardingQuest(
            "Travel",
            listOf(
                MoveTask(1000L, this, "Run 1,000 blocks", false, JSONObject())
            ),
            this,
            false,
            1028,
            "1,028 XP"
        ))
        quests.add(XpRewardingQuest(
            "Minecart through the land",
            listOf(
                MoveTask(2500L, this, "Cart through 2500 blocks", false, JSONObject())
            ),
            this,
            false,
            1028,
            "1,028 XP"
        ))
        quests.add(XpRewardingQuest(
            "Swim",
            listOf(
                MoveTask(500L, this, "Swim with the fishes for 500 blocks", false, JSONObject())
            ),
            this,
            false,
            1028,
            "1,028 XP"
        ))
        saveQuests()

        SqlHandler.username = configuration.getString("username")
        SqlHandler.password = configuration.getString("password")
        SqlHandler.url = configuration.getString("url")

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