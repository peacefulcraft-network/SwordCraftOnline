package net.peacefulcraft.sco.structures;

public class StructureIdentifier {
    
    public static boolean structureExists(String name) {
        try{
            String sName = name.replaceAll(" ", "");
            Class<?> classs = Class.forName("net.peacefulcraft.sco.structures.structures." + sName);
            if(classs == null) {
                return false;
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}