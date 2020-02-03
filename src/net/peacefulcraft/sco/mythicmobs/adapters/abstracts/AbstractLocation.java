package net.peacefulcraft.sco.mythicmobs.adapters.abstracts;

import org.bukkit.util.Vector;

public class AbstractLocation implements Cloneable {
    private AbstractWorld world;
  
    private double x;
    
    private double y;
    
    private double z;
    
    private float yaw;
    
    private float pitch;
    
    public AbstractLocation() {}
    
    public AbstractLocation(AbstractWorld world, double x, double y, double z) {
      this.world = world;
      this.x = x;
      this.y = y;
      this.z = z;
    }
    
    public AbstractLocation(AbstractWorld world, double x, double y, double z, float yaw, float pitch) {
      this.world = world;
      this.x = x;
      this.y = y;
      this.z = z;
      this.yaw = yaw;
      this.pitch = pitch;
    }
    
    public void setX(double x) {
      this.x = x;
    }
    
    public double getX() {
      return this.x;
    }
    
    public void setY(double y) {
      this.y = y;
    }
    
    public double getY() {
      return this.y;
    }
    
    public void setZ(double z) {
      this.z = z;
    }
    
    public double getZ() {
      return this.z;
    }
    
    public int getBlockX() {
      return (int)this.x;
    }
    
    public int getBlockY() {
      return (int)this.y;
    }
    
    public int getBlockZ() {
      return (int)this.z;
    }
    
    public int getChunkX() {
      return getBlockX() / 16;
    }
    
    public int getChunkZ() {
      return getBlockZ() / 16;
    }
    
    public float getYaw() {
      return this.yaw;
    }
    
    public void setYaw(float yaw) {
      this.yaw = yaw;
    }
    
    public float getPitch() {
      return this.pitch;
    }
    
    public void setPitch(float pitch) {
      this.pitch = pitch;
    }
    
    public AbstractWorld getWorld() {
      return this.world;
    }
    
    public double distance(AbstractLocation t) throws IllegalArgumentException {
      return Math.sqrt(distanceSquared(t));
    }
    
    public double distanceSquared(AbstractLocation t) throws IllegalArgumentException {
      if (!t.getWorld().equals(this.world))
        throw new IllegalArgumentException("Cannot measure distance between two different worlds."); 
      return Math.pow(t.getX() - getX(), 2.0D) + Math.pow(t.getY() - getY(), 2.0D) + Math.pow(t.getZ() - getZ(), 2.0D);
    }
    
    public double distance2D(AbstractLocation t) {
      return Math.sqrt(distance2DSquared(t));
    }
    
    public double distance2DSquared(AbstractLocation t) {
      if (!t.getWorld().equals(this.world))
        throw new IllegalArgumentException("Cannot measure distance between two different worlds."); 
      return Math.pow(t.getX() - getX(), 2.0D) + Math.pow(t.getZ() - getZ(), 2.0D);
    }
    
    public AbstractLocation add(double xA, double yA, double zA) {
      this.x += xA;
      this.y += yA;
      this.z += zA;
      return this;
    }
    
    public Vector getDirection() {
      Vector vector = new Vector();
      double rotX = getYaw();
      double rotY = getPitch();
      vector.setY(-Math.sin(Math.toRadians(rotY)));
      double xz = Math.cos(Math.toRadians(rotY));
      vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
      vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
      return vector;
    }
    
    public AbstractLocation add(AbstractLocation vec) {
      if (vec == null || !vec.getWorld().equals(getWorld()))
        throw new IllegalArgumentException("Cannot add Locations of differing worlds"); 
      this.x += vec.x;
      this.y += vec.y;
      this.z += vec.z;
      return this;
    }
    
    public AbstractLocation add(Vector vec) {
      this.x += vec.getX();
      this.y += vec.getY();
      this.z += vec.getZ();
      return this;
    }
    
    public AbstractLocation subtract(double x, double y, double z) {
      this.x -= x;
      this.y -= y;
      this.z -= z;
      return this;
    }
    
    public AbstractLocation subtract(AbstractLocation vec) {
      if (vec == null || !vec.getWorld().equals(getWorld()))
        throw new IllegalArgumentException("Cannot add Locations of differing worlds"); 
      this.x -= vec.x;
      this.y -= vec.y;
      this.z -= vec.z;
      return this;
    }
    
    public AbstractLocation subtract(Vector vec) {
      this.x -= vec.getX();
      this.y -= vec.getY();
      this.z -= vec.getZ();
      return this;
    }
    
    public AbstractLocation multiply(double m) {
      this.x *= m;
      this.y *= m;
      this.z *= m;
      return this;
    }
    
    public AbstractLocation zero() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      return this;
    }
    
    public Vector toVector() {
      return new Vector(this.x, this.y, this.z);
    }
    
    public boolean equals(Object obj) {
      if (obj == null)
        return false; 
      if (getClass() != obj.getClass())
        return false; 
      AbstractLocation other = (AbstractLocation)obj;
      if (this.world != other.world && (this.world == null || !this.world.equals(other.world)))
        return false; 
      if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
        return false; 
      if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
        return false; 
      if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
        return false; 
      if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch))
        return false; 
      if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw))
        return false; 
      return true;
    }
    
    public String toString() {
      return "Location{world=" + this.world + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",pitch=" + this.pitch + ",yaw=" + this.yaw + '}';
    }
    
    public int hashCode() {
      int hash = 3;
      hash = 19 * hash + ((this.world != null) ? this.world.hashCode() : 0);
      hash = 19 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32L);
      hash = 19 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32L);
      hash = 19 * hash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32L);
      hash = 19 * hash + Float.floatToIntBits(this.pitch);
      hash = 19 * hash + Float.floatToIntBits(this.yaw);
      return hash;
    }
    
    public AbstractLocation clone() {
      try {
        return (AbstractLocation)super.clone();
      } catch (CloneNotSupportedException e) {
        throw new Error(e);
      } 
    }
    
    public boolean isLoaded() {
      return getWorld().isLocationLoaded(this);
    }
}