package net.peacefulcraft.sco.mythicmobs.mobs.entities;

import org.bukkit.entity.EntityType;

public enum MythicEntityType {
    ARMOR_STAND, BABY_DROWNED, BABY_HUSK, BABY_PIG_ZOMBIE, BABY_PIG_ZOMBIE_VILLAGER, BABY_ZOMBIE, BABY_ZOMBIE_VILLAGER, BAT, BLAZE, CAT, CAVE_SPIDER, CHICKEN, COD, COW, CREEPER, CUSTOM, DOLPHIN, DONKEY, DROWNED, ELDER_GUARDIAN, ENDER_DRAGON, ENDERMAN, ENDERMITE, ENDER_CRYSTAL , EVOKER, FALLING_BLOCK, FOX, GHAST, GIANT, GUARDIAN, HORSE, HUSK, ILLUSIONER, IRON_GOLEM, LLAMA, MAGMA_CUBE, MINECART, MINECART_CHEST, MINECART_COMMAND, MINECART_FURNACE, MINECART_HOPPER, MINECART_MOB_SPAWNER, MINECART_TNT, MULE, MUSHROOM_COW, OCELOT, PANDA, PARROT, PHANTOM, PIG, PIG_ZOMBIE, PIG_ZOMBIE_VILLAGER, PILLAGER, POLAR_BEAR, PRIMED_TNT, PUFFERFISH, RABBIT, RAVAGER, SALMON, SHEEP, SHULKER, SILVERFISH, SKELETON, SKELETON_HORSE, SLIME, SNOWMAN, SPIDER, STRAY, SQUID, TRADER_LLAMA, TROPICAL_FISH, TURTLE, VEX, VILLAGER, VINDICATOR, WANDERING_TRADER, WITCH, WITHER, WITHER_SKELETON, WOLF, ZOMBIE, ZOMBIE_HORSE, ZOMBIE_VILLAGER;

    public static MythicEntityType get(String s) {
        if(s == null)
            return null;
        try{
            return valueOf(s.toUpperCase());
        } catch (IllegalArgumentException ex) {
            try {
                EntityType et = EntityType.valueOf(s.toUpperCase());
                if(et == null)
                    return null;
                return CUSTOM;
            } catch (Exception exx) {
                return null;
            }
        }
    }
}