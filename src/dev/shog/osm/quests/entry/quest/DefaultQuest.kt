package dev.shog.osm.quests.entry.quest

import dev.shog.osm.quests.entry.Entry
import org.bukkit.Server
import org.bukkit.entity.Player

/**
 * A default quest.
 */
open class DefaultQuest(
    private val name: String,
    private val server: Server,
    private val entries: List<Entry>
): Quest {
    override fun toString(): String {
        return "Quest: {${getName()}, ${getEntries()}, ${playerStatus}}"
    }

    private val playerStatus = mutableMapOf<String, Int>()

    init {
        val lambda = { player: Player, _: Server ->
            val current = playerStatus[player.name.toLowerCase()]

            playerStatus[player.name.toLowerCase()] =
                if (current == null)
                    1
                else current + 1

            checkFinish(player)
        }

        entries
            .forEach { ent -> ent.setPlayerCompete(lambda) }
    }

    private fun checkFinish(player: Player) {
        if (isCompleteFor(player))
            onPlayerComplete(player)
    }

    override fun getName(): String =
        name

    override fun getEntries(): List<Entry> =
        this.entries

    override fun getServer(): Server =
        server

    override fun onPlayerComplete(player: Player) {
    }

    override fun onPlayerCancel(player: Player) {
        playerStatus[player.name.toLowerCase()] = 0
    }

    override fun isCompleteFor(player: Player): Boolean {
        val finished = playerStatus[player.name.toLowerCase()]
            ?: return false

        return finished == getEntries().size
    }

    override fun enterUser(player: Player) {
        playerStatus[player.name.toLowerCase()] = 0
    }
}