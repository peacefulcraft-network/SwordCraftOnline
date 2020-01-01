package net.peacefulcraft.sco.swordskills;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.skills.SkillBase;

public class ItemSkillProvider implements ISkillProvider {

    private final Material material;
    
    private float weaponDamage;

    private final String skillName;

    private final int level;

    private SkillBase skill;

    private final boolean grantsBasicSkill;

    /**
     * Shortcut sets ISkillItem to always grant basic skill if needed
     */
    public ItemSkillProvider(Material material, SkillBase skill, int level) {
        this(material, skill, level, true);
    }

    /**
     * Standard sword weapon with max stack size of 1; 
     */
    public ItemSkillProvider(Material material, SkillBase skill, int level, boolean grantsBasicSkill) {
        super();
        this.material = material;
        this.weaponDamage = 4.0F + getItemDamageValue(material);
        this.skillName = skill.getName();
        this.level = level;
        this.grantsBasicSkill = grantsBasicSkill;
    }

    public float getItemDamageValue(Material mat) {
        float damageValue = 0;
        if(mat != null) {
            if(mat == Material.AIR) {
                damageValue = 0;
            } else if(mat == Material.WOODEN_SWORD) {
                damageValue = 5;
            } else if(mat == Material.STONE_SWORD) {
                damageValue = 6;
            } else if(mat == Material.IRON_SWORD) {
                damageValue = 7;
            } else if(mat == Material.DIAMOND_SWORD) {
                damageValue = 8;
            }
        }
        return damageValue;
    }

    /**
     * DO NOT USE as a player's active instance
     */
    protected SkillBase getSkill(ItemStack stack) {
        if(skill == null) {
            skill = SkillBase.getSkillFromItem(stack, this);
        }
        return skill;
    }

    @Override
    public int getSkillId(ItemStack stack) {
        SkillBase skill = SkillBase.getSkillByName(this.skillName);
        return (skill == null ? -1 : skill.getId());
    }

    @Override
    public int getSkillLevel(ItemStack stack) {
        return level;
    }

    @Override
    public boolean grantsBasicSwordSkill(ItemStack stack) {
        return grantsBasicSkill;
    }
    
}