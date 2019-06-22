package net.peacefulcraft.sco.gamehandle;

import java.util.HashMap;
import net.peacefulcraft.sco.SwordCraftOnline;

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
			Party party = (Party) SwordCraftOnline.getSCOConfig().getParty(name); //TODO: Get datamanger
			getListParties().put(name, party);
			
			return true;
		}
		return false;
	}
	
	public Party getParty(String name) {
		Party ret = null;
		if(name != null && !name.isEmpty()) {
			ret = getListParties().get(name);
			if(ret == null) {
				//ret = //TODO: Get party from database
			} 
		}
		return ret;
	}
	
}
