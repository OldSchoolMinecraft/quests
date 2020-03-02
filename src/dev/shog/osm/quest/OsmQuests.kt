package dev.shog.osm.quest

import dev.shog.osm.quest.handle.SqlHandler
import dev.shog.osm.quest.handle.commands.RANK
import dev.shog.osm.quest.handle.commands.RANK_UP
import dev.shog.osm.quest.handle.commands.VIEW_QUESTS
import dev.shog.osm.quest.handle.commands.VIEW_XP
import dev.shog.osm.quest.handle.parser.QuestParser
import dev.shog.osm.quest.handle.quests.Quest
import org.bukkit.plugin.java.JavaPlugin

class OsmQuests : JavaPlugin() {
    lateinit var quests: MutableList<Quest>

    override fun onEnable() {
        getCommand("xp").executor = VIEW_XP
        getCommand("quests").executor = VIEW_QUESTS.invoke(this)
        getCommand("rankup").executor = RANK_UP
        getCommand("rank").executor = RANK

        quests = QuestParser.getAllQuests(this)

        SqlHandler.username = configuration.getString("username")
        SqlHandler.password = configuration.getString("password")
        SqlHandler.url = configuration.getString("url")

        println(SqlHandler.getConnection().prepareStatement("SELECT * FROM quests").executeQuery())

        println("OSMQuests: Loaded ${quests.size} quests.")
    }

    override fun onDisable() {
        println("OSMQuests: Saving quests...")
        quests
            .forEach { quest -> QuestParser.saveQuest(quest) }
        println("OSMQuests: Completed quest saving!")
    }
}