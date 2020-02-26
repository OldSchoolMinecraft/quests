package dev.shog.osm.quest.handle.quests

import dev.shog.osm.quest.Quests
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

abstract class Quest(
    val questName: String,
    val tasks: List<QuestTask>,
    quests: Quests
) {
    val status = ConcurrentHashMap<String, List<String>>()

    abstract fun onComplete(player: Player)

    fun isComplete(player: Player): Boolean =
        status[player.name.toLowerCase()]?.size == tasks.size

    fun getMissingTasks(player: Player): List<QuestTask> =
        tasks.filter { task -> !task.isComplete(player) }

    init {
        tasks.forEach { task ->
            task.onPlayerComplete = { player ->
                val current = status[player.name.toLowerCase()]?.toMutableList()

                val new = if (current == null)
                    listOf(task.name)
                else {
                    current.add(task.name)
                    current
                }

                player.sendMessage("You have completed ${task.name}")

                status[player.name.toLowerCase()] = new

                if (new.size == tasks.size)
                    onComplete(player)
            }
        }
    }
}