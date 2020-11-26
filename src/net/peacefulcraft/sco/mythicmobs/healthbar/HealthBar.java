package net.peacefulcraft.sco.mythicmobs.healthbar;

import org.bukkit.attribute.Attribute;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;

public class HealthBar {
    public enum HealthBarType {
        DISABLED, NUMBER, BAR;
    }

    private String bar;
        public String getHealthBar() { return bar; }
    private int density;
    private double maxHealth; 
    private ActiveMob am = null;

    public HealthBar(double maxHealth) {
        this.density = 10;
        this.maxHealth = maxHealth;
        this.bar = createBar();
    }

    public HealthBar(ActiveMob am) {
        this.density = 10;
        this.maxHealth = am.getLivingEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        this.am = am;
        this.bar = createBar();
    }

    /**
     * Updates health bar on damage events.
     */
    public void updateBar(double health) {
        double percent = health / this.maxHealth;
        //barNumber represents number of "x" to be filled in.
        //this.density - barNumber is number of "x" to be different color.
        int barNumber = (int)(percent * this.density);
        if(SwordCraftOnline.getSCOConfig().getHealthBarConfig().equals(HealthBarType.BAR)) {
            if(this.am != null && am.duringNightwave()) {
                String b = ChatColor.DARK_GRAY + " [";
                b += ChatColor.DARK_RED + new String(new char[barNumber]).replace("\0", "x");
                b += ChatColor.RED + new String(new char[this.density - barNumber]).replace("\0", "x");
                b += ChatColor.DARK_GRAY + "]";
                this.bar = b;
            } else {
                String b = ChatColor.GOLD + " [";
                b += ChatColor.BLUE + new String(new char[barNumber]).replace("\0", "x");
                b += ChatColor.AQUA + new String(new char[this.density - barNumber]).replace("\0", "x");
                b += ChatColor.GOLD + "]";
                this.bar = b;
            }
        } else if(SwordCraftOnline.getSCOConfig().getHealthBarConfig().equals(HealthBarType.NUMBER)) {
            if(this.am != null && am.duringNightwave()) {
                String b = ChatColor.DARK_GRAY + " [";
                b += ChatColor.DARK_RED + String.valueOf(Math.round(health));
                b += ChatColor.DARK_GRAY + "]";
                this.bar = b;
            } else {
                String b = ChatColor.GOLD + " [";
                b += ChatColor.BLUE + String.valueOf(Math.round(health));
                b += ChatColor.GOLD + "]";
                this.bar = b;
            }
        } else {
            this.bar = "";
        }
    }

    /**
     * Initializes baseline health bar with full health.
     */
    private String createBar() {
        if(SwordCraftOnline.getSCOConfig().getHealthBarConfig().equals(HealthBarType.DISABLED)) { return ""; }

        if(this.am != null && am.duringNightwave()) {
            String b = ChatColor.DARK_GRAY + " [";
            if(SwordCraftOnline.getSCOConfig().getHealthBarConfig().equals(HealthBarType.BAR)) {
                b += ChatColor.DARK_RED + new String(new char[this.density]).replace("\0", "x");
            } else {
                b += ChatColor.DARK_RED + String.valueOf(this.maxHealth);
            }
            b += ChatColor.DARK_GRAY + "]";
            return b;
        } else {
            String b = ChatColor.GOLD + " [";
            if(SwordCraftOnline.getSCOConfig().getHealthBarConfig().equals(HealthBarType.BAR)) {
                b += ChatColor.BLUE + new String(new char[this.density]).replace("\0", "x");
            } else {
                b += ChatColor.BLUE + String.valueOf(this.maxHealth);
            }
            b += ChatColor.GOLD + "]";
            return b;
        }
    }
}