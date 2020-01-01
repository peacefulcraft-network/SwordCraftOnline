package net.peacefulcraft.sco.swordskills;

import org.bukkit.inventory.ItemStack;

public interface ISkillProvider {
    
    /**
     * Returns ID of the skill this Item provides; should return SkillBase.{skill}.id
     * @param stack allows returning different values based on ItemStack's data
     * @return negative value or invalid id will prevent the Item from granting skill
     */
    public int getSkillId(ItemStack stack);

    /**
     * Returns the skill level given by this item; autp capped at skill's max level
     * @param stack
     * @return value less than 1 will prevent skill from being granted
     */
    public int getSkillLevel(ItemStack stack);

    /**
     * Return true to grant player basic sword skills when necessary to
     * use the primary skill
     */
    public boolean grantsBasicSwordSkill(ItemStack stack);
}