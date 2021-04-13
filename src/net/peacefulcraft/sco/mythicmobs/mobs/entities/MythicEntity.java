package net.peacefulcraft.sco.mythicmobs.mobs.entities;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitBabyDrowned;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitBabyHusk;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitBabyPigZombie;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitBabyZombie;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitBabyZombieVillager;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitBat;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitBlaze;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitCaveSpider;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitChicken;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitCod;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitCow;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitCreeper;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitCustom;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitDolphin;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitDonkey;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitDrowned;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitElderGuardian;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitEnderCrystal;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitEnderDragon;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitEnderman;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitEndermite;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitEvoker;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitFallingBlock;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitFox;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitGhast;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitGiant;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitGuardian;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitHorse;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitHusk;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitIllusioner;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitIronGolem;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitLlama;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitMagmaCube;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitMule;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitMushroomCow;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitOcelot;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitPanda;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitParrot;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitPhantom;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitPig;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitPigZombie;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitPillager;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitPolarBear;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitPufferFish;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitRabbit;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitRavager;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSalmon;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSheep;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitShulker;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSilverfish;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSkeleton;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSkeletonHorse;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSlime;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSnowman;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSpider;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitSquid;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitStray;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitTNT;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitTropicalFish;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitTurtle;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitVex;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitVillager;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitVindicator;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitWitch;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitWither;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitWitherSkeleton;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitWolf;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitZombie;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitZombieHorse;
import net.peacefulcraft.sco.mythicmobs.adapters.entities.BukkitZombieVillager;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public abstract class MythicEntity {
    private static HashMap<MythicEntityType, Class<? extends MythicEntity>> entities = new HashMap<>();

    private static HashMap<EntityType, MythicEntityType> passive = new HashMap<>();

    public abstract void instantiate(MythicConfig paramMythicConfig);

    public abstract Entity spawn(MythicMob paramMythicMob, Location paramLocation);

    public abstract Entity spawn(Location paramLocation);

    public abstract Entity applyOptions(Entity paramEntity);

    public abstract boolean compare(Entity paramEntity);

    public int getHeight() {
        return 2;
    }

    public double getHealthbarOffset() {
        return -0.1D;
    }

    public static MythicEntity getMythicEntity(MythicMob mm) {
        return getMythicEntity(mm.getStrMobType());
    }

    public static MythicEntity getMythicEntity(String s) {
        MythicEntityType met = MythicEntityType.get(s);
        if(met == null)
            return null;
        return getMythicEntity(met);
    }

    public static MythicEntity getMythicEntity(MythicEntityType entityType) {
        Class<BukkitCustom> clazz1;
        if(entityType == null) { return null; }

        Class<? extends MythicEntity> clazz = entities.get(entityType);
        if(clazz == null) {

            try {
                clazz1 = BukkitCustom.class;
                return (MythicEntity)clazz1.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            return (MythicEntity)clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static EntityType fromName(String name) {
        for(EntityType type : EntityType.values()) {
            if(type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public static EntityType fromId(Integer id) {
        for(World w : Bukkit.getWorlds()) {
            for(Entity e : w.getEntities()) {
                if(e.getEntityId() == id) {
                    return e.getType();
                }
            }
        }
        return null;
    }

    public static boolean isPassive(EntityType type) {
        if(passive.containsKey(type)) { return true; }
        return false;
    }

    public static boolean isPassive(MythicEntityType type) {
        if(passive.containsValue(type)) { return true; }
        return false;
    }

    public static boolean isPassive(String type) {
        for(EntityType t : passive.keySet()) {
            if(t.name().equalsIgnoreCase(type)) { return true; }
        }
        return false;
    }

    static {
        passive.put(EntityType.BAT, MythicEntityType.BAT);
        passive.put(EntityType.COD, MythicEntityType.COD);
        passive.put(EntityType.COW, MythicEntityType.COW);
        passive.put(EntityType.CHICKEN, MythicEntityType.CHICKEN);
        passive.put(EntityType.DOLPHIN, MythicEntityType.DOLPHIN);
        passive.put(EntityType.DONKEY, MythicEntityType.DONKEY);
        passive.put(EntityType.FOX, MythicEntityType.FOX);
        passive.put(EntityType.HORSE, MythicEntityType.HORSE);
        passive.put(EntityType.LLAMA, MythicEntityType.LLAMA);
        passive.put(EntityType.MULE, MythicEntityType.MULE);
        passive.put(EntityType.MUSHROOM_COW, MythicEntityType.MUSHROOM_COW);
        passive.put(EntityType.OCELOT, MythicEntityType.OCELOT);
        passive.put(EntityType.PANDA, MythicEntityType.PANDA);
        passive.put(EntityType.PARROT, MythicEntityType.PARROT);
        passive.put(EntityType.PIG, MythicEntityType.PIG);
        passive.put(EntityType.POLAR_BEAR, MythicEntityType.POLAR_BEAR);
        passive.put(EntityType.PUFFERFISH, MythicEntityType.PUFFERFISH);
        passive.put(EntityType.RABBIT, MythicEntityType.RABBIT);
        passive.put(EntityType.SHEEP, MythicEntityType.SHEEP);
        passive.put(EntityType.SNOWMAN, MythicEntityType.SNOWMAN);
        passive.put(EntityType.SQUID, MythicEntityType.SQUID);
        passive.put(EntityType.TROPICAL_FISH, MythicEntityType.TROPICAL_FISH);
        passive.put(EntityType.VILLAGER, MythicEntityType.VILLAGER);

        //entities.put(MythicEntityType.ARMOR_STAND, (Class) ArmorStand.class);
        entities.put(MythicEntityType.BABY_DROWNED, BukkitBabyDrowned.class);
        entities.put(MythicEntityType.BABY_HUSK, BukkitBabyHusk.class);
        entities.put(MythicEntityType.BABY_PIG_ZOMBIE, BukkitBabyPigZombie.class);
        //entities.put(MythicEntityType.BABY_PIG_ZOMBIE_VILLAGER, BukkitBabyPigZombieVillager.class);
        entities.put(MythicEntityType.BABY_ZOMBIE, BukkitBabyZombie.class);
        entities.put(MythicEntityType.BABY_ZOMBIE_VILLAGER, BukkitBabyZombieVillager.class);
        entities.put(MythicEntityType.BAT, BukkitBat.class);
        entities.put(MythicEntityType.BLAZE, BukkitBlaze.class);
        entities.put(MythicEntityType.CAVE_SPIDER, BukkitCaveSpider.class);
        entities.put(MythicEntityType.CHICKEN, BukkitChicken.class);
        entities.put(MythicEntityType.COD, BukkitCod.class);
        entities.put(MythicEntityType.COW, BukkitCow.class);
        entities.put(MythicEntityType.CREEPER, BukkitCreeper.class);
        entities.put(MythicEntityType.CUSTOM, BukkitCustom.class);
        entities.put(MythicEntityType.DOLPHIN, BukkitDolphin.class);
        entities.put(MythicEntityType.DONKEY, BukkitDonkey.class);
        entities.put(MythicEntityType.DROWNED, BukkitDrowned.class);
        entities.put(MythicEntityType.ELDER_GUARDIAN, BukkitElderGuardian.class);
        entities.put(MythicEntityType.ENDERMAN, BukkitEnderman.class);
        entities.put(MythicEntityType.ENDERMITE, BukkitEndermite.class);
        entities.put(MythicEntityType.ENDER_DRAGON, BukkitEnderDragon.class);
        entities.put(MythicEntityType.EVOKER, BukkitEvoker.class);
        entities.put(MythicEntityType.FALLING_BLOCK, BukkitFallingBlock.class);
        entities.put(MythicEntityType.FOX, BukkitFox.class);
        entities.put(MythicEntityType.GHAST, BukkitGhast.class);
        entities.put(MythicEntityType.GIANT, BukkitGiant.class);
        entities.put(MythicEntityType.GUARDIAN, BukkitGuardian.class);
        entities.put(MythicEntityType.HORSE, BukkitHorse.class);
        entities.put(MythicEntityType.HUSK, BukkitHusk.class);
        entities.put(MythicEntityType.ILLUSIONER, BukkitIllusioner.class);
        entities.put(MythicEntityType.IRON_GOLEM, BukkitIronGolem.class);
        entities.put(MythicEntityType.LLAMA, BukkitLlama.class);
        entities.put(MythicEntityType.MAGMA_CUBE, BukkitMagmaCube.class);
        entities.put(MythicEntityType.MULE, BukkitMule.class);
        entities.put(MythicEntityType.MUSHROOM_COW, BukkitMushroomCow.class);
        entities.put(MythicEntityType.OCELOT, BukkitOcelot.class);
        entities.put(MythicEntityType.PANDA, BukkitPanda.class);
        entities.put(MythicEntityType.PARROT, BukkitParrot.class);
        entities.put(MythicEntityType.PHANTOM, BukkitPhantom.class);
        entities.put(MythicEntityType.PIG, BukkitPig.class);
        entities.put(MythicEntityType.PIG_ZOMBIE, BukkitPigZombie.class);
        //entities.put(MythicEntityType.PIG_ZOMBIE_VILLAGER,(Class) PigZombieVillager.class);
        entities.put(MythicEntityType.PILLAGER, BukkitPillager.class);
        entities.put(MythicEntityType.POLAR_BEAR, BukkitPolarBear.class);
        entities.put(MythicEntityType.PRIMED_TNT, BukkitTNT.class);
        entities.put(MythicEntityType.PUFFERFISH, BukkitPufferFish.class);
        entities.put(MythicEntityType.RABBIT, BukkitRabbit.class);
        entities.put(MythicEntityType.RAVAGER, BukkitRavager.class);
        entities.put(MythicEntityType.SALMON, BukkitSalmon.class);
        entities.put(MythicEntityType.SHEEP, BukkitSheep.class);
        entities.put(MythicEntityType.SHULKER, BukkitShulker.class);
        entities.put(MythicEntityType.SILVERFISH, BukkitSilverfish.class);
        entities.put(MythicEntityType.SKELETON, BukkitSkeleton.class);
        entities.put(MythicEntityType.SKELETON_HORSE, BukkitSkeletonHorse.class);
        entities.put(MythicEntityType.SLIME, BukkitSlime.class);
        entities.put(MythicEntityType.SNOWMAN, BukkitSnowman.class);
        entities.put(MythicEntityType.SPIDER, BukkitSpider.class);
        entities.put(MythicEntityType.STRAY, BukkitStray.class);
        entities.put(MythicEntityType.SQUID, BukkitSquid.class);
        entities.put(MythicEntityType.TROPICAL_FISH, BukkitTropicalFish.class);
        entities.put(MythicEntityType.TURTLE, BukkitTurtle.class);
        entities.put(MythicEntityType.VEX, BukkitVex.class);
        entities.put(MythicEntityType.VILLAGER, BukkitVillager.class);
        entities.put(MythicEntityType.VINDICATOR, BukkitVindicator.class);
        entities.put(MythicEntityType.WITCH, BukkitWitch.class);
        entities.put(MythicEntityType.WITHER, BukkitWither.class);
        entities.put(MythicEntityType.WITHER_SKELETON, BukkitWitherSkeleton.class);
        entities.put(MythicEntityType.WOLF, BukkitWolf.class);
        entities.put(MythicEntityType.ZOMBIE, BukkitZombie.class);
        entities.put(MythicEntityType.ZOMBIE_HORSE, BukkitZombieHorse.class);
        entities.put(MythicEntityType.ZOMBIE_VILLAGER, BukkitZombieVillager.class);

        entities.put(MythicEntityType.ENDER_CRYSTAL, BukkitEnderCrystal.class);
    };
}