package net.peacefulcraft.sco.commands;

import java.util.Hashtable;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;

public class setTeleport implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!(sender instanceof Player)) {return false;}
		Player p = (Player) sender;
		
		if(command.getName().equalsIgnoreCase("setTeleport")) {
			
			if(args.length == 0) {
				p.sendMessage("Invalid Argument");
				return true;
			}
			if(!(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)) {
				if(args[0].equalsIgnoreCase("spawn")) {
					spawn(p);
					p.sendMessage("Game spawn set to your location.");
					return true;
				} else if (args[0].equalsIgnoreCase("quit")) {
					quit(p);
					p.sendMessage("Game quit set to your location.");
					return true;
				} else {
					p.sendMessage("Invalid Argument");
					return true;
				}
			} else {
				p.sendMessage("Invalid location for teleport.");
				return true;
			}			
		} else { 
			return false;
		}
		
	}
	
	private void spawn(Player p) {
		Hashtable<String, Object> loc = new Hashtable<String, Object>();
		loc.put("world", p.getLocation().getWorld().getName());
		loc.put("x", "" + p.getLocation().getBlockX());
		loc.put("y", "" + (p.getLocation().getBlockY() - 1));
		loc.put("z", "" + p.getLocation().getBlockZ());
		SwordCraftOnline.getSCOConfig().setSpawn(loc);
	}
	
	private void quit(Player p) {
		Hashtable<String, Object> loc = new Hashtable<String, Object>();
		loc.put("world", p.getLocation().getWorld().getName());
		loc.put("x", "" + p.getLocation().getBlockX());
		loc.put("y", "" + (p.getLocation().getBlockY() - 1));
		loc.put("z", "" + p.getLocation().getBlockZ());
		SwordCraftOnline.getSCOConfig().setQuit(loc);
	}
}
