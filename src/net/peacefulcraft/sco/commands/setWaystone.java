package net.peacefulcraft.sco.commands;

import java.util.Hashtable;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;

public class setWaystone implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!(sender instanceof Player)) {return false;}
		Player p = (Player) sender;
		
		if(command.getName().equalsIgnoreCase("setWaystone")) {
			
			if(args.length == 0) {
				p.sendMessage("Invalid Argument");
				return true;
			}
			if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_LAMP) {
				if(args[0].equalsIgnoreCase("1")) {
					waystone(p, 1);
					p.sendMessage("First floor waystone set to your location.");
					return true;
				} else if(args[0].equalsIgnoreCase("2")) {
					waystone(p, 2);
					p.sendMessage("Second floor waystone set to your location.");
					return true;
				} else if(args[0].equalsIgnoreCase("3")) {
					waystone(p, 3);
					p.sendMessage("Third floor waystone set to your location.");
					return true;
				} else {
					p.sendMessage("Invalid Argument");
					return true;
				}	
			} else {
				p.sendMessage("Invalid location for waystone. Must be above redstone lamp.");
				return true;
			}
		} else {
			return false;
		}
	} 
	
	private void waystone(Player p, int index) {
		Hashtable<String, Object> loc = new Hashtable<String, Object>();
		loc.put("world", p.getLocation().getWorld().getName());
		loc.put("x", "" + p.getLocation().getBlockX());
		loc.put("y", "" + (p.getLocation().getBlockY() - 1));
		loc.put("z", "" + p.getLocation().getBlockZ());
		switch(index) {
		case 1:
			SwordCraftOnline.getSCOConfig().setWaystone(loc, 1);
		case 2:
			SwordCraftOnline.getSCOConfig().setWaystone(loc, 2);
		case 3:
			SwordCraftOnline.getSCOConfig().setWaystone(loc, 3);
		default:
			throw new RuntimeException("Waystone index out of bounds.");
		}
	}
	
}
