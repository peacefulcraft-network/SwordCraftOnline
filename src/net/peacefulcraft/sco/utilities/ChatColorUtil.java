package net.peacefulcraft.sco.utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;



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
            char c = str1.charAt(i);
            if(c == '&') {
                char id = str1.charAt(i+1);
                list.add(ChatColor.getByChar(id));
            }
        }

        return list;
    }

    /**
     * Gets the ChatColors used at the end of the given input string.
     *
     * @param input Input string to retrieve the colors from.
     * @return Any remaining ChatColors to pass onto the next line.
     */
    public static String getLastColors(String input) {
        String result = "";
        int length = input.length();

        // Search backwards from the end as it is faster
        for (int index = length - 1; index > -1; index--) {
            char section = input.charAt(index);
            if (section == ChatColor.COLOR_CHAR && index < length - 1) {
                char c = input.charAt(index + 1);
                ChatColor color = ChatColor.getByChar(c);

                if (color != null) {
                    result = color.toString() + result;

                    // Once we find a color or reset we can stop searching
                    if (color.isColor() || color.equals(ChatColor.RESET)) {
                        break;
                    }
                }
            }
        }

        return result;
    }

}
