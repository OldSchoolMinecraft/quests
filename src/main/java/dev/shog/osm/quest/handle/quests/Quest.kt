package dev.shog.osm.quest.handle.quests

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.task.QuestTask
import dev.shog.osm.quest.handle.quests.task.type.block.BlockBreakTask
import dev.shog.osm.quest.handle.quests.task.type.block.BlockPlaceTask
import dev.shog.osm.quest.handle.quests.task.type.move.MoveTask
import dev.shog.osm.quest.handle.quests.task.type.WolfTameTask
import dev.shog.osm.quest.handle.quests.task.type.entity.EntityKillTask
import dev.shog.osm.quest.handle.quests.task.type.move.BoatMoveTask
import dev.shog.osm.quest.handle.quests.task.type.move.JumpTask
import dev.shog.osm.quest.handle.quests.task.type.move.MinecartMoveTask
import org.bukkit.entity.Minecart
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
    val donor: Boolean,
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

            specialAttributes(task, obj)

            questTasks.put(obj)
        }

        data.put("tasks", questTasks)
        data.put("name", questName)
        data.put("reward", rewardString)
        data.put("identifier", identifier)
        data.put("donor", donor)

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
     * Add task specific attributes to [obj]
     */
    private fun specialAttributes(task: QuestTask, obj: JSONObject) {
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

            is MinecartMoveTask -> {
                obj.put("distance", task.distance)
            }

            is BoatMoveTask -> {
                obj.put("distance", task.distance)
            }

            is JumpTask -> {
                obj.put("times", task.times)
            }

            is EntityKillTask -> {
                obj.put("type", task.entity)
                obj.put("amount", task.amount)
            }
        }
    }

    init {
        // Add a player complete to each task
        tasks.forEach { task ->
            task.onPlayerComplete = { player ->
                player.sendMessage(MessageHandler.getMessage("quests.task-complete", task.name, questName))

                if (isComplete(player))
                    onComplete(player)
            }
        }
    }
}