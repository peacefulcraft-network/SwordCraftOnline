package net.peacefulcraft.sco.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class GuildTabCompleter implements TabCompleter{

	private ArrayList<String> confirm_options;
	private ArrayList<String> arg0_options;
	
	public GuildTabCompleter() {
		confirm_options = new ArrayList<String>();
		confirm_options.add("cofirm");
		confirm_options.add("cancel");
		
		arg0_options = new ArrayList<String>();
		arg0_options.add("create");
		arg0_options.add("disban");
		arg0_options.add("setname");
		arg0_options.add("setdescription");
		arg0_options.add("join");
		arg0_options.add("leave");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!(sender instanceof Player)) { return null; }
		Player p = (Player) sender;
		
		if(args.length > 1) {
			if(args[0].equalsIgnoreCase("create")) {
				if(args.length > 2) {

				} else {

				}
				
			} else if(args[0].equalsIgnoreCase("disban")) {
				
			} else if(args[0].equalsIgnoreCase("setname")) {
				
			} else if(args[0].equalsIgnoreCase("setdescription")) {
				
			} else if(args[0].equalsIgnoreCase("invite")) {
				
			} else if(args[0].equalsIgnoreCase("join")) {
			
			} else if(args[0].equalsIgnoreCase("leave")) {
				
			}
		}
		
		return arg0_options;
	}

}
