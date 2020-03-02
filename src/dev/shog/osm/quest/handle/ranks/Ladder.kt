package dev.shog.osm.quest.handle.ranks

import dev.shog.osm.quest.handle.ranks.user.User

object Ladder {
    private val LADDER = arrayOf(
        Rank("Player", "§8[Player]§7", RankRequirements(0, 0.0, 0)),
        Rank("Newcomer", "§8[Newcomer]§7", RankRequirements(10, 150.0, 5)),
        Rank("Traveller", "§8[Traveller]§7", RankRequirements(50, 250.0, 25)),
        Rank("Known", "§8[Known]§7", RankRequirements(150, 500.0, 75)),
        Rank("Expert", "§8[Expert]§7", RankRequirements(300, 750.0, 150)),
        Rank("Elite", "§8[Elite]§7", RankRequirements(500, 1000.0, 250)),
        Rank("Virtuoso", "§8[Virtuoso]§7", RankRequirements(800, 1015.0, 400)),
        Rank("Veteran", "§8[Veteran]§7", RankRequirements(1000, 5000.8925, 700))
    )

    /**
     * Get the rank above [user]'s rank.
     */
    fun getUpperRank(user: User): Rank? {
        return getRank(user.rank + 1)
    }

    /**
     * Get the ladder position of [rank].
     */
    fun getPos(rank: Rank): Int =
        LADDER.indexOf(rank)

    /**
     * Get a rank by it's ladder position.
     * Ladder starts at 0 (default)
     */
    fun getRank(pos: Int): Rank? =
        LADDER[pos]
}