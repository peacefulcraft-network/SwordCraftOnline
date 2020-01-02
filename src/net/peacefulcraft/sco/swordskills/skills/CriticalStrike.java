package net.peacefulcraft.sco.swordskills.skills;

import java.util.Random;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class CriticalStrike extends SwordSkill {

    private SCOPlayer s;

    public CriticalStrike(SCOPlayer s) {
        super(s, -1, "Critical Strike");

        this.s = s;
    }

    @Override
    public boolean skillSignature(Event ev) {
        if(!(s.getSkills().contains(this))) { return false; }

        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        Random rand = new Random();
        if(rand.nextInt(100) <= s.getCriticalChance())  {
            s.getPlayer().sendMessage(ChatColor.RED + "Critical Hit!");
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) ev;
            e.setDamage(e.getDamage() + 1);
        }
    }
    
}