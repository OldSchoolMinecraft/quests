package dev.shog.osm.quest.handle.quests.task

import dev.shog.osm.quest.Quests
import dev.shog.osm.quest.handle.quests.Quest
import org.bukkit.entity.Player

abstract class QuestTask(val name: String, val quests: Quests) {
    var onPlayerComplete: Quests.(Player) -> Unit = {}

    abstract fun isComplete(player: Player): Boolean
}