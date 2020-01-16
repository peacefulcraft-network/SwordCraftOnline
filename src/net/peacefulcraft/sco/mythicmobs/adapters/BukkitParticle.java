package net.peacefulcraft.sco.mythicmobs.adapters;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public enum BukkitParticle {
    EXPLOSION_NORMAL(new String[] { "poof", "explode", "explosion", "explosion_small" }),
    EXPLOSION_LARGE(new String[] { "largeexplode", "largeexplosion" }),
    EXPLOSION_HUGE(new String[] { "explosion_emitter", "hugeexplode", "hugeexplosion" }),
    FIREWORKS_SPARK(new String[] { "firework", "fireworksspark" }),
    WATER_BUBBLE(new String[] { "bubble" }),
    WATER_SPLASH(new String[] { "splash" }),
    WATER_WAKE(new String[] { "fishing", "wake" }),
    SUSPENDED(new String[] { "underwater" }),
    SUSPENDED_DEPTH(new String[] { "underwater", "depthsuspend" }),
    CRIT(new String[] { "crit" }),
    CRIT_MAGIC(new String[] { "enchanted_hit", "magiccrit" }),
    SMOKE_NORMAL(new String[] { "smoke" }),
    SMOKE_LARGE(new String[] { "large_smoke", "largesmoke" }),
    SPELL(new String[] { "effect" }),
    SPELL_INSTANT(new String[] { "instant_effect", "instantSpell" }),
    SPELL_MOB(new String[] { "entity_effect", "mobSpell" }),
    SPELL_MOB_AMBIENT(new String[] { "ambient_entity_effect", "mobSpellAmbient" }),
    SPELL_WITCH(new String[] { "witch", "witchMagic" }),
    DRIP_WATER(new String[] { "dripping_water", "dripWater" }),
    DRIP_LAVA(new String[] { "dripping_lava", "dripLava" }),
    VILLAGER_ANGRY(new String[] { "angry_villager", "angryVillager" }),
    VILLAGER_HAPPY(new String[] { "happy_villager", "happyVillager" }),
    TOWN_AURA(new String[] { "mycelium", "townaura" }),
    NOTE(new String[] { "note" }),
    PORTAL(new String[] { "portal" }),
    ENCHANTMENT_TABLE(new String[] { "enchant", "enchantmenttable", "enchantingtable" }),
    FLAME(new String[] { "flame" }),
    LAVA(new String[] { "lava" }),
    CLOUD(new String[] { "cloud" }),
    REDSTONE(new String[] { "dust", "reddust" }),
    SNOWBALL(new String[] { "item_snowball", "snowballpoof" }),
    SNOW_SHOVEL(new String[] { "item_snowball", "snowshovel" }),
    SLIME(new String[] { "item_slime" }),
    HEART(new String[] { "heart" }),
    BARRIER(new String[] { "barrier" }),
    ITEM_CRACK(new String[] { "item", "iconcrack", "itemcrack" }),
    BLOCK_CRACK(new String[] { "block", "blockcrack" }),
    BLOCK_DUST(new String[] { "dust", "blockdust" }),
    WATER_DROP(new String[] { "rain", "droplet" }),
    MOB_APPEARANCE(new String[] { "elder_guardian", "mobappearance" }),
    DRAGON_BREATH(new String[] { "dragon_breath", "dragonbreath" }),
    END_ROD(new String[] { "end_rod", "endRod" }),
    DAMAGE_INDICATOR(new String[] { "damage_indicator", "damageIndicator" }),
    SWEEP_ATTACK(new String[] { "sweep_attack", "sweepAttack" }),
    FALLING_DUST(new String[] { "falling_dust", "fallingDust" }),
    TOTEM(new String[] { "totem_of_undying" }),
    SPIT(new String[] { "spit" }),
    SQUID_INK(new String[] { "squid_ink", "squidink" }),
    BUBBLE_POP(new String[] { "bubble_pop", "bubblepop" }),
    CURRENT_DOWN(new String[] { "current_down", "currentdown" }),
    BUBBLE_COLUMN_UP(new String[] { "bubble_column_up", "bubblecolumn", "bubble_column" }),
    NAUTILUS(new String[] { "nautilus" }),
    DOLPHIN(new String[] { "dolphin" }),
    COMPOSTER(new String[] { "composter" }),
    FALLING_LAVA(new String[] { "fallinglava", "falling_lava" }),
    FALLING_WATER(new String[] { "fallingwater", "falling_water" }),
    FLASH(new String[] { "flash" }),
    LANDING_LAVA(new String[] { "landinglava", "landing_lava" }),
    SNEEZE(new String[] { "sneeze" });
    
    private static final Map<String, BukkitParticle> PARTICLE_ALIASES;
    
    private final String[] aliases;
    
    static {
      PARTICLE_ALIASES = new HashMap<>();
      for (BukkitParticle particle : values()) {
        PARTICLE_ALIASES.put(particle.toString(), particle);
        for (String alias : particle.getAliases())
          PARTICLE_ALIASES.put(alias.toUpperCase(), particle); 
      } 
    }
    
    public static BukkitParticle get(String key) {
      BukkitParticle particle = PARTICLE_ALIASES.getOrDefault(key.toUpperCase(), null);
      if (particle == null) {
        //Log errors MythicLogger.errorGenericConfig("Particle '" + key + "' is not supported by this version of MythicMobs.");
        return CLOUD;
      } 
      return particle;
    }
    
    public String[] getAliases() {
      return this.aliases;
    }
    
    BukkitParticle(String... aliases) {
      this.aliases = aliases;
    }
    
    public Particle toBukkitParticle() {
      return Particle.valueOf(toString());
    }
    
    public boolean requiresData() {
      return !toBukkitParticle().getDataType().equals(Void.class);
    }
    
    public boolean validateData(Object obj) {
      Particle particle = toBukkitParticle();
      if (particle.getDataType().equals(Void.class))
        return false; 
      if (particle.getDataType().equals(ItemStack.class))
        return obj instanceof ItemStack; 
      if (particle.getDataType() == BlockData.class)
        return obj instanceof BlockData; 
      if (particle.getDataType() == Particle.DustOptions.class)
        return obj instanceof Particle.DustOptions; 
      return true;
    }
    
    public Object parseDataOptions(MythicLineConfig config) {
      Particle particle = toBukkitParticle();
      if (particle.getDataType().equals(ItemStack.class)) {
        String strMaterial = config.getString(new String[] { "material", "m" }, "STONE", new String[0]);
        try {
          return new ItemStack(Material.matchMaterial(strMaterial.toUpperCase()));
        } catch (Exception ex) {
          return new ItemStack(Material.STONE);
        } 
      } 
      if (particle.getDataType().equals(BlockData.class)) {
        String strMaterial = config.getString(new String[] { "material", "m" }, "STONE", new String[0]);
        try {
          Material material = Material.matchMaterial(strMaterial.toUpperCase());
          return Bukkit.getServer().createBlockData(material);
        } catch (Exception ex) {
          return Bukkit.getServer().createBlockData(Material.STONE);
        } 
      } 
      if (particle.getDataType().equals(MaterialData.class)) {
        String strMaterial = config.getString(new String[] { "material", "m" }, "STONE", new String[0]);
        try {
          Material.matchMaterial(strMaterial.toUpperCase()).getData();
        } catch (Exception ex) {
          return Material.STONE.getData();
        } 
      } 
      if (particle.getDataType().equals(Particle.DustOptions.class)) {
        String strColor = config.getString(new String[] { "color", "c" }, "#FF0000", new String[0]);
        Color color = Color.decode(strColor);
        float size = config.getFloat(new String[] { "size" }, 1.0F);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        Color c = Color.fromRGB(r, g, b);
        return new Particle.DustOptions(c, size);
      } 
      return null;
    }
    
    public void send(AbstractLocation location) {
      Particle particle = toBukkitParticle();
      Location loc = BukkitAdapter.adapt(location);
      for (Player player : Bukkit.getOnlinePlayers())
        player.spawnParticle(particle, loc, 1, 0.0D, 0.0D, 0.0D, 0.0D); 
    }
    
    public void send(AbstractPlayer target, AbstractLocation location, float speed, int amount, float offsetX, float offsetY, float offsetZ) {
      Particle particle = toBukkitParticle();
      Location loc = BukkitAdapter.adapt(location);
      Player player = (Player)target.getBukkitEntity();
      player.spawnParticle(particle, loc, amount, offsetX, offsetY, offsetZ, speed);
    }
    
    public void send(AbstractLocation location, float speed, int amount, float offsetX, float offsetY, float offsetZ) {
      Particle particle = toBukkitParticle();
      Location loc = BukkitAdapter.adapt(location);
      for (Player player : Bukkit.getOnlinePlayers()) {
        if (player.getWorld().equals(loc.getWorld()))
          player.spawnParticle(particle, loc, amount, offsetX, offsetY, offsetZ, speed); 
      } 
    }
    
    public void send(AbstractLocation location, float speed, int amount, float offsetX, float offsetY, float offsetZ, Object data) {
      if (!validateData(data)) {
        MythicMobs.error("Could not send particle: invalid particle data supplied.");
        return;
      } 
      Particle particle = toBukkitParticle();
      Location loc = BukkitAdapter.adapt(location);
      for (Player player : Bukkit.getOnlinePlayers()) {
        if (player.getWorld().equals(loc.getWorld()))
          player.spawnParticle(particle, loc, amount, offsetX, offsetY, offsetZ, speed, data); 
      } 
    }
    
    public void sendDirectional(AbstractLocation location, float speed, int amount, float offsetX, float offsetY, float offsetZ, AbstractVector direction) {
      Particle particle = toBukkitParticle();
      Location loc = BukkitAdapter.adapt(location);
      for (int i = 0; i < amount; i++) {
        Location ln = loc.clone().add((0.0F - offsetX) + MythicMobs.r.nextDouble() * offsetX * 2.0D, (offsetY - offsetY) + MythicMobs.r.nextDouble() * offsetY * 2.0D, (0.0F - offsetZ) + MythicMobs.r.nextDouble() * offsetZ * 2.0D);
        for (Player player : Bukkit.getOnlinePlayers()) {
          if (player.getWorld().equals(loc.getWorld()))
            player.spawnParticle(particle, ln, 0, (float)direction.getX(), (float)direction.getY(), (float)direction.getZ(), speed); 
        } 
      } 
    }
    
    public void sendLegacyColored(AbstractLocation location, float speed, int amount, float offsetX, float offsetY, float offsetZ, Color color) {
      Particle particle = toBukkitParticle();
      Location loc = BukkitAdapter.adapt(location);
      float r = color.getRed() / 255.0F;
      float g = color.getGreen() / 255.0F;
      float b = color.getBlue() / 255.0F;
      if (r < 1.17549435E-38F)
        r = 1.17549435E-38F; 
      for (int i = 0; i < amount; i++) {
        Location ln = loc.clone().add((0.0F - offsetX) + MythicMobs.r.nextDouble() * offsetX * 2.0D, (offsetY - offsetY) + MythicMobs.r.nextDouble() * offsetY * 2.0D, (0.0F - offsetZ) + MythicMobs.r.nextDouble() * offsetZ * 2.0D);
        for (Player player : Bukkit.getOnlinePlayers()) {
          if (player.getWorld().equals(loc.getWorld()))
            player.spawnParticle(particle, ln, 0, r, g, b, (speed > 0.0F) ? speed : 1.0D); 
        } 
      } 
    }
  }
 