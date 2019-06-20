package net.peacefulcraft.sco.gamehandle.party;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class InviteTask implements Runnable
{
	private Party party;
	private SCOPlayer invitedPlayer;
	
	public InviteTask(Party party, SCOPlayer invitedPlayer) {
		this.party = party;
		this.invitedPlayer = invitedPlayer;
	}

	@Override
	public void run()
	{
				
	}
	
	
}
