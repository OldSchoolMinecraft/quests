package dev.shog.osm.quests.objs

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object View: CommandExecutor {
    override fun onCommand(sender: CommandSender?, cmd: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender != null && cmd != null && label != null && args != null) {
            when (cmd.name.toLowerCase()) {
                "quests" -> {

                }
            }
        }

        return true
    }

}