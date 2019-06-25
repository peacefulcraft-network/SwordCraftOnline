package net.peacefulcraft.sco.inventories;

import org.bukkit.entity.Player;

public class SwordSkillInventory extends InventoryBase{

	private Player p;
	
	public SwordSkillInventory(Player p) {
		super(p);
		this.p = p;
	}

	@Override
	public Class getInventoryType() {
		return SwordSkillInventory.class;
	}
}
