package dev.shog.osm.quest.handle.quests.task.type.block

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
import org.json.JSONObject

/**
 * A block break task.
 * You must break a [material] block [amount] times.
 */
class BlockBreakTask(
    val material: Material,
    val amount: Int,
    osmQuests: OsmQuests,
    name: String,
    donor: Boolean,
    data: JSONObject
) : QuestTask(name, osmQuests, donor, data) {
    private val status: HashMap<String, Int>
    override val identifier: String = "BLOCK_BREAK"

    init {
        status = if (!data.isEmpty) {
            val mapper = ObjectMapper()

            mapper.readValue(
                data.toString(),
                mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, Int::class.java)
            )
        } else hashMapOf()

        osmQuests.server.pluginManager.registerEvent(Event.Type.BLOCK_BREAK,  object : BlockListener() {
            override fun onBlockBreak(event: BlockBreakEvent?) {
                if (
                    event != null
                    && event.block.type == material
                    && !isComplete(event.player)
                    && userOk(event.player)
                ) {
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

        return JSONObject(mapper.writeValueAsString(status))
    }

    /**
     * The player's status. "0/1 wood blocks placed" etc
     */
    override fun getStatusString(player: Player): String {
        val status = status[player.name.toLowerCase()] ?: 0

        return MessageHandler.getMessage(
            "commands.view-quest.status.block-break",
            status, amount,
            material.name.split("_").joinToString(" ") { str -> str.toLowerCase().capitalize() }
        )
    }
}