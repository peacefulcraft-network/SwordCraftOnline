package net.peacefulcraft.sco.inventories;

import java.io.FileNotFoundException;

import org.bukkit.entity.Player;

public class SwordSkillInventory extends InventoryBase{

	private Player p;
		public Player getPlayer() { return p; }

	
	public SwordSkillInventory(Player p) throws FileNotFoundException {
		super(p, SwordSkillInventory.class);
		this.p = p;
	}

	@Override
	public Class getInventoryType() {
		return SwordSkillInventory.class;
	}
	
}
