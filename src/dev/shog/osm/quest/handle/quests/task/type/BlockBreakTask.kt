package dev.shog.osm.quest.handle.quests.task.type

import dev.shog.osm.quest.Quests
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockListener
import java.util.concurrent.ConcurrentHashMap

class BlockBreakTask(
    val material: Material,
    val amount: Int,
    quests: Quests,
    name: String
) : QuestTask(name, quests) {
    val status = ConcurrentHashMap<String, Int>()
    val completeUsers = mutableListOf<String>()

    private val listener = object : BlockListener() {
        override fun onBlockBreak(event: BlockBreakEvent?) {
            if (event != null && event.block.type == material && !completeUsers.contains(event.player.name.toLowerCase())) {
                val current = status[event.player.name.toLowerCase()] ?: 0

                status[event.player.name.toLowerCase()] = current + 1

                if (isComplete(event.player)) {
                    completeUsers.add(event.player.name.toLowerCase())
                    onPlayerComplete.invoke(quests, event.player)
                }
            }
        }
    }

    init {
        quests.server.pluginManager.registerEvent(Event.Type.BLOCK_BREAK, listener, Event.Priority.Low, quests)
    }

    override fun isComplete(player: Player): Boolean {
        val current = status[player.name.toLowerCase()] ?: 0

        return current >= amount
    }
}