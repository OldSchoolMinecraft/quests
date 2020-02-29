package dev.shog.osm.quest.handle.quests.task.type.move

import com.fasterxml.jackson.databind.ObjectMapper
import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerListener
import org.bukkit.event.player.PlayerMoveEvent
import org.json.JSONObject

/**
 * A move task.
 * You must walk [distance] blocks.
 */
class MoveTask(
    val distance: Long,
    osmQuests: OsmQuests,
    name: String,
    data: JSONObject
) : QuestTask(name, osmQuests, data) {
    private val status: HashMap<String, Double>
    override val identifier: String = "MOVE"

    init {
        status = if (!data.isEmpty && data.has("status")) {
            val status = data.getJSONObject("status")
            val mapper = ObjectMapper()

            mapper.readValue(
                status.toString(),
                mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, Double::class.java)
            )
        } else hashMapOf()

        osmQuests.server.pluginManager.registerEvent(Event.Type.PLAYER_MOVE, object : PlayerListener() {
            override fun onPlayerMove(event: PlayerMoveEvent?) {
                if (event != null && !isComplete(event.player)) {
                    val current = status[event.player.name.toLowerCase()] ?: 0.0

                    val to = event.to.block
                    val aboveTo = event.player.server.getWorld("world").getBlockAt(to.x, to.y + 1, to.z)

                    if (!to.isLiquid && !aboveTo.isLiquid) {
                        status[event.player.name.toLowerCase()] = current + event.to.distance(event.from)

                        if (isComplete(event.player)) {
                            onPlayerComplete.invoke(osmQuests, event.player)
                        }
                    }
                }
            }
        }, Event.Priority.Low, osmQuests)
    }

    /**
     * If [player] has completed this task.
     */
    override fun isComplete(player: Player): Boolean {
        val current = status[player.name.toLowerCase()] ?: 0.0

        return current.toLong() >= distance
    }

    /**
     * The save data for the task.
     */
    override fun getSaveData(quest: Quest): JSONObject {
        val mapper = ObjectMapper()

        val st = JSONObject(mapper.writeValueAsString(status))

        val obj = JSONObject()

        obj.put("status", st)

        return obj
    }

    /**
     * The player's status. "0/1 wood blocks placed" etc
     */
    override fun getStatusForPlayer(player: Player): String {
        val status = status[player.name.toLowerCase()] ?: 0

        return MessageHandler.getMessage("commands.view-quest.status.move", status, distance)
    }
}