package dev.shog.osm.quests.entry.quest

import dev.shog.osm.quests.MessageHandler
import dev.shog.osm.quests.entry.Entry
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemRewardQuest(
    name: String,
    entries: List<Entry>,
    server: Server,
    private vararg val item: ItemStack
) : DefaultQuest(name, server, entries) {
    override fun onPlayerComplete(player: Player) {
        item.forEach {
            player.inventory.addItem(it)
        }

        val items = item.asSequence()
            .joinToString { "§6${it.amount} §e${it.type.toString().toLowerCase()}" }

        player.sendMessage(MessageHandler.getMessage("quests.item-complete", getName(), items))
    }
}