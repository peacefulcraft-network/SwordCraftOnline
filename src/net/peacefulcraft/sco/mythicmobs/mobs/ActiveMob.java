package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.healthbar.HealthBar;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillManager;
import net.peacefulcraft.sco.swordskills.utilities.IDamage;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * Active instance of Mythicmob
 */
public class ActiveMob extends ModifierUser implements SwordSkillCaster, IDamage {
    
    private long aliveTime = 0L;
    
    private UUID uuid;
        public UUID getUUID() { return uuid; }
        public void setUUID(UUID i) { this.uuid = i; }

    private AbstractEntity entity;

    private MythicMob type;

    private String mobType;

    private AbstractLocation spawnLocation;
        public AbstractLocation getSpawnLocation() { return spawnLocation; }

    private int level;
        public int getLevel() { return this.level; }
        public float getPower() { return (float)(1.0D + (getLevel()-1) * getType().getPerLevelPower()); }

    /**ActiveMobs faction for AI stored in metadata */
    private Optional<String> faction = Optional.empty();
        public boolean hasFaction() {
            if(this.faction.isPresent()) {return true; }
            return this.type.hasFaction();
        }
        public String getFaction() {
            if(this.faction.isPresent()) { return this.faction.get(); }
            return this.type.getFaction();
        }
        public ActiveMob setFaction(String faction) {
            this.faction = Optional.ofNullable(faction);
            if(faction != null) { getLivingEntity().setMetadata("Faction", (MetadataValue)new FixedMetadataValue(SwordCraftOnline.getPluginInstance(), getFaction())); }
            return this;
        }

    /**Player kills of mob */
    private int playerKills = 0;
        public int getPlayerKills() { return this.playerKills; }
        public void incrementPlayerKills() { this.playerKills++; }
        public void importPlayerKills(int pk) { this.playerKills = pk; }

    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();

    /**Stores the mobs owner */
    private Optional<UUID> owner = Optional.empty();
        public Optional<UUID> getOwner() { return this.owner; }

    /**Stores mob boss bar if it has one */
    private Optional<BossBar> bossBar = Optional.empty();

    private boolean dead = false;
        public void setDead() { this.dead = true; }
        public boolean isDead() { return this.dead; }

    private Optional<ActiveMob> mount = Optional.empty();

    /**Global cool down */
    private int gcd = 0;
        public int getGlobalCooldown() { return this.gcd; }
        public void setGlobalCooldown(int gcd) { this.gcd = gcd; }

    private String stance;
        public String getStance() { return stance; }
        public void setStance(String s) { this.stance = s; }

    private String lastSignal = "";

    private int noDamageTicks;
        public int getNoDamageTicks() { return this.noDamageTicks; }

    private AbstractEntity newTarget = null;
        public void setTarget(AbstractEntity ae) { this.newTarget = ae; }

    private double lastDamageSkillAmount = 0.0D;

    private ActiveMob parent;
        public ActiveMob getParent() { return this.parent; }
        public void setParent(ActiveMob am) { this.parent = am; }

    private Collection<AbstractEntity> children = new HashSet<>();
        public void addChild(AbstractEntity e) { this.children.add(e); }

    protected AbstractEntity lastAggroCause;
        public void setLastAggroCause(AbstractEntity aggro) { this.lastAggroCause = aggro; }
        public AbstractEntity getLastAggroCause() { return this.lastAggroCause; }

    private SwordSkillManager swordSkillManager;
        public SwordSkillManager getSwordSkillManager() { return this.swordSkillManager; }

    /**
     * Active Mobs instance of Health Bar. 
     */
    private HealthBar healthBar;
        public HealthBar getHealthBar() { return this.healthBar; }
        public void setHealthBar(HealthBar b) { this.healthBar = b; }

    /**Active mobs criticla hit chance */
    private int criticalChance;
		public int getCriticalChance() { return this.criticalChance; }
		public void setCriticalChance(int num) { this.criticalChance = num; }
		public void addCritical(int num) { this.criticalChance+=num; }

    /**Active mobs damage multiplier on critical hit */
	private double criticalMultiplier;
		public double getCriticalMultiplier() { return this.criticalMultiplier; }
		public void setCriticalMultiplier(double num) { this.criticalMultiplier = num; }

    /**Active mobs chance to parry or evade damage */
    private int parryChance;
		public int getParryChance() { return this.parryChance; }
        public void setParryChance(int num) { this.parryChance = num; }
    
