package net.peacefulcraft.sco.mythicmobs.adapters.abstracts;

import org.bukkit.util.NumberConversions;

public class AbstractVector implements Cloneable {
    private static final double epsilon = 1.0E-6D;
    
    protected double x;
    
    protected double y;
    
    protected double z;
    
    public AbstractVector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public AbstractVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public AbstractVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public AbstractVector() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }
    
    public AbstractVector add(AbstractVector vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }
    
    public AbstractVector subtract(AbstractVector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }
    
    public AbstractVector multiply(AbstractVector vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }
    
    public AbstractVector multiply(int m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }
    
    public AbstractVector multiply(double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }
    
    public AbstractVector multiply(float m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }
    
    public AbstractVector divide(AbstractVector vec) {
        this.x /= vec.x;
        this.y /= vec.y;
        this.z /= vec.z;
        return this;
    }
    
    public AbstractVector copy(AbstractVector vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }
    
    public double length() {
        return Math.sqrt(NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z));
    }
    
    public double lengthSquared() {
        return NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z);
    }
    
    public double getX() {
        return this.x;
    }
    
    public int getBlockX() {
        return NumberConversions.floor(this.x);
    }
    
    public double getY() {
        return this.y;
    }
    
    public int getBlockY() {
        return NumberConversions.floor(this.y);
    }
    
    public double getZ() {
        return this.z;
    }
    
    public int getBlockZ() {
        return NumberConversions.floor(this.z);
    }
    
    public AbstractVector setX(int x) {
        this.x = x;
        return this;
    }
    
    public AbstractVector setX(double x) {
        this.x = x;
        return this;
    }
    
    public AbstractVector setX(float x) {
        this.x = x;
        return this;
    }
    
    public AbstractVector setY(int y) {
        this.y = y;
        return this;
    }
    
    public AbstractVector setY(double y) {
        this.y = y;
        return this;
    }
    
    public AbstractVector setY(float y) {
        this.y = y;
        return this;
    }
    
    public AbstractVector setZ(int z) {
        this.z = z;
        return this;
    }
    
    public AbstractVector setZ(double z) {
        this.z = z;
        return this;
    }
    
    public AbstractVector setZ(float z) {
        this.z = z;
        return this;
    }
    
    public AbstractVector normalize() {
        double length = length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }
    
    public AbstractVector rotate(float degrees) {
        double rad = Math.toRadians(degrees);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double x = getX() * cos - getZ() * sin;
        double z = getX() * sin + getZ() * cos;
        setX(x);
        setZ(z);
        return this;
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractVector)) { return false; } 
        AbstractVector other = (AbstractVector)obj;
        return (Math.abs(this.x - other.x) < 1.0E-6D && Math.abs(this.y - other.y) < 1.0E-6D && Math.abs(this.z - other.z) < 1.0E-6D && getClass().equals(obj.getClass()));
    }
    
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32L);
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32L);
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32L);
        return hash;
    }
    
    public AbstractVector clone() {
        try {
            return (AbstractVector)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        } 
    }
    
    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }
    
    public AbstractLocation toLocation(AbstractWorld world) {
        return new AbstractLocation(world, this.x, this.y, this.z);
    }
    
    public AbstractLocation toLocation(AbstractWorld world, float yaw, float pitch) {
        return new AbstractLocation(world, this.x, this.y, this.z, yaw, pitch);
    }
    
    public static double getEpsilon() {
        return epsilon;
    }
    
    public double dot(AbstractVector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }
    
    public float angle(AbstractVector other) {
        double dot = dot(other) / length() * other.length();
        return (float)Math.acos(dot);
    }
}