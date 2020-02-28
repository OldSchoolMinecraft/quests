package dev.shog.osm.quest.handle.quests

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.quests.task.QuestTask
import dev.shog.osm.quest.handle.quests.task.type.BlockBreakTask
import dev.shog.osm.quest.handle.quests.task.type.BlockPlaceTask
import dev.shog.osm.quest.handle.quests.task.type.MoveTask
import dev.shog.osm.quest.handle.quests.task.type.WolfTameTask
import org.bukkit.entity.Player
import org.json.JSONArray
import org.json.JSONObject

/**
 * A quest.
 *
 * @param questName The name of the quest.
 * @param tasks The tasks you must complete to finish the quest.
 * @param osmQuests The OSM quests instance.
 */
abstract class Quest(
    val questName: String,
    val tasks: List<QuestTask>,
    private val osmQuests: OsmQuests
) {
    /**
     * What should happen when a user has completed the quest.
     *
     * @param player The player that has completed the quest.
     */
    abstract fun onComplete(player: Player)

    /**
     * A description of the quest's reward.
     */
    abstract val rewardString: String

    /**
     * The identifier of the quest.
     */
    abstract val identifier: String

    /**
     * Get the save data.
     */
    fun getSaveData(): JSONObject {
        val data = JSONObject()
        val questTasks = JSONArray()

        tasks.forEach { task ->
            val obj = JSONObject()

            obj.put("name", task.name)
            obj.put("identifier", task.identifier)
            obj.put("data", task.getSaveData(this))

            when (task) {
                is BlockBreakTask -> {
                    obj.put("material", task.material)
                    obj.put("amount", task.amount)
                }

                is BlockPlaceTask -> {
                    obj.put("material", task.material)
                    obj.put("amount", task.amount)
                }

                is WolfTameTask -> {
                    obj.put("amount", task.amount)
                }

                is MoveTask -> {
                    obj.put("distance", task.distance)
                }
            }

            questTasks.put(obj)
        }

        data.put("tasks", questTasks)
        data.put("name", questName)
        data.put("reward", rewardString)
        data.put("identifier", identifier)

        when (this) {
            is BalanceRewardingQuest -> {
                data.put("balanceReward", this.balReward)
            }

            is XpRewardingQuest -> {
                data.put("xpReward", this.xpReward)
            }
        }

        return data
    }

    /**
     * If [player] has completed this quest.
     */
    fun isComplete(player: Player): Boolean {
        return !tasks
            .map { task -> task.isComplete(player) }
            .any { complete -> !complete }
    }

    /**
     * Get all of the tasks that haven't been completed by [player].
     */
    fun getMissingTasks(player: Player): List<QuestTask> =
        tasks.filter { task -> !task.isComplete(player) }

    init {
        // Add a player complete to each task
        tasks.forEach { task ->
            task.onPlayerComplete = { player ->
                player.sendMessage("You have completed ${task.name}")

                if (isComplete(player))
                    onComplete(player)
            }
        }
    }
}