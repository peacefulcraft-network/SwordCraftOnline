package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Creature;
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
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * Active instance of Mythicmob
 */
public class ActiveMob extends ModifierUser implements SwordSkillCaster {
    
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

    /**Active mobs target to follow */
    private LivingEntity target = null;
        public LivingEntity getTarget() { return this.target; }

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
        setCombatModifier(CombatModifier.CRITICAL_CHANCE, type.getCriticalChance(), -1);
        setCombatModifier(CombatModifier.CRITICAL_MULTIPLIER, type.getCriticalMultiplier(), -1);
        setCombatModifier(CombatModifier.PARRY_CHANCE, type.getParryChance(), -1);
        setCombatModifier(CombatModifier.PARRY_MULTIPLIER, type.getParryMultiplier(), -1);

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

    @Override
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
        SwordCraftOnline.getPluginInstance().getMobManager().unregisterActiveMob(this.uuid);
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
                this.getLivingEntity().setHealth(0);
                unregister();
            } else {
                SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().remove(this.uuid);
            }
        } 
    }

    /**
     * Unregisters mob and kills it
     */
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

    /**
     * Sets active mobs target and instances attribute
     * @param e LivingEntity to be tracked
     */
    public void setTarget(LivingEntity e) {
        LivingEntity ent = getLivingEntity();
        ((Creature)ent).setTarget(e);

        this.target = e;
    }
}

