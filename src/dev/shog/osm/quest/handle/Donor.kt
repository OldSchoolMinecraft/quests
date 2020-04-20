package dev.shog.osm.quest.handle

import dev.shog.osm.util.api.OsmApi
import java.util.concurrent.ConcurrentHashMap

object Donor {
    private val cache = ConcurrentHashMap<String, Boolean>()

    fun check(user: String): Boolean {
        if (cache.contains(user))
            return cache[user]!!

        val check = OsmApi
            .isDonor(user)
            .join()

        cache[user] = check

        return check
    }
}