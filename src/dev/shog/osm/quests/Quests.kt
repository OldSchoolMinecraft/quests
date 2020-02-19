package dev.shog.osm.quests

import dev.shog.osm.quests.entry.complete.BlockPlaceEntry
import dev.shog.osm.quests.entry.quest.ItemRewardQuest
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Quests : JavaPlugin() {
    override fun onEnable() {
        val r = ItemRewardQuest("Sho's Fun Quest", listOf(BlockPlaceEntry(Material.LOG, 5) { _, _ -> }), this.server, ItemStack(Material.BEDROCK, 2))

        r.registerEntries(this)
    }

    override fun onDisable() {
    }
}