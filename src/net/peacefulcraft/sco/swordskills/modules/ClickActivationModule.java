package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ClickActivationModule implements SwordSkillModule {

    private ItemStack clickItem;
    private boolean hasClicked = false;

    public ClickActivationModule(SwordSkill ss, ItemStack clickitem) {
        ss.listenFor(SwordSkillType.PLAYER_INTERACT_RIGHT_CLICK, this);
        this.clickItem = clickitem;
    }

    @Override
    public void executeSupportLifecycle(SwordSkillType type, SwordSkill ss, Event ev) {
        PlayerInteractEvent e = (PlayerInteractEvent)ev;
        this.hasClicked = e.getPlayer().getInventory().getItemInMainHand().equals(this.clickItem);
    }

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) {
        return this.hasClicked;
    }

    @Override
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev) {
        return true;
    }

    @Override
    public void afterTriggerSkill(SwordSkill ss, Event ev) {
        this.hasClicked = false;
    }

    @Override
    public void onUnregistration(SwordSkill ss) {
        return;    
    }
    
}