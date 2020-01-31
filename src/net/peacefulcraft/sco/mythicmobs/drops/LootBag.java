package net.peacefulcraft.sco.mythicmobs.drops;

import java.util.ArrayList;

public class LootBag {
    private ArrayList<Drop> drops;
        public ArrayList<Drop> getDrops() { return this.drops; }
        public void addDrop(Drop d) { this.drops.add(d); }

    private int experience;
        public int getExp() { return this.experience; }
        public void setExp(int i) { this.experience = i; }

    public LootBag() {
        this.drops = new ArrayList<Drop>();

        this.experience = 0;
    }
}