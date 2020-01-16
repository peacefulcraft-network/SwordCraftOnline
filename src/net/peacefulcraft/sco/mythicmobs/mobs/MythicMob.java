package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import net.peacefulcraft.sco.mythicmobs.abstracts.AbstractBossBar;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class MythicMob implements Comparable<MythicMob> {
    private String file;

    private MythicConfig config;

    private String internalName;

    protected String displayName;

    protected MythicEntity mobType;

    protected boolean optionDespawn;

    protected boolean optionPersistent;

    protected boolean optionShowHealthInChat = false;

    protected String strMobType;

    protected String faction = null;

    protected double health;

    protected double damage;

    protected double armor;

    protected double attrMovementSpeed;

    protected double attrKnockbackResist;

    protected double attrFollowRange;

    protected double attrAttacKSpeed;

    protected double lvlModDamage;

    protected double lvlModHealth;

    protected double lvlModArmor;

    protected double lvlModKBR;

    protected double lvlModPower;

    protected double lvlModSpeed;

    protected double lvlModAttackSpeed;

    protected boolean optionSilent = false;

    protected boolean optionNoAI = false;

    protected boolean optionFlowing = false;

    protected  boolean optionInvincible = false;

    protected boolean optionCollidable = true;

    protected boolean optionNoGravity = true;

    protected boolean optionInteractable = true;

    protected boolean useBossBar = false;

    protected int bossBarRange;

    protected int bossBarRangeSq;

    protected String bossBarTitle;

    protected AbstractBossBar.BarColor bossBarColor;

    protected AbstractBossBar.BarStyle bossBarStyle;

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

    public MythicMob(String file, String internalName, MythicConfig mc) {
        this.config = mc;
        this.file = file;
        this.internalName = internalName;
        this.strMobType = mc.getString("Type", this.strMobType);
        this.strMobType = mc.getString("MobType", this.strMobType);
        if(this.strMobType == null) {
            MythicEntity me = MythicEntity.getMythicEntity(internalName);
        }
        
    }

}