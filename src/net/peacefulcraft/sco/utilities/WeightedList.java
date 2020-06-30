package net.peacefulcraft.sco.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.peacefulcraft.sco.SwordCraftOnline;

public class WeightedList<T> {
    private ArrayList<WeightedItem<T>> lis;

    public WeightedList() {
        this.lis = new ArrayList<WeightedItem<T>>();
    }

    public List<WeightedList<T>.WeightedItem<T>> getList() {
        return Collections.unmodifiableList(lis);
    }

    public void add(T item, Double weight) {
        lis.add(new WeightedItem<T>(item, weight));
    }

    public boolean remove(T item) {
        Iterator<WeightedItem<T>> iter = this.lis.iterator();
        while(iter.hasNext()) {
            WeightedItem<T> wItem = iter.next();
            if(wItem.getItem().equals(item)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    public boolean setWeight(T item, Double weight) {
        Iterator<WeightedItem<T>> iter = this.lis.iterator();
        while(iter.hasNext()) {
            WeightedItem<T> wItem = iter.next();
            if(wItem.getItem().equals(item)) {
                wItem.setWeight(weight);
                return true;
            }
        }
        return false;
    }

    public void clear() {
        this.lis.clear();
    }

    public T getItem() {
        double totalWeight = 0.0D;
        for(WeightedItem<T> w : this.lis) {
            totalWeight += w.getWeight();
        }

        int randomIndex = -1;
        double rand = SwordCraftOnline.r.nextDouble() * totalWeight;
        for(int i = 0; i < this.lis.size(); ++i) {
            rand -= this.lis.get(i).getWeight();
            if(rand <= 0.0D) {
                randomIndex = i;
                break;
            }
        }
        return this.lis.get(randomIndex).getItem();
    }

    public WeightedList<T> clone() {
        WeightedList<T> copy = new WeightedList<>();
        for(WeightedItem<T> w : this.lis) {
            copy.add(w.getItem(), w.getWeight());
        }
        return copy;
    }

    private class WeightedItem<T> {
        private T item = null;
            public T getItem() { return this.item; }

        private Double weight = null;
            public Double getWeight() { return this.weight; }
            public void setWeight(Double d) { this.weight = d; }

        public WeightedItem(T item, Double weight) {
            this.item = item;
            this.weight = weight;
        }
    }
}