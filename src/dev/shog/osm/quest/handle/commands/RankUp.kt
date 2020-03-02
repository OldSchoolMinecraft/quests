package dev.shog.osm.quest.handle.commands

import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.ranks.user.User
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

/**
 * Rank up.
 */
val RANK_UP = CommandExecutor { sender, cmd, label, args ->
    if (sender !is Player) {
        sender.sendMessage(MessageHandler.getMessage("command.no-console"))
        return@CommandExecutor true
    }
    val user = User.getUser(sender.name)

    user.rankUp(sender)

    true
}