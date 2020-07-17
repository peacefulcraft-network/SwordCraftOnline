package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.drops.Reward;

public class ActiveQuest {
    
    private Quest quest;

    private SCOPlayer s;

    private boolean locked = false;
        public boolean isLocked() { return this.locked; }

    private int currentStep;

    public ActiveQuest(SCOPlayer s, Quest quest) {
        this.quest = quest;
        this.s = s;

        if(!quest.checkRequirements(s)) {
            this.locked = true;
        }

        this.currentStep = 0;

        //TODO: Register listeners to player
    }

    /**Gives player associated with quest rewards */
    public void giveRewards() {
        for(Reward r : this.quest.getQuestStep(this.currentStep).getRewards()) {
            ItemStack item = r.getReward();
            Player p = s.getPlayer();
            if(item == null) { 
                p.giveExp(r.getExperience());
                continue;
            }

            //Dropping items on player if inventory full
            HashMap<Integer, ItemStack> ret = p.getInventory().addItem(r.getReward());
            if(ret != null) {
                for(ItemStack i : ret.values()) {
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), i);
                }
            }
        }
    }

    /**@return Item represent of quest at current step */
    public ItemStack getItem() {
        ItemStack icon = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = icon.getItemMeta();

        QuestStep qs = quest.getQuestStep(this.currentStep);
        
        ArrayList<String> desc = new ArrayList<>();
        desc.add(qs.getDescription());

        if(this.locked) {
            meta.setDisplayName(ChatColor.RED + "" + quest.getQuestName());
            desc.add(ChatColor.RED + "" + qs.getDescription());
        } else {
            meta.setDisplayName(ChatColor.AQUA + "" + quest.getQuestName());
            desc.add(ChatColor.WHITE + "" + qs.getDescription());
        }

        desc.add("");
        desc.add(ChatColor.DARK_PURPLE + getProgress());
        desc.add("");
        desc.add(getRewardStr());
        meta.setLore(desc);
        icon.setItemMeta(meta);

        return icon;
    }

    /**Helper function gets current progress as string bar */
    private String getProgress() {
        String ret = "[" + new String(new char[this.currentStep]).replace("\0", Announcer.getSquare());
        ret += new String(new char[this.quest.getSize()-this.currentStep]).replace("\0", "-") + "]";
        return ret;
    }

    /**Helper function gets rewards as string */
    private String getRewardStr() {
        String ret = "Rewards: ";
        for(Reward r : this.quest.getQuestStep(this.currentStep).getRewards()) {
            if(r.getReward() == null) {
                ret += String.valueOf(r.getExperience()) + " Exp, ";
            } else {
                String item = r.getReward().getItemMeta().getDisplayName();
                String amount = String.valueOf(r.getReward().getAmount());
                ret += amount + " " + item + ", ";
            }
        }
        ret = ret.substring(0, ret.length()-2);
        return ret;
    }
}