package dev.shog.osm.quest.handle.commands

import com.earth2me.essentials.api.Economy
import dev.shog.osm.quest.handle.MessageHandler
import dev.shog.osm.quest.handle.ranks.Ladder
import dev.shog.osm.quest.handle.ranks.user.User
import dev.shog.osm.quest.sendMultiline
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player
import dev.shog.osm.pl.fancyDate

/**
 * View rank.
 */
val RANK = CommandExecutor { sender, cmd, label, args ->
    if (sender !is Player) {
        sender.sendMessage(MessageHandler.getMessage("command.no-console"))
        return@CommandExecutor true
    }

    val user = User.getUser(sender.name)
    val upperRank = Ladder.getUpperRank(user)

    val nextRank = if (upperRank != null) {
        MessageHandler.getMessage("commands.rank.next-rank",
            upperRank.name,
            MessageHandler.getMessage("commands.rank.requirements",
                user.getPlayTime().fancyDate(), upperRank.requirements.timeHr,
                user.xp, upperRank.requirements.xp,
                Economy.getMoney(sender.name), upperRank.requirements.balance
            )
        )
    } else MessageHandler.getMessage("commands.rank.max-rank")

    val message = MessageHandler.getMessage(
        "commands.rank.default",
        Ladder.getRank(user.rank)?.name,
        nextRank
    )

    sender.sendMultiline(message)

    true
}