package net.peacefulcraft.sco.particles;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;

public class EffectIdentifier {
    public static boolean effectExists(String name) {
        try {
            String eName = name.replaceAll(" ", "");
            Class<?> classs = Class.forName("net.peacefulcraft.sco.particles.effect." + eName);
            if(classs == null) {
                return false;
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void runEffect(String name, Player p) {
        try {
            name = name.replaceAll(" ", "");
            Class<?> clazz = Class.forName("net.peacefulcraft.sco.particles.effect." + name);
            Constructor<?> constructor = clazz.getConstructor(EffectManager.class);

            Object o = constructor.newInstance(SwordCraftOnline.getEffectManager());
            
        } catch (ClassNotFoundException e) {
            SwordCraftOnline.logSevere("Attempted to create effect " + name + ", but no corresponding class was found.");
        } catch (NoSuchMethodException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.particles.effect." + name + " must have a constuctor.");
        } catch (SecurityException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.particles.effect." + name + " must have public constuctor.");
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}