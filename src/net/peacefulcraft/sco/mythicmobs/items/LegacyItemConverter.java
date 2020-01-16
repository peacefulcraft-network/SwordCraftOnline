package net.peacefulcraft.sco.mythicmobs.items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import net.peacefulcraft.sco.SwordCraftOnline;

public class LegacyItemConverter {
    private static final Map<String, ItemMapping> LEGACY_MAPPINGS = new HashMap<>();
   
    private static final String INTERNAL_DELIMITER = ":"; 

    private static class ItemMapping {
        private final String materialName;

        private final int legacyId;

        private final int legacyDataValue;

        private final String legacyName;

        public boolean equals(Object o) {
            if(o == this)
                return true; 
            if(!(o instanceof ItemMapping))
                return false; 
            ItemMapping other = (ItemMapping)o;
            if(!other.canEqual(this))
                return false; 
            Object this$materialName = getMaterialName(), other$materialName = other.getMaterialName();
            if((this$materialName == null) ? (other$materialName != null) : !this$materialName.equals(other$materialName))
                return false; 
            if(getLegacyId() != other.getLegacyId())
                return false; 
            if(getLegacyDataValue() != other.getLegacyDataValue())
                return false; 
            Object this$legacyName = getLegacyName(), other$legacyName = other.getLegacyName();
            return !((this$legacyName == null) ? (other$legacyName != null) : !this$legacyName.equals(other$legacyName));
        }

        public boolean canEqual(Object other) {
            return other instanceof ItemMapping;
        }

        public int hashCode() {
            int result = 1;
            Object $materialName = getMaterialName();
            result = result * 59 + (($materialName == null) ? 43 : $materialName.hashCode());
            result = result * 59 + getLegacyId();
            result = result * 59 + getLegacyDataValue();
            Object $legacyName = getLegacyName();
            return result * 59 + (($legacyName == null) ? 43 : $legacyName.hashCode());
        }

        public String toString() {
            return "LegacyItemConverter.ItemMapping(materialName=" + getMaterialName() + ", legacyId=" + getLegacyId() + ", legacyDataValue=" + getLegacyDataValue() + ", legacyName=" + getLegacyName() + ")";
        }

        public String getMaterialName() {
            return this.materialName;
        }

        public int getLegacyId() {
            return this.legacyId;
        }

        public int getLegacyDataValue() {
            return this.legacyDataValue;
        }

        public String getLegacyName() {
            return this.legacyName;
        }

        public ItemMapping(int legacyId, int legacyDataValue, String legacyType, String name) {
            this.materialName = name.toUpperCase();
            this.legacyName = legacyType.toUpperCase();
            this.legacyId = legacyId;
            this.legacyDataValue = legacyDataValue;
        }

        public ItemMapping(int legacyId, String name) {
            this(legacyId, 0, name, name);
        }

        public ItemMapping(int legacyId, String legacyType, String name) {
            this(legacyId, 0, legacyType, name);
        }

        public ItemMapping(int legacyId, int data, String name) {
            this(legacyId, data, name, name);
        }
    }

    private static Material getMaterialByKey(String key) {
        if (LEGACY_MAPPINGS.containsKey(key)) {
            return Material.valueOf(((ItemMapping)LEGACY_MAPPINGS.get(key)).getMaterialName());
        } 
        return null;
    }

    public static Material getMaterial(String id, byte dv) {
        String key = id + ":" + dv;
        Material material = getMaterialByKey(key);
        if(material == null && dv > 0) {
            return getMaterialByKey(id + ":" + "0");
        }
        return material;
    }
}