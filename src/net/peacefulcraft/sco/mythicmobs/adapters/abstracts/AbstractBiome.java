package net.peacefulcraft.sco.mythicmobs.adapters.abstracts;

public class AbstractBiome {
    private String biome;
    
    public AbstractBiome(String biome) {
        this.biome = biome.toUpperCase();
    }
    
    public String toString() {
        return this.biome;
    }
    
    public boolean equals(Object o) {
        if (o instanceof AbstractBiome)
            return o.toString().equals(this.biome); 
        if (o instanceof String)
            return this.biome.equals(((String)o).toUpperCase()); 
        return false;
    }
    
    public int hashCode() {
        return this.biome.hashCode();
    }
  }