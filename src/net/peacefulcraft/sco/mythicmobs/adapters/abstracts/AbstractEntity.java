package net.peacefulcraft.sco.mythicmobs.adapters.abstracts;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;

public interface AbstractEntity {
    AbstractLocation getLocation();

    Entity getBukkitEntity();

    boolean isLiving();
  
    boolean isCreature();
    
    boolean isPlayer();
    
    boolean isAnimal();
    
    boolean isWaterMob();
    
    boolean isFlyingMob();
    
    UUID getUniqueId();

    boolean hasLineOfSight(AbstractEntity paramAbstractEntity);
  
    void teleport(AbstractLocation paramAbstractLocation);
    
    double getHealth();
    
    boolean isDead();
    
    boolean isValid();
    
    boolean remove();
    
    double getMaxHealth();
    
    void setMaxHealth(double paramDouble);
    
    void setFireTicks(int paramInt);

    Object getCustomName();
  
    void addPotionEffect(PotionEffect paramPotionEffect);
    
    void setPassenger(Entity paramEntity);
    
    AbstractLocation getEyeLocation();
    
    double getEyeHeight();

    AbstractWorld getWorld();
  
    AbstractEntity getTarget();
    
    AbstractEntity getVehicle();
    
    void eject();
    
    void setHealth(double paramDouble);
    
    String getName();
    
    void damage(float paramFloat);

    void setPassenger(AbstractEntity paramAbstractEntity);
  
    AbstractPlayer asPlayer();
    
    void setNoDamageTicks(int paramInt);
    
    boolean isMonster();
    
    boolean isCitizensNPC();
    
    boolean isGliding();
    
    boolean hasPotionEffect(String paramString);

    boolean hasPotionEffect(String paramString, Double paramRangedDouble1, Double paramRangedDouble2);

    boolean hasPotionEffect();
  
    boolean hasScoreboardTag(String paramString);
    
    void addScoreboardTag(String paramString);
    
    void removeScoreboardTag(String paramString);
    
    List<AbstractEntity> getPassengers();
    
    void setGravity(boolean paramBoolean);
    
    void equipItemHead(AbstractItemStack paramAbstractItemStack);
    
    void equipItemChest(AbstractItemStack paramAbstractItemStack);
    
    void equipItemLegs(AbstractItemStack paramAbstractItemStack);
    
    void equipItemFeet(AbstractItemStack paramAbstractItemStack);
    
    void equipItemMainHand(AbstractItemStack paramAbstractItemStack);
    
    void equipItemOffHand(AbstractItemStack paramAbstractItemStack);
    
    void setDamage(double paramDouble);
    
    void setMovementSpeed(double paramDouble);
    
    void setAttackSpeed(double paramDouble);
    
    int getLuck();
    
    void setMetadata(String paramString, Object paramObject);
    
    boolean hasMetadata(String paramString);
    
    void removeMetadata(String paramString);
    
    Optional<Object> getMetadata(String paramString);
    
    boolean isLoaded();
    
    void setAI(boolean paramBoolean);

    void setVelocity(AbstractVector paramAbstractVector);
  
    int getEnchantmentLevel(String paramString);
    
    int getEnchantmentLevelHeld(String paramString);
}