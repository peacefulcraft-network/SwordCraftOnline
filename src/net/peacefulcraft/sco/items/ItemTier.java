package net.peacefulcraft.sco.items;

public enum ItemTier
{
	COMMON, UNCOMMON, RARE, LEGENDARY,
	MASTERY, ETHEREAL;

	public String toString() {
		switch(this) {
			case COMMON:
			return "common";
			case UNCOMMON:
			return "uncommon";
			case RARE:
			return "rare";
			case LEGENDARY:
			return "legendary";
			case MASTERY:
			return "mastery";
			case ETHEREAL:
			return "etheral";
			default:
			return "common";
		}
	}
}
