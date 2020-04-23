# OSMQuests

When mostly complete, below is what will be available through this plugin.

# How to use yourself
This plugin will require small changes to be able to properly be used on a non-OSM server. 
## What to Change
- The `dev.shog.quest.handle.Donor` object must have a different way to detect Supporters.
- Player saving requires an SQL server. You can either create get an SQL server, or rewrite to save the data locally. (To see what to create, look around the `dev.shog.quest.handle.ranks.user.User` class, or ask in the OSM Discord.)

## Required Plugins
- PermissionsEX
- Essentials (What OSM uses for Economy, you could change this)
- OSMUtil

# Areas

### Quests

#### Quest Examples

- Break 10 sand blocks
- Place 10 sand blocks
- Kill 10 zombies
- Move 100 blocks

#### Quest Description

Quests are something that a player can do. If they do happen to complete a quest, they are rewarded with XP (this will be mentioned later) or other types, such as items or balance. They can view their progress on quests with /quests. In a working system, these will be replaced weekly.

### Ranks

#### Rank Examples

- Newbie (0xp, 0 play time)
- Adventurer (15xp, 2hrs of playtime)
- Veteran (100xp, 1 day of playtime, $250 in-game balance)

#### Rank Description

Ranks are something that should be sort-of grindy, and rewarded with frequent gameplay. There should be enough ranks to not seem boring, but not too much that it may be overwhelming.
