package net.peacefulcraft.sco.mythicmobs.mobs.bosses;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.peacefulcraft.sco.SwordCraftOnline;

public class BossIdentifier {

    public static boolean bossExists(String name) {
        try {
            String sName = name.replaceAll(" ", "");
            Class<?> classs = Class.forName("net.peacefulcraft.sco.mobs.bosses." + sName);
            if(classs == null) {
                return false;
            }
            return true;
        }catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static MythicBoss getBoss(String name, Integer level) {
        try {
            name = "Boss" + name.replaceAll(" ", "");
            Class<?> classs = Class.forName("net.peacefulcraft.sco.mythicmobs.mobs.bosses." + name);
            Constructor<?> constructor = classs.getConstructor(int.class);
            MythicBoss boss = (MythicBoss) constructor.newInstance(level);

            return boss;
        } catch (ClassNotFoundException e) {
			SwordCraftOnline.logSevere("Attempted to create boss " + name + ", but no coresponding class was found in net.peacefulcraft.sco.mythicmobs.bosses");
		} catch (NoSuchMethodException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.mythicmobs.bosses." + name + " must have a constuctor with arguments (int)");
		} catch (SecurityException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.mythicmobs.bosses." + name + " does not have a public constructor.");
		} catch (InstantiationException | InvocationTargetException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.mythicmobs.bosses." + name + " generated exception during reflective instantiation:");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.mythicmobs.bosses." + name + " is an abstract class and cannot be instantiated.");
		} catch (IllegalArgumentException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.mythicmobs.bosses." + name + " received an invalid arguement type during insantiation. Arguements must be of type (int, ItemTier).");
		} catch (ClassCastException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.mythicmobs.bosses." + name + " must implement SwordSkillProvider.");
		}
		
		throw new RuntimeException();
    }
}