package dev.shog.osm.quest

import dev.shog.osm.quest.handle.commands.VIEW_QUESTS
import dev.shog.osm.quest.handle.commands.VIEW_XP
import dev.shog.osm.quest.handle.parser.QuestParser
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.XpRewardingQuest
import dev.shog.osm.quest.handle.quests.task.type.BlockBreakTask
import dev.shog.osm.quest.handle.quests.task.type.MoveTask
import dev.shog.osm.quest.handle.quests.task.type.WolfTameTask
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityListener
import org.bukkit.event.entity.EntityTameEvent
import org.bukkit.plugin.java.JavaPlugin
import org.json.JSONObject
import java.io.File

class OsmQuests : JavaPlugin() {
    lateinit var quests: MutableList<Quest>

    override fun onEnable() {
        getCommand("xp").executor = VIEW_XP
        getCommand("quests").executor = VIEW_QUESTS.invoke(this)

        quests = QuestParser.getAllQuests(this)

        println("OSMQuests: Loaded ${quests.size} quests.")
    }

    override fun onDisable() {
        println("OSMQuests: Saving quests...")
        quests
            .forEach { quest -> QuestParser.saveQuest(quest) }
        println("OSMQuests: Completed quest saving!")
    }
}