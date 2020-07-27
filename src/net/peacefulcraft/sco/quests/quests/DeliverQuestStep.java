package net.peacefulcraft.sco.quests.quests;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;

public class DeliverQuestStep extends QuestStep {

    private ItemStack deliverable;

    public ItemStack getDeliverable() {
        return this.deliverable;
    }

    private int amount;

    public int getAmount() {
        return this.amount;
    }

    private MythicMob npc;

    public MythicMob getNPC() {
        return this.npc;
    }

    public DeliverQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        try {
            this.amount = Integer.valueOf(split[2]);
            this.deliverable = ItemIdentifier.generate(split[1], this.amount, false);

            this.npc = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(split[3]);
            if (npc == null) {
                SwordCraftOnline.logInfo("Issue loading DeliverQuestStep. Invalid mob target: " + split[1]);
                this.setInvalid();
                return;
            }

            // TODO: Add location of npc to loading and attribute

            this._setDescription();

        } catch (Exception ex) {
            this.setInvalid();
            return;
        }
    }

    public String getDescription() {
        String itemName = this.deliverable.getItemMeta().getDisplayName();
        String amount = String.valueOf(this.amount);
        String npcName = this.npc.getDisplayName();
        // TODO: Get location
        String ret = "Deliver these " + amount + " " + itemName + " to " + npcName + "!";
        return ret;
    }

    @Override
    public void _setDescription() {
        String itemName = this.deliverable.getItemMeta().getDisplayName();
        String amount = String.valueOf(this.amount);
        String npcName = this.npc.getDisplayName();
        // TODO: Get location

        //If quest is not activated we use find npc description
        if(!this.isActivated()) {
            this.setDescription("Talk to " + npcName + " in [] to start quest");
            return;
        }

        if (this.getDescriptionRaw() == null) {
            this.setDescription("Deliver these " + amount + " " + itemName + " to " + npcName + "!");
        } else {
            try {
                this.setDescription(String.format(this.getDescriptionRaw(), amount, itemName, npcName));
            } catch (Exception ex) {
                this.setDescription("Deliver these " + amount + " " + itemName + " to " + npcName + "!");
            }
        }
    }

    @Override
    public boolean stepPreconditions(Event ev) {
        if(!(ev instanceof PlayerInteractEntityEvent)) { return false; }
        PlayerInteractEntityEvent e = (PlayerInteractEntityEvent)ev;
        
        //Is player in the game. Double check.
        SCOPlayer s = GameManager.findSCOPlayer(e.getPlayer());
        if(s == null) { return false; }

        //Is entity an active mob and is it the mythic mob we want
        Entity entity = e.getRightClicked();       
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(entity.getUniqueId());
        if(am == null) { return false; }
        if(!am.getDisplayName().equals(npc.getDisplayName())) { return false; }

        //Does player have the items in hand
        if(!(e.getPlayer().getInventory().contains(this.deliverable, this.amount))) { return false; }
        
        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        Player p = s.getPlayer();

        //Giving player deliverable item. If inv full we drop it
        HashMap<Integer, ItemStack> ret = p.getInventory().addItem(deliverable);
        if(ret != null) {
            for(ItemStack i : ret.values()) {
                p.getLocation().getWorld().dropItemNaturally(p.getLocation(), i);
            }
        }
    }
}