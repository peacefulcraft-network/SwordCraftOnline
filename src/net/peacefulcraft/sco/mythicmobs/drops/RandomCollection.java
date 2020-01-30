package net.peacefulcraft.sco.mythicmobs.drops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.peacefulcraft.sco.SwordCraftOnline;
/**
 * Weighted collection for drop table item distribution
 */
public class RandomCollection<E extends WeightedItem> {
    private List<E> items = new ArrayList<>();

    public double weight = 0.0D;
        public double getWeight() { return this.weight; }

    public RandomCollection(RandomCollection<E> source) {
        this.items = source.getCopy();
        this.weight = source.weight;
    }

    public RandomCollection() {}

    public int size() {
        return this.items.size();
    }

    public boolean add(E item) {
        return this.items.add(item);
    }

    public boolean addAll(List<E> items) {
        for(E t : items) {
            if(this.items.add(t)) { continue; }
        }
        return true;
    }

    public boolean remove(E item) {
        return this.items.remove(item);
    }

    public void clear() {
        this.items.clear();
        this.weight = 0.0D;
    }

    public List<E> getView() {
        return Collections.unmodifiableList(this.items);
    }

    public List<E> getCopy() {
        return new ArrayList<>(this.items);
    }

    public E get() {
        double weight = SwordCraftOnline.r.nextDouble() * this.getWeight();
        if(this.items.isEmpty()) { return null; }
        List<E> itemsView = this.getView();
        for(WeightedItem item : itemsView) {
            if(weight <= item.getWeight()) {
                return (E)item;
            }
            weight -= item.getWeight();
        }
        return itemsView.get(0);
    }

    public Collection<E> get(double offset) {
        List<E> itemList = new LinkedList<E>();
        for(int i = 0; i < offset; i++) {
            E item = get();
            if(item != null) {
                itemList.add(item);
            }
        }
        return itemList;
    }
}