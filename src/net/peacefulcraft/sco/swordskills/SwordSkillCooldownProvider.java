package net.peacefulcraft.sco.swordskills;

/**
 * Exposes the data on an ItemIdentifier that is used for tracking cooldowns on items.
 * Note that the underlying ItemIdentifier must also be a CustomDataHolder that reports 'ss_cooldown'
 * in the item serialization methods for the value to be persistent.
 */
public interface SwordSkillCooldownProvider {
	
	/**
	 * @return Check if the item is marked as being on cooldown
	 */
	public abstract Boolean isOnCooldown();

	/**
	 * @return Cooldown end in milliseconds (system time).
	 */
	public abstract Long getCooldownEnd();

	/**
	 * NOTE: By specificiation, this does not update the cooldown on the SwordSkill.
	 * This method primarly exists for SwordSkills to notify their providers they're on cooldown, not the other way around.
	 * @param cooldownEnd Mark the item as being on cooldown until the provided time (system time ms).
	 */
	public abstract void markCooldownEnd(Long cooldownEnd);
}