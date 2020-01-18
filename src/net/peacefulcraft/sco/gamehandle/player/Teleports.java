package net.peacefulcraft.sco.gamehandle.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.peacefulcraft.sco.SwordCraftOnline;

public class Teleports
{
	public static Location getWaystone(int index) {
		return new Location(
			Bukkit.getWorld((String) SwordCraftOnline.getSCOConfig().getWaystone(index).get("world")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getWaystone(index).get("x")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getWaystone(index).get("y")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getWaystone(index).get("z"))
			);
	}
	
	public static Location getSpawn() {
		return new Location(
			Bukkit.getWorld((String) SwordCraftOnline.getSCOConfig().getSpawn().get("world")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getSpawn().get("x")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getSpawn().get("y")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getSpawn().get("z"))
			);
	}
	
	public static Location getQuit() {
		return new Location(
			Bukkit.getWorld((String) SwordCraftOnline.getSCOConfig().getSpawn().get("world")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getQuit().get("x")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getQuit().get("y")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getQuit().get("z"))
			);
	}

	/**Returns teleport location of dungeon entrance */
	public static Location getDungeonEntrance(int index) {
		return new Location(
			Bukkit.getWorld((String) SwordCraftOnline.getSCOConfig().getDungeonEntrance(index).get("world")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getDungeonEntrance(index).get("x")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getDungeonEntrance(index).get("y")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getDungeonEntrance(index).get("x"))
		);
	}

	/**Returns teleport location of dungeon arena */
	public static Location getDungeonArena(int index) {
		return new Location(
			Bukkit.getWorld((String) SwordCraftOnline.getSCOConfig().getDungeonArena(index).get("world")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getDungeonArena(index).get("x")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getDungeonArena(index).get("y")),
				Double.parseDouble( (String) SwordCraftOnline.getSCOConfig().getDungeonArena(index).get("x"))
		);
	}
}
