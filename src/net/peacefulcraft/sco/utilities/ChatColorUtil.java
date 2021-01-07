package net.peacefulcraft.sco.utilities;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;


/**
 * Utility methods for chatcolor 
 */
public class ChatColorUtil {
    
    /**
     * Gets chat color of string
     * @return List of chat colors in string
     */
    public static List<ChatColor> getColor(String str) {
        List<ChatColor> list = new ArrayList<ChatColor>();

        String str1 = str.replace(ChatColor.COLOR_CHAR, '&');

        for(int i = 0; i < str1.length(); i++) {
            char c = str.charAt(i);
            if(c == '&') {
                char id = str.charAt(i+1);
                list.add(ChatColor.getByChar(id));
            }
        }

        return list;
    }

}
