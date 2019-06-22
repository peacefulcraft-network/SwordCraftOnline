package net.peacefulcraft.sco.gamehandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class Party
{
	private String name;
	private ArrayList<SCOPlayer> members;
	private SCOPlayer leader;
	private HashMap<SCOPlayer, SCOPlayer> inviteMap;
	
	private FileConfiguration partyConfig;

	public Party(String name) {
		this.name = name;
		members = new ArrayList<>();
		leader = null;
		
		inviteMap = new HashMap<>();
	}
	
	public void createParty(SCOPlayer leader) {
		if(leader == null) {
			return;
		} else {
			setLeader(leader);
			getMembers().add(leader);
		}
	}
	
	public void updateParty() {
		Hashtable<String, Object> partyTable = new Hashtable<String, Object>();
		ArrayList<String> str = new ArrayList<String>();
		
		for(SCOPlayer member : getMembers()) {
			str.add(member.getUUID().toString());
		}
		partyTable.put("members", str);
		partyTable.put("leader", getLeader().getUUID());
		partyTable.put("inviteMap", getInviteMap());
		
		partyConfig.set(getName(), partyTable);
	}
	
	public void leaveParty(SCOPlayer p) {
		if(p.isInParty()) {
			if(getMembers().size() == 1 && p == SwordCraftOnline.getPartyManager().getParty(p.getPartyName()).getLeader()) {
				removeParty();				
			} else {
				getMembers().remove(p);
				p.setPartyName("");
				for(SCOPlayer member : getMembers()) {
					if(member.getUUID() != p.getUUID()) {
						setLeader(member);
						break;
					}
				}
			}
			updateParty();
		}
	}
	
	public void renameParty(String newName) {
		SwordCraftOnline.getPartyManager().getListParties().remove(getName());
		
		//TODO: get datamanger
		//SwordCraftOnline.getSCOConfig().setParty(newName, this);
		
		for(SCOPlayer member : getMembers()) {
			member.setPartyName(newName);
		}
		setName(newName);
		SwordCraftOnline.getPartyManager().getListParties().put(newName.toLowerCase(), this);
		
		updateParty();
	}
	
	public void removeParty() {		
		SwordCraftOnline.getPartyManager().getListParties().remove(getName());
		//TODO: Get datamanager instead
		//SwordCraftOnline.getSCOConfig().removeParty(getName());
		for(SCOPlayer member : getMembers()) {
			member.setPartyName("");
		}
	}
	
	public void invitePlayer(SCOPlayer invitedBy, SCOPlayer invitedPlayer) {
		if(invitedPlayer.getPlayer().isOnline()) {
			inviteMap.put(invitedPlayer, invitedBy);
			invitedPlayer.setLastInvite(invitedBy.getPartyName());
			
			updateParty();
			
		} else {
			invitedBy.getPlayer().sendMessage(ChatColor.GOLD + "Cannot send invite to offline player.");
		}
	}
	
	public boolean cancelInvite(SCOPlayer invitedPlayer) {
		if(!(inviteMap.containsKey(invitedPlayer))) {
			return false;
		} else {
			SCOPlayer invitedBy = inviteMap.get(invitedPlayer);
			
			invitedBy.getPlayer().sendMessage(ChatColor.GOLD + "Invite to " + ChatColor.BLUE + "" + invitedPlayer + ChatColor.GOLD + " cancelled.");
			invitedPlayer.getPlayer().sendMessage("Most recent invite has been cancelled.");
			
			
			invitedPlayer.setLastInvite("");
			
			inviteMap.remove(invitedPlayer);
			
			updateParty();
			return true;
		}
	}
	
	public void acceptInvite(SCOPlayer invitedPlayer, Boolean accepted) {
		SCOPlayer invitedBy = inviteMap.get(invitedPlayer);
		if(accepted) {
			invitedBy.getPlayer().sendMessage(ChatColor.BLUE + "" + invitedPlayer + ChatColor.GOLD + " has accepted your party invite.");
			invitedPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You have accepted " + ChatColor.BLUE + "" + invitedBy + ChatColor.GOLD + "'s invite.");
			
			inviteMap.remove(invitedBy);
			invitedPlayer.setLastInvite("");
			
			getMembers().add(invitedPlayer);
			
			invitedPlayer.setPartyName(getName());
			
			updateParty();
		} 
	}
	
	public void denyInvite(SCOPlayer invitedPlayer) {
		SCOPlayer invitedBy = inviteMap.get(invitedPlayer);
		invitedBy.getPlayer().sendMessage(ChatColor.BLUE + "" + invitedPlayer + ChatColor.GOLD + " has denied your party invite.");
		invitedPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You have denied " + ChatColor.BLUE + "" + invitedBy + ChatColor.GOLD + "'s invite.");
		
		invitedPlayer.setLastInvite("");
		inviteMap.remove(invitedPlayer);
		
		updateParty();
	}
	
	public double onlineMemberPercent() {
		int online = 0;
		for(SCOPlayer member : getMembers()) {
			if(member.getPlayer().isOnline()) {
				online ++;
			}
		}
		return online / getMembers().size();
	}
	
	public void setLeader(SCOPlayer leader) {
		leader.setPartyName(this.name);
		this.leader = leader;
	}
	
	public SCOPlayer getLeader() {
		return this.leader;
	}
	
	public ArrayList<SCOPlayer> getMembers() {
		return this.members;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public HashMap<SCOPlayer, SCOPlayer> getInviteMap() {
		return this.inviteMap;
	}
}
