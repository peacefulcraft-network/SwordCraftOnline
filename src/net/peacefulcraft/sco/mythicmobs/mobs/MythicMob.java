package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.peacefulcraft.log.Banners;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.drops.DropTable;
import net.peacefulcraft.sco.mythicmobs.healthbar.HealthBar;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.swordskills.utilities.Generator;
import net.peacefulcraft.sco.swordskills.utilities.Modifier;

/**
 * Holds custom mob data read from YML in mob mananger.
 */
public class MythicMob implements Comparable<MythicMob> {
    /**File name */
    private String file;
        /**Returns String of mob file name */
        public String getFile() { return this.file; }

    /**Generic config for YML */
    private MythicConfig config;
        /**Returns generic config of mob */
        public MythicConfig getConfig() { return this.config; }

    /**Internal name of mob provided from config */
    private String internalName;
        /**Returns internal mob name provided by config */
        public String getInternalName() { return this.internalName; }

    /**Display name of mob */
    protected String displayName;
        /**Returns display name of mob */
        public String getDisplayName() { return this.displayName; }

    /**Type of vanilla mob */
    protected MythicEntity mobType;
        /**Returns mobtype of mob */
        public MythicEntity getMobType() { return this.mobType; }

    /**Type of vanilla mob in string form */
    protected String strMobType;
        /**Returns string representation of mobtype */
        public String getStrMobType() { return this.strMobType; }

    /**Option: Despawn mob at range */
    protected boolean optionDespawn;
        /**Returns Option: Despawn */
        public boolean despawns() { return this.optionDespawn; }

    /**Option: Mob is persistant. I.e. no despawn */
    protected boolean optionPersistent;
        /**Returns Option: Persistant */
        public boolean isPersistent() { return this.optionPersistent; }

    /**Option: Shows health in chat. Defaulted to false */
    protected boolean optionShowHealthInChat = false;
        /**Returns option showHealthInChat */
        public boolean doesShowHealthInChat() { return this.optionShowHealthInChat; }

    /**Determines what mob attacks */
    protected String faction = null;
        /**Returns faction of MM */
        public String getFaction() { return this.faction; }

    /**Set health of mob */
    protected double health;

    /**Set damage of mob */
    protected double damage;

    /**Set armor of mob */
    protected double armor;

    /**Mobs attribute movement speed */
    protected double attrMovementSpeed;

    /**Mobs attribute knockback resistance */
    protected double attrKnockbackResist;

    /**Mobs attribute follow range */
    protected double attrFollowRange;

    /**Mobs attribute attack speed */
    protected double attrAttackSpeed;

    /**Damage scaling with level */
    protected double lvlModDamage;

    /**Health scaling with level */
    protected double lvlModHealth;

    /**Armor scaling with level */
    protected double lvlModArmor;

    protected double lvlModKBR;

    /**Power scaling with level */
    protected double lvlModPower;

    /**Speed scaling with level */
    protected double lvlModSpeed;

    /**Attack speed scaling with level */
    protected double lvlModAttackSpeed;

    /**Option: Makes mob silent. Default false */
    protected boolean optionSilent = false;
        /**Returns mob silent */
        public boolean isSilent() { return this.isSilent(); }

    /**Option: Makes mob do nothing. Default false */
    protected boolean optionNoAI = false;
        /**Returns mob NoAI */
        public boolean hasNoAI() { return this.optionNoAI; }

    /**Option: Makes mob glow. Default false */
    protected boolean optionGlowing = false;
        /**Returns option glowing */
        public boolean isGlowing() { return this.optionGlowing; }

    /**Option: Makes mob invincible. Default false */
    protected  boolean optionInvincible = false;
        /**Returns option invincible */
        public boolean isInvincible() { return this.optionInvincible; }

    /**Option: Makes mob intangible. Default true */
    protected boolean optionCollidable = true;
        /**Returns option collidable */
        public boolean isCollidable() { return this.optionCollidable; }

    /**Option: Makes mob hae gravity. Default true */
    protected boolean optionNoGravity = true;
        /**Returns Option no gravity */
        public boolean hasNoGravity() { return this.optionNoGravity; }

    /**Option: Makes mob interactable. Default true */
    protected boolean optionInteractable = true;
        /**Returns option interactable */
        public boolean isInteractable() { return this.optionInteractable; }

    /**Uses boss bar provided in YML. Default false */
    protected boolean useBossBar = false;

    /**Range of boss bar */
    protected int bossBarRange;

    /**Range squared of boss bar */
    protected int bossBarRangeSq;

    /**Title of boss bar */
    protected String bossBarTitle;

    /**Color of boss bar */
    protected BarColor bossBarColor;

    /**Style of boss bar */
    protected BarStyle bossBarStyle;

    /**Create boss bar fog */
    protected boolean bossBarCreateFog;

    /**Darkens sky with boss bar */
    protected boolean bossBarDarkenSky;

    /**Play music with boss bar */
    protected boolean bossBarPlayMusic;

    /**Optional: Mob is mount */
    protected Optional<String> mount = Optional.empty();

    /**Optional: Mob is rider */
    protected Optional<String> rider = Optional.empty();

    /**Drops of mob read from YML */
    private List<String> drops;

    /**Returns mob drops */
    public List<String> getDrops() { return this.drops; }

    /**Drops of mob read from YML */
    private DropTable dropTable;

    /**Returns mob drop table */
    public DropTable getDropTable() { return this.dropTable; }

    /**Mobs equipment. */
    private List<String> equipment;
        /**Returns mob equipment */
        public List<String> getEquipment() { return this.equipment; }

    /**Droptable for mob equipment */
    private DropTable equipmentTable;
            /**Returns mob equipment droptable */
        public DropTable getEquipmentTable() { return this.equipmentTable; }

    /**
     * Mob damage modifiers.
     */
    private List<Modifier> damageModifiers;
        /**Returns mobs damage modifiers */
        public List<Modifier> getDamageModifiers() { return this.damageModifiers; }

    private List<String> levelmods;

    private List<String> aiGSelectors;

    private List<String> aiTSelectors;

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

    /**Size of mob. I.e. Large slimes */
    int size;

    private int noDamageTicks;
        public int getNoDamageTicks() { return this.noDamageTicks; }

    private int maxAttackRange;
        public int getMaxAttackRange() { return this.maxAttackRange; }

    private int maxAttackableRange;
        public int getMaxAttackableRange() { return this.maxAttackableRange; }

    /**Determines to show mob name. Default true */
    private boolean alwaysShowName = true;

    /**Determines show name on damage. Default true */
    private boolean showNameOnDamage = true;

    /*
    private Boolean repeatAllSkills = Boolean.valueOf(false);

    public boolean getRepeatAllSkills() {
        return this.repeatAllSkills;
    }*/

    /**Prevents vanilla drops. Default false */
    private Boolean preventOtherDrops = Boolean.valueOf(false);
        /**Returns preventOtherDrops. Defaulted false */
        public boolean getPreventOtherDrops() { return this.preventOtherDrops; }

    /**Prevents random equipment */
    private Boolean preventRandomEquipment = Boolean.valueOf(false);
        /**Returns preventRandomEquipment. Default false */
        public boolean getPreventRandomEquipment() { return this.preventRandomEquipment; }

    /**Prevents leashing of mob */
    private Boolean preventLeashing = Boolean.valueOf(false);
        /**Returns preventleashing. Default false */
        public boolean getPreventLeashing() { return this.preventLeashing; }

    /**Prevents rename of mob */
    private Boolean preventRename = Boolean.valueOf(true);
        /**Returns preventRename. Default true */
        public boolean getPreventRename() { return this.preventRename; }

    /**Prevents slimes splitting. Default true */
    private Boolean preventSlimeSplit = Boolean.valueOf(true);
        /**Returns preventslimesplitting */
        public boolean getPreventSlimeSplit() { return this.preventSlimeSplit; }

    /**Prevents enderman teleporting. Default true */
    private Boolean preventEndermanTeleport = Boolean.valueOf(true);
        /**returns preventEndermanTeleport */
        public boolean getPreventEndermanTeleport() { return this.preventEndermanTeleport; }

    /**Prevents item pickup of mob. Default true */
    private Boolean preventItemPickup = Boolean.valueOf(true);
        /**Returns preventitempickup */
        public boolean getPreventItemPickup() { return this.preventItemPickup; }

    /**Prevents silverfish from spreading into blocks */
    private Boolean preventSilverfishInfection = Boolean.valueOf(true);
        /**Returns preventSilverfishInfection */
        public boolean getPreventSilverfishInfection() { return this.preventSilverfishInfection; }

    /**Prevents sunburn of mob. Default false */
    private Boolean preventSunburn = Boolean.valueOf(false);
        /**Returns preventsunburn */
        public boolean getPreventSunburn() { return this.preventSunburn; }

    /**Prevents exploding of creeper. Default false */
    private Boolean preventExploding = Boolean.valueOf(false);
        /**Returns preventExploding */
        public boolean getPreventExploding() { return this.preventExploding; }

    /**Prevents mobs from droping items on death */
    private Boolean preventMobKillDrops = Boolean.valueOf(false);
        /**Returns PreventMobKillDrops */
        public boolean getPreventMobKillDrops() { return this.preventMobKillDrops; }

    private Boolean passthroughDamage = Boolean.valueOf(false);
        public boolean getPassthroughDamage() { return this.passthroughDamage; }

    private Boolean digOutOfGround = Boolean.valueOf(false);
        public boolean getDigOutOfGround() { return this.digOutOfGround; }
    
    protected double spawnVelocityX;

    protected double spawnVelocityXMax = 0.0D;
        public double getSpawnVelocityXMax() { return this.spawnVelocityXMax; }

    protected double spawnVelocityY;

    protected double spawnVelocityYMax = 0.0D;
        public double getSpawnVelocityYMax() { return this.spawnVelocityYMax; }

    protected double spawnVelocityZ;

    protected double spawnVelocityZMax = 0.0D;
        public double getSpawnVelocityZMax() { return this.spawnVelocityZMax; }

    protected boolean spawnVelocityXRange = false;
        public boolean isSpawnVelocityXRange() { return this.spawnVelocityXRange; }

    protected boolean spawnVelocityYRange = false;
        public boolean isSpawnVelocityYRange() { return this.spawnVelocityYRange; }

    protected boolean spawnVelocityZRange = false;
        public boolean isSpawnVelocityZRange() { return this.spawnVelocityZRange; }

    protected String villagerType;
        public String getVillagerType() { return this.villagerType; }

    /**Is mob disguised as player */
    private boolean fakePlayer = false;
        /**Returns if mob is fake player */
        public boolean isFakePlayer() { return this.fakePlayer; }

    /**Creeper explosion radius */
    private int explosionRadius;
        /**Returns mobs explosion radius */
        public int getExplosionRadius() { return this.explosionRadius; }

    /**Is creeper charged creeper */
    private boolean isPowered = Boolean.valueOf(false);
        public boolean isPowered() { return this.isPowered; }

    /**
     * Creepers fuse ticks. The maximum amount of time in which
     * a creeper is allowed to be in primed state before exploding
     */
    private int maxFuseTicks;
        public int getMaxFuseTicks() { return this.maxFuseTicks; }

    /**Horses equipped armor */
    private String horseArmor;
        /**Returns the horses armor */
        public String getHorseArmor() { return this.horseArmor; }

    /**If true equips chest */
    private boolean carryingChest;
        public boolean isCarryingChest() { return this.carryingChest; }

    /**Determines horse color */
    private String horseColor;
        public String getHorseColor() { return this.horseColor; }

    /**If true mob is adult. False mob is baby */
    private boolean isAdult;
        public boolean isAdult() { return this.isAdult; }

    /**Determines horse style */
    private String horseStyle;
        public String getHorseStyle() { return this.horseStyle; }

    /**Determines if horse is tamed */
    private boolean isTamed;
        public boolean isTamed() { return this.isTamed; }

    /**Determins is horse is saddled */
    private boolean isSaddled;
        public boolean isSaddled() { return this.isSaddled; }

    /**Determines if iron golem is player created. */
    private boolean playerCreated;
        public boolean isPlayerCreated() { return this.playerCreated; }

    /**Determines ocelot type */
    private String cat;
        public String getCatType() { return this.cat; }

    /**Determines rabbit type */
    private String rabbit;
        public String getRabbitType() { return this.rabbit; }

    /**Determines if sheep is sheared or not */
    private boolean isSheared;
        public boolean isSheared() { return this.isSheared; }

    /**Determines if snowman has pumpkin */
    private boolean isDerp;
        public boolean isDerp() { return this.isDerp; }

    /**Determines if snowman leaves snow trail */
    private boolean preventSnowFormation;
        public boolean getPreventSnowFormation() { return this.preventSnowFormation; }

    /**Determin fish pattern design */
    private String tropicalFishPattern;
        public String getFishPattern() { return this.tropicalFishPattern; }

    /**Determin fish body color */
    private String tropicalFishBodyColor;
        public String getFishBodyColor() { return this.tropicalFishBodyColor; }

    /**Determin fish pattern color */
    private String tropicalFishPatternColor;
        public String getFishPatternColor() { return this.tropicalFishPatternColor; }

    /**Locks age of ageable mob */
    private boolean ageLock;
        public boolean getAgeLock() { return this.ageLock; }

    /**Determines sheep or wolf color */
    private String color;
        public String getColor() { return this.color; }

    /**Determines if wolf or zombie pigman is angry */
    private boolean angry;
        public boolean isAngry() { return this.angry; }

    /**Determines slime size */
    private int slimeSize;
        public int getSlimeSize() { return this.slimeSize; }

    /**Determines what potion effects are equipped to mob. */
    private List<String> potionEffects;
        public List<String> getPotionEffects() { return this.potionEffects; }

    private boolean isShowingBottom;
        public boolean isShowingBottom() { return this.isShowingBottom; }

    protected List<String> killMessages;

    public String disguise;

    /**Stores factions to target */
    protected List<String> factionTargets;
        /**Returns list of faction targets */
        public List<String> getFactionTargets() { return this.factionTargets; }
        /**Returns if mob has faction targets */
        public boolean hasFactionTargets() { return this.factionTargets.size() > 0; }

    /**Stores skill information */
    protected List<String> skills;
        /**Returns list of mobs skills */
        public List<String> getSkills() { return this.skills; }

    /**Determines if mob has displayed health bar */
    private Boolean usesHealthBar;
        public Boolean usesHealthBar() { return this.usesHealthBar; }

    /**Stores mobs base critical chance */
    private int criticalChance;
        /**Returns mobs base critical chance */
        public int getCriticalChance() { return this.criticalChance; }

    /**Stores mobs base critical multiplier */
    private double criticalMultiplier;
        /**Returns mobs base critical multiplier */
        public double getCriticalMultiplier() { return this.criticalMultiplier; }

    /**Stores mobs base parry chance */
    private int parryChance;
        /**Returns mobs base parry chance */
        public int getParryChance() { return this.parryChance; }
    
    /**Stores mobs base parry damage multiplier */
    private double parryMultiplier;
        /**Returns mobs base parry damage multiplier */
        public double getParryMultiplier() { return this.parryMultiplier; }

    /**Determines if mob is used in tutorial and added to tutorial manager list */
    private boolean isTutorialMob;
        /**Returns if mob is part of tutorial */
        public boolean isTutorialMob() { return this.isTutorialMob; }

    /**
     * Constructor and decoder for MythicMobs
     * @param file MM file i.e. SkeletonKing.yml
     * @param internalName Name of file i.e. SkeletonKing
     */
    public MythicMob(String file, String internalName, MythicConfig mc) {
        this.config = mc;
        this.file = file;
        this.internalName = internalName;

        this.strMobType = mc.getString("Type", this.strMobType);
        this.strMobType = mc.getString("MobType", this.strMobType);
        this.strMobType = mc.getString("MobType", this.strMobType);
        if(this.strMobType == null) {
            MythicEntity me = MythicEntity.getMythicEntity(internalName);
            if(me == null) {
                SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Could not load MythicMob " + this.internalName);
                this.strMobType = "NULL";
                this.mobType = MythicEntity.getMythicEntity("SKELETON");
                this.displayName = "ERROR: MOB TYPE FOR '" + this.internalName + "' IS NOT OPTIONAL";
                return;
            }
            SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "EntityType is vanilla override for " + this.strMobType);
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
        this.health = mc.getDouble("Health", 0.0D);
        this.damage = mc.getDouble("Damage", -1.0D);
        this.armor = mc.getDouble("Armor");
        this.armor = mc.getDouble("Armour", this.armor);
        this.optionInvincible = mc.getBoolean("Options.Invincible", false);
        this.optionInvincible = mc.getBoolean("Options.Invulnerable", this.optionInvincible);
        String mount = mc.getString("Mount", null);
        this.mount = Optional.ofNullable(mount);
        String rider = mc.getString("Rider", null);
        rider = mc.getString("Passenger", rider);
        this.rider = Optional.ofNullable(rider);
        
        //Combat mechanics
        this.criticalChance = mc.getInteger("CriticalChance", 2);
        this.criticalChance = mc.getInteger("CritChance", this.criticalChance);
        this.criticalMultiplier = mc.getDouble("CriticalMultiplier", 1.2D);
        this.criticalMultiplier = mc.getDouble("CritMultiplier", this.criticalMultiplier);
        this.parryChance = mc.getInteger("ParryChance", 0);
        this.parryMultiplier = mc.getDouble("ParryMutliplier", 0.95D);

        //Options handling
        this.optionDespawn = mc.getBoolean("Despawn", true);
        this.optionDespawn = mc.getBoolean("Options.Despawn", this.optionDespawn);
        this.optionPersistent = mc.getBoolean("Persistent", false);
        this.optionPersistent = mc.getBoolean("Options.Persistent", this.optionPersistent);
        this.attrAttackSpeed = mc.getDouble("Options.AttackSpeed", 0.0D);
        this.attrMovementSpeed = mc.getDouble("Options.MovementSpeed", 0.0D);
        this.attrKnockbackResist = mc.getDouble("Options.KnockbackResistance", 0.0D);
        this.attrFollowRange = mc.getDouble("Options.FollowRange", 0.0D);
        this.optionGlowing = mc.getBoolean("Options.Glowing", false);
        this.optionCollidable = mc.getBoolean("Options.Collidable", true);
        this.optionNoGravity = mc.getBoolean("Options.NoGravity", false);
        this.optionInteractable = mc.getBoolean("Options.Interactable", this.optionInteractable);
        this.optionSilent = mc.getBoolean("Options.Silent", this.optionSilent);
        this.optionNoAI = mc.getBoolean("Options.NoAI", this.optionNoAI);
        this.noDamageTicks = mc.getInteger("Options.NoDamageTicks", 10) * 2;
        this.isTutorialMob = mc.getBoolean("Options.isTutorialMob", false);

        //Creeper options
        this.explosionRadius = mc.getInteger("Options.ExplosionRadius", 3);
        this.isPowered = mc.getBoolean("Options.Powered", false);
        this.maxFuseTicks = mc.getInteger("Options.MaxFuseTicks", 30);
        //Horse Options
        this.horseArmor = mc.getString("Options.HorseArmor", null);
        this.carryingChest = mc.getBoolean("Options.CarryingChest", false);
        this.horseColor = mc.getString("Options.HorseColor", "creamy");
        this.horseStyle = mc.getString("Options.HorseStyle", "white");
        this.isTamed = mc.getBoolean("Options.Tamed", false);
        this.isSaddled = mc.getBoolean("Options.Saddled", false);
        //IronGolem options
        this.playerCreated = mc.getBoolean("Options.PlayerCreated", false);
        //Cat options
        this.cat = mc.getString("Options.Cat", "black");
        //Rabbit options
        this.rabbit = mc.getString("Options.Rabbit", "black");
        //Sheep options
        this.isSheared = mc.getBoolean("Options.Sheared", false);
        //Snowman options
        this.isDerp = mc.getBoolean("Options.Derp", false);
        this.preventSnowFormation = mc.getBoolean("Options.PreventSnowmanTrail", false);
        //Tropical fish options
        this.tropicalFishPattern = mc.getString("Options.Pattern", "betty");
        this.tropicalFishBodyColor = mc.getString("Options.BodyColor", "blue");
        this.tropicalFishPatternColor = mc.getString("Options.PatternColor", "yellow");
        //Slime options
        this.slimeSize = mc.getInteger("Options.SlimeSize", 8);
        this.preventSlimeSplit = mc.getBoolean("Options.PreventSlimeSplit", true);
        //Ageable option
        this.isAdult = mc.getBoolean("Options.Adult", true);
        this.ageLock = mc.getBoolean("Options.AgeLock", false);
        //Colorable options
        this.color = mc.getString("Options.Color", "black");
        //Anger options
        this.angry = mc.getBoolean("Options.Angry", false);
        //PotionEffect handling
        this.potionEffects = mc.getStringList("Options.PotionEffects");
        //Villager options
        if(this.strMobType.equalsIgnoreCase("Villager")) {
            this.villagerType = mc.getString("Options.VillagerType");
        }
        //Ender crystal options
        this.isShowingBottom = mc.getBoolean("Options.ShowingBottom", false);

        //More Options
        this.maxAttackRange = mc.getInteger("Options.MaxAttackRange", 64);
        this.maxAttackableRange = mc.getInteger("Options.MaxCombatRange", 256);
        this.maxAttackableRange = mc.getInteger("Options.MaxAttackableRange", this.maxAttackableRange);
        this.alwaysShowName = mc.getBoolean("Options.AlwaysShowName", false);
        this.showNameOnDamage = mc.getBoolean("Options.ShowNameOnDamaged", false);
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
        this.usesHealthBar = Boolean.valueOf(mc.getBoolean("Options.HealthBar", true));
        
        //Boss Bar Handling
        this.useBossBar = mc.getBoolean("BossBar.Enabled", false);
        this.bossBarTitle = mc.getString("BossBar.Title", (this.displayName == null) ? null : this.displayName.toString());
        this.bossBarRange = mc.getInteger("BossBar.Range", 64);
        this.bossBarRangeSq = (int)Math.pow(this.bossBarRange, 2.0D);
        String bossBarColor = mc.getString("BossBar.Color", "WHITE");
        String bossBarStyle = mc.getString("BossBar.Style", "SOLID");
        try {
            this.bossBarColor = BarColor.valueOf(bossBarColor);
        } catch (Exception ex) {
            this.bossBarColor = BarColor.WHITE;
        }
        try {
            this.bossBarStyle = BarStyle.valueOf(bossBarStyle);
        } catch (Exception ex) {
            this.bossBarStyle = BarStyle.SOLID;
        }
        this.bossBarCreateFog = mc.getBoolean("BossBar.CreateFog", false);
        this.bossBarDarkenSky = mc.getBoolean("BossBar.DarkenSky", false);
        this.bossBarPlayMusic = mc.getBoolean("BossBar.PlayMusic", false);

        //Boss Mob Handling
        

        //Mob Skill handling
        //TODO: Load sword skills from naming convetion and apply to mob.
        this.skills = mc.getStringList("Skills");

        //Loading mob faction details.
        this.faction = mc.getString("Faction", null);
        this.factionTargets = mc.getStringList("Target");
        
        this.aiGSelectors = mc.getStringList("AIGoalSelectors");
        this.aiTSelectors = mc.getStringList("AITargetSelectors");

        //Drops, droptables, killmessages
        this.drops = mc.getStringList("Drops");
        /**
         * Passing to drop table constructor.
         * FILE NAMES SHOULD MATCH.
         */
        try {
            if(SwordCraftOnline.getPluginInstance().getDropManager().isInDropTable(this.drops.get(0))) {
                //Check if Custom drop table loaded from file.
                this.dropTable = SwordCraftOnline.getPluginInstance().getDropManager().getDropTable(this.drops.get(0));
            } else {
                this.dropTable = new DropTable(this.file, "Mob:" + this.internalName, this.drops);
            }
        } catch(IndexOutOfBoundsException ex) {
            this.dropTable = null;
        }

        this.equipment = mc.getStringList("Equipment");
        List<String> killMessages = mc.getStringList("KillMessages");
        if(killMessages != null && killMessages.size() > 0) {
            SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Loading mob kill messages.");
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
                this.lvlModDamage = this.damage / 5.0D;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
            if(this.lvlModHealth < 0.0D) {
                this.lvlModHealth = this.health / 5.0D;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
            if(this.lvlModArmor < 0.0D) {
                this.lvlModArmor = this.armor / 5.0D;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
            if(this.lvlModKBR < 0.0D) {
                this.lvlModKBR = this.attrKnockbackResist / 5.0D;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        //Spawning factors
        this.digOutOfGround = Boolean.valueOf(mc.getBoolean("Options.DigOutOfGround", false));
        String strSpawnVelocityX = mc.getString("SpawnModifiers.VelocityX", "0");
        String strSpawnVelocityY = mc.getString("SpawnModifiers.VelocityY", "0");
        String strSpawnVelocityZ = mc.getString("SpawnModifiers.VelocityZ", "0");
        if(strSpawnVelocityX.contains("-")) {
            String[] split = strSpawnVelocityX.split("-");
            try {
                this.spawnVelocityX = Double.valueOf(split[0]).doubleValue();
                this.spawnVelocityXMax = Double.valueOf(split[1]).doubleValue();
                this.spawnVelocityXRange = true;
            } catch(Exception ex) {
                this.spawnVelocityX = 0.0D;
                SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Invalid X Velocity Modifier.");
            }
        } else {
            try {
                this.spawnVelocityX = Double.valueOf(strSpawnVelocityX).doubleValue();
            } catch (Exception ex) {
                this.spawnVelocityX = 0.0D;
                SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Invalid X Velocity Modifier.");
            }
        }
        if(strSpawnVelocityY.contains("-")) {
            String[] split = strSpawnVelocityY.split("-");
            try {
                this.spawnVelocityY = Double.valueOf(split[0]).doubleValue();
                this.spawnVelocityYMax = Double.valueOf(split[1]).doubleValue();
                this.spawnVelocityYRange = true;
            } catch(Exception ex) {
                this.spawnVelocityY = 0.0D;
                SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Invalid Y Velocity Modifier.");
            }
        } else {
            try {
                this.spawnVelocityY = Double.valueOf(strSpawnVelocityY).doubleValue();
            } catch(Exception ex) {
                this.spawnVelocityY = 0.0D;
                SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Invalid Y Velocity Modifier.");
            }
        }
        if(strSpawnVelocityZ.contains("-")) {
            String[] split = strSpawnVelocityZ.split("-");
            try {
                this.spawnVelocityZ = Double.valueOf(split[0]).doubleValue();
                this.spawnVelocityZMax = Double.valueOf(split[1]).doubleValue();
                this.spawnVelocityZRange = true;
            } catch(Exception ex) {
                this.spawnVelocityZ = 0.0D;
                SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Invalid Z Velocity Modifier.");
            }
        } else {
            try {
                this.spawnVelocityZ = Double.valueOf(strSpawnVelocityZ).doubleValue();
            }  catch (Exception ex) {
                this.spawnVelocityZ = 0.0D;
                SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Invalid Z Velocity Modifier.");
            }
        }

        //Disguise handling
        this.disguise = mc.getString("Options.Disguise", this.disguise);
        this.disguise = mc.getString("Disguise.Type", this.disguise);
        if(this.disguise != null && this.disguise.toUpperCase().contains("PLAYER")) {
            this.fakePlayer = true;
        }

        //Extra overrides for tutorial mobs
        if(this.isTutorialMob) {
            this.optionInvincible = true;
            this.optionPersistent = true;
            this.optionDespawn = false;
        }
    }

    /**Applies mob options and returns ActiveMob */
    public ActiveMob spawn(AbstractLocation loc, int level) {
        Entity e;
        if(this.mobType != null) {
            e = this.mobType.spawn(this, BukkitAdapter.adapt(loc));
        } else {
            return null;
        }
        ActiveMob am = new ActiveMob(e.getUniqueId(), BukkitAdapter.adapt(e), this, level);
        SwordCraftOnline.getPluginInstance().getMobManager().registerActiveMob(am);
        
        //Applying all options.
        am = applySkills(am);
        am = applyMobOptions(am, level);
        am = applyMobVolatileOptions(am);
        am = applySpawnModifiers(am);
        am = applyDisplayName(am, level);
        return am;
    }

    /**Applies any name modifiers. Color, healthbar, etc. */
    public ActiveMob applyDisplayName(ActiveMob am, int level) {
        if(!SwordCraftOnline.getSCOConfig().healthBarEnabled()) { return am; }
        if(!this.usesHealthBar) { return am; }

        Entity e = am.getEntity().getBukkitEntity();
        if(am.getEntity().isLiving()) {
            am.setHealthBar(new HealthBar(am));
            LivingEntity asLiving = (LivingEntity)e;
            
            String name = "";
            if(getDisplayName() != null) {
                name = getDisplayName();
            }
            asLiving.setCustomName(getDisplayColor(level) + "" + name + am.getHealthBar().getHealthBar());
        }
        return am;
    }

    /**
     * Applies swordskills and registers to mob.
     */
    public ActiveMob applySkills(ActiveMob am) {
        for(String s : this.skills) {
            Generator.readSkill(s, am);
        }
        return am;
    }

    /**
     * Applies attribute modifiers and other options to mob
     * @param level Determines modifiers of attributes.
     */
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
                asLiving.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(am.getDamage());
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
                    double fhealth = Math.floor(health);
                    asLiving.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(fhealth);
                    asLiving.setHealth(fhealth);
                } catch(IllegalArgumentException ex) {
                    SwordCraftOnline.logWarning("Illegal health argument in: " + this.internalName);
                    asLiving.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2048);
                    asLiving.setHealth(2048);
                }
            }

            /**
             * Handling equipment. ALL drop chances set to 0. 
             * Drops handled by drop tables
             * Format: 0-Material, 1-Location
             */
            if(!this.equipment.isEmpty()) {
                EntityEquipment ee = asLiving.getEquipment();
                for(String s : this.equipment) {
                    String[] split = s.split(" ");
                    /**TODO: Add compatability with Custom Items.
                     * Check if item is in custom item list.
                    */
                    try {
                        if(split[1].equalsIgnoreCase("HEAD")) {
                            ee.setHelmet(new ItemStack(Material.getMaterial(split[0])));
                            ee.setHelmetDropChance(0.0F);
                        } else if(split[1].equalsIgnoreCase("CHEST")) {
                            ee.setChestplate(new ItemStack(Material.getMaterial(split[0])));
                            ee.setChestplateDropChance(0.0F);
                        } else if(split[1].equalsIgnoreCase("LEGS")) {
                            ee.setLeggings(new ItemStack(Material.getMaterial(split[0])));
                            ee.setLeggingsDropChance(0.0F);
                        } else if(split[1].equalsIgnoreCase("FEET")) {
                            ee.setBoots(new ItemStack(Material.getMaterial(split[0])));
                            ee.setBootsDropChance(0.0F);
                        } else if(split[1].equalsIgnoreCase("HAND")) {
                            ee.setItemInMainHand(new ItemStack(Material.getMaterial(split[0])));
                            ee.setItemInMainHandDropChance(0.0F);
                        } else if(split[1].equalsIgnoreCase("OFFHAND")) {
                            ee.setItemInOffHand(new ItemStack(Material.getMaterial(split[0])));
                            ee.setItemInOffHandDropChance(0.0F);
                        }
                    } catch(Exception ex) {
                        SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Could not load items. Invalid material name.");
                    }
                }

                /**
                 * Equipping potion effects
                 */
                if(!(this.potionEffects.isEmpty())) {
                    //Strings are format: Effect-level
                    for(String s : this.potionEffects) {
                        try{
                            String[] split = s.split("-");
                            asLiving.addPotionEffect(new PotionEffect(PotionEffectType.getByName(split[0].toUpperCase()), 99999, Integer.valueOf(split[1])));
                        } catch(Exception ex) {
                            SwordCraftOnline.logInfo("Attempted to load invalid potion effect on " + this.internalName);
                        }
                    }
                }
            }

            asLiving.setMaximumNoDamageTicks(this.noDamageTicks);

            /**
             * Prevents random equipment of mob.
             */
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
            /**
             * Applying creeper options
             */
            if(e instanceof Creeper) {
                Creeper creeper = (Creeper)e;
                creeper.setPowered(true);
                creeper.setMaxFuseTicks(this.maxFuseTicks);
                creeper.setExplosionRadius(this.explosionRadius);
            }
            /**
             * Applying horse options
             */
            if(e instanceof Horse) {
                Horse horse = (Horse)e;
                horse.setAdult();
                if(!(isAdult())) { horse.setBaby(); }
                try {
                    if(this.horseArmor != null) {
                        String armor = (this.horseArmor + "_horse_armor").toUpperCase();
                        horse.getInventory().setArmor(new ItemStack(Material.valueOf(armor)));
                    }
                    horse.setColor(Color.valueOf(this.horseColor));
                    horse.setStyle(Style.valueOf(this.horseStyle));
                } catch(IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo("Attempted to load invalid horse optional in " + this.internalName);
                }
                horse.setTamed(this.isTamed);
                if(this.isTamed && this.isSaddled) {
                    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                }
                horse.setAgeLock(this.ageLock);
            }
            /**
             * Applying iron golem options
             */
            if(e instanceof IronGolem) {
                ((IronGolem)e).setPlayerCreated(this.playerCreated);
            }
            /**
             * Applying ocelot options
             */
            if(e instanceof Cat) {
                try{
                    ((Cat)e).setCatType(Cat.Type.valueOf(this.cat.toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo("Attempted to load invalid cat optional in " + this.internalName);
                }
            }
            /**
             * Pig options
             */
            if(e instanceof Pig) {
                Pig pig = (Pig)e;
                pig.setSaddle(this.isSaddled);
                pig.setAdult();
                if(!(this.isAdult)) { pig.setBaby(); }
                pig.setAgeLock(this.ageLock);
            }
            /**
             * Rabbit options
             */
            if(e instanceof Rabbit) {
                Rabbit rabbit = (Rabbit)e;
                try{
                    rabbit.setRabbitType(Rabbit.Type.valueOf(this.rabbit.toUpperCase()));
                } catch(IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo("Attempted to load invalid rabbit optional in " + this.internalName);
                }
                rabbit.setAdult();
                if(!(this.isAdult)) { rabbit.setBaby(); }
                rabbit.setAgeLock(this.ageLock);
            }
            /**
             * Sheep options
             */
            if(e instanceof Sheep) {
                Sheep sheep = (Sheep)e;
                sheep.setSheared(this.isSheared);
                sheep.setAdult();
                if(!(this.isAdult)) { sheep.setBaby(); }
                sheep.setAgeLock(this.ageLock);
                try {
                    sheep.setColor(DyeColor.valueOf(this.color.toUpperCase()));
                } catch(IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo("Attempted to load invalid dye color in " + this.internalName);
                }
            }
            /**
             * Snowman options
             */
            if(e instanceof Snowman) {
                ((Snowman)e).setDerp(this.isDerp);
            }
            /**
             * Tropical fish options
             */
            if(e instanceof TropicalFish) {
                try{
                    TropicalFish fish = (TropicalFish)e;
                    fish.setPattern(Pattern.valueOf(this.tropicalFishPattern.toUpperCase()));
                    fish.setBodyColor(DyeColor.valueOf(this.tropicalFishBodyColor.toUpperCase()));
                    fish.setPatternColor(DyeColor.valueOf(this.tropicalFishPatternColor.toUpperCase()));
                } catch(IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo("Attempted to load invalid tropical fish optional in " + this.internalName);
                }
            }
            /**
             * Wolf options
             */
            if(e instanceof Wolf) {
                Wolf wolf = (Wolf)e;
                try {
                    wolf.setCollarColor(DyeColor.valueOf(this.color.toUpperCase()));
                } catch(IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo("Attempted to load invalid dye color in " + this.internalName);
                }
                wolf.setAngry(this.angry);
            }
            /**
             * pig zombie options
             */
            if(e instanceof PigZombie) {
                PigZombie pz = (PigZombie)e;
                pz.setAngry(this.angry);
            }
            /**
             * Slime or magma cube options
             */
            if(e instanceof Slime) {
                ((Slime)e).setSize(this.slimeSize);
            }
            if(getDisplayName() != null) {
                asLiving.setCustomName(am.getDisplayName());
            }
            /**
             * Ender crystal options
             */
            if(e instanceof EnderCrystal) {
                ((EnderCrystal)e).setShowingBottom(this.isShowingBottom);
            }

            //If villager has profession set it. If not nitwit
            if(e instanceof Villager) {
                if((!this.villagerType.isEmpty())) {
                    try {
                        ((Villager)e).setProfession(Profession.valueOf(this.villagerType));
                    } catch(IllegalArgumentException ex) {
                        SwordCraftOnline.logInfo(Banners.get(Banners.MYTHIC_MOB) + "Attempted to load invalid villager modifier.");
                    }
                }
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

    /**Applies volatile options. No AI and Faction */
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

    /**Spawn modifiers. Spawn velocity, etc. */
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

    /**
     * Returns if mob uses boss bar.
     * Use this to determine if mob is boss or not.
     */
    public boolean usesBossBar() {
        return this.useBossBar;
    }

    /**Gets boss bar from config */
    public Optional<BossBar> getBossBar() {
        if(!this.useBossBar) {
            return Optional.empty();
        }
        try{
            BossBar bar = Bukkit.getServer().createBossBar(" ", this.bossBarColor, this.bossBarStyle);
            bar.setProgress(1.0D);
            if(this.bossBarCreateFog) { bar.addFlag(BarFlag.CREATE_FOG); }
            if(this.bossBarDarkenSky) { bar.addFlag(BarFlag.DARKEN_SKY); }
            if(this.bossBarPlayMusic) { bar.addFlag(BarFlag.PLAY_BOSS_MUSIC); }
            return Optional.of(bar);
        } catch(NullPointerException ex) {
            SwordCraftOnline.logWarning("Attempted to load invalid boss bar on " + this.internalName);
        }
        return Optional.empty();
    }

    public String getBossBarTitle() {
        return this.bossBarTitle;
    }

    public int getBossBarRangeSquared() {
        return this.bossBarRangeSq;
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
      
    public List<Modifier> getEntityDamageModifiers() {
        return this.damageModifiers;
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

    /**Returns randomly selected kill message. */
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

    /**Converts level to chat color */
    public ChatColor getDisplayColor(int level) {
        if(level > 0 && level <= 24) {
            return ChatColor.WHITE; 
        } else if(level >= 25  && level <= 49) {
            return ChatColor.GREEN;
        } else if(level >= 50 && level <= 74) {
            return ChatColor.BLUE;
        } else if(level >= 75 && level <= 99) {
            return ChatColor.LIGHT_PURPLE;
        } else if(level >= 100 && level <= 149) {
            return ChatColor.AQUA;
        } else if(level >= 150) {
            return ChatColor.GOLD;
        } else {
            return ChatColor.WHITE;
        }
    }
}