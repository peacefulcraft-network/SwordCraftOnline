package net.peacefulcraft.sco.gamehandle.player;

import java.util.UUID;

import org.bukkit.entity.Player;

public class SCOPlayer
{
	private String partyName;
	private String lastInvite;
	
	private Player user;
		public Player getPlayer() {return this.user;}
		public UUID getUUID() {return this.user.getUniqueId();}
	
	public SCOPlayer (Player user) {
		this.user = user;
		
		partyName = "";
	}
	
	public boolean isInParty() {
		if(partyName.equalsIgnoreCase("")) {
			return false;
		} else {
			return true;
		}
	}
	
	public int getLevel() {
		return user.getLevel();
	}
	
	public String getPartyName() {
		return this.partyName;
	}
	
	public void setPartyName(String name) {
		this.partyName = name;
	}
	
	public void setLastInvite(String invite) {
		this.lastInvite = invite;
	}
	
	public String getLastInvite() {
		return this.lastInvite;
	}
}
