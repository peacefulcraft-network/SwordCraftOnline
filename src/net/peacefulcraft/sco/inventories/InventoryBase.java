package net.peacefulcraft.sco.inventories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.SwordCraftOnline;

public class InventoryBase{

	private File invData;
	
	private Player observer;
		public Player getObserver() { return observer; }
	
	private Class inventoryType;
		public Class getInventoryType() { return inventoryType; }
		
	private Inventory inventory;
		public Inventory getInventory() { return inventory; }
		
		
	public InventoryBase(Player observer, Class type) throws FileNotFoundException {
		this.observer = observer;
	
		invData = new File(
			SwordCraftOnline.getPluginInstance().getDataFolder().getPath() 
			+ "/data/" + observer.getUniqueId() + "/" + inventoryType + ".inv" 
		); // plugins/SwordCraftOnline/data/[uuid]/[SwordSkillInventory].inv
		
		loadInventory();
	}		
	
		/**
		 * Load serialized inventory from disk
		 * Create actual MC inventory that we can .open
		 * @throws FileNotFoundException 
		 */
		private void loadInventory() throws FileNotFoundException {
			if(invData.exists()) {
				try {
					FileReader fr = new FileReader(invData);
					
					long size = invData.length();
					if(size > Integer.MAX_VALUE) {
						// char array can only be of length int so if the file is too big it will not load properly
						fr.close();
						throw new RuntimeException("Datafile " + invData + " exceeds maximium readin size. Aborint file load.");
					}
					
					int length = (int) invData.length();
					char[] data = new char[length];
					
					fr.read(data);
					fr.close();
					inventory = InventorySerializer.StringToInventory( String.valueOf(data) );
					
				} catch (RuntimeException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				// Use InventoryBase.createNewInventory( size ) to create a new inventory
				throw new FileNotFoundException("Inventory " + invData + " does not exist.");
			}
		}
	
		/**
		 * Create a new inventory
		 * @param type: <T extends InventoryBase>
		 * @param size: # of slots
		 * @param u: UUID of player
		 * @throws FileNotFoundException 
		 */
		public static void createNewInventory(Class type, int size, UUID u) throws FileNotFoundException {
			Inventory inventory = Bukkit.getServer().createInventory(null, size);
			saveInventory(inventory, type, u);
		}
		
		
		/**
		 * proxy the static function for internal use because it's less typing
		 * @throws FileNotFoundException 
		 * @throws FileAlreadyExistsException
		 */
		private void saveInventory() throws FileNotFoundException {
			InventoryBase.saveInventory(inventory, inventoryType, observer.getUniqueId());
		}
		
			/**
			 * Save serialized inventory to disk
			 * @throws FileNotFoundException 
			 */
			private static void saveInventory(Inventory inventory, Class type, UUID u) throws FileNotFoundException {
				String serializedInventory = InventorySerializer.InventoryToString(inventory);
				File invData = new File(
						SwordCraftOnline.getPluginInstance().getDataFolder().getPath() 
						+ "/data/" + u + "/" + type + ".inv" 
					); // plugins/SwordCraftOnline/data/[uuid]/[SwordSkillInventory].inv
				
				PrintWriter pw = new PrintWriter(invData);
				pw.write(serializedInventory);
				pw.close();
			}
		
	/**
	 * Open inventory for player
	 */
	public void openInventory() {
		observer.openInventory(inventory);
	}
	
	/**
	 * close inventory for player
	 */
	public void closeInventory() {
		observer.closeInventory();
		try {
			this.saveInventory();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	};
	
}
