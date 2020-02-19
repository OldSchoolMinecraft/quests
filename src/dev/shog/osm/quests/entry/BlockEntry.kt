package dev.shog.osm.quests.entry

import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.entity.*
import org.bukkit.event.Event
import org.bukkit.event.block.BlockListener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityListener

/**
 * A type of entry that involves blocks.
 */
interface BlockEntry : Entry {
    /**
     * The block material.
     */
    fun getMaterial(): Material
}