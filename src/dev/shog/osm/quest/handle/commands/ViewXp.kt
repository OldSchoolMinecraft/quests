package dev.shog.osm.quest.handle.commands

import dev.shog.osm.quest.handle.XpHandler
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

val VIEW_XP = CommandExecutor { sender, cmd, label, args ->
    if (sender is Player) {
        sender.sendMessage("You currently have ${XpHandler.xp[sender.name.toLowerCase()]}")
    } else {
        sender.sendMessage("You can't do this as console!")
    }

    true
}