package dev.shog.osm.quest

import dev.shog.osm.quest.handle.parser.QuestParser
import org.bukkit.command.CommandSender

/**
 * Send a multi-line message.
 */
fun CommandSender.sendMultiline(message: String) {
    message.split("\n")
        .forEach { msg -> this.sendMessage(msg) }
}