package net.peacefulcraft.sco.quests.quests;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;

public class GatherQuestStep extends QuestStep {

    private HashMap<ItemStack, Integer> items = new HashMap<>();

    private MythicMob npc;

    public GatherQuestStep(MythicConfig mc) {
        super(mc);
        
        List<String> itemLis = mc.getStringList("Items");
        
        for(String s : itemLis) {
            String[] split = s.split(" ");
            
            try {
                int amount = Integer.valueOf(split[1]);

                ItemStack item = ItemIdentifier.generate(split[0], 1, false);
                if(item.getType().equals(Material.FIRE)) {
                    this.logInfo("Invalid Item field ");
                    return;
                }
    
                this.items.put(item, amount);
            } catch(Exception ex) {
                this.logInfo("Invalid amount field generating item.");
                return;
            }
        }

        String npcName = mc.getString("npc", "");
        if(npcName == null || npcName.isEmpty()) {
            this.logInfo("No npc field in config.");
            return;
        }
        this.npc = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(npcName);
        if(npc == null) {
            this.logInfo("Invalid npc name in config.");
            return;
        }

        // TODO: Location of npc

        this._setDescription();
    }

    @Override
    public void _setDescription() {
        String npcName = npc.getDisplayName();
        String itemStr = "";
        for (ItemStack i : items.keySet()) {
            String dis = i.getItemMeta().getDisplayName();
            String num = String.valueOf(items.get(i));
            itemStr += num + " " + dis + ", ";
        }
        itemStr = itemStr.substring(0, itemStr.length() - 2);

        //If quest is not activated we use find npc description
        if(!this.isActivated()) {
            this.setDescription("Talk to " + npcName + " in [] to start quest");
            return;
        }

        if (this.getDescriptionRaw() == null) {
            this.setDescription("Gather " + itemStr + " and deliver them to " + npcName + ". ");
        } else {
            try {
                // TODO: Add location string
                this.setDescription(String.format(this.getDescriptionRaw(), itemStr, npcName));
            } catch (Exception ex) {
                this.setDescription("Gather " + itemStr + " and deliver them to " + npcName + ". ");
            }
        }
    }

    @Override
    public boolean stepPreconditions(Event ev) {
        //Checking if items were picked up
        if(!(ev instanceof InventoryOpenEvent)) { return false; }
        InventoryOpenEvent e = (InventoryOpenEvent)ev;

        //Is player opening their inventory
        if(e.getInventory().getType() != InventoryType.PLAYER) { return false; }

        //Is player in the game.
        Player p = (Player) e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return false; }

        //Checking player inventory
        for(ItemStack i : items.keySet()) {
            if(!p.getInventory().contains(i, items.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        return;
    }
    
}