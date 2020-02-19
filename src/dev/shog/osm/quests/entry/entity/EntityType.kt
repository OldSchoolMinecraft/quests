package dev.shog.osm.quests.entry.entity

import org.bukkit.entity.Chicken
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Zombie

/**
 * The type of entity and it's class.
 *
 * @param clazz The class of the entity.
 */
enum class EntityType(clazz: Class<*>) {
    ZOMBIE(Zombie::class.java),
    SKELETON(Skeleton::class.java),
    CHICKEN(Chicken::class.java);

    fun parse(string: String): EntityType? {
        for (value in values())
            if (value.toString().equals(string, true))
                return value

        return null
    }
}