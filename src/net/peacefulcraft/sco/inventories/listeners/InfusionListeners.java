package net.peacefulcraft.sco.inventories.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InfusionInventory;
import net.peacefulcraft.sco.items.ItemTier;

public class InfusionListeners implements Listener {
    
    /**
     * n/10 chance the infusion fails
     */
    private final int FAIL_CHANCE = 2;

    /*
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        ItemStack clicked = e.getCurrentItem();
        Inventory inv = e.getInventory();

        if(clicked == null) { return; }
        if(!e.getView().getTitle().equalsIgnoreCase("Infusion Table")) { return; }

        // Cancelling if they hit a blocked slot
        NBTItem nbti = new NBTItem(clicked);
        if(nbti.hasKey("movable") && nbti.getBoolean("movable") == false) {
            e.setCancelled(true);
        }

        // Verifying there are skills in input slots
        // and setting indicators
        boolean checkedIng = checkIngredientSlots(inv);
        setValidInfusionSlots(inv, checkedIng);

        // Checking if clicked on start button
        if(checkedIng && clicked.getType().equals(Material.PURPLE_STAINED_GLASS_PANE) 
            && clicked.getItemMeta().getDisplayName().equalsIgnoreCase("Click to begin infusion")) {
            
            beginInfusion(inv);
        }
    }
    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(!e.getView().getTitle().equalsIgnoreCase("Infusion Table")) { return; }

        clearIngredients(e.getInventory(), p, true);
    }
    */

    /**
     * Handling when player right clicks enchanting table
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }

        Block b = e.getClickedBlock();
        if(!b.getType().equals(Material.ENCHANTING_TABLE)) { return; }

        // All checks pass. We create and open inventory
        (new InfusionInventory(s)).openInventory(s);
        e.setCancelled(true);
    }
    /*
    private boolean checkIngredientSlots(Inventory inv) {
        // Infusion requires catalyst. Not valid without.
        ItemStack catalyst = inv.getItem(11);
        if(catalyst == null || catalyst.getType().equals(Material.AIR)) { return false; }

        int skillCount = 0;
        for(int row = 3; row <= 4; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = inv.getItem(row * 9 + col);
                if(item == null || item.getType().equals(Material.AIR)) { continue; }

                // Checking if item is sword skill
                NBTItem nbti = new NBTItem(item);
                if(nbti.hasKey("sword_skill") && nbti.getBoolean("sword_skill")) {
                    skillCount += item.getAmount();
                } else {
                    // We only want sword skills
                    return false;
                }
            }
        }

        return skillCount >= 1;
    }

    private void clearIngredients(Inventory inv, Player p, boolean returnItems) {
        ArrayList<ItemStack> leftovers = new ArrayList<>();
        for(int row = 3; row <= 4; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = inv.getItem(row * 9 + col);
                if(item == null || item.getType().equals(Material.AIR)) { continue; }

                // Adding items to inv or leftover
                if(returnItems) {
                    HashMap<Integer,ItemStack> temp = p.getInventory().addItem(item);
                    for(ItemStack i : temp.values()) {
                        leftovers.add(i);
                    }
                }
            } 
        }

        // Dropping item at player or consuming items
        // If consuming, it leaves catalyst slot
        if(returnItems) {
            for(ItemStack item : leftovers) {
                p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
            }
        }
    }

    private ArrayList<ItemStack> getIngredients(Inventory inv) {
        ArrayList<ItemStack> out = new ArrayList<>();

        for(int row = 3; row <= 4; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = inv.getItem(row * 9 + col);
                if(item == null || item.getType().equals(Material.AIR)) { 
                    continue; 
                }
                out.add(item);
            }
        }
        return out;
    }

    private void setValidInfusionSlots(Inventory inv, boolean isValid) {
        if(isValid) {
            inv.setItem(15, (new GreenSlotItem()).create(1, false, false));
            inv.setItem(23, (new GreenSlotItem()).create(1, false, false));
            inv.setItem(25, (new GreenSlotItem()).create(1, false, false));
            inv.setItem(34, (new GreenSlotItem()).create(1, false, false));
        } else {
            inv.setItem(15, (new RedSlotItem()).create(1, false, false));
            inv.setItem(23, (new RedSlotItem()).create(1, false, false));
            inv.setItem(25, (new RedSlotItem()).create(1, false, false));
            inv.setItem(33, (new RedSlotItem()).create(1, false, false));
        }
    }


    private void beginInfusion(Inventory inv) {
        // Determine if infusion failed right away
        boolean failed = SwordCraftOnline.r.nextInt(9) < FAIL_CHANCE;
        int fail_row = SwordCraftOnline.r.nextInt(5);         

        new BukkitRunnable(){
            int row = 5;

            @Override
            public void run() {
                if(failed && row == fail_row) {
                    resetProgressBar(inv, true);
                    this.cancel();
                }
                // Replacing row slots
                for(int col = 0; col <= 8; col++) {
                    ItemStack item = inv.getItem(row * 9 + col);
                    if(item.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
                        inv.setItem(row * 9 + col, (new BlueSlotItem()).create(1, false, false));
                    }
                }
                row--;
            }
        }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 0, 20);

        // Failed infusion. We consume items and quit
        if(failed) { 
            clearIngredients(inv, null, false);
            return; 
        }

        // Beginning of main infusion logic
        int infusionExp = 0;
        ArrayList<ItemStack> skills = getIngredients(inv);
        for(ItemStack skill : skills) {
            NBTItem nbti = new NBTItem(skill);
            if(nbti.hasKey("tier")) {
                ItemTier tier = ItemTier.valueOf(nbti.getString("tier"));
                switch(tier) {
                    case COMMON:
                        infusionExp += 10 * skill.getAmount();
                    break; case UNCOMMON:
                        infusionExp += 25 * skill.getAmount();
                    break; case RARE:
                        infusionExp += 50 * skill.getAmount();
                    break; case LEGENDARY:
                        infusionExp += 80 * skill.getAmount();
                    break; case ETHEREAL:
                        infusionExp += 115 * skill.getAmount();
                    break; case GODLIKE:
                        infusionExp += 160 * skill.getAmount();
                    default:
                        break;
                }
            }
        }
        
        // Calculating cost of infusion upgrade
        ItemStack catalyst = inv.getItem(11);
        NBTItem nbti = new NBTItem(catalyst);
        ItemTier catTier = ItemTier.valueOf(nbti.getString("tier"));

        // Progressing tier of item
        for(; infusionExp <= 0; infusionExp -= getInfusionCost(catTier)) {
            catTier = ItemTier.progressTier(catTier);
        }

        String name = ChatColor.stripColor(catalyst.getItemMeta().getDisplayName().replace(" ", ""));
        ItemStack result = (new SkillIdentifier(name, 1, catTier)).getProvider().getItem();

        inv.setItem(24, result);
    }

    private void resetProgressBar(Inventory inv, boolean isFail) {
        // If infusion failed we freeze rows and then re call reset
        if(isFail) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    resetProgressBar(inv, false);
                }
            }.runTaskLater(SwordCraftOnline.getPluginInstance(), SwordCraftOnline.r.nextInt(4) * 20);
        } else {
            new BukkitRunnable() {
                int row = 0;

                @Override
                public void run() {
                    if(row >= 5) {
                        this.cancel();
                        return;
                    }

                    for(int col = 0; col <= 8; col++) {
                        ItemStack item = inv.getItem(row * 9 + col);
                        if(item.getType().equals(Material.BLUE_STAINED_GLASS_PANE)) {
                            inv.setItem(row * 9 + col, (new BlackSlotItem()).create(1, false, false));
                        }                        
                    }
                    row++;
                }
            }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 0, 10);
        }
    }


    private int getInfusionCost(ItemTier tier) {
        int infusionCost = 0;
        switch(tier) {
            case COMMON:
                infusionCost = 35;
            break; case UNCOMMON:
                infusionCost = 88;
            break; case RARE:
                infusionCost = 175;
            break; case LEGENDARY:
                infusionCost = 280;
            break; case ETHEREAL:
                infusionCost = 403;
            break; case GODLIKE:
                infusionCost = 560;
        }
        return infusionCost;
    }

    private boolean hitIngredientSlot(int index) {
        // This is ugly. I hate this
        switch(index){
            case 11: return true;
            case 28: return true;
            case 29: return true;
            case 30: return true;
            case 37: return true;
            case 38: return true;
            case 39: return true;
            default: return false;
        }
    }
    */
}
