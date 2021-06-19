package net.peacefulcraft.sco.quests.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;

public class DeliverQuestStep extends QuestStep {

    private List<ItemStack> deliverables = new ArrayList<>();

    public List<ItemStack> getDeliverables() {
        return this.deliverables;
    }

    /** NPC we are delivering to */
    private MythicMob npc;

    public MythicMob getNPC() {
        return this.npc;
    }

    /** Location NPC can be found */
    private Region npcRegion;

    public DeliverQuestStep(MythicConfig mc, String questName) {
        super(mc, questName);

        // Item name validation
        List<String> items = mc.getStringList("Items");
        if (items == null || items.isEmpty()) {
            this.logInfo("No Item field in config.");
            return;
        }

        // Custom item validation
        for(String s : items) {
            String name = s.split(" ")[0];
            int amount = Integer.valueOf(s.split(" ")[1]);
            ItemStack item = ItemIdentifier.generateItem(name, ItemTier.COMMON, amount);
            if (item.getType().equals(Material.FIRE) || item == null) {
                this.logInfo("Invalid Item field in config.");
                return;
            }
            this.deliverables.add(item);
        }

        // Deliver to npc validation
        String npcName = mc.getString("npc", "");
        npcName = mc.getString("NPC", npcName);
        if (npcName == null || npcName.isEmpty()) {
            this.logInfo("No npc field in config.");
            return;
        }
        this.npc = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(npcName);
        if (npc == null) {
            this.logInfo("Invalid npc name in config.");
            return;
        }

        // NPC region validation
        String regionName = mc.getString("NPCRegion", "");
        regionName = mc.getString("npcRegion", regionName);
        if (regionName == null || regionName.isEmpty()) {
            this.logInfo("No NPC Region field in config.");
            return;
        }
        this.npcRegion = RegionManager.getRegion(regionName);
        if (this.npcRegion == null) {
            this.logInfo("Invalid NPC Region field in config.");
            return;
        }

        this._setDescription();
    }

    @Override
    public void _setDescription() {
        String itemNames = "";
        for(ItemStack i : deliverables) {
            itemNames += i.getItemMeta().getDisplayName();
        }
        String npcName = this.npc.getDisplayName();
        String giverRegionName = getGiverRegion().getName();
        String deliverRegionName = this.npcRegion.getName();

        // If quest is not activated we use find npc description
        if (!this.isActivated()) {
            this.setDescription(this.name + "\nTalk to " + npcName + " in " + giverRegionName + " to start quest");
            return;
        }

        if (this.getDescriptionRaw() == null || this.getDescriptionRaw().isEmpty()) {
            this.setDescription(
                this.name + "\nDeliver these " + itemNames + " to " + npcName + " in " + deliverRegionName + ".");
        } else {
            try {
                this.setDescription(
                        String.format(this.getDescriptionRaw(), itemNames, npcName, deliverRegionName));
            } catch (Exception ex) {
                this.setDescription(
                    this.name + "\nDeliver these " + itemNames + " to " + npcName + " in "
                        + deliverRegionName + ".");
            }
        }
    }

    @Override
    public boolean stepPreconditions(Event ev) {
        if (!(ev instanceof PlayerInteractEntityEvent)) {
            return false;
        }
        PlayerInteractEntityEvent e = (PlayerInteractEntityEvent) ev;

        // Is player in the game. Double check.
        SCOPlayer s = GameManager.findSCOPlayer(e.getPlayer());
        if (s == null) {
            return false;
        }

        // Is the player in the region of the npc?
        if (!s.getRegion().equals(this.npcRegion)) {
            return false;
        }

        // Is entity an active mob and is it the mythic mob we want
        Entity entity = e.getRightClicked();
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(entity.getUniqueId());
        if (am == null) {
            return false;
        }
        if (!am.getDisplayName().equals(npc.getDisplayName())) {
            return false;
        }

        // Does player have the items in inventory
        for(ItemStack item : this.deliverables) {
            if(!(e.getPlayer().getInventory().contains(item, item.getAmount()))) {
                return false;
            }
        }

        completeMessage(s);
        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        Player p = s.getPlayer();

        // Giving player deliverable item. If inv full we drop it
        HashMap<Integer, ItemStack> ret = new HashMap<>();
        for(ItemStack item : deliverables) {
            HashMap<Integer, ItemStack> temp = p.getInventory().addItem(item);
            temp.forEach((key,value) -> {
                ret.put(key, value);
            });
        }
        if (ret != null) {
            for (ItemStack i : ret.values()) {
                p.getLocation().getWorld().dropItemNaturally(p.getLocation(), i);
            }
        }

        startupMessage(s);
    }

    @Override
    public Map<String, Object> getSaveData() {
        //No data necessary for player progression
        //Item checks are done at quest completion
        return null;
    }

    @Override
    public void processStepData(JsonObject data) {
        // TODO Auto-generated method stub
        
    }
}