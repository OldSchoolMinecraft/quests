package dev.shog.osm.quest.handle.quests.task.type

import com.fasterxml.jackson.databind.ObjectMapper
import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockListener
import org.bukkit.event.entity.EntityListener
import org.bukkit.event.entity.EntityTameEvent
import org.json.JSONArray
import org.json.JSONObject

class WolfTameTask(
    val amount: Int,
    osmQuests: OsmQuests,
    name: String,
    data: JSONObject
) : QuestTask(name, osmQuests, data) {
    private val status: HashMap<String, Int>

    init {
        status = if (!data.isEmpty && data.has("status")) {
            val status = data.getJSONObject("status")
            val mapper = ObjectMapper()

            mapper.readValue(
                status.toString(),
                mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, Int::class.java)
            )
        } else hashMapOf()

        osmQuests.server.pluginManager.registerEvent(Event.Type.ENTITY_TAME, object : EntityListener() {
            override fun onEntityTame(event: EntityTameEvent?) {
                if (event != null) {
                    val player = event.owner as Player

                    if (event.entity is Wolf && !isComplete(player)) {
                        val current = status[player.name.toLowerCase()] ?: 0

                        status[player.name.toLowerCase()] = current + 1

                        if (isComplete(player)) {
                            onPlayerComplete.invoke(osmQuests, player)
                        }
                    }
                }
            }
        }, Event.Priority.Low, osmQuests)

    }

    override val identifier: String = "WOLF_TAME"

    override fun isComplete(player: Player): Boolean {
        val current = status[player.name.toLowerCase()] ?: 0

        return current >= amount
    }

    override fun getSaveData(quest: Quest): JSONObject {
        val mapper = ObjectMapper()

        val st = JSONObject(mapper.writeValueAsString(status))

        val obj = JSONObject()

        obj.put("status", st)

        return obj
    }


    override fun getStatusForPlayer(player: Player): String {
        val status = status[player.name.toLowerCase()] ?: 0

        return MessageHandler.getMessage("commands.view-quest.status.wolf-tame", status, amount)
    }
}