package dev.shog.osm.quest.handle.quests.task.type.move

import com.fasterxml.jackson.databind.ObjectMapper
import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Boat
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerListener
import org.bukkit.event.player.PlayerMoveEvent
import org.json.JSONObject

/**
 * A move task.
 * You must walk [distance] blocks.
 */
class MinecartMoveTask(
    val distance: Long,
    osmQuests: OsmQuests,
    name: String,
    donor: Boolean,
    data: JSONObject
) : QuestTask(name, osmQuests, donor, data) {
    private val status: HashMap<String, Double>
    override val identifier: String = "MOVE_MINECART"

    init {
        status = if (!data.isEmpty) {
            val mapper = ObjectMapper()

            mapper.readValue(
                data.toString(),
                mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, Double::class.java)
            )
        } else hashMapOf()

        osmQuests.server.pluginManager.registerEvent(Event.Type.PLAYER_MOVE, object : PlayerListener() {
            override fun onPlayerMove(event: PlayerMoveEvent?) {
                if (event != null
                    && !isComplete(event.player)
                    && event.player.isInsideVehicle
                    && event.player.vehicle is Minecart
                    && userOk(event.player)
                ) {
                    val current = status[event.player.name.toLowerCase()] ?: 0.0

                    status[event.player.name.toLowerCase()] = current + event.to.distance(event.from)

                    if (isComplete(event.player)) {
                        onPlayerComplete.invoke(osmQuests, event.player)
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

        return JSONObject(mapper.writeValueAsString(status))
    }

    /**
     * The player's status. "0/1 wood blocks placed" etc
     */
    override fun getStatusString(player: Player): String {
        val status = status[player.name.toLowerCase()] ?: 0

        return MessageHandler.getMessage("commands.view-quest.status.minecart", status.toInt(), distance)
    }
}