package net.peacefulcraft.sco.mythicmobs.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class MythicItem implements Comparable<MythicItem> {
    private final MythicConfig config;

    private ItemStack itemStack;

    private final String internalName;

    private final String file;

    private String displayName;

    private String id;

    private Material material;

    private int iid;

    private boolean useiid = false;

    @Deprecated
    private int data = 0;

    private int amount = 1;

    private int customModelData;

    private String color;

    private String player;

    private String skinURL;

    private String skinTexture;

    private UUID skinUUID;

    private List<String> lore;

    private List<String> enchantments;

    private List<String> potionEffects;

    private List<String> bannerLayers;

    private List<String> fireworkColors;

    private List<String> fireworkFadeColors;

    private double speed;

    private double damage;

    private double knock;

    private double health;

    private double range;

    private Map<String, Map<String, String>> itemNBT = new HashMap<>();

    private Map<String, Map<String, Object>> itemAttributes = new HashMap<>();

    private Map<String, Object> itemOptions = new HashMap<>();

    private List<String> hideOptions = new ArrayList<>();

    private boolean unbreakable;

    private boolean hideFlags;

    public MythicItem(String file, String internalName, MythicConfig mc) {
        this.config = mc;
        this.file = file;
        this.internalName = internalName;
        this.data = mc.getInteger("Data", 0);
        this.data = mc.getInteger("Durability", this.data);
        this.data = mc.getInteger("Options.Durability", this.data);
        if(mc.isSet("ItemStack")) {
            ItemStack is = mc.getItemStack("ItemStack", null);
            if(is != null) {
                this.itemStack = new ItemStack(is);
                try {
                    this.displayName = is.getItemMeta().getDisplayName();
                    this.lore = new ArrayList<>();
                    for(String s : is.getItemMeta().getLore()) {
                        this.lore.add(s);
                    } 
                } catch (Exception ex) {
                    //TODO: Log error
                }
                return;
            }
            this.itemStack = new ItemStack(Material.STONE);
        } else {
            this.id = mc.getString("Id", "STONE").toUpperCase();
            try {
                this.material = Material.AIR;
                try{
                    this.material = (this.data == 0) ? Material.valueOf(this.id) : 
                }
            }
        }
    }
}