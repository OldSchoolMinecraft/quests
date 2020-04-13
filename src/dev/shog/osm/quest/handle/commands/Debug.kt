package dev.shog.osm.quest.handle.commands

import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.ranks.user.User
import dev.shog.osm.util.api.OsmApi
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

/**
 * Debug
 */
val DEBUG_COMMAND = CommandExecutor { sender, cmd, label, args ->
    if (sender !is Player)
        return@CommandExecutor true

    val isDonor = OsmApi.isDonor(sender.name).join().toString()

    sender.sendMessage(isDonor)

    true
}