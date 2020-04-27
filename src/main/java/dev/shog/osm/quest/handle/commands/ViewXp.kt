package dev.shog.osm.quest.handle.commands

import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.ranks.user.User
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

/**
 * View your XP.
 */
val VIEW_XP = CommandExecutor { sender, cmd, label, args ->
    if (sender is Player) {
        sender.sendMessage(MessageHandler.getMessage("commands.xp.xp", User.getUser(sender.name).xp))
    } else {
        sender.sendMessage(MessageHandler.getMessage("command.no-console"))
    }

    true
}