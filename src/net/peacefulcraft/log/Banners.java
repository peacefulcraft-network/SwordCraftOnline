package net.peacefulcraft.log;

public enum Banners {
    ACTIVE_MOB, MYTHIC_MOB, MOB_MANAGER, DROP_TABLE, DROP, DROP_MANAGER;

    public static String get(Banners b) {
        switch(b) {
            case ACTIVE_MOB:
            return "[ACTIVE MOB]";
            case MYTHIC_MOB:
            return "[MYTHIC MOB]";
            case DROP_TABLE:
            return "[DROP TABLE]";
            case DROP:
            return "[DROP]";
            case MOB_MANAGER:
            return "[MOB MANAGER]";
            case DROP_MANAGER:
            return "[DROP MANAGER]";
            default:
            return "[DEFAULT]";
        }
        
    }
}