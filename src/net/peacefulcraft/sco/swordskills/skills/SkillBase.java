package net.peacefulcraft.sco.swordskills.skills;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.ISkillProvider;

/**
 * Default skill class for active and passive skills
 */
public abstract class SkillBase {
    /**Max level of all skills */
    private final int MAX_LEVEL = 5;

    /**Providing initial id values */
    private static int skillIndex = 0;

    /**Map containing all registered skills */
    private static final Map<Integer, SkillBase> skillsMap = new HashMap<Integer, SkillBase>();

    /**List of registered skills, names, for commands */
    private static final List<String> skillNames = new ArrayList<String>();
    
    //Initialization of skills here
    public static final SkillBase skillBasic = new SkillBasic("skillbasic").addDescriptions(1);

    /**Name for registry */
    private final String name;

    /**IDs used to retrieve skill instance from skillmap */
    private final int id;

    /**Mutable field storing current level for instance of SkillBase */
    protected int level = 0;

    /**Contains descriptions for tooltip display */
    private final List<String> tooltip = new ArrayList<String>();
     
    /**
     * Constructs initial instance of skill and stores it in the skill list
     * @param name should not contain any spaces
     * @param register whether to register the skill, adding skill to skills list
     */
    protected SkillBase(String name, boolean register) {
        this.name = name;
        this.id = skillIndex++;
        if(register) {
            if(skillsMap.containsKey(id)) {
                //TODO:Logger warning conflict
            }
            skillsMap.put(id, this);
            skillNames.add(name);
        }
    }

    /**
     * Copy constructor creates a level zero version of the skill
     * @param skill
     */
    protected SkillBase(SkillBase skill) {
        this.name = skill.name;
        this.id = skill.id;
        this.tooltip.addAll(skill.tooltip);
    }

    /**Returns true if the id is mapped to a skill */
    public static final boolean doesSkilExist(int id) {
        return (id >= 0 && skillsMap.containsKey(id));
    }

    /** Returns a new instance of the skill with id, or null if doesn't exist*/
    public static final SkillBase getNewSkillInstance(int id) {
        return (skillsMap.containsKey(id) ? skillsMap.get(id).newInstance() : null);
    }

    /**Returns the instance of the skill stored in the map if it exists, or null */
    public static final SkillBase getSkill(int id) {
        return (doesSkilExist(id) ? skillsMap.get(id) : null);
    }

    /**Returns an iterable collection of all the skills in the map */
    public static final Collection<SkillBase> getSkills() {
        return Collections.unmodifiableCollection(skillsMap.values());
    }

    /**Returns total number of registered skills */
    public static final int getNumSkills() {
        return skillsMap.size();
    }

    /**Returns all registered skills' names as an array */
    public static final String[] getSkillNames() {
        return skillNames.toArray(new String[skillNames.size()]);
    }

    /**
     * Retrieves skill by its name, or null if not found
     * @param name name of skill to search by
     * @return Skill
     */
    public static final SkillBase getSkillByName(String name) {
        for(SkillBase skill : SkillBase.getSkills()) {
            if(name.equals(skill.getName())) {
                return skill;
            }
        }
        return null;
    }

    /**
     * Returns a leveled skill from an IskillItem 
     */
    public static final SkillBase getSkillFromItem(final ItemStack stack, final ISkillProvider item) {
        return createLeveledSkill(item.getSkillId(stack), item.getSkillLevel(stack));
    }

    /**
     * Returns a leveled skill from an id and level, capped at the 
     * max level for the skill.
     * May return null if id is invalid or less than 1
     */
    public static final SkillBase createLeveledSkill(final int id, final int level) {
        if(doesSkilExist(id) && level > 0) {
            SkillBase skill = getNewSkillInstance(id);
            skill.level =  (level > skill.getMaxLevel() ? skill.getMaxLevel() : level);
            return skill;
        }
        return null;
    }

    /**Returns a new instance of the skill with class type without registering it. */
    public abstract SkillBase newInstance();

    public final int getId() {
        return id;
    }

    public final int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    public String getName() {
        return name;
    }

    protected final String getInfoString(String label, int n) {
        return getName() + ".desc." + label + (n < 0 ? "" : ("." + n));
    }

    /**Adds a single string to skill's tooltip display */
    protected final SkillBase addDescription(String string) {
        tooltip.add(string);
        return this;
    }

    /**Adds all entries in the provided lsit to the skill's tooltip display */
    protected final SkillBase addDescription(List<String> list) {
        for(String s : list) { tooltip.add(s); }
        return this;
    }

    protected final SkillBase addDescriptions(int n) {
        for(int i = 1; i <= n; ++i) {
            tooltip.add(getInfoString("tooltip", i));
        }
        return this;
    }

    /**Returns lsit of strings for tooltip display */
    public final List<String> getDescription() {
        List<String> desc = new ArrayList<String>(tooltip.size());
        for(String s : tooltip) {
            desc.add(s);
        }
        return desc;
    }
    
    /**Called each time a skill's level increases; Used for everything
     * OTHER THAN increasing the skill's level: bonuses, xp, etc.
    */
    protected abstract void levelUp(SCOPlayer p);

    /**Recalculate bonsuses, etc. on player respawn */
    public void validateSkill(SCOPlayer p) {
        levelUp(p);
    }

    /**Returns true if player meets requirements to learn this skill */
    protected boolean canIncreaseLevel(SCOPlayer p, int targetLevel) {
        return ((level + 1) == targetLevel && targetLevel <= getMaxLevel());
    }

    /**
     * Attempts to level up skill to target level, returning true
     * if skill's level increases.
     */
    public final boolean grantSkill(SCOPlayer p, int targetLevel) {
        if(targetLevel <= level || targetLevel > getMaxLevel()) {
            return false;
        }
        int oldLevel = level;
        while(level < targetLevel && canIncreaseLevel(p, level + 1)) {
            ++level;
            levelUp(p);
        }
        return oldLevel < level;
    }

    /**This method should be called on update tick */
    protected void onUpdate(Player p) {}
}