package net.peacefulcraft.sco.item;

/**
 * Item that unique identifies a SwordSkill.
 * Can be used to instantiate a SwordSkillProvider in package net.peacefulcraft.sco.items
 */
public class SkillIdentifier{
	
	private String skillName;
		public String getSkillName() { return skillName; }
	private int skillLevel;;
		public int getSkillLevel() { return skillLevel; }
	private ItemTier rarity;
		public ItemTier getRarity() { return rarity; }
	
	public SkillIdentifier(String skillName, int skillLevel, ItemTier rarity) {
		this.skillName = skillName;
		this.skillLevel = skillLevel;
		this.rarity = rarity;
	}
}
