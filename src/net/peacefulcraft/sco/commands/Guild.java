package net.peacefulcraft.sco.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.storage.StorageTaskOutcome;
import net.peacefulcraft.sco.storage.tasks.GuildCreateAsyncTask;

public class Guild implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!(sender instanceof Player)) { return true; }
		Player p = (Player) sender;
		
		if(args.length > 1) {
			if(args[0].equalsIgnoreCase("create")) {
				
				SCOPlayer s = SwordCraftOnline.getGameManager().findSCOPlayer(p);
				if(s.isInGuild()) {
					p.sendMessage(ChatColor.GOLD + "You must leave your current guild before you can found a new guild.");
					return true;
				}
				
				if(args.length > 2) {
					String guildDescription = (args.length > 3)? args[2] : "";
					
					(new GuildCreateAsyncTask(args[1], guildDescription, p.getUniqueId(), (StorageTaskOutcome outcome, ArrayList<HashMap<String, Object>> result) -> {
						if(outcome == StorageTaskOutcome.SUCESS) {
							s.setGuildId((Long) result.get(0).get("guildId"));
							s.setGuildRank(GuildRank.OWNER);
							p.sendMessage(ChatColor.GOLD + "Guild " + args[1] + " sucesfully created.");

						} else if(outcome == StorageTaskOutcome.DUPLICATE_KEY_FAILURE) {
							p.sendMessage(ChatColor.RED + "A guild with the name " + args[1] + " already exists.");

						} else {
							p.sendMessage(ChatColor.RED + "Internal server error. Guild creation failed. Please try again. If the issue persists, contact a staff member.");

						}
					})).runTaskAsynchronously(SwordCraftOnline.getPluginInstance());

				} else {
					p.sendMessage(ChatColor.RED + "Please specify a guild name.");
				}
				
			} else if(args[0].equalsIgnoreCase("disban")) {
				
			} else if(args[0].equalsIgnoreCase("setname")) {
				
			} else if(args[0].equalsIgnoreCase("setdescription")) {
				
			} else if(args[0].equalsIgnoreCase("invite")) {
				
			} else if(args[0].equalsIgnoreCase("join")) {
			
			}
		} else { 
			p.sendMessage(ChatColor.GOLD + "Valid arguments: " + ChatColor.RED + "create, disban, setname, description");
		}
		return true;
	}

	public enum GuildRank {
		OWNER, LEADERSHIP, MEMBER, ALLY
	}
	
	public enum GuildAuditAction {
		JOIN, LEAVE, PROMOTE, DEMOTE, CREATE_GUILD, DISBAN_GUILD,
		SET_TITLE, SET_PROPERTY, INVITE, INVITE_ACCEPT, INVITE_REJECT
	}
}
