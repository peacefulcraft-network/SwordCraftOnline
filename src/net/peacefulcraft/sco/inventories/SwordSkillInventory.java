package net.peacefulcraft.sco.inventories;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.storage.tasks.InventorySaveTask;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;

public class SwordSkillInventory extends InventoryBase implements SaveableInventory {
	
	private SwordSkillCaster caster;
	private long inventoryId;
		@Override
		public long getInventoryId() { return inventoryId; }
		@Override
		public void setInventoryId(long inventoryId) { this.inventoryId = inventoryId; }
 
	@Override
	public InventoryType getType() { return InventoryType.SWORD_SKILL; }

	public SwordSkillInventory(SwordSkillCaster caster, ItemIdentifier[] items) {
		super(items);

		this.caster = caster;
	}

	@Override
	public void initializeDefaultLoadout() {
	}

	@Override
	public void resizeInventory(int size) {
	}

	@Override
	public void saveInventory() {
		(new InventorySaveTask(
				inventoryId, 0, InventoryType.SWORD_SKILL,
				this.generateItemIdentifiers(), null
			)
		).runTaskAsynchronously(SwordCraftOnline.getPluginInstance());
	}
}
