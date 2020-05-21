package net.peacefulcraft.sco.mythicmobs.mobs.bosses;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.utilities.TeleportUtil;

public class BossWarden implements MythicBoss {
    private final String nameplate = ChatColor.DARK_RED + "[" + ChatColor.BLUE + "The Warden" + ChatColor.DARK_RED + "]";
    
    private ActiveMob am;
        public ActiveMob getActiveMob() { return this.am; }
        public Location getLocation() { return this.am.getBukkitLocation(); }

    private MythicMob mm;
        public MythicMob getMythicMob() { return this.mm; }

    /**
     * Handles bosses unique events, i.e. special attack sequences.
     * Runs every 0.5 seconds by default.
     */
    private Runnable sequenceTask;

    private int level;
        public int getLevel() { return this.level; }

    /**Holds wardens crystals in a map */
    private HashMap<Integer, ActiveMob> crystals;

    private int powerModifier;

    private boolean inDungeon = false;

    private boolean isUsing;

    /**
     * Constructor for boss instance. Initializes all necessary variables
     * @param level must be above 20. Any level in intervals of 2 over this increases difficulty of boss
     */
    public BossWarden(int level) {
        this.level = level;
        this.crystals = new HashMap<>();
        this.isUsing = false;
        //Determining different factors based on level over 50
        this.powerModifier = (level - 20) / 2;
        this.sequenceTask = new Runnable(){
            @Override
            public void run() {
                phaseSelection();
            }
        };
    }

    /**
     * Constructor for boss in dungeon instance. 
     */
    public BossWarden(ArrayList<Player> players) {
        this(20 + (players.size() * 2));
        this.inDungeon = true;
    }

    @Override
    public MythicBoss spawn(Location loc) {
        this.am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob("BossWarden", loc, this.level);
        this.am.updateBossBar();

        //Creates crystals around the boss
        this.crystals.put(1, SwordCraftOnline.getPluginInstance().getMobManager().spawnMob("WardenCrystal", loc.add(5, 1, 0)));
        this.crystals.put(2, SwordCraftOnline.getPluginInstance().getMobManager().spawnMob("WardenCrystal", loc.subtract(5, 1, 0)));
        this.crystals.put(3, SwordCraftOnline.getPluginInstance().getMobManager().spawnMob("WardenCrystal", loc.add(0, 1, 5)));
        this.crystals.put(4, SwordCraftOnline.getPluginInstance().getMobManager().spawnMob("WardenCrystal", loc.subtract(0, 1, 5)));

        phaseSelection();

        return this;
    }

    @Override
    public void phaseSelection() {
        int select = SwordCraftOnline.r.nextInt(3);

        /**Clearing all beam locations */
        for(ActiveMob crystal : crystals.values()) {
            ((EnderCrystal)crystal.getEntity().getBukkitEntity()).setBeamTarget(null);
        }

        switch(select) {
            case 0:
                ((EnderCrystal)crystals.get(0).getEntity().getBukkitEntity()).setBeamTarget(am.getBukkitLocation());
                this.isUsing = true;
                abilityZero();
            break;case 1:
                ((EnderCrystal)crystals.get(1).getEntity().getBukkitEntity()).setBeamTarget(am.getBukkitLocation());
                this.isUsing = true;
                abilityOne();
            break;case 2:
                ((EnderCrystal)crystals.get(2).getEntity().getBukkitEntity()).setBeamTarget(am.getBukkitLocation());
                this.isUsing = true;
                abilityTwo();
        }

    }

    /**
     * Wardens defensive ability. Increases armor and armor toughness by 3x for 10 seconds
     * with a 8 - powerModifier cooldown
     */
    private void abilityZero() {
        double armor = this.am.getArmor();
        this.am.setArmor(3 + this.powerModifier, true);

        double toughness = this.am.getArmorToughness();
        this.am.setArmorToughness(3 + this.powerModifier, true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
            public void run() {
                am.setArmor(armor, false);
                am.setArmor(toughness, false);
            }
        }, 200);

        //Selects new phase after (8 - powermodifier) seconds
        this.isUsing = false;
        Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), sequenceTask, 160 - (this.powerModifier * 20));
    }
    
    /**
     * Wardens summoning ability. Summons 7 + powerModifier amount of minions
     * at level half of bosses + 3*powerModifier.
     * Gives Warden regen 2 + powerModifier for 8 seconds
     */
    private void abilityOne() {
        for(int i = 0; i <= (7 + this.powerModifier); i++) {
            int r = SwordCraftOnline.r.nextInt(7);
            int x = 0;
            if(r != 0) { x += SwordCraftOnline.r.nextInt(r); }
            int z = (int)Math.sqrt(Math.pow(r,2) - Math.pow(x,2));
            if(SwordCraftOnline.r.nextBoolean()) { x *= -1; }
            if(SwordCraftOnline.r.nextBoolean()) { z *= -1; }

            Location loc = this.am.getBukkitLocation().add(x, 0, z);
            if(!TeleportUtil.safeTeleport(loc)) { continue; }

            int level = (this.level/2) + (3*this.powerModifier);
            
            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob("WardenGuard", loc, level);
            if(am == null) {
                SwordCraftOnline.logInfo("[Boss Warden] Error spawning minion");
            }
        }

        this.am.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 2 + this.powerModifier));

        //Selects new phase after (12 - powermodifier) seconds
        this.isUsing = false;
        Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), sequenceTask, 240 - (this.powerModifier * 20));
    }

    /**
     * Slows all nearby entities and gives warden strength
     */
    private void abilityTwo() {
        for(Entity e : this.am.getLivingEntity().getNearbyEntities(20, 20, 20)) {
            if(e instanceof LivingEntity) {
                ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1 + this.powerModifier));
            }
            if(e instanceof Player) {
                ((Player)e).sendMessage(nameplate + ": " + ChatColor.RED + " GET IN YOUR CELLS");
            }
        }
        
        this.am.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 240, 2 + this.powerModifier));

        //Selects new phase after (14 - powerModifier) seconds
        this.isUsing = false;
        Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), sequenceTask, 280 - (this.powerModifier * 20));
    }
}