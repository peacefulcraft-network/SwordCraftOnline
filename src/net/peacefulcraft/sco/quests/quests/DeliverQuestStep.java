package net.peacefulcraft.sco.quests.quests;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;
import net.peacefulcraft.sco.quests.Quest.QuestType;

public class DeliverQuestStep extends QuestStep {

    private ItemStack deliverable;
        public ItemStack getDeliverable() { return this.deliverable; }

    private int amount;
        public int getAmount() { return this.amount; }

    private MythicMob npc;
        public MythicMob getNPC() { return this.npc; }

    public DeliverQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        try {
            this.amount = Integer.valueOf(split[2]);
            this.deliverable = ItemIdentifier.generate(split[1], this.amount, false);
            
            this.npc = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(split[3]);
            if(npc == null) {
                SwordCraftOnline.logInfo("Issue loading DeliverQuestStep. Invalid mob target: " + split[1]);
                this.setInvalid();
                return;
            }

            //TODO: Add location of npc to loading and attribute

            this._setDescription();
        
        } catch(Exception ex) {
            this.setInvalid();
            return;
        }
    }

    public String getDescription() {
        String itemName = this.deliverable.getItemMeta().getDisplayName();
        String amount = String.valueOf(this.amount);
        String npcName = this.npc.getDisplayName();
        //TODO: Get location
        String ret = "Deliver these " + amount + " " + itemName + " to " + npcName + "!";
        return ret;
    }

    @Override
    public void _setDescription() {
        String itemName = this.deliverable.getItemMeta().getDisplayName();
        String amount = String.valueOf(this.amount);
        String npcName = this.npc.getDisplayName();
        //TODO: Get location

        if(this.getDescriptionRaw() == null) {
            this.setDescription("Deliver these " + amount + " " + itemName + " to " + npcName + "!");
        } else {
            try{
                this.setDescription(String.format(this.getDescriptionRaw(), amount, itemName, npcName));
            } catch(Exception ex) {
                this.setDescription("Deliver these " + amount + " " + itemName + " to " + npcName + "!");
            }
        }
    }
}