package dev.shog.osm.quest.handle.commands

import dev.shog.osm.quest.OsmQuests
import dev.shog.osm.quest.handle.Donor
import dev.shog.osm.util.defaultFormat
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

/**
 * Debug
 */
val DEBUG_COMMAND = { osmQuests: OsmQuests ->
    CommandExecutor { sender, cmd, label, args ->
        if (sender !is Player)
            return@CommandExecutor true

        val isDonor = Donor.check(sender)

        sender.sendMessage("S: ${isDonor}, LS: ${osmQuests.lastQuestSave.defaultFormat()}")

        true
    }
}