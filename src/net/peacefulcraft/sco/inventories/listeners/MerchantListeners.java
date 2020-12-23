package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.AlchemistInventory;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class MerchantListeners implements Listener {
    private final String alchemistTag = ChatColor.BLUE + "[" + ChatColor.GOLD + "Bill the Alchemist" + ChatColor.BLUE + "]";
    
    @EventHandler
    public void interactVillager(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        //If mob is villager and mob is mm merchant
        if(!(entity instanceof Villager)) { return; }
        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().containsKey(entity.getUniqueId()))) { return; }

        SCOPlayer s = GameManager.findSCOPlayer(e.getPlayer());
        if(s == null) { return; }

        if(entity.getCustomName().equalsIgnoreCase("Alchemist")) {
            new AlchemistInventory(s).openInventory(s);
        }
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) { return; }
        SCOPlayer s = GameManager.findSCOPlayer((Player) e.getViewers().get(0));
        if(s == null) { return; }
        
        if(e.getView().getTitle().equalsIgnoreCase("Alchemist Shop")) {
            e.setCancelled(true);
            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
            priceCheck(s, itemName, e.getView());
            return;
        }
    }

    private void priceCheck(SCOPlayer s, String item, InventoryView inv) {
        String[] split = item.split(" - ");
        int price = Integer.valueOf(split[1].replace(",", ""));
        
        if(!(s.withdrawWallet(price))) {
            s.getPlayer().sendMessage(alchemistTag + " Seems you don't have enough money for my wares... Come back with more coin...");
            return;
        }
        ItemStack bought = ItemIdentifier.generateItem(split[0], ItemTier.COMMON, 1);
        if(bought == null || bought.getType().equals(Material.AIR)) { return; }

        if(!(s.getPlayer().getInventory().addItem(bought).isEmpty())) {
            s.getPlayer().getWorld().dropItemNaturally(s.getPlayer().getLocation(), bought);
        }
        return;
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        SCOPlayer s = GameManager.findSCOPlayer((Player)e.getPlayer());
        if(s == null) { return; }

        if(e.getView().getTitle().equalsIgnoreCase("Alchemist Shop")) {
            s.getPlayer().sendMessage(alchemistTag + " Enjoy ye' wares.");
        }
    }
}