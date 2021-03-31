package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.WeaponAttributeHolder;

public class ForgeInventory extends BukkitInventoryBase {

    protected SCOPlayer s;

    private static HashMap<ItemTier, String> ingredientMap = new HashMap<>();

    /**
     * Forge task. Runs to check for correct
     * amount of forging items.
     * Called when inventory closes.
     */
    private BukkitTask forgeTask;

    private int ingredientRec = 0;
        private void setIngredientRec(int num) { this.ingredientRec = num; }

    public ForgeInventory(SCOPlayer s) {
        this.s = s;
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Forge Anvil");
        setBlockers();

        forgeTask = new BukkitRunnable(){

            @Override
            public void run() {
                try {
                    ItemStack item = verifyWeaponSlot();
                    if(item == null) { setUISlots(false); return; }
    
                    ItemIdentifier identifier = ItemIdentifier.resolveItemIdentifier(item);
                    if(identifier == null || identifier.getMaterial().equals(Material.AIR) 
                        || !(identifier instanceof CustomDataHolder)) { setUISlots(false); return; }
    
                    CustomDataHolder cus = ((CustomDataHolder)identifier);
                    cus.parseCustomItemData(item);
                    JsonObject customObj = cus.getCustomData();
                    //JsonObject reforgeObj = customObj.getAsJsonObject("reforge");
                    //if(reforgeObj == null) { return; }
                    JsonObject weaponObj = customObj.getAsJsonObject("Weapon Data");
                    if(weaponObj == null) { return; }
    
                    int reforgeCount = weaponObj.get("Reforge Count").getAsInt();
                    int ingredientRec = (int) (37 + (Math.sqrt(reforgeCount * 25) * 9));
                    int ingredientCount = verifyIngredientSlots(identifier.getTier());
    
                    if(ingredientCount < ingredientRec) { setUISlots(false); return; }
    
                    setIngredientRec(ingredientRec);
                    setUISlots(true);
                } catch(NullPointerException ex) {
                    ex.printStackTrace();
                    this.cancel();
                }
            }

        }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 20, 10);
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
        if(!ev.getView().getTitle().equalsIgnoreCase("Forge Anvil")) { return; }

        if(clickedItem.getName().equalsIgnoreCase("Forge Start Slot")) {
            ItemStack item = verifyWeaponSlot();
            ItemIdentifier catalyst = ItemIdentifier.resolveItemIdentifier(item);
            if(catalyst == null || catalyst.getMaterial().equals(Material.AIR)) { return; }

            WeaponAttributeHolder wCatalyst = (WeaponAttributeHolder)catalyst;
            CustomDataHolder cCatalyst = (CustomDataHolder)catalyst;
            cCatalyst.parseCustomItemData(item);
            cCatalyst.applyCustomItemData(item, cCatalyst.getCustomData());
            JsonObject rolledObj = WeaponAttributeHolder.rollWeaponData(wCatalyst.getWeaponData());

            SwordCraftOnline.logDebug("Rolled Obj: " + rolledObj.toString() + "\n");

            ItemIdentifier weapon = ItemIdentifier.generateIdentifier(catalyst.getName(), catalyst.getTier(), 1);
            CustomDataHolder cWeapon = (CustomDataHolder)weapon;
            JsonObject setObj = cWeapon.getCustomData();
            SwordCraftOnline.logDebug("Set Obj: " + setObj.toString() + "\n");
            //setObj.add("reforge", rolledObj);
            
            setObj.add("PASSIVE", rolledObj.get("PASSIVE"));
            setObj.add("ACTIVE", rolledObj.get("ACTIVE"));
            setObj.add("Max Reforge", rolledObj.get("Max Reforge"));
            setObj.add("Disposition", rolledObj.get("Disposition"));
            setObj.add("Reforge Count", rolledObj.get("Reforge Count"));
            setObj.add("reforge", rolledObj.get("reforge"));

            cWeapon.setCustomData(setObj);

            SwordCraftOnline.logDebug("Final Obj: " + setObj.toString() + "\n");

            SwordCraftOnline.logDebug("Ingredient Rec 2: " + this.ingredientRec);
            removeIngredientSlots(catalyst.getTier(),this. ingredientRec);

            setItem(24, weapon);
            setUISlots(false);
            removeItem(11);
            this.ingredientRec = 0;
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
        //TODO: clear slots and return all input ingredients

        forgeTask.cancel();
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
        setItem(14, ItemIdentifier.generateIdentifier("PurpleSlot", ItemTier.COMMON, 1));
        setItem(16, ItemIdentifier.generateIdentifier("PurpleSlot", ItemTier.COMMON, 1));
        setItem(32, ItemIdentifier.generateIdentifier("PurpleSlot", ItemTier.COMMON, 1));
        setItem(34, ItemIdentifier.generateIdentifier("PurpleSlot", ItemTier.COMMON, 1));

        setItem(15, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(23, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(25, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(33, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
    }

    private void setUISlots(boolean valid) {
        if(valid) {
            setItem(15, ItemIdentifier.generateIdentifier("ForgeStartSlot", ItemTier.COMMON, 1));
            setItem(23, ItemIdentifier.generateIdentifier("ForgeStartSlot", ItemTier.COMMON, 1));
            setItem(25, ItemIdentifier.generateIdentifier("ForgeStartSlot", ItemTier.COMMON, 1));
            setItem(33, ItemIdentifier.generateIdentifier("ForgeStartSlot", ItemTier.COMMON, 1));
        } else {
            setItem(15, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
            setItem(23, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
            setItem(25, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
            setItem(33, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        }
    }

    /**
     * Checks weapon slot for valid weapon
     * 
     * @return Itemstack in slot or null
     */
    private ItemStack verifyWeaponSlot() {
        ItemStack item = this.inventory.getItem(11);
        if(item == null || item.getType().equals(Material.AIR)) { return null; }

        ItemIdentifier identifier = ItemIdentifier.resolveItemIdentifier(item);
        if(identifier == null || identifier.getMaterial().equals(Material.AIR)) { return null; }
        if(!(identifier instanceof WeaponAttributeHolder)) { return null; }
 
        return item;
    }

    /**
     * Checks ingredient slots and counts valid items
     */
    private int verifyIngredientSlots(ItemTier tier) {
        int count = 0;
        for(int row = 3; row <= 4; row++){
            for(int col = 1; col <= 3; col++) {
                ItemIdentifier item = getItem(row * 9 + col);
                if(item == null || item.getMaterial().equals(Material.AIR)) { continue; }

                if(!item.getName().equalsIgnoreCase(ingredientMap.get(tier))) { continue; }
                count += item.getQuantity();
            }
        }
        return count;
    }

    private void removeIngredientSlots(ItemTier tier, int rec) {
        for(int row = 3; row <= 4; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemIdentifier item = getItem(row * 9 + col);
                if(!item.getName().equalsIgnoreCase(ingredientMap.get(tier))) { continue; }
                if(rec - item.getQuantity() > 0) {
                    removeItem(row * 9 + col);
                    rec -= item.getQuantity();
                } else {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier(
                        item.getName(), 
                        item.getTier(), 
                        item.getQuantity() - rec));
                }
            }
        }
    }
    
    static {
        ingredientMap.put(ItemTier.COMMON, "Forge Steel");
        ingredientMap.put(ItemTier.UNCOMMON, "Hardened Forge Steel");
    }

}
