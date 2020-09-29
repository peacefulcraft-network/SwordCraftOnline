package net.peacefulcraft.sco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.Party;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class partyCommands implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!(sender instanceof Player)) {return false;}
		Player p = (Player) sender;
		if(GameManager.findSCOPlayer(p) == null) {return false;}
		
		if(command.getName().equalsIgnoreCase("party")) {
			
			if(args.length == 0) {
				p.sendMessage("Invalid Argument");
				return true;
			}
			if(args[0].equalsIgnoreCase("create")) {
				if(args[1].isEmpty()) {
					p.sendMessage("Invalid arguments! Enter party name following command");
					return false;
				} else {
					if(createParty(p, args[1])) {
						p.sendMessage(ChatColor.GOLD + "You are now the founder of " + ChatColor.BLUE + args[1]);
						return true;
					} else {
						p.sendMessage("Cannot create party. Pick new name or leave current party");
						return true;
					}
				}
			} else if(args[0].equalsIgnoreCase("rename")) {
				if(args[1].isEmpty()) {
					p.sendMessage("Invalid arguments! Enter party name following command");
					return false;
				} else {
					if(renameParty(p, args[1])) {
						p.sendMessage(ChatColor.GOLD + "You have renamed your party to " + ChatColor.BLUE + args[1]);
						return true;
					} else {
						p.sendMessage("Party name already taken");
						return true;
					}
				}
			} else if(args[0].equalsIgnoreCase("disband")) {
				
				SwordCraftOnline.getPartyManager().getParty(GameManager.findSCOPlayer(p).getPartyName()).removeParty();
				p.sendMessage(ChatColor.GOLD + "You have disbanded " + ChatColor.BLUE + "" + GameManager.findSCOPlayer(p).getPartyName());
			
			} else if(args[0].equalsIgnoreCase("invite")) {
				if(args[1].isEmpty()) {
					p.sendMessage("Invalid arguments! Enter online player name following command");
					return true;
				} else {
					if(invite(p, args[1])) {
						return true;
					} else {
						p.sendMessage("Cannot send message to offline player");
						return true;
					}
				}
			} else if(args[0].equalsIgnoreCase("cancelinvite")) {
				if(args[1].isEmpty()) {
					p.sendMessage("Invalid arguments! Enter player name to cancel invite");
					return true;
				} else {
					if(cancelInvite(p, args[1])) {
						return true;
					} else {
						p.sendMessage("Invalid arguments! Player not invited to party");
						return true;
					}
				}
			} else if(args[0].equalsIgnoreCase("accept")) {
				acceptInvite(p);
				return true;
			} else if(args[0].equalsIgnoreCase("deny")) {
				denyInvite(p);
				return true;
			} else if(args[0].equalsIgnoreCase("setleader")) {
				if(args[1].isEmpty()) {
					p.sendMessage("Invalid arguments! Enter name of party member after command");
					return true;
				} else {
					setLeader(p, args[1]);
					return true;
				}
			} else if(args[0].equalsIgnoreCase("leave")) {
				leave(p);
				return true;
			} else if(args[0].equalsIgnoreCase("name")) {
				name(p);
				return true;
			}
		}
		p.sendMessage("Unkown command.");
		
		return false;
	}
	
	//TODO: CHange all getSCOConfig to getDatabase
	private boolean createParty(Player p, String name) {
		if(SwordCraftOnline.getSCOConfig().getParties().containsKey(name)) {
			return false;
		}
		if(GameManager.findSCOPlayer(p).isInParty()) {
			return false;
		} else {
			Party party = new Party(name);
			party.createParty(GameManager.findSCOPlayer(p));
			//SwordCraftOnline.getPartyManager().saveDatabase(party);
			return true;
		}
	}
	
	private boolean renameParty(Player p, String name) {
		if(SwordCraftOnline.getSCOConfig().getParties().containsKey(name)) {
			return false;
		} else {
			Party party = SwordCraftOnline.getPartyManager().getParty(GameManager.findSCOPlayer(p).getPartyName());
			party.renameParty(name);
			return true;
		}
	}
	
	private boolean invite(Player p, String name) {
		Party party = SwordCraftOnline.getPartyManager().getParty(GameManager.findSCOPlayer(p).getPartyName());
		SCOPlayer invited = GameManager.findSCOPlayer(name);
		if(invited == null) {
			return false;
		} else {
			party.invitePlayer(GameManager.findSCOPlayer(p), invited);
			return true;
		}
	}
	
	private boolean cancelInvite(Player p, String name) {
		SCOPlayer invited = GameManager.findSCOPlayer(name);
		Party party = SwordCraftOnline.getPartyManager().getParty(GameManager.findSCOPlayer(p).getPartyName());
		if(party.cancelInvite(invited)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void acceptInvite(Player p) {
		SCOPlayer invited = GameManager.findSCOPlayer(p);
		Party party = SwordCraftOnline.getPartyManager().getParty(GameManager.findSCOPlayer(p).getLastInvite());
		party.acceptInvite(invited, true);
	}
	
	private void denyInvite(Player p) {
		SCOPlayer invited = GameManager.findSCOPlayer(p);
		Party party = SwordCraftOnline.getPartyManager().getParty(GameManager.findSCOPlayer(p).getLastInvite());
		party.denyInvite(invited);
	}
	
	private void setLeader(Player p, String name) {
		Party party = SwordCraftOnline.getPartyManager().getParty(GameManager.findSCOPlayer(p).getPartyName());
		if(!(p.getUniqueId() == party.getLeader().getUUID())) { 
			p.sendMessage(ChatColor.GOLD + "Must be party leader to set new leader");
			return;
		}
		party.setLeader(GameManager.findSCOPlayer(name));
		p.sendMessage(ChatColor.BLUE + name + ChatColor.GOLD + " has been made party leader");
	}
	
	private void leave(Player p) {
		Party party = SwordCraftOnline.getPartyManager().getParty(GameManager.findSCOPlayer(p).getPartyName());
		if(party == null) {
			p.sendMessage("Must be in party to leave");
			return;
		}
		party.leaveParty(GameManager.findSCOPlayer(p));
		
	}
	
	private void name(Player p) {
		if(GameManager.findSCOPlayer(p).isInParty()) {
			p.sendMessage(ChatColor.GOLD + "Your party name is: " + ChatColor.BLUE + "" + GameManager.findSCOPlayer(p).getPartyName());
		} else {
			p.sendMessage(ChatColor.GOLD + "You are not in a party");
		}
			
	}
}
