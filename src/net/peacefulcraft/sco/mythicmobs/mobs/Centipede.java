package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.LinkedList;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import net.peacefulcraft.sco.SwordCraftOnline;

public class Centipede {

    private EntityType type;

    private LinkedList<Entity> list;

    private int size;

    private String name;

    public Centipede(String name, EntityType type, int size) {
        this.type = type;
        this.list = new LinkedList<Entity>();
        this.size = size;
        this.name = name;
    }

    public void spawn(Location loc) {
        SwordCraftOnline.logDebug("Constructing centipede...");
        for(int i = 0; i < this.size; i++) {
            Entity temp = loc.getWorld().spawnEntity(loc.add(0 + i, 0, 0), this.type);
            temp.setMetadata("centipede", new FixedMetadataValue(SwordCraftOnline.getPluginInstance(), 0));

            //Making centipede slow
            LivingEntity asLiving = (LivingEntity)temp;
            asLiving.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);
            //TODO: Remove
            asLiving.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1);
            
            this.list.add(temp);
            if(i != 0) {
                //If not head of list, set target of current to one before it.
                ((Mob)temp).setTarget((LivingEntity)this.list.get(i - 1));
                SwordCraftOnline.logDebug("Set target of: " + String.valueOf(i) + " Successful.");
            }
        }
        SwordCraftOnline.getPluginInstance().getMobManager().addCenti(this.name, this);
        SwordCraftOnline.logDebug("Centipede spawned.");
    }

    public boolean setTarget(Entity e) {
        return setTarget((LivingEntity)e);
    }

    public boolean setTarget(Player p) {
        return setTarget((LivingEntity)p);
    }

    public boolean setTarget(LivingEntity e) {
        if(this.list.isEmpty()) { return false; }

        ((Mob)this.list.get(0)).setTarget(e);
        return true;
    }

    public void update() {
        for(int i = 0; i < this.list.size(); i++) {
            if(i != 0) {
                Entity e = this.list.get(i);
                ((Mob)e).setTarget((LivingEntity)this.list.get(i - 1));
            }
        }
    }

    public boolean contains(Entity e) {
        ListIterator<Entity> iter = this.list.listIterator();
        while(iter.hasNext()) {
            if(iter.next().equals(e)) {
                return true;
            }
        }
        return false;
    }

    public int search(Entity e) {
        for(int i = 0; i < this.list.size(); i++) {
            if(this.list.get(i).equals(e)) {
                return i;
            } 
        }
        return -1;
    }
}