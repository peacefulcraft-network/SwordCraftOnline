package net.peacefulcraft.sco.gamehandle.player;

import org.bukkit.entity.Player;

public class SCOPlayer
{
	private Player user;
		public Player getPlayer() {return this.user;}
	
	public SCOPlayer (Player user) {
		this.user = user;
	}
	
}
