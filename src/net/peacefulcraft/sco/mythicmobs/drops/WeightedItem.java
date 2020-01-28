package net.peacefulcraft.sco.mythicmobs.drops;

public abstract class WeightedItem implements Comparable<WeightedItem> {
    protected double weight = 1.0D;
    
    protected WeightedItem(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }

    public int compareTo(WeightedItem o) {
        return Double.compare(this.weight, o.getWeight());
    }

    protected WeightedItem() {}
}