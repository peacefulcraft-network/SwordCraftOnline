package net.peacefulcraft.sco.gamehandle.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class Party
{
	private String name;
	private ArrayList<SCOPlayer> members;
	private UUID leader;
	private HashMap<SCOPlayer, SCOPlayer> inviteMap;

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
			setLeader(leader.getUUID());
			getMembers().add(leader);
		}
	}
	
	public void updateParty() {
		//TODO: Databases manager update this
	}
	
	public void renameParty(String newName) {
		SwordCraftOnline.getPartyManager().getListParties().remove(getName());
		//TODO: Database rename
		for(SCOPlayer member : getMembers()) {
			member.setPartyName(newName);
		}
		setName(newName);
		SwordCraftOnline.getPartyManager().getListParties().put(newName.toLowerCase(), this);
	}
	
	public void removeParty() {		
		SwordCraftOnline.getPartyManager().getListParties().remove(getName().toLowerCase());
		//TODO: Database remove
		for(SCOPlayer member : getMembers()) {
			member.setPartyName("");
		}
	}
	
	public void invitePlayer(SCOPlayer invitedBy, SCOPlayer invitedPlayer) {
		if(invitedPlayer.getPlayer().isOnline()) {
			inviteMap.put(invitedPlayer, invitedBy);
		} else {
			invitedBy.getPlayer().sendMessage(ChatColor.WHITE + "Cannot send invite to offline player.");
		}
	}
	
	public void cancelInvite(SCOPlayer invitedPlayer) {
		SCOPlayer invitedBy = inviteMap.get(invitedPlayer);
		
		invitedBy.getPlayer().sendMessage("Invite to " + ChatColor.BLUE + "" + invitedPlayer + ChatColor.WHITE + " cancelled.");
		invitedPlayer.getPlayer().sendMessage("Most recent invite has been cancelled.");
		
		
		invitedPlayer.setLastInvite("");
		
		inviteMap.remove(invitedPlayer);
	}
	
	public void acceptInvite(SCOPlayer invitedPlayer, Boolean accepted) {
		SCOPlayer invitedBy = inviteMap.get(invitedPlayer);
		if(accepted) {
			invitedBy.getPlayer().sendMessage(ChatColor.BLUE + "" + invitedPlayer + ChatColor.WHITE + " has accepted your party invite.");
			invitedPlayer.getPlayer().sendMessage(ChatColor.WHITE + "You have accepted " + ChatColor.BLUE + "" + invitedBy + ChatColor.WHITE + "'s invite.");
			
			inviteMap.remove(invitedBy);
			invitedPlayer.setLastInvite("");
			
			getMembers().add(invitedPlayer);
			
			invitedPlayer.setPartyName(getName());
			
			//TODO: Database update player
			
		} 
	}
	
	public void denyInvite(SCOPlayer invitedPlayer) {
		SCOPlayer invitedBy = inviteMap.get(invitedPlayer);
		invitedBy.getPlayer().sendMessage(ChatColor.BLUE + "" + invitedPlayer + ChatColor.WHITE + " has denied your party invite.");
		invitedPlayer.getPlayer().sendMessage(ChatColor.WHITE + "You have denied " + ChatColor.BLUE + "" + invitedBy + ChatColor.WHITE + "'s invite.");
		
		invitedPlayer.setLastInvite("");
		inviteMap.remove(invitedPlayer);
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
	
	public void setLeader(UUID leader) {
		this.leader = leader;
	}
	
	public UUID getLeader() {
		return this.leader;
	}
	
	private ArrayList<SCOPlayer> getMembers() {
		return this.members;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
