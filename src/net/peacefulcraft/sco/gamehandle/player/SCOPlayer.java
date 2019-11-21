package net.peacefulcraft.sco.gamehandle.player;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.peacefulcraft.sco.gamehandle.storage.SCOPlayerDataManager;

public class SCOPlayer 
{
	private String partyName;
	private String lastInvite;
	private int playerKills;
	
	private Player user;
		public Player getPlayer() {return this.user;}
		public UUID getUUID() {return this.user.getUniqueId();}
	
	private SCOPlayerDataManager scopData;
		public SCOPlayerDataManager getData() { return scopData; }
		
	public SCOPlayer (Player user) {
		this.user = user;
		
		partyName = "";
		lastInvite = "";
		playerKills = 0;
		
		scopData = new SCOPlayerDataManager(this);
	}
	
	public void playerDisconnect() {
		scopData.saveConfig();
		scopData.getInventories().unregisterInventories();
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
	
	public String getName() {
		return user.getName();
	}

	public String getPartyName() {
		return this.partyName;
	}

	public void setPartyName(String name) {
		this.partyName = name;
	}

	public String getLastInvite() {
		return this.lastInvite;
	}

	public void setLastInvite(String invite) {
		this.lastInvite = invite;
	}

	public int getPlayerKills() {
		return this.playerKills;
	}
	
	public void setPlayerKills(int red) {
		this.playerKills = red;
	}
	
}
