package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.SwordSkillTome;

public class ItemDropOnDeath implements Listener {

    @EventHandler
    public void ItemDrop(PlayerDeathEvent e) {
        for(ItemStack item : e.getDrops()) {
            if(item == (new SwordSkillTome()).create()) {
                
            }
        }
    }
}