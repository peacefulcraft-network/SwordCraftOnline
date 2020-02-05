package net.peacefulcraft.sco.mythicmobs.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractItemStack;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractVector;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractWorld;

/** This file was directly copy and pasted. Minimal reformating */
public class BukkitEntity implements AbstractEntity {
    private final Entity entityRef;

    public BukkitEntity(Entity e) {
        this.entityRef = e;
    }

    public Entity getBukkitEntity() {
        Entity entity = this.entityRef;
        if (entity != null)
            return entity;
        return null;
    }

    public LivingEntity getEntityAsLiving() {
        return (LivingEntity) getBukkitEntity();
    }

    public Creature getEntityAsCreature() {
        return (Creature) getBukkitEntity();
    }

    public Player getEntityAsPlayer() {
        return (Player) getBukkitEntity();
    }

    public AbstractLocation getLocation() {
        return BukkitAdapter.adapt(getBukkitEntity().getLocation());
    }

    public AbstractWorld getWorld() {
        return getLocation().getWorld();
    }

    public boolean isLiving() {
        return getBukkitEntity() instanceof LivingEntity;
    }

    public boolean isCreature() {
        return getBukkitEntity() instanceof Creature;
    }

    public boolean isMonster() {
        return getBukkitEntity() instanceof org.bukkit.entity.Monster;
    }

    public boolean isPlayer() {
        return getBukkitEntity() instanceof Player;
    }

    public UUID getUniqueId() {
        return getBukkitEntity().getUniqueId();
    }

    public boolean hasLineOfSight(AbstractEntity e) {
        return ((LivingEntity) getBukkitEntity()).hasLineOfSight(e.getBukkitEntity());
    }

    public void teleport(AbstractLocation l) {
        getBukkitEntity().teleport(BukkitAdapter.adapt(l));
    }

    public double getHealth() {
        return getEntityAsLiving().getHealth();
    }

    public boolean isDead() {
        return getBukkitEntity().isDead();
    }

    public boolean isValid() {
        return getBukkitEntity().isValid();
    }

    public boolean remove() {
        Entity entity = this.entityRef;
        if (entity != null) {
            entity.remove();
            return entity.isDead();
        }
        return true;
    }

    public double getMaxHealth() {
     return getEntityAsLiving().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
   }
   
   public void setFireTicks(int ticks) {
     getBukkitEntity().setFireTicks(ticks);
   }
   
   public String getCustomName() {
     return getEntityAsLiving().getCustomName();
   }
   
   public void addPotionEffect(PotionEffect effect) {
     getEntityAsLiving().addPotionEffect(effect);
   }
   
   public void setPassenger(Entity entity) {
     getBukkitEntity().getPassengers().add(entity);
   }
   
   public void setGravity(boolean bool) {
     getBukkitEntity().setGravity(bool);
   }
   
   public void setAI(boolean bool) {
     if (isLiving())
       getEntityAsLiving().setAI(bool); 
   }
   
   public AbstractLocation getEyeLocation() {
     if (isLiving())
       return BukkitAdapter.adapt(getEntityAsLiving().getEyeLocation()); 
     return BukkitAdapter.adapt(getBukkitEntity().getLocation());
   }
   
   public double getEyeHeight() {
     if (isLiving())
       return getEntityAsLiving().getEyeHeight(); 
     return 0.0D;
   }
   
   public AbstractEntity getTarget() {
     if (isCreature())
       return BukkitAdapter.adapt((Entity)getEntityAsCreature().getTarget()); 
     if (getBukkitEntity().getLastDamageCause() != null)
       return BukkitAdapter.adapt(getBukkitEntity().getLastDamageCause().getEntity()); 
     return null;
   }
   
   public List<AbstractEntity> getPassengers() {
     List<Entity> ls = getBukkitEntity().getPassengers();
     if (ls != null) {
       List<AbstractEntity> out = new ArrayList<AbstractEntity>();
        for(Entity e : ls) {
            out.add(BukkitAdapter.adapt(e));
        }
        return out;
     }
     return null;
   }
   
   public AbstractEntity getVehicle() {
     Entity e = getBukkitEntity().getVehicle();
     if (e != null)
       return BukkitAdapter.adapt(e); 
     return null;
   }
   
   public void eject() {
     getBukkitEntity().eject();
   }
   
   public void setHealth(double d) {
     Entity e = getBukkitEntity();
     if (e instanceof LivingEntity)
       ((LivingEntity)e).setHealth(d); 
   }
   
   public String getName() {
     if (isPlayer())
       return getEntityAsPlayer().getName(); 
     if (getBukkitEntity().getName() != null)
       return getBukkitEntity().getName(); 
     return getBukkitEntity().getCustomName();
   }
   
   public void setMaxHealth(double health) {
     getEntityAsLiving().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
   }
   
   public void damage(float damage) {
     getEntityAsLiving().damage(damage);
   }
   
   public void setPassenger(AbstractEntity entity) {
     getBukkitEntity().getPassengers().add(BukkitAdapter.adapt(entity));
   }
   
   public AbstractPlayer asPlayer() {
     if (getBukkitEntity() instanceof Player)
       return new BukkitPlayer(getEntityAsPlayer()); 
     return null;
   }
   
   public boolean equals(Object o) {
     if (o instanceof BukkitEntity)
       return getUniqueId().equals(((BukkitEntity)o).getUniqueId()); 
     return super.equals(o);
   }
   
   public int hashCode() {
     return getBukkitEntity().getUniqueId().hashCode();
   }
   
   public void setNoDamageTicks(int ticks) {
     if (isLiving())
       ((LivingEntity)getBukkitEntity()).setNoDamageTicks(ticks); 
   }
   
   public boolean isCitizensNPC() {
     return getBukkitEntity().hasMetadata("NPC");
   }
   
   public boolean isAnimal() {
     return getBukkitEntity() instanceof org.bukkit.entity.Animals;
   }
   
   public boolean isWaterMob() {
     return getBukkitEntity() instanceof org.bukkit.entity.WaterMob;
   }
   
   public boolean isFlyingMob() {
     return getBukkitEntity() instanceof org.bukkit.entity.Flying;
   }
   
   public boolean isGliding() {
     return ((LivingEntity)getBukkitEntity()).isGliding();
   }
   
   public boolean hasPotionEffect(String type) {
     return hasPotionEffect(type, null, null);
   }
   
   public boolean hasPotionEffect(String type, Double level, Double duration) {
     if (isLiving()) {
       LivingEntity e = (LivingEntity)getBukkitEntity();
       PotionEffectType potiontype = PotionEffectType.getByName(type);
       if (e.hasPotionEffect(potiontype)) {
         PotionEffect effect = e.getPotionEffect(potiontype);
         if (level != null && !level.equals(Integer.valueOf(effect.getAmplifier())))
           return false; 
         if (duration != null && !duration.equals(Integer.valueOf(effect.getDuration())))
           return false; 
         return true;
       } 
     } 
     return false;
   }
   
   public boolean hasPotionEffect() {
     if (isLiving()) {
       LivingEntity e = (LivingEntity)getBukkitEntity();
       return (e.getActivePotionEffects().size() > 0);
     } 
     return false;
   }
   
   public boolean hasScoreboardTag(String tag) {
     return getBukkitEntity().getScoreboardTags().contains(tag);
   }
   
   public void addScoreboardTag(String tag) {
     getBukkitEntity().addScoreboardTag(tag);
   }
   
   public void removeScoreboardTag(String tag) {
     getBukkitEntity().removeScoreboardTag(tag);
   }
   
   public void equipItemHead(AbstractItemStack item) {
     if (!isLiving())
       return; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     EntityEquipment ee = le.getEquipment();
     ee.setHelmet(BukkitAdapter.adapt(item));
   }
   
   public void equipItemChest(AbstractItemStack item) {
     if (!isLiving())
       return; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     EntityEquipment ee = le.getEquipment();
     ee.setChestplate(BukkitAdapter.adapt(item));
   }
   
   public void equipItemLegs(AbstractItemStack item) {
     if (!isLiving())
       return; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     EntityEquipment ee = le.getEquipment();
     ee.setLeggings(BukkitAdapter.adapt(item));
   }
   
   public void equipItemFeet(AbstractItemStack item) {
     if (!isLiving())
       return; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     EntityEquipment ee = le.getEquipment();
     ee.setBoots(BukkitAdapter.adapt(item));
   }
   
    public void equipItemMainHand(AbstractItemStack item) {
        if (!isLiving()) {
        return; 
        }
        LivingEntity le = (LivingEntity)getBukkitEntity();
        EntityEquipment ee = le.getEquipment();
        ee.setItemInMainHand(BukkitAdapter.adapt(item));
    }
   
   public void equipItemOffHand(AbstractItemStack item) {
     if (!isLiving()) {
       return; 
     }    
     LivingEntity le = (LivingEntity)getBukkitEntity();
     EntityEquipment ee = le.getEquipment();
     ee.setItemInOffHand(BukkitAdapter.adapt(item));
   }
   
   public void setDamage(double damage) {
     if (!isLiving()) { return; }
     LivingEntity le = (LivingEntity)getBukkitEntity();
     if (le.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
       le.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage); 
     }
   }
   
   public double getDamage() {
     if (!isLiving())
       return 0.0D; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     if (le.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
       return le.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue(); 
     }
     return 0.0D;
   }
   
   public void setMovementSpeed(double speed) {
     if (!isLiving())
       return; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
   }
   
   public void setAttackSpeed(double speed) {
     if (!isLiving())
       return; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     if (le.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
       le.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed); 
   }
   
   public int getLuck() {
     if (!isLiving())
       return 0; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     double luck = 0.0D;
     if (le.getAttribute(Attribute.GENERIC_LUCK) != null)
       luck = le.getAttribute(Attribute.GENERIC_LUCK).getBaseValue(); 
     luck += le.getEquipment().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK);
     for (PotionEffect pe : le.getActivePotionEffects()) {
       if (pe.getType() == PotionEffectType.LUCK) {
         luck += pe.getAmplifier();
         continue;
       } 
       if (pe.getType() == PotionEffectType.UNLUCK)
         luck -= pe.getAmplifier(); 
     } 
     return (int)luck;
   }
   
   public int getEnchantmentLevel(String enchantmentName) {
     Enchantment enchant;
     if (!isLiving())
       return 0; 
     try {
       enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));
     } catch (Exception ex) {
       return 0;
     } 
     if (enchant == null)
       return 0; 
     LivingEntity le = (LivingEntity)getBukkitEntity();
     int level = 0;
     level += le.getEquipment().getItemInMainHand().getEnchantmentLevel(enchant);
     level += le.getEquipment().getItemInOffHand().getEnchantmentLevel(enchant); 
     for (ItemStack item : le.getEquipment().getArmorContents())
       level += item.getEnchantmentLevel(enchant); 
     return level;
   }
   
   public int getEnchantmentLevelHeld(String enchantmentName) {
     Enchantment enchant;
     if (!isLiving()) {
       return 0; 
     }
     try {
       enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));
     } catch (Exception ex) {
       return 0;
     } 
     if (enchant == null) { return 0; }

     LivingEntity le = (LivingEntity)getBukkitEntity();
     return le.getEquipment().getItemInMainHand().getEnchantmentLevel(enchant);
   }
   
   public void setMetadata(String key, Object value) {
     Entity e = getBukkitEntity();
     e.setMetadata(key, (MetadataValue)new FixedMetadataValue(SwordCraftOnline.getPluginInstance(), value));
   }
   
   public boolean hasMetadata(String key) {
     Entity e = getBukkitEntity();
     return e.hasMetadata(key);
   }
   
   public void removeMetadata(String key) {
     Entity e = getBukkitEntity();
     e.removeMetadata(key, SwordCraftOnline.getPluginInstance());
   }
   
   public synchronized boolean isLoaded() {
     Entity e = getBukkitEntity();
     return e.getLocation().getChunk().isLoaded();
   }
   
   public Optional<Object> getMetadata(String key) {
     Entity e = getBukkitEntity();
     List<MetadataValue> values = e.getMetadata(key);
     for (MetadataValue mv : values) {
       if (mv.getOwningPlugin().equals(SwordCraftOnline.getPluginInstance()))
         return Optional.of(mv.value()); 
     } 
     return Optional.empty();
   }
   
   public void setVelocity(AbstractVector velocity) {
     Entity e = getBukkitEntity();
     Vector v = BukkitAdapter.adapt(velocity);
     e.setVelocity(v);
   }
}