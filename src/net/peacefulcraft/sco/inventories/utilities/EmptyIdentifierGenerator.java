package net.peacefulcraft.sco.inventories.utilities;

import java.util.ArrayList;
import java.util.List;

import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public abstract class EmptyIdentifierGenerator {
	/**
   * Generates a List of Air items
   * @param size Number of items to generate (%9=0)
   * @return The list of air items
   */
  public static List<ItemIdentifier> generateEmptyIdentifierList(int size) {
    size = (size % 9) * 9;
    List<ItemIdentifier> items = new ArrayList<ItemIdentifier>(size);
    ItemIdentifier air = ItemIdentifier.generateIdentifier("Air", ItemTier.COMMON, 1);
    for(int i=0; i<size; i++) {
      items.add(air);
    }
    return items;
  }

}