    /**Active mobs damage "dampener" on successful parry */
    private double parryMultiplier;
        public double getParryMultiplier() { return this.parryMultiplier; }
        public void setParryMultiplier(double num) { this.parryMultiplier = num; }

    /**Determines if mob was spawned during nightwave timeframe */
    private boolean duringNightwave;
        public boolean duringNightwave() { return this.duringNightwave; }
    
    public ActiveMob(UUID uuid, AbstractEntity e, MythicMob type, int level, boolean isNightwave) {
        this.uuid = uuid;
        this.entity = e;
        this.type = type;
        this.mobType = type.getInternalName();
        this.spawnLocation = e.getLocation();
        this.level = level;
        this.faction = Optional.ofNullable(type.getFaction());
        this.gcd = 0;
        this.stance = "default";
        this.noDamageTicks = type.getNoDamageTicks();
        if(type.usesBossBar()) {
            this.bossBar = type.getBossBar();
        }

        this.swordSkillManager = new SwordSkillManager(this);

        /**Setting combat modifiers to type instance */
        this.criticalChance = type.getCriticalChance();
        this.criticalMultiplier = type.getCriticalMultiplier();
        this.parryChance = type.getParryChance();
        this.parryMultiplier = type.getParryMultiplier();
        
        this.duringNightwave = isNightwave;
    }

    /**
     * Initializes active mob instance.
     * @param e Abstract entity
     * @param type Mob type read from Mythic Mob
     */
    public ActiveMob(UUID uuid, AbstractEntity e, MythicMob type, int level) {
        this(uuid, e, type, level, false);
    }

    public void tick(int c) {
        if(getEntity() != null && !getEntity().isValid() && !isDead()) {
            setUnloaded();
            return;
        }
        this.aliveTime += c;
        if(this.gcd > 0) { this.gcd -= c; }
        updateBossBar();
        this.children.removeIf(child -> (child.isDead() || !child.isValid()));
    }

    public AbstractEntity getEntity() {
        if(this.entity == null) {
            this.entity = BukkitAdapter.adapt(SwordCraftOnline.getPluginInstance().getServer().getEntity(this.uuid));
        }
        return this.entity;
    }

    public LivingEntity getLivingEntity() {
        if(this.entity.isLiving()) {
            return (LivingEntity)this.entity.getBukkitEntity();
        }
        return null;
    }

    public void setEntity(AbstractEntity e) {
        this.entity = e;
    }

    public MythicMob getType() {
        if(this.type == null) {
            this.type = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(this.mobType);
        }
        return this.type;
    }

    public void setMount(ActiveMob am) {
        this.mount = Optional.of(am);
    }

    public Optional<ActiveMob> getMount() {
        return this.mount;
    }

    public String getDisplayName() {
        String display = this.type.getDisplayName();
        return (display == null) ? null : display;
    }

    public AbstractLocation getLocation() {
        return this.entity.getLocation();
    }

    public Location getBukkitLocation() {
        return this.entity.getBukkitEntity().getLocation();
    }

    public double getDamage() {
        double damage = getType().getBaseDamage();
        if(this.level > 1 && getType().getPerLevelDamage() > 0.0D) {
            damage += getType().getPerLevelDamage() * (this.level -1);
        }
        return damage;
    }

    public double getArmor() {
        double armor = getType().getBaseArmor();
        if(this.level > 1 && getType().getPerLevelArmor() > 0.0D) {
            armor += getType().getPerLevelArmor() * (this.level - 1);
        }
        return armor;
    }

    public void setLevel(int level) {
        this.level = level;
        getType().applyMobOptions(this, level);
    }   
    
    public void unregister() {
        if(this.bossBar.isPresent()) {
            this.bossBar.get().removeAll();
            this.bossBar = Optional.empty();
        }
    }

    public double getHealth() {
        return getEntity().getHealth();
    }

    /**Call to update health on in display name health bar */
    public void updateHealthBar() {
        if(!this.type.usesHealthBar()) { return; }
        if(this.type.usesBossBar()) { return;}
        this.healthBar.updateBar(getHealth());
        getLivingEntity().setCustomName(getType().getDisplayColor(this.level) + "" + getDisplayName() + this.healthBar.getHealthBar());
    }

