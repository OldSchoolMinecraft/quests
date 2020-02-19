package dev.shog.osm.quests.entry.quest

import dev.shog.osm.quests.Quests
import dev.shog.osm.quests.entry.Entry
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.Event

interface Quest {
    fun getName(): String

    fun getEntries(): List<Entry>

    fun registerEntries(quests: Quests) {
        getEntries()
            .forEach { quests.server.pluginManager.registerEvent(it.getEventType(), it, Event.Priority.Normal, quests) }
    }

    fun getServer(): Server

    fun onPlayerComplete(player: Player)

    fun onPlayerCancel(player: Player)

    fun isCompleteFor(player: Player): Boolean

    fun enterUser(player: Player)
}