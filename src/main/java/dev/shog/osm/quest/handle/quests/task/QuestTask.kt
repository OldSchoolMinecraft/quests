package dev.shog.osm.quest.handle.quests.task

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.Donor
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.pl.api.OsmApi
import org.bukkit.entity.Player
import org.json.JSONObject

/**
 * @param name The name of the task
 * @param osmQuests The [OsmQuests] instance
 * @Param data The for the task. This is empty if there's no previous data.
 */
abstract class QuestTask(
    val name: String,
    val osmQuests: OsmQuests,
    val donor: Boolean,
    val data: JSONObject
) {
    /**
     * When the player is complete with the task.
     */
    var onPlayerComplete: OsmQuests.(Player) -> Unit = {}

    /**
     * The task identifier. This should identify what type of task it is.
     * This could be something like BLOCK_BREAK
     */
    abstract val identifier: String

    /**
     * If [player] is complete with this [QuestTask].
     */
    abstract fun isComplete(player: Player): Boolean

    /**
     * Get the save data from a [QuestTask].
     */
    abstract fun getSaveData(quest: Quest): JSONObject

    /**
     * Get a status string for a player.
     *
     * This could be "5/16 blocks placed" etc.
     */
    abstract fun getStatusString(player: Player): String

    /**
     * If the user meets the requirements (donor)
     */
    fun userOk(player: Player): Boolean {
        if (!donor)
            return true

        return Donor.check(player)
    }
}