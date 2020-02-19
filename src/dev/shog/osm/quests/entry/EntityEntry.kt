package dev.shog.osm.quests.entry

import dev.shog.osm.quests.entry.entity.EntityType

/**
 * An entry that involves an entity.
 */
interface EntityEntry : Entry {
    /**
     * The entity that the player should be interacting with.
     */
    fun getEntityType(): EntityType

    /**
     * The interact type.
     */
    fun getInteractType(): Unit
}