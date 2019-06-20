package net.peacefulcraft.sco.gamehandle;

import java.util.HashMap;

import net.peacefulcraft.sco.gamehandle.party.Party;

public class PartyManager
{
	private HashMap<String, Party> parties = new HashMap<String, Party>();
		public HashMap<String, Party> getListParties() {return parties; }
		
	private HashMap<String, Integer> listPartiesToDelete;
		public HashMap<String, Integer> getPartiesToDelete() {return listPartiesToDelete; }
		
	public PartyManager() {
		parties = new HashMap<String, Party>();
	}
	
	public Party loadParty(String name) {
		Party ret = getParty(name);
		if(ret != null) {
			getListParties().put(name.toLowerCase(), ret);
		}
		return ret;
	}
	
	public void unloadParty(String name) {
		getListParties().remove(name.toLowerCase());
	}
	
	public boolean reloadParty(String name) {
		if(getListParties().containsKey(name)) {
			//TODO: database get party 
			//TODO: put name party 
			
			return true;
		}
		return false;
	}
	
	public Party getParty(String name) {
		Party ret = null;
		if(name != null && !name.isEmpty()) {
			ret = getListParties().get(name.toLowerCase());
			if(ret == null) {
				//TODO: Databse join party name
			} 
		}
		return ret;
	}
	
}
