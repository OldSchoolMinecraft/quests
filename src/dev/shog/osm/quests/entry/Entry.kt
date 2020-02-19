package dev.shog.osm.quests.entry

import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener

/**
 * An empty entry, this is something a player needs to complete for a quest.
 */
interface Entry : Listener {
    var playerComplete: (Player, Server) -> Unit

    /**
     * What it should be invoked on.
     */
    fun getEventType(): Event.Type

    /**
     * When the player has successfully completed the quest.
     *
     * @param player The player who completed the quest.
     * @param server The server where the player resides.
     */
    fun onPlayerComplete(player: Player, server: Server) {
        playerComplete.invoke(player, server)
    }

    /**
     * When the player has successfully completed the quest.
     *
     * @param lambda What to do when the player completes the entry.
     */
    fun setPlayerCompete(lambda: (Player, Server) -> Unit) {
        playerComplete = lambda
    }
}