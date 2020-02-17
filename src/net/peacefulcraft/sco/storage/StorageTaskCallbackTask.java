package net.peacefulcraft.sco.storage;

import java.util.ArrayList;
import java.util.HashMap;

public interface StorageTaskCallbackTask {
	
	public void run(StorageTaskOutcome outcome, ArrayList<HashMap<String, Object>> result);
}
