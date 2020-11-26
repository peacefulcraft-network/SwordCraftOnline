package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.storage.tasks.SavePlayerInventory;

public class QuestBookInventory extends InventoryBase implements Listener {

    //Private inventory storing quest items
    public QuestBookInventory(SCOPlayer s, HashMap<Integer, ItemStack> items, int size) {
        super(s.getPlayer(), InventoryType.QUEST_BOOK);
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, size);
        for(Integer loc : items.keySet()) {
            inventory.setItem(loc, items.get(loc));
        }
    }

    public void destroy() {

    }
    
    @Override
    public void openInventory() {
        SwordCraftOnline.getPluginInstance().getServer().getPluginManager().registerEvents(this, SwordCraftOnline.getPluginInstance());
        super.openInventory();
    }
    
    @Override
    public void closeInventory() {
        super.closeInventory();
        InventoryClickEvent.getHandlerList().unregister(this);
    }

    @Override
    public void initializeDefaultLoadout() {
        
    }

    @Override
    public void resizeInventory(int size) {
        
    }

    @Override
    public void saveInventory() {
        SCOPlayer s = GameManager.findSCOPlayer(this.getObserver());
        //(new SavePlayerInventory(s, this.getType(), ))
        //TODO: Method to save the items
    }
    
}