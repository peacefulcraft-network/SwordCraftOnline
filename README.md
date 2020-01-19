# SwordCraftOnline
A Minecraft Java Edition RPG Bukkit plugin, inspired by the Japaneese Novel Series turned T.V. show [Sword Art Online](https://en.wikipedia.org/wiki/Sword_Art_Online).

---

## Technologies

- [Bukkit API](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse)
- [NBT API](https://www.spigotmc.org/resources/nbt-api.7939/)
- [MythicMobs](https://www.spigotmc.org/resources/%E2%9A%94-mythicmobs-free-version-%E2%96%BAthe-1-custom-mob-creator%E2%97%84.5702/)
- [Hikari](https://github.com/brettwooldridge/HikariCP)
- [Maven](https://maven.apache.org/)
- [MariaDB JDBC](https://mariadb.com/kb/en/about-mariadb-connector-j/)

---

## Project Organization

| Package | Description |
|---------|-------------|
| `main.resources` | Meta files and static templates used in the plugin or as dependencies |
| `net.peacefulcraft.sco` | Main project package with main class and global config |
| `net.peacefulcraft.sco.commands` | In-game command handlers |
| `net.peacefulcraft.sco.gamehandle` | Non-tangable game tasks and objects  |
| `net.peacefulcraft.sco.gamehandle.listeners` | Event listeners for gamehandle objects |
| `net.peacefulcraft.sco.gamehandle.player` | Mostly data classes related to players |
| `net.peacefulcraft.sco.inventories` | ADT and implementation for custom user inventories |
| `net.peacefulcraft.sco.inventories.listeners` | Event listeners for inventory tirggers and interactions |
| `net.peacefulcraft.sco.inventories.utilities` | Utility functions for inventoryies
| `net.peacefulcraft.sco.items` | ADT and implementation for custom items and their interactions with sword skills |
| `net.peacefulcraft.sco.items.utilities` | Utility functions for custom items |
| `net.peacefulcraft.sco.storage` | Player data loading, processing, and saving |
| `net.peacefulcraft.sco.storage.tasks` | Tasks, mostly asyc database calls, to store and retrieve data |
| `net.peacefulcraft.sco.swordskills` | ADT and implementations for entity abilities and skills |
| `net.peacefulcraft.sco.swordskills.listeners` | Event listeners for abilities and skills |
| `net.peacefulcraft.sco.swordskills.utilities` | Utility functions for abilities and skills |