    /**Call to update players and damage on boss bar */
    public void updateBossBar() {
        if(!this.bossBar.isPresent()) { return; }
        BossBar bar = this.bossBar.get();
        Collection<Player> inRange = SwordCraftOnline.getGameManager().getPlayersInRangeSq(getBukkitLocation(), getType().getBossBarRangeSquared());
        Collection<Player> current = bar.getPlayers();
        double progress = getEntity().getHealth() / getEntity().getMaxHealth();
        String title = this.type.getBossBarTitle();

        bar.setTitle(title);
        bar.setProgress(progress);
        
        for(Player p : inRange) {
            bar.addPlayer(p);
        }
        for(Player p : current) {
            if(!inRange.contains(p)) { bar.removePlayer(p); }
        }
    }

    public void setUnloaded() {
        if(!this.dead) {
            if(this.type.despawns() && !this.type.isPersistent()) {
                this.dead = true;
                unregister();
            } else {
                SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().remove(this.uuid);
            }
        } 
    }

    public void setDespawned() {
        if(!this.dead) {
            this.dead = true;
            //TODO: Handling death events and skills.
        }
        unregister();
    }

    public void setDespawnedSync() {
        if(!this.dead) {
            this.dead = true;
            //TODO: Handling death events and skills.
        }
        unregister();
    }

    /**
     * @return True if mob is herculean
     */
    public boolean isHerculean() {
        return this.type.isHerculean();
    }

    @Override
    /**Returns active mobs attack damage attribute */
    public double getAttackDamage() {
        return getLivingEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
    }

    @Override
    /**
     * Sets active mobs attack damage attribute.
     * @param multiply If True attribute is mulitplied by value. if False attirbute is set to value.
     */
    public void setAttackDamage(double mod, boolean multiply) {
        if(multiply) {
			getLivingEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mod * getAttackDamage()); 
		} else {
			getLivingEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mod); 
		}
    }

    @Override
    /**Returns active mobs movement speed attribute */
    public double getMovementSpeed() {
        return getLivingEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
    }

    @Override
    /**
     * Sets active mobs movement speed attribute.
     * @param multiply If True attribute is mulitplied by value. if False attirbute is set to value.
     */
    public void setMovementSpeed(double mod, boolean multiply) {
        if(multiply) {
			getLivingEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mod * getAttackDamage()); 
		} else {
			getLivingEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mod); 
		}
    }

    @Override
    /**Returns active mobs max health attribute */
    public double getMaxHealth() {
        return getLivingEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    @Override
    /**
     * Sets active mobs max helth attribute.
     * @param multiply If True attribute is mulitplied by value. if False attirbute is set to value.
     */
    public void setMaxHealth(double mod, boolean multiply) {
        if(multiply) {
			getLivingEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mod * getAttackDamage()); 
		} else {
			getLivingEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mod); 
		}
    }

    @Override
    /**Returns active mobs attack speed */
    public double getAttackSpeed() {
        return getLivingEntity().getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue();
    }

    @Override
    /**
     * Sets active mobs attack speed attribute.
     * @param multiply If True attribute is mulitplied by value. if False attirbute is set to value.
     */
    public void setAttackSpeed(double mod, boolean multiply) {
        if(multiply) {
			getLivingEntity().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(mod * getAttackDamage()); 
		} else {
			getLivingEntity().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(mod); 
		}
    }

    @Override
    /**Returns active mobs knockback resistance */
    public double getKnockResist() {
        return getLivingEntity().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue();
    }

    @Override
    /**
     * Sets active mobs knockback resistance attribute.
     * @param multiply If True attribute is mulitplied by value. if False attirbute is set to value.
     */
    public void setKnockResist(double mod, boolean multiply) {
        if(multiply) {
			getLivingEntity().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(mod * getAttackDamage()); 
		} else {
			getLivingEntity().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(mod); 
		}
    }

    @Override
    /**
     * Sets active mobs armor attribute.
     * @param multiply If True attribute is mulitplied by value. if False attirbute is set to value.
     */
    public void setArmor(double mod, boolean multiply) {
        if(multiply) {
			getLivingEntity().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(mod * getAttackDamage()); 
		} else {
			getLivingEntity().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(mod); 
		}
    }

    @Override
    /**Returns active mobs armor toughness attribute */
    public double getArmorToughness() {
        return getLivingEntity().getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getBaseValue();
    }

    @Override
    /**
     * Sets active mobs armor toughness attribute.
     * @param multiply If True attribute is mulitplied by value. if False attirbute is set to value.
     */
    public void setArmorToughness(double mod, boolean multiply) {
        if(multiply) {
			getLivingEntity().getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(mod * getAttackDamage()); 
		} else {
			getLivingEntity().getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(mod); 
		}
    }
}

