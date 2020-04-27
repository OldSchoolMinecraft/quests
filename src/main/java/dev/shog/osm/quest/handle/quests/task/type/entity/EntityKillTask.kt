package dev.shog.osm.quest.handle.quests.task.type.entity

import com.fasterxml.jackson.databind.ObjectMapper
import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.quests.Quest
import dev.shog.osm.quest.handle.quests.task.QuestTask
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityListener
import org.json.JSONObject

/**
 * A entity kill event.
 * You must kill an [entity] [amount] amount of times.
 */
class EntityKillTask(
    val entity: EntityType,
    val amount: Int,
    osmQuests: OsmQuests,
    name: String,
    donor: Boolean,
    data: JSONObject
) : QuestTask(name, osmQuests, donor, data) {
    private val status: HashMap<String, Int>
    override val identifier: String = "ENTITY_KILL"

    init {
        status = if (!data.isEmpty) {
            val mapper = ObjectMapper()

            mapper.readValue(
                data.toString(),
                mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, Int::class.java)
            )
        } else hashMapOf()

        osmQuests.server.pluginManager.registerEvent(Event.Type.ENTITY_DEATH, object : EntityListener() {
            override fun onEntityDeath(event: EntityDeathEvent?) {
                if (event != null && event.entity::class.java == entity.entityClass) {
                    val ldc = event.entity.lastDamageCause

                    if (ldc is EntityDamageByEntityEvent && ldc.damager is Player) {
                        val player = ldc.damager as Player

                        if (userOk(player) && !isComplete(player)) {
                            val current = status[player.name.toLowerCase()] ?: 0

                            status[player.name.toLowerCase()] = current + 1

                            if (isComplete(player)) {
                                onPlayerComplete.invoke(osmQuests, player)
                            }
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

        return MessageHandler.getMessage("commands.view-quest.status.entity-kill", status.toLong(), amount, entity.entityName)
    }
}