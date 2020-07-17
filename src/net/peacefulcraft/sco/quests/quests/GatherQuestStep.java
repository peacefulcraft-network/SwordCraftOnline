package net.peacefulcraft.sco.quests.quests;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;
import net.peacefulcraft.sco.quests.Quest.QuestType;

public class GatherQuestStep extends QuestStep {

    private HashMap<ItemStack, Integer> items = new HashMap<>();

    private MythicMob npc;

    public GatherQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        try {
            //If there are multiple items to gather
            if(split[1].contains(",")) {
                String[] split2 = split[1].split(",");
                for(String s : split2) {
                    addItem(s);
                }
            } else {
                addItem(split[1]);
            }
            
            this.npc = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(split[2]);
            if(npc == null) {
                SwordCraftOnline.logInfo("Issue loading GatherQuestStep. Invalid mob target: " + split[1]);
                this.setInvalid();
                return;
            }

            //TODO: Location of npc

            this._setDescription();
        } catch(Exception ex) {
            this.setInvalid();
            return;
        }
    }

    private void addItem(String s) {
        try {
            String[] split = s.split("&");

            ItemStack item = ItemIdentifier.generate(split[0]);
            int amount = Integer.valueOf(split[1]);
            this.items.put(item, amount);
        } catch(Exception ex) {
            this.setInvalid();
        }
    }

    @Override
    public void _setDescription() {
        String npcName = npc.getDisplayName();
        String itemStr = "";
        for(ItemStack i : items.keySet()) {
            String dis = i.getItemMeta().getDisplayName();
            String num = String.valueOf(items.get(i));
            itemStr += num + " " + dis + ", ";
        }
        itemStr = itemStr.substring(0, itemStr.length()-2);

        if(this.getDescriptionRaw() == null) {
            this.setDescription("Gather " + itemStr + " and deliver them to " + npcName + ". ");
        } else {
            try {
                //TODO: Add location string
                this.setDescription(String.format(this.getDescriptionRaw(), itemStr, npcName));
            } catch(Exception ex) {
                this.setDescription("Gather " + itemStr + " and deliver them to " + npcName + ". ");
            }
        }
    }
    
}