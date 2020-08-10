package net.peacefulcraft.sco.gamehandle.regions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class Region {
    
    private String file;

    private String internalName;

    private MythicConfig config;

    private Integer x1;

    private Integer x2;

    private Integer z1;

    private Integer z2;

    /**Name of location that is displayed anywhere */
    private String name;
        public String getName() { return this.name; }

    /**Brief description of location */
    private String description;

    private Integer floor;
        public Integer getFloor() { return this.floor; }

    private Boolean preventPVP;
        public Boolean doesPreventPVP() { return preventPVP; }

    private Boolean preventHostileSpawn;
        public Boolean doesPreventHostile() { return preventHostileSpawn; }

    private Boolean preventPassiveSpawn;
        public Boolean doesPreventPassive() { return preventPassiveSpawn; }

    private Boolean preventSwordSkills;
        public Boolean doesPreventSwordSkills() { return this.preventSwordSkills; }

    private Boolean preventPlayerDeath;
        public Boolean doesPreventPlayerDeath() { return this.preventPlayerDeath; }

    public Region(String file, String internalName, MythicConfig mc) {
        this.file = file;
        this.internalName = internalName;
        this.config = mc;

        //Loading bounding points
        this.x1 = config.getInteger("x1", 0);
        this.x2 = config.getInteger("x2", 0);
        this.z1 = config.getInteger("z1", 0);
        this.z2 = config.getInteger("z2", 0);

        this.name = config.getString("Name");
        this.description = config.getString("Description", "");
        this.description = config.getString("Desc", this.description);
        this.floor = config.getInteger("Floor", 0);

        //Loading Flags
        this.preventPVP = config.getBoolean("PreventPVP", false);
        this.preventHostileSpawn = config.getBoolean("PreventHostileSpawn", false);
        this.preventPassiveSpawn = config.getBoolean("PreventPassiveSpawn", false);
        this.preventSwordSkills = config.getBoolean("PreventSwordSkills", false);
        this.preventPlayerDeath = config.getBoolean("PreventPlayerDeath", false);
    }

    /**
     * @param loc Location to be checked in this region
     * @return True if location is inside region
     */
    public boolean isInRegion(Location loc) {
        int locX = loc.getBlockX();
        int locZ = loc.getBlockZ();

        //(x1,y1) -> (x2,y1)
        boolean test1 = _isInRegion(x1, x2, z1, z1, locX, locZ);
        //(x2,y1) -> (x2,y2)
        boolean test2 = _isInRegion(x2, x2, z1, z2, locX, locZ);
        //(x2,y2) -> (x1,y2)
        boolean test3 = _isInRegion(x2, x1, z2, z2, locX, locZ);
        //(x1,y2) -> (x1,y1)
        boolean test4 = _isInRegion(x1, x1, z2, z1, locX, locZ);

        if(test1 && test2 && test3 && test4) { return true; }
        return false;
    }

    /**Helper function used to check sides of rectangle bounds */
    private boolean _isInRegion(int x1, int x2, int z1, int z2, int pointX, int pointY) {
        double A = -(z2 - z1);
        double B = x2 - x1;
        double C = -(A * x1 + B * z1);

        double D = (A * pointX) + (B * pointY) + C;
        if(D > 0) { return false; }
        return true;
    }

    /**Sends player title of name and desc */
    public void sendTitle(Player p) {
        Announcer.sendTitle(p, name, description);
    }

    /**@return String of information regarding this region */
    public String getInfo() {
        String s = "Name: " + name + "\n";
        s += "Description: " + description + "\n";
        s += "Floor: " + String.valueOf(floor) + "\n";
        s += "Point 1: " + String.valueOf(x1) + "," + String.valueOf(z1) + "\n";
        s += "Point 2: " + String.valueOf(x2) + "," + String.valueOf(z2) + "\n";
        s += "DoesPreventPVP: " + String.valueOf(preventPVP) + "\n";
        s += "DoesPreventHostileSpawn: " + String.valueOf(preventHostileSpawn) + "\n";
        s += "DoesPreventPassiveSpawn: " + String.valueOf(preventPassiveSpawn) + "\n";
        s += "DoesPreventSwordSkills: " + String.valueOf(preventSwordSkills);
        return s;
    }

}