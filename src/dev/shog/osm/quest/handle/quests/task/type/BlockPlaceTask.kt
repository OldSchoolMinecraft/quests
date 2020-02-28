package dev.shog.osm.quest.handle.quests.task.type

import com.fasterxml.jackson.databind.ObjectMapper
import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockListener
import org.bukkit.event.block.BlockPlaceEvent
import org.json.JSONArray
import org.json.JSONObject

/**
 * A block place task.
 * You must place a [material] block [amount] times.
 */
class BlockPlaceTask(
    val material: Material,
    val amount: Int,
    osmQuests: OsmQuests,
    name: String,
    data: JSONObject
) : QuestTask(name, osmQuests, data) {
    private val status: HashMap<String, Int>
    override val identifier: String = "BLOCK_PLACE"

    init {
        status = if (!data.isEmpty && data.has("status")) {
            val status = data.getJSONObject("status")
            val mapper = ObjectMapper()

            mapper.readValue(
                status.toString(),
                mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, Int::class.java)
            )
        } else hashMapOf()

        osmQuests.server.pluginManager.registerEvent(Event.Type.BLOCK_PLACE, object : BlockListener() {
            override fun onBlockPlace(event: BlockPlaceEvent?) {
                if (event != null && event.block.type == material && !isComplete(event.player)) {
                    val current = status[event.player.name.toLowerCase()] ?: 0

                    status[event.player.name.toLowerCase()] = current + 1

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
        val current = status[player.name.toLowerCase()] ?: 0

        return current >= amount
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

        return MessageHandler.getMessage("commands.view-quest.status.block-place", status.toLong(), amount, material.toString())
    }
}