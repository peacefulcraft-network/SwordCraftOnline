package net.peacefulcraft.sco.inventories;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.particles.DisplayType;

public class PlayerDataInventory extends BukkitInventoryBase {

    private SCOPlayer s;

    public PlayerDataInventory(SCOPlayer s) {
        this.s = s;
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Player Profile");

        JsonObject obj = s.getPlayerData();

        // Parsing player money info
        int bank = obj.get("bank").getAsInt();
        int wallet = obj.get("wallet").getAsInt();
        String sBank = "Bank Account: " + bank + " gold\n" +
                        "Wallet Amount: " + wallet + "gold.";
        JsonObject bankObj = new JsonObject();
        bankObj.addProperty("lore", sBank);
        ItemIdentifier bankIdentifier = ItemIdentifier.generateIdentifier("Bank Slot", ItemTier.LEGENDARY, 1);
        ((CustomDataHolder)bankIdentifier).setCustomData(bankObj);
        setItem(10, bankIdentifier);

        // Parsing player combat stats excluding weapon modifiers
        String sCombat = "";
        JsonArray combatArr = obj.get("combatModifier").getAsJsonArray();
        Iterator<JsonElement> iter = combatArr.iterator();   
        while(iter.hasNext()) {
            JsonObject temp = iter.next().getAsJsonObject();
            for(Entry<String, JsonElement> entry : temp.entrySet()) {
                sCombat += entry.getKey() + ": " + entry.getValue().getAsString() + "%\n";
            }
        }    
        JsonArray modifierArr = obj.get("damageModifier").getAsJsonArray();
        Iterator<JsonElement> iterr = modifierArr.iterator();
        while(iterr.hasNext()) {
            JsonObject temp = iterr.next().getAsJsonObject();
            sCombat += temp.get("incoming").getAsBoolean() ? "Incoming " : "Outgoing ";
            sCombat += temp.get("modifierType").getAsString() + ": ";
            sCombat += temp.get("multiplier").getAsDouble() + "x\n";
        }
        int playerKills = obj.get("playerKills").getAsInt();
        sCombat += "\nPlayer Kills: " + playerKills + "\n";
        JsonObject combatObj = new JsonObject();
        combatObj.addProperty("lore", sCombat);
        ItemIdentifier combatIdentifier = ItemIdentifier.generateIdentifier("Combat Stat Slot", ItemTier.LEGENDARY, 1);
        ((CustomDataHolder)combatIdentifier).setCustomData(combatObj);
        setItem(19, combatIdentifier);

        // Parsing player display type
        ItemIdentifier displayIdentifier = ItemIdentifier.generateIdentifier("Display Type Slot", ItemTier.LEGENDARY, 1);
        ((CustomDataHolder)displayIdentifier).setCustomData(obj);
        setItem(12,displayIdentifier);

        // Parsing player message recieve setting
        ItemIdentifier recIdentifier = ItemIdentifier.generateIdentifier("Recieve Skill Messages Slot", ItemTier.LEGENDARY, 1);
        ((CustomDataHolder)recIdentifier).setCustomData(obj);
        setItem(21, recIdentifier);

        // Parsing other data
        int farmingChance = obj.get("farmingChance").getAsInt();
        String sOther = "Farming Chance: " + farmingChance + "%\n";
        JsonObject otherObj = new JsonObject();
        otherObj.addProperty("lore", sOther);
        ItemIdentifier otherIdentifier = ItemIdentifier.generateIdentifier("Other Player Data Slot", ItemTier.LEGENDARY, 1);
        ((CustomDataHolder)otherIdentifier).setCustomData(otherObj);
        setItem(28, otherIdentifier);
    }

    @Override
    public boolean isInventory(Inventory inventory) {
        return this.inventory == inventory;
    }

    @Override
    public void openInventory(SCOPlayer s) {
        SwordCraftOnline.getInventoryListeners().onInventoryOpen(s.getPlayer().openInventory(this.inventory), this);
    }

    @Override
    public void closeInventory() {
        inventory.getViewers().forEach((viewer) -> {
            viewer.closeInventory();
        });
    }

    @Override
    public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        SwordCraftOnline.logDebug("Clicked Player Data inventory.");

        if (clickedItem.getName().equals("Display Type Slot")) {
            JsonObject obj = new JsonObject();
            switch(clickedItem.getMaterial()) {
                case GREEN_DYE:
                    obj.addProperty("displayType", DisplayType.REDUCED.toString());
                break; case YELLOW_DYE:
                    obj.addProperty("displayType", DisplayType.NONE.toString());
                break; case RED_DYE:
                    obj.addProperty("displayType", DisplayType.FULL.toString());
                default:
            }
            ItemIdentifier slot = ItemIdentifier.generateIdentifier("Display Type Slot", ItemTier.LEGENDARY, 1);
            ((CustomDataHolder)slot).setCustomData(obj);
            setItem(12, slot);
        } else if (clickedItem.getName().equals("Recieve Skill Messages")) {
            JsonObject obj = new JsonObject();
            obj.addProperty("recieveSkillMessages", clickedItem.getMaterial().equals(Material.GREEN_DYE));
            ItemIdentifier slot = ItemIdentifier.generateIdentifier("Recieve Skill Messages Slot", ItemTier.LEGENDARY, 1);
            ((CustomDataHolder)slot).setCustomData(obj);
            setItem(21, slot);
        }
    }

    @Override
    public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        SwordCraftOnline.logDebug("Clicked player inv while in Player Data Inventory");

    }

    @Override
    public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
        SwordCraftOnline.logDebug("Drug items in Player Data Inventory.");

    }

    @Override
    public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
        SwordCraftOnline.logDebug("Drug items in player inv while in Player Data inventory.");

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent ev) {
        

    }
    
}
