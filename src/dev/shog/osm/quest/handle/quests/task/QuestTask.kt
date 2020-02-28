package dev.shog.osm.quest.handle.quests.task

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.quests.Quest
import org.bukkit.entity.Player
import org.json.JSONObject

abstract class QuestTask(val name: String, val osmQuests: OsmQuests, val data: JSONObject) {
    var onPlayerComplete: OsmQuests.(Player) -> Unit = {}

    /**
     * The task identifier. This should identify what type of task it is.
     * This could be something like BLOCK_BREAK
     */
    abstract val identifier: String

    abstract fun isComplete(player: Player): Boolean

    abstract fun getSaveData(quest: Quest): JSONObject

    /**
     * Get a status string for a player.
     *
     * This could be "5/16 blocks placed" etc.
     */
    abstract fun getStatusForPlayer(player: Player): String
}