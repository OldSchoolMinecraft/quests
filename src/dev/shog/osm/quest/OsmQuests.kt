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
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.json.JSONObject

class OsmQuests : JavaPlugin() {
    lateinit var quests: MutableList<Quest>

    override fun onEnable() {
        getCommand("xp").executor = VIEW_XP
        getCommand("quests").executor = VIEW_QUESTS.invoke(this)
        getCommand("rankup").executor = RANK_UP
        getCommand("rank").executor = RANK
        getCommand("osmq_debug").executor = DEBUG_COMMAND

        quests = QuestParser.getAllQuests(this)
//        quests.add(BalanceRewardingQuest(
//            "Commit Mass Genocide",
//            listOf(
//                EntityKillTask(EntityType.CREEPER, 15, this, "Creepers", false, JSONObject()),
//                EntityKillTask(EntityType.ZOMBIE, 55, this, "Zombies", false, JSONObject()),
//                EntityKillTask(EntityType.SPIDER, 82, this, "Spiders", false, JSONObject()),
//                EntityKillTask(EntityType.SKELETON, 51, this, "Skeletons", false, JSONObject())
//            ),
//            this,
//            false,
//            1500.00,
//            "$1,500 smackeroonies"
//        ))
//        quests.add(BalanceRewardingQuest(
//            "Get Some Friends",
//            listOf(
//                WolfTameTask(3, this, "Tame some friends", true, JSONObject()),
//                EntityKillTask(EntityType.COW, 5, this, "Get some food for friends", true, JSONObject())
//            ),
//            this,
//            true,
//            5000.00,
//            "$5,000 stones"
//        ))
//        quests.add(XpRewardingQuest(
//            "Find Diamonds",
//            listOf(
//                BlockBreakTask(Material.DIAMOND_ORE, 1, this, "Mine Diamond Ore", false, JSONObject())
//            ),
//            this,
//            false,
//            5024,
//            "5,024 XP"
//        ))
//        saveQuests()

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