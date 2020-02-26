package dev.shog.osm.quest

import dev.shog.osm.quest.handle.commands.VIEW_QUESTS
import dev.shog.osm.quest.handle.commands.VIEW_XP
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.XpRewardingQuest
import dev.shog.osm.quest.handle.quests.task.type.BlockBreakTask
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class Quests : JavaPlugin() {
    lateinit var quests: List<Quest>

    override fun onEnable() {
        getCommand("xp").executor = VIEW_XP
        getCommand("quests").executor = VIEW_QUESTS.invoke(this)

        quests = listOf(XpRewardingQuest("fun", listOf(BlockBreakTask(Material.WOOD, 5, this, "task-1")), this, 1000))
    }

    override fun onDisable() {
    }
}