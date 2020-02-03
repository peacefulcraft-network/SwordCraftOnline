package net.peacefulcraft.sco.gamehandle.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.drops.Conditions;
import net.peacefulcraft.sco.mythicmobs.drops.DropManager;
import net.peacefulcraft.sco.mythicmobs.drops.LootBag;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class MythicMobDeathEvent implements Listener {    
    @EventHandler
    public void MythicMobDeath(EntityDeathEvent e) {
        Entity ent = e.getEntity();
        e.setDroppedExp(0);
        //Range to detect nearby players. Changed if has condition.
        int range = 20;

        //Removing normal drops.
        Iterator<ItemStack> itr = e.getDrops().iterator();
        while(itr.hasNext()) {
            ItemStack item = itr.next();
            if(item != null && !item.getType().equals(Material.AIR)) {
                itr.remove();
            }
        }

        if(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().containsKey(ent.getUniqueId())) {
            
            MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

            if(ent.getLastDamageCause() instanceof Player) {
                Player p = (Player) ent.getLastDamageCause();
                //Checking Droptable conditions
                if(mm.getDropTable().hasConditions()) {
                    HashMap<String, String> conditions = mm.getDropTable().getConditions();
                    for(String c : conditions.keySet()) {
                        switch(Conditions.valueOf(c.toUpperCase())) {
                            case PLAYER_WITHIN:
                            range = Integer.valueOf(conditions.get(c));
                            if(ent.getNearbyEntities(range, range, range).isEmpty()) { return; }

                            break; case IN_BIOME:
                            Biome biome = ent.getWorld().getBiome((int)ent.getLocation().getX(), (int)ent.getLocation().getZ());
                            if(biome != Biome.valueOf(conditions.get(c))) { return; }

                            break; case KILLED_WITH:
                            ItemStack item = p.getInventory().getItemInMainHand();
                            ItemStack killedWith = ItemIdentifier.generate(conditions.get(c));
                            if(!item.equals(killedWith)) { return; }
                        }
                    }
                }

                //Checking for SCOPlayer killer.
                SCOPlayer s = GameManager.findSCOPlayer((Player)ent.getLastDamageCause());
                if(s == null) { return; }

                LootBag bag = mm.getDropTable().generate(s);
                DropManager.drop(ent.getLocation(), bag);

                SwordCraftOnline.getPluginInstance().getMobManager().unregisterActiveMob(ent.getUniqueId());
            } else {
                //If cause of death wasn't player. Check nearby for players.
                List<Entity> l = ent.getNearbyEntities(range, range, range);
                for(Entity temp : l) {
                    if(temp instanceof Player) {
                        SCOPlayer s = GameManager.findSCOPlayer((Player)temp);
                        if(s == null) { return; }

                        if(mm.getDropTable().hasConditions()) {
                            HashMap<String, String> conditions = mm.getDropTable().getConditions();
                            for(String c : conditions.keySet()) {
                                switch(Conditions.valueOf(c.toUpperCase())) {
                                    case PLAYER_WITHIN:
                                    range = Integer.valueOf(conditions.get(c));
                                    if(ent.getNearbyEntities(range, range, range).isEmpty()) { return; }
        
                                    break; case IN_BIOME:
                                    Biome biome = ent.getWorld().getBiome((int)ent.getLocation().getX(), (int)ent.getLocation().getZ());
                                    if(biome != Biome.valueOf(conditions.get(c))) { return; }
                                    
                                    break; case KILLED_WITH:
                                    ItemStack item = ((Player)temp).getInventory().getItemInMainHand();
                                    ItemStack killedWith = ItemIdentifier.generate(conditions.get(c));
                                    if(!item.equals(killedWith)) { return; }
                                }
                            }
                        }

                        LootBag bag = mm.getDropTable().generate(s);
                        DropManager.drop(ent.getLocation(), bag);

                        SwordCraftOnline.getPluginInstance().getMobManager().unregisterActiveMob(ent.getUniqueId());
                    }
                }
            }
        }
    }
}