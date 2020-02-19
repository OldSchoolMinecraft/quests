package dev.shog.osm.quests.entry.complete

import dev.shog.osm.quests.entry.BlockEntry
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockListener
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlaceEntry(
    private val type: Material,
    private val amount: Int,
    complete: (Player, Server) -> Unit
) : BlockEntry, BlockListener() {
    private val status = HashMap<String, Int>()

    override fun getMaterial(): Material =
        type

    override var playerComplete: (Player, Server) -> Unit = complete

    override fun getEventType(): Event.Type =
        Event.Type.BLOCK_PLACE

    override fun onBlockPlace(event: BlockPlaceEvent?) {
        if (event != null && event.blockPlaced.type == type) {
            val st = status[event.player.name.toLowerCase()] ?: 0

            status[event.player.name.toLowerCase()] = st + 1

            if (st + 1 >= amount)
                onPlayerComplete(event.player, event.player.server)
        }
    }
}
