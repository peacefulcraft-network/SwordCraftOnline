package net.peacefulcraft.sco.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ConstructTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if(cmd.getName().equalsIgnoreCase("scoadmin") && args.length >= 2 && args[0].equals("mm")) {
            if(sender instanceof Player) {
                //Player p = (Player) sender;
                return mmList;
            }
        }
        return null;
    }

    private static List<String> mmList = new ArrayList<String>();
    static {
        mmList.add("loadDropTables");
        mmList.add("loadMobs");
        mmList.add("spawn");
        mmList.add("killall");
        mmList.add("list");
        mmList.add("generatedroptable");
    }
}