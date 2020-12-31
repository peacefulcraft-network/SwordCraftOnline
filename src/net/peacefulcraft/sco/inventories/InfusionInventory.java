package net.peacefulcraft.sco.inventories;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class InfusionInventory extends BukkitInventoryBase {

    protected SCOPlayer s;

    /**
     * n/10 chance the infusion fails
     */
    private final int FAIL_CHANCE = 2;

    public InfusionInventory(SCOPlayer s) {
        this.s = s;
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Infusion Table");
        setBlockers();
    }

    /**
     * Initializes the blocked slots
     */
    private void setBlockers() {
        for (int row = 0; row <= 5; row++) {
            for (int col = 0; col <= 8; col++) {
                // Broke statements into different lines b\c I got annoyed.
                // Leaves 1 catalyst slot in row 1.
                // 3 component slots in row 3 and 4
                if (row == 0 || row == 5) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                } else if (row == 1 && (col <= 1 || col == 3 || col == 4 || col == 8)) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                } else if (row == 2 && (col <= 4 || col == 8)) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                } else if (row == 3 && (col == 0 || col == 4 || col == 8)) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                } else if (row == 5 || (row == 4 && (col == 0 || col >= 4))) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                }
            }
        }
        // Places start button and indicator buttons
        setItem(14, ItemIdentifier.generateIdentifier("InfusionStartSlot", ItemTier.COMMON, 1));
        setItem(16, ItemIdentifier.generateIdentifier("InfusionStartSlot", ItemTier.COMMON, 1));
        setItem(32, ItemIdentifier.generateIdentifier("InfusionStartSlot", ItemTier.COMMON, 1));
        setItem(34, ItemIdentifier.generateIdentifier("InfusionStartSlot", ItemTier.COMMON, 1));

        setItem(15, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(23, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(25, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(33, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
    }

    @Override
    public boolean isInventory(Inventory inventory) {
        return false;
    }

    @Override
    public void openInventory(SCOPlayer s) {
        SwordCraftOnline.getInventoryListeners().onInventoryOpen(s.getPlayer().openInventory(this.inventory), this);    
    }

    @Override
    public void closeInventory() {
        
    }

    @Override
    public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        if(!ev.getView().getTitle().equalsIgnoreCase("Infusion Table")) { return; }

        boolean checkedIng = checkIngredientSlots(this.inventory);
        setValidInfusionSlots(checkedIng);

        // Checking if clicked on start button
        if(checkedIng && clickedItem.getMaterial().equals(Material.PURPLE_STAINED_GLASS_PANE) 
            && clickedItem.getName().equalsIgnoreCase("Click to begin infusion")) {
        
            beginInfusion();
        }
    }

    @Override
    public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {

    }

    @Override
    public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {

    }

    @Override
    public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent ev) {
        clearIngredients(s.getPlayer(), true);
    }

    /**
     * Main infusion logic.
     * @param inv Inventory we are changing
     */
    private void beginInfusion() {
        // Determine if infusion failed right away
        boolean failed = SwordCraftOnline.r.nextInt(9) < FAIL_CHANCE;
        int fail_row = SwordCraftOnline.r.nextInt(5);         

        new BukkitRunnable(){
            /**
             * Starting rows from bottom -> up
             */
            int row = 5;

            @Override
            public void run() {
                if(failed && row == fail_row) {
                    resetProgressBar(true);
                    this.cancel();
                }
                // Replacing row slots
                for(int col = 0; col <= 8; col++) {
                    ItemIdentifier item = getItem(row * 9 + col);
                    if(item.getMaterial().equals(Material.BLACK_STAINED_GLASS_PANE)) {
                        setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlueSlot", ItemTier.COMMON, 1));
                    }
                }
                row--;
            }
        }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 0, 20);

        // Failed infusion. We consume items and quit
        if(failed) { 
            clearIngredients(s.getPlayer(), false);
            return; 
        }

        // Beginning of main infusion logic
        int infusionExp = 0;
        ArrayList<ItemIdentifier> skills = getIngredients();
        for(ItemIdentifier skill : skills) {
            NBTItem nbti = new NBTItem(ItemIdentifier.generateItem(skill));
            if(nbti.hasKey("tier")) {
                ItemTier tier = ItemTier.valueOf(nbti.getString("tier"));
                switch(tier) {
                    case COMMON:
                        infusionExp += 10 * skill.getQuantity();
                    break; case UNCOMMON:
                        infusionExp += 25 * skill.getQuantity();
                    break; case RARE:
                        infusionExp += 50 * skill.getQuantity();
                    break; case LEGENDARY:
                        infusionExp += 80 * skill.getQuantity();
                    break; case ETHEREAL:
                        infusionExp += 115 * skill.getQuantity();
                    break; case GODLIKE:
                        infusionExp += 160 * skill.getQuantity();
                    default:
                        break;
                }
            }
        }
        
        // Calculating cost of infusion upgrade
        ItemStack catalyst = ItemIdentifier.generateItem(getItem(11));
        NBTItem nbti = new NBTItem(catalyst);
        ItemTier catTier = ItemTier.valueOf(nbti.getString("tier"));

        // Progressing tier of item
        for(; infusionExp <= 0; infusionExp -= getInfusionCost(catTier)) {
            catTier = ItemTier.progressTier(catTier);
        }

        String name = ChatColor.stripColor(catalyst.getItemMeta().getDisplayName().replace(" ", ""));
        ItemStack result = ItemIdentifier.generateItem(name, catTier, 1);

        setItem(24, ItemIdentifier.generateIdentifier(name, catTier, 1));
    }

    /**
     * Resets progress of UI
     * @param inv Inventory we are modifying
     */
    private void resetProgressBar(boolean isFail) {
        // If infusion failed we freeze rows and then re call reset
        if(isFail) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    resetProgressBar(false);
                }
            }.runTaskLater(SwordCraftOnline.getPluginInstance(), SwordCraftOnline.r.nextInt(4) * 20);
        } else {
            new BukkitRunnable() {
                /**
                 * Clean up from top down
                 */
                int row = 0;

                @Override
                public void run() {
                    if(row >= 5) {
                        this.cancel();
                        return;
                    }

                    for(int col = 0; col <= 8; col++) {
                        ItemIdentifier item = getItem(row * 9 + col);
                        if(item.getMaterial().equals(Material.BLUE_STAINED_GLASS_PANE)) {
                            setItem(row * 9 + col, item);
                        }                        
                    }
                    row++;
                }
            }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 0, 10);
        }
    }

    /**
     * Clears the input slots and returns items to player
     * If player inventory is full, drops items at feet
     * @param p Player instance
     * @param returnItems Returns ingredients to player if true
     */
    private void clearIngredients(Player p, boolean returnItems) {
        ArrayList<ItemStack> leftovers = new ArrayList<>();
        for(int row = 3; row <= 4; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemIdentifier item = getItem(row * 9 + col);
                if(item == null || item.getMaterial().equals(Material.AIR)) { continue; }

                // Adding items to inv or leftover
                if(returnItems) {
                    HashMap<Integer,ItemStack> temp = p.getInventory().addItem(ItemIdentifier.generateItem(item));
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

    /**
     * Verifies there are more than 1 sword skill item in slots
     * @param inv Inventory we check
     * @return True if valid recipe, false otherwise
     */
    private boolean checkIngredientSlots(Inventory inv) {
        // Infusion requires catalyst. Not valid without.
        ItemStack catalyst = inv.getItem(11);
        if(catalyst == null || catalyst.getType().equals(Material.AIR)) { return false; }
        if(catalyst.getAmount() > 1) { return false; }

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

    /**
     * Gets item identifiers in ingredient slots
     * @return List of identifiers
     */
    private ArrayList<ItemIdentifier> getIngredients() {
        ArrayList<ItemIdentifier> out = new ArrayList<>();

        for(int row = 3; row <= 4; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemIdentifier item = getItem(row * 9 + col);
                if(item == null || item.getMaterial().equals(Material.AIR)) { continue; }
                out.add(item);
            }
        }
        return out;
    }

    /**
     * Sets valid slots in inventory UI
     * @param isValid Determines what color UI is set to
     */
    private void setValidInfusionSlots(boolean isValid) {
        if(isValid) {
            setItem(15, ItemIdentifier.generateIdentifier("GreenSlot", ItemTier.COMMON, 1));
            setItem(23, ItemIdentifier.generateIdentifier("GreenSlot", ItemTier.COMMON, 1));
            setItem(25, ItemIdentifier.generateIdentifier("GreenSlot", ItemTier.COMMON, 1));
            setItem(34, ItemIdentifier.generateIdentifier("GreenSlot", ItemTier.COMMON, 1));
        } else {
            setItem(15, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
            setItem(23, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
            setItem(25, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
            setItem(34, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        }
    }

    /**
     * Returns infusion exp cost based on tier
     * Cost is roughly 3.5x exp gain of ingredient
     * @param tier Tier of catalyst
     * @return Int cost of upgrade
     */
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
}
