package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBarColor;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBarStyle;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBossBar;
import net.peacefulcraft.sco.mythicmobs.drops.DropTable;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class MythicMob implements Comparable<MythicMob> {
    private String file;

    private MythicConfig config;

    private String internalName;
        public String getInternalName() { return this.internalName; }

    protected String displayName;

    protected MythicEntity mobType;

    protected boolean optionDespawn;

    protected boolean optionPersistent;

    protected boolean optionShowHealthInChat = false;

    protected String strMobType;

    protected String faction = null;
        public String getFaction() { return this.faction; }

    protected double health;

    protected double damage;

    protected double armor;

    protected double attrMovementSpeed;

    protected double attrKnockbackResist;

    protected double attrFollowRange;

    protected double attrAttackSpeed;

    protected double lvlModDamage;

    protected double lvlModHealth;

    protected double lvlModArmor;

    protected double lvlModKBR;

    protected double lvlModPower;

    protected double lvlModSpeed;

    protected double lvlModAttackSpeed;

    protected boolean optionSilent = false;

    protected boolean optionNoAI = false;

    protected boolean optionGlowing = false;

    protected  boolean optionInvincible = false;

    protected boolean optionCollidable = true;

    protected boolean optionNoGravity = true;

    protected boolean optionInteractable = true;

    protected boolean useBossBar = false;

    protected int bossBarRange;

    protected int bossBarRangeSq;

    protected String bossBarTitle;

    protected BarColor bossBarColor;

    protected BarStyle bossBarStyle;

    protected boolean bossBarCreateFog;

    protected boolean bossBarDarkenSky;

    protected boolean bossBarPlayMusic;

    protected Optional<String> mount = Optional.empty();

    protected Optional<String> rider = Optional.empty();

    private List<String> drops;

    private DropTable dropTable;

    private List<String> equipment;

    private DropTable equipmentTable;

    private List<String> damageModifiers;

    private List<String> entityDamageModifiers;

    private List<String> levelmods;

    private List<String> aiGSelectors;

    private List<String> aiTSelectors;

    public List<String> getDrops() {
        return this.drops;
    }

    public DropTable getDropTable() {
        return this.dropTable;
    } 

    public List<String> getEquipment() {
        return this.equipment;
    }

    public DropTable getEquipmentTable() {
        return this.equipmentTable;
    }

    /*
    private Queue<SkillMechanic> mSkills = new LinkedList<>();

    private Queue<SkillMechanic> mSpawnSkills = new LinkedList<>();

    private Queue<SkillMechanic> mDeathSkills = new LinkedList<>();

    private Queue<SkillMechanic> mPlayerDeathSkills = new LinkedList<>();

    private Queue<SkillMechanic> mTimerSkills = new LinkedList<>();

    private Queue<SkillMechanic> mGenericSignalSkills = new LinkedList<>();

    private Queue<SkillMechanic> mSpawnerReadySkills = new LinkedList<>();

    private Map<String, SkillMechanic> mSignalSkills = new HashMap<>();

    private List<String> legacySkills;

    public List<LegacyMythicTimerSkill> legacyTimerSkills;
    */

    public boolean usingTimers = false;

    int size;

    private int noDamageTicks;

    private int maxAttackRange;

    private int maxAttackableRange;

    private int maxThreatDistance;

    public int getNoDamageTicks() {
        return this.noDamageTicks;
    }

    public int getMaxAttackRange() {
        return this.maxAttackRange;
    }

    public int getMaxAttackableRange() {
        return this.maxAttackableRange;
    }

    public int getMaxThreatDistance() {
        return this.maxThreatDistance;
    }

    private boolean alwaysShowName = true;

    private boolean showNameOnDamage = true;

    private boolean useThreatTable;

    private boolean useImmunityTable;

    private boolean useCustomNameplate;

    public boolean isUseCustomNameplate() {
        return this.useCustomNameplate;
    }

    private boolean optionTTFromDamage = true;

    private boolean optionTTDecayUnreachable = true;

    private Boolean repeatAllSkills = Boolean.valueOf(false);

    public boolean getRepeatAllSkills() {
        return this.repeatAllSkills;
    }

    private Boolean preventOtherDrops = Boolean.valueOf(false);

    public boolean getPreventOtherDrops() {
        return this.preventOtherDrops;
    }

    private Boolean preventRandomEquipment = Boolean.valueOf(false);

    public boolean getPreventRandomEquipment() {
        return this.preventRandomEquipment;
    }

    private Boolean preventLeashing = Boolean.valueOf(false);

    public boolean getPreventLeashing() {
        return this.preventLeashing;
    }

    private Boolean preventRename = Boolean.valueOf(true);

    public boolean getPreventRename() {
        return this.preventRename;
    }

    private Boolean preventSlimeSplit = Boolean.valueOf(true);

    public boolean getPreventSlimeSplit() {
        return this.preventSlimeSplit;
    }

    private Boolean preventEndermanTeleport = Boolean.valueOf(true);

    public boolean getPreventEndermanTeleport() {
        return this.preventEndermanTeleport;
    }

    private Boolean preventItemPickup = Boolean.valueOf(true);

    public boolean getPreventItemPickup() {
        return this.preventItemPickup;
    }

    private Boolean preventSilverfishInfection = Boolean.valueOf(true);

    public boolean getPreventSilverfishInfection() {
        return this.preventSilverfishInfection;
    }

    private Boolean preventSunburn = Boolean.valueOf(false);

    public boolean getPreventSunburn() {
        return this.preventSunburn;
    }

    private Boolean preventExploding = Boolean.valueOf(false);

    public boolean getPreventExploding() {
        return this.preventExploding;
    }

    private Boolean preventMobKillDrops = Boolean.valueOf(false);

    public boolean getPreventMobKillDrops() {
        return this.preventMobKillDrops;
    }

    private Boolean passthroughDamage = Boolean.valueOf(false);

    public boolean getPassthroughDamage() {
        return this.passthroughDamage;
    }

    private Boolean digOutOfGround = Boolean.valueOf(false);

    public boolean getDigOutOfGround() {
        return this.digOutOfGround;
    }

    private Boolean usesHealthBar = Boolean.valueOf(false);

    protected double spawnVelocityX;

    public boolean getUsesHealthBar() {
        return this.usesHealthBar;
    }

    protected double spawnVelocityXMax = 0.0D;

    protected double spawnVelocityY;

    public double getSpawnVelocityXMax() {
        return this.spawnVelocityXMax;
    }

    protected double spawnVelocityYMax = 0.0D;

    protected double spawnVelocityZ;

    public double getSpawnVelocityYMax() {
        return this.spawnVelocityYMax;
    }

    protected double spawnVelocityZMax = 0.0D;

    public double getSpawnVelocityZMax() {
        return this.spawnVelocityZMax;
    }

    protected boolean spawnVelocityXRange = false;

    public boolean isSpawnVelocityXRange() {
        return this.spawnVelocityXRange;
    }

    protected boolean spawnVelocityYRange = false;

    public boolean isSpawnVelocityYRange() {
        return this.spawnVelocityYRange;
    }

    protected boolean spawnVelocityZRange = false;

    public boolean isSpawnVelocityZRange() {
        return this.spawnVelocityZRange;
    }

    private boolean fakePlayer = false;

    protected List<String> killMessages;

    public String disguise;

    public boolean isFakePlayer() {
        return this.fakePlayer;
    }

    private final String BANNER = "[MYTHIC MOB]";

    public MythicMob(String file, String internalName, MythicConfig mc) {
        this.config = mc;
        this.file = file;
        this.internalName = internalName;

        System.out.println(BANNER + "Loading MythicMob " + this.internalName); 

        this.strMobType = mc.getString("Type", this.strMobType);
        this.strMobType = mc.getString("MobType", this.strMobType);
        this.strMobType = mc.getString("MobType", this.strMobType);
        if(this.strMobType == null) {
            MythicEntity me = MythicEntity.getMythicEntity(internalName);
            if(me == null) {
                System.out.println(BANNER + "Could not load MythicMob" + this.internalName);
                this.strMobType = "NULL";
                this.mobType = MythicEntity.getMythicEntity("SKELETON");
                this.displayName = "ERROR: MOB TYPE FOR '" + this.internalName + "' IS NOT OPTIONAL";
                return;
            }
            System.out.println(BANNER + "EntityType is vanilla override for " + this.strMobType);
            this.mobType = me;
        } 
        this.mobType = MythicEntity.getMythicEntity(this.strMobType);

        if(this.mobType != null) {
            this.mobType.instantiate(mc);
        } 
        String strDisplayName = mc.getString("Display", (this.displayName == null) ? null : this.displayName.toString());
        strDisplayName = mc.getString("DisplayName", strDisplayName);
        if(strDisplayName != null) {
            this.displayName = strDisplayName;
        }
        
        //Handling extra mob effects
        this.health = mc.getDouble("Health");
        this.damage = mc.getDouble("Damage", -1.0D);
        this.armor = mc.getDouble("Armor");
        this.armor = mc.getDouble("Armour", this.armor);
        this.optionInvincible = mc.getBoolean("Options.Invincible", false);
        this.optionInvincible = mc.getBoolean("Options.Invulnerable", this.optionInvincible);
        this.faction = mc.getString("Faction", null);
        String mount = mc.getString("Mount", null);
        this.mount = Optional.ofNullable(mount);
        String rider = mc.getString("Rider", null);
        rider = mc.getString("Passenger", rider);
        this.rider = Optional.ofNullable(rider);

        //Options handling
        this.optionDespawn = mc.getBoolean("Despawn");
        this.optionDespawn = mc.getBoolean("Options.Despawn", this.optionDespawn);
        this.optionPersistent = mc.getBoolean("Persistent", false);
        this.optionPersistent = mc.getBoolean("Options.Persistent", this.optionPersistent);
        this.attrAttackSpeed = mc.getDouble("Options.AttackSpeed", 0.0D);
        this.attrMovementSpeed = mc.getDouble("Options.MovementSpeed", 0.0D);
        this.attrKnockbackResist = mc.getDouble("Options.KnockbackResistance", 0.0D);
        this.attrFollowRange = mc.getDouble("Options.FollowRange", 0.0D);
        this.attrAttackSpeed = mc.getDouble("Options.AttackSpeed", 0.0D);
        this.optionGlowing = mc.getBoolean("Options.Glowing", false);
        this.optionCollidable = mc.getBoolean("Options.Collidable", true);
        this.optionNoGravity = mc.getBoolean("Options.NoGravity", false);
        this.optionInteractable = mc.getBoolean("Options.Interactable", this.optionInteractable);
        this.optionSilent = mc.getBoolean("Options.Silent", this.optionSilent);
        this.optionNoAI = mc.getBoolean("Options.NoAI", this.optionNoAI);
        this.noDamageTicks = mc.getInteger("Options.NoDamageTicks", 10) * 2;
        
        //Boss Bar Handling
        this.useBossBar = mc.getBoolean("BossBar.Enabled", false);
        this.bossBarTitle = mc.getString("BossBar.Title", (this.displayName == null) ? null : this.displayName.toString());
        this.bossBarRange = mc.getInteger("BossBar.Range", 64);
        this.bossBarRangeSq = (int)Math.pow(this.bossBarRange, 2.0D);
        String bossBarColor = mc.getString("BossBar.Color", "WHITE");
        String bossBarStyle = mc.getString("BossBar.Style", "SOLID");
        try {
            this.bossBarColor = AbstractBarColor.getBarColor(bossBarColor);
        } catch (Exception ex) {
            this.bossBarColor = AbstractBarColor.getBarColor("WHITE");
        }
        try {
            this.bossBarStyle = AbstractBarStyle.getBarStyle(bossBarStyle);
        } catch (Exception ex) {
            this.bossBarStyle = AbstractBarStyle.getBarStyle("SOLID");
        }
        this.bossBarCreateFog = mc.getBoolean("BossBar.CreateFog", false);
        this.bossBarDarkenSky = mc.getBoolean("BossBar.DarkenSky", false);
        this.bossBarPlayMusic = mc.getBoolean("BossBar.PlayMusic", false);
        this.usesHealthBar = Boolean.valueOf(mc.getBoolean("HealthBar.Enabled", false));

        //Mob Skill handling
        //TODO: Load sword skills from naming convetion and apply to mob.

        //Options
        this.useThreatTable = mc.getBoolean("Modules.ThreatTable", false);
        this.useImmunityTable = mc.getBoolean("Modules.ImmunityTable", false);
        this.useCustomNameplate = mc.getBoolean("Nameplate.Enabled", false);
        this.useThreatTable = mc.getBoolean("Options.UseThreatTable", this.useThreatTable);
        this.optionTTFromDamage = mc.getBoolean("ThreatTable.UseDamageTaken", true) ;
        this.optionTTDecayUnreachable = mc.getBoolean("ThreatTable.DecayUnreachable");
        this.maxAttackRange = mc.getInteger("Options.MaxAttackRange", 64);
        this.maxAttackableRange = mc.getInteger("Options.MaxCombatRange", 256);
        this.maxAttackableRange = mc.getInteger("Options.MaxAttackableRange", this.maxAttackableRange);
        this.maxThreatDistance = mc.getInteger("Options.MaxThreatDistance", 40);
        this.alwaysShowName = mc.getBoolean("Options.AlwaysShowName", false);
        this.showNameOnDamage = mc.getBoolean("Options.ShowNameOnDamaged", false);
        this.repeatAllSkills = Boolean.valueOf(mc.getBoolean("Options.RepeatAllSkills", false));
        this.preventOtherDrops = Boolean.valueOf(mc.getBoolean("Options.PreventOtherDrops", true));
        this.preventRandomEquipment = Boolean.valueOf(mc.getBoolean("Options.PreventRandomEquipment", false));
        this.preventLeashing = Boolean.valueOf(mc.getBoolean("Options.PreventLeashing", true));
        this.preventRename = Boolean.valueOf(mc.getBoolean("Options.PreventRenaming", true));
        this.preventSunburn = Boolean.valueOf(mc.getBoolean("Options.PreventSunburn", false));
        this.preventEndermanTeleport = Boolean.valueOf(mc.getBoolean("Options.PreventTeleport", false));
        this.preventEndermanTeleport = Boolean.valueOf(mc.getBoolean("Options.PreventTeleporting", this.preventEndermanTeleport.booleanValue()));
        this.preventItemPickup = Boolean.valueOf(mc.getBoolean("Options.PreventItemPickup", false));
        this.preventMobKillDrops = Boolean.valueOf(mc.getBoolean("Options.PreventMobKillDrops", false));
        this.passthroughDamage = Boolean.valueOf(mc.getBoolean("Options.PassthroughDamage", false));
        this.aiGSelectors = mc.getStringList("AIGoalSelectors");
        this.aiTSelectors = mc.getStringList("AITargetSelectors");
        
        //Drops, droptables, killmessages
        this.drops = mc.getStringList("Drops");
        this.dropTable = new DropTable(this.file, "Mob:" + this.internalName, this.drops);
        this.equipment = mc.getStringList("Equipment");
        List<String> killMessages = mc.getStringList("KillMessages");
        if(killMessages != null && killMessages.size() > 0) {
            //Log loading kill messages
            if(this.killMessages == null) {
                this.killMessages = new ArrayList<>();
            }
            killMessages.forEach(message -> this.killMessages.add(message));
        }

        //Level modifiers
        this.lvlModDamage = mc.getDouble("LevelModifiers.Damage", -1.0D);
        this.lvlModHealth = mc.getDouble("LevelModifiers.Health", -1.0D);
        this.lvlModKBR = mc.getDouble("LevelModifiers.KnockbackResistance", -1.0D);
        this.lvlModPower = mc.getDouble("LevelModifiers.Power", -1.0D);
        this.lvlModArmor = mc.getDouble("LevelModifiers.Armor", -1.0D);
        this.lvlModSpeed = mc.getDouble("LevelModifiers.MovementSpeed", -1.0D);
        this.lvlModAttackSpeed = mc.getDouble("LevelModifiers.AttackSpeed", -1.0D);
        try {
            if(this.lvlModDamage < 0.0D) {
                //TODO: Adjust to be edited from main config?
                this.lvlModDamage = this.damage * 2.0D;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
            if(this.lvlModHealth < 0.0D) {
                this.lvlModHealth = this.health * 2.0D;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
            if(this.lvlModArmor < 0.0D) {
                this.lvlModArmor = this.armor * 2.0D;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
            if(this.lvlModKBR < 0.0D) {
                this.lvlModKBR = 1.0D;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        //Spawning factors
        this.digOutOfGround = Boolean.valueOf(mc.getBoolean("Options.DigOutOfGround", false));
        String strSpawnVelocityX = mc.getString("SpawnModifiers.VelocityX", "0");
        String strSpawnVelocityY = mc.getString("SpawnModifiers.VelocityY", "0");
        String strSpawnVelocityZ = mc.getString("SpawnModifiers.VelocityZ", "0");
        if(strSpawnVelocityX.contains("to")) {
            String[] split = strSpawnVelocityX.split("to");
            try {
                this.spawnVelocityX = Double.valueOf(split[0]).doubleValue();
                this.spawnVelocityXMax = Double.valueOf(split[1]).doubleValue();
                this.spawnVelocityXRange = true;
            } catch(Exception ex) {
                this.spawnVelocityX = 0.0D;
                //TODO: Log invalid velocity on load
            }
        } else {
            try {
                this.spawnVelocityX = Double.valueOf(strSpawnVelocityX).doubleValue();
            } catch (Exception ex) {
                this.spawnVelocityX = 0.0D;
                //TODO: log invalid velocity on load
            }
        }
        if(strSpawnVelocityY.contains("to")) {
            String[] split = strSpawnVelocityY.split("to");
            try {
                this.spawnVelocityY = Double.valueOf(split[0]).doubleValue();
                this.spawnVelocityYMax = Double.valueOf(split[1]).doubleValue();
                this.spawnVelocityYRange = true;
            } catch(Exception ex) {
                this.spawnVelocityY = 0.0D;
                //TODO: Log invalid velocity on load
            }
        } else {
            try {
                this.spawnVelocityY = Double.valueOf(strSpawnVelocityY).doubleValue();
            } catch(Exception ex) {
                this.spawnVelocityY = 0.0D;
                //TODO: Log invalid velocity on load
            }
        }
        if(strSpawnVelocityZ.contains("to")) {
            String[] split = strSpawnVelocityZ.split("to");
            try {
                this.spawnVelocityZ = Double.valueOf(split[0]).doubleValue();
                this.spawnVelocityZMax = Double.valueOf(split[1]).doubleValue();
                this.spawnVelocityZRange = true;
            } catch(Exception ex) {
                this.spawnVelocityZ = 0.0D;
                //TODO: Log invalid velocity on load
            }
        } else {
            try {
                this.spawnVelocityZ = Double.valueOf(strSpawnVelocityZ).doubleValue();
            }  catch (Exception ex) {
                this.spawnVelocityZ = 0.0D;
                //TODO: log invalid velocity on load
            }
        }

        //Disguise handling
        this.disguise = mc.getString("Options.Disguise", this.disguise);
        this.disguise = mc.getString("Disguide.Type", this.disguise);
        if(this.disguise != null && this.disguise.toUpperCase().contains("PLAUER")) {
            this.fakePlayer = true;
        }
    }

    public MythicConfig getConfig() {
        return this.config;
    }

    /**Applies mob options and returns ActiveMob */
    public ActiveMob spawn(AbstractLocation loc, int level) {
        Entity e;
        if(this.mobType != null) {
            System.out.println("[MOB DEBUG] Point 1 Hit.");
            e = this.mobType.spawn(this, BukkitAdapter.adapt(loc));
        } else {
            System.out.println("[MOB DEBUG] Point 2 Hit.");
            return null;
        }
        System.out.println("[MOB DEBUG] Point 3 Hit.");
        ActiveMob am = new ActiveMob(e.getUniqueId(), BukkitAdapter.adapt(e), this, level);
        SwordCraftOnline.getPluginInstance().getMobManager().registerActiveMob(am);
        am = applyMobOptions(am, level);
        am = applyMobVolatileOptions(am);
        am = applySpawnModifiers(am);
        return am;
    }

    public ActiveMob applyMobOptions(ActiveMob am, int level) {
        Entity e = am.getEntity().getBukkitEntity();
        if(am.getEntity().isLiving()) {
            LivingEntity asLiving = (LivingEntity)e;
            if(!this.optionDespawn) { 
                asLiving.setRemoveWhenFarAway(false);
            }
            if(this.alwaysShowName) {
                asLiving.setCustomNameVisible(true);
            }
            if(this.preventItemPickup) {
                asLiving.setCanPickupItems(false);
            }
            if(this.damage > 0.0D && asLiving.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                //asLiving.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(am.getDamage());
            }
            if(this.attrMovementSpeed != 0.0D) {
                asLiving.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getMovementSpeed(level));
            }
            if(this.attrAttackSpeed != 0.0D && asLiving.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null) {
                asLiving.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(getAttackSpeed(level));
            }
            if(this.attrFollowRange != 0.0D) {
                asLiving.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(this.attrFollowRange);
            }
            if(this.attrKnockbackResist != 0.0D) {
                asLiving.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(getKnockbackResistance(level));
            }
            if(!this.optionCollidable) {
                asLiving.setCollidable(false);
            }
            
            if(this.health > 0.0D) {
                double health = this.health;
                if(level > 1 && this.lvlModHealth > 0.0D) {
                    health += this.lvlModHealth * (level - 1);
                }
                try {
                    double fhealth = Math.ceil(health);
                    asLiving.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(fhealth);
                    asLiving.setHealth(fhealth);
                } catch(IllegalArgumentException ex) {
                    ex.printStackTrace();
                }
            }

            asLiving.setMaximumNoDamageTicks(this.noDamageTicks);
            if(e instanceof Creature) {
                if(this.preventRandomEquipment.booleanValue()) {
                    EntityEquipment ee = asLiving.getEquipment();
                    ee.setHelmet(new ItemStack(Material.AIR));
                    ee.setChestplate(new ItemStack(Material.AIR));
                    ee.setLeggings(new ItemStack(Material.AIR));
                    ee.setBoots(new ItemStack(Material.AIR));
                    ee.setItemInMainHand(new ItemStack(Material.AIR));
                }
            }
            if(getDisplayName() != null) {
                asLiving.setCustomName(am.getDisplayName());
            }
        }

        if(this.optionInvincible) { e.setInvulnerable(true); }
        if(this.optionGlowing) { e.setGlowing(true); }
        if(this.optionNoGravity) { e.setGravity(true); }
        if(this.optionSilent) { e.setSilent(true); }

        am.getEntity().setMetadata("mobname", getInternalName());
        am.getEntity().setMetadata("mythicmob", "true");

        if(this.mount.isPresent()) {
            if(SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(this.mount.get()) != null) {
                if(e.getVehicle() != null) {
                    e.getVehicle().remove();
                }
                AbstractEntity mount = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(this.mount.get()).spawn(am.getLocation(), level).getEntity();
                mount.setPassenger(e);
                ActiveMob mountInstance = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMobInstance(mount);
                am.setMount(mountInstance);
                mountInstance.setParent(am);
            }
        }
        if(this.rider.isPresent()) {
            if(SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(this.rider.get()) != null) {
                AbstractEntity rider = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(this.rider.get()).spawn(am.getLocation(), level).getEntity();
                rider.setPassenger(e);
                ActiveMob riderInstance = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMobInstance(rider);
                riderInstance.setMount(am);
                riderInstance.setParent(am);
            }
        }
        return am;
    }

    public ActiveMob applyMobVolatileOptions(ActiveMob am) {
        Entity e = am.getEntity().getBukkitEntity();
        if(hasFaction()) {
            e.setMetadata("Faction", (MetadataValue)new FixedMetadataValue(SwordCraftOnline.getPluginInstance(), getFaction()));
        }
        if(am.getEntity().isCreature()) {
            if(this.optionNoAI) {
                ((LivingEntity)BukkitAdapter.adapt(am.getEntity())).setAI(false);
            } 
        }
        return am;
    }

    public ActiveMob applySpawnModifiers(ActiveMob am) {
        Entity e = am.getEntity().getBukkitEntity();
        Vector v = e.getVelocity();
        if(this.spawnVelocityXRange) {
            double vl = SwordCraftOnline.r.nextDouble() * (this.spawnVelocityXMax - this.spawnVelocityX) + this.spawnVelocityX;
            v.setX(vl);
        } else {
            v.setX(this.spawnVelocityX);
        }
        if(this.spawnVelocityYRange) {
            double vl = SwordCraftOnline.r.nextDouble() * (this.spawnVelocityYMax - this.spawnVelocityY) + this.spawnVelocityY;
            v.setY(vl);
        } else {
            v.setY(this.spawnVelocityY);
        }
        if(this.spawnVelocityZRange) {
            double vl = SwordCraftOnline.r.nextDouble() * (this.spawnVelocityZMax - this.spawnVelocityZ) + this.spawnVelocityZ;
            v.setZ(vl);
        } else {
            v.setZ(this.spawnVelocityZ);
        }
        e.setVelocity(v);
        return am;
    }

    public boolean usesBossBar() {
        return this.useBossBar;
    }

    public Optional<AbstractBossBar> getBossBar() {
        if(!this.useBossBar) {
            return Optional.empty();
        }
        AbstractBossBar bar = SwordCraftOnline.getPluginInstance().server().createBossBar(" ", this.bossBarColor, this.bossBarStyle);
        bar.setProgress(1.0D);
        if(this.bossBarCreateFog) { bar.setCreateFog(true); }
        if(this.bossBarDarkenSky) { bar.setDarkenSky(true); }
        if(this.bossBarPlayMusic) { bar.setPlayBossMusic(true); }
        return Optional.of(bar);
    }

    public String getBossBarTitle() {
        return this.bossBarTitle;
    }

    public int getBossBarRangeSquared() {
        return this.bossBarRangeSq;
    }

    public String getFile() {
        return this.file;
    } 

    public String getDisplayName() {
        return this.displayName;
    }

    public String getEntityType() {
        return this.strMobType;
    }

    public MythicEntity getMythicEntity() {
        return this.mobType;
    }

    public boolean getDespawns() {
        return this.optionDespawn;
    }

    public boolean isPersistent() {
        return this.optionPersistent;
    }

    public double getBaseHealth() {
        return this.health;
    }

    /**Edits config for base health */
    public void setBaseHealth() {
        this.config.set("Health", Double.valueOf(health));
        this.config.save();
    }

    public double getPerLevelHealth() {
        return this.lvlModHealth;
    }

    public double getBaseDamage() {
        return this.damage;
    }

    public double getPerLevelDamage() {
        return this.lvlModDamage;
    }

    public double getPerLevelPower() {
        return this.lvlModPower;
    }

    public double getBaseArmor() {
        return this.armor;
    }

    public double getPerLevelArmor() {
        return this.lvlModArmor;
    }

    public double getMovementSpeed(int level) {
        double attr = this.attrMovementSpeed;
        if(level > 1 && this.lvlModSpeed > 0.0D) {
            attr += this.lvlModSpeed * (level - 1);
        }
        return attr;
    }

    public double getKnockbackResistance(int level) {
        double attr = this.attrKnockbackResist;
        if (level > 1 && this.lvlModKBR > 0.0D) {
            attr += this.lvlModKBR * (level - 1);
        } 
        return attr;
    }

    public double getAttackSpeed(int level) {
        double attr = this.attrAttackSpeed;
        if (level > 1 && this.lvlModAttackSpeed > 0.0D) {
            attr += this.lvlModAttackSpeed * (level - 1); 
        }
        return attr;
    }

    public boolean hasFaction() {
        return (this.faction != null);
    }
      
    public double getHealth() {
        return this.health;
    }
      
    public boolean getIsInvincible() {
        return this.optionInvincible;
    }
      
    public boolean usesThreatTable() {
        return this.useThreatTable;
    }
      
    public boolean usesImmunityTable() {
        return this.useImmunityTable;
    }
      
    public boolean getThreatTableUseDamageTaken() {
        return this.optionTTFromDamage;
    }
      
    public boolean getThreatTableDecaysUnreachable() {
        return this.optionTTDecayUnreachable;
    }
      
    public List<String> getDamageModifiers() {
        return this.damageModifiers;
    }
      
    public List<String> getEntityDamageModifiers() {
        return this.entityDamageModifiers;
    }
      
    public List<String> getLevelModifiers() {
        return this.levelmods;
    }
      
    public List<String> getAIGoalSelectors() {
        return this.aiGSelectors;
    }
      
    public List<String> getAITargetSelectors() {
        return this.aiTSelectors;
    }

    public boolean hasKillMessges() {
        return (this.killMessages != null && this.killMessages.size() > 0);
    }

    public String getKillMessage() {
        if(!hasKillMessges()) { return null; }
        return this.killMessages.get(SwordCraftOnline.r.nextInt(this.killMessages.size()));
    }

    public double getSpawnVelocityX() {
        return this.spawnVelocityX;
    }
      
    public double getSpawnVelocityY() {
        return this.spawnVelocityY;
    }
      
    public double getSpawnVelocityZ() {
        return this.spawnVelocityZ;
    }
      
    public boolean getIsInteractable() {
        return this.optionInteractable;
    }

    public boolean equals(Object o) {
        if(o instanceof MythicMob) {
            return ((MythicMob)o).getInternalName().equals(this.internalName);
        }
        return false;
    }

    public String toString() {
        return "MythicMob{" + this.internalName + "}";
    }

    public int compareTo(MythicMob m) {
        return this.internalName.compareTo(m.getInternalName());
    }

    public boolean getShowHealthInChat() {
        return this.optionShowHealthInChat;
    }

    public boolean getShowNameOnDamaged() {
        return this.showNameOnDamage;
    }
}