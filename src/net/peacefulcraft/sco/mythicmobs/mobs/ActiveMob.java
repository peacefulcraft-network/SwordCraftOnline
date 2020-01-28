package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBossBar;
import net.peacefulcraft.sco.mythicmobs.skills.SkillCaster;

public class ActiveMob implements SkillCaster {
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

        /**ActiveMobs faction for AI */
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

    private int playerKills = 0;
        public int getPlayerKills() { return this.playerKills; }
        public void incrementPlayerKills() { this.playerKills++; }
        public void importPlayerKills(int pk) { this.playerKills = pk; }

    public HashMap<String, Long> cooldowns = new HashMap<String, Long>();

    private Optional<UUID> owner = Optional.empty();
        public Optional<UUID> getOwner() { return this.owner; }

    private Optional<AbstractBossBar> bossBar = Optional.empty();

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

    public ActiveMob(UUID uuid, AbstractEntity e, MythicMob type, int Level) {
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
        /*
        if(!getType().getDespawns()) {
            remountSpawner();
        }
        */
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
            ((AbstractBossBar)this.bossBar.get()).removeAll();
            this.bossBar = Optional.empty();
        }
    }

    public void updateBossBar() {
        if(!this.bossBar.isPresent()) { return; }
        AbstractBossBar bar = this.bossBar.get();
        Collection<AbstractPlayer> inRange = SwordCraftOnline.getGameManager().getPlayersInRangeSq(getLocation(), getType().getBossBarRangeSquared());
        Collection<AbstractPlayer> current = bar.getPlayers();
        double progress = getEntity().getHealth() / getEntity().getMaxHealth();
        String title = this.type.getBossBarTitle();

        bar.setTitle(title);
        bar.setProgress(progress);
        current.stream().forEach(player -> {
            if(!current.contains(player)) { bar.addPlayer(player); }
        });
        inRange.stream().forEach(player -> {
            if(!inRange.contains(player)) { bar.addPlayer(player); }
        });
    }

    public void setUnloaded() {
        if(!this.dead) {
            if(this.type.getDespawns() && !this.type.isPersistent()) {
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

    @Override
    public void setUsingSkill(boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isUsingSkill() {
        // TODO Auto-generated method stub
        return false;
    }
}

