package net.peacefulcraft.sco.items;

public enum ItemTier
{
	COMMON, UNCOMMON, RARE, LEGENDARY,
	ETHEREAL, GODLIKE;

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
			case GODLIKE:
			return "mastery";
			case ETHEREAL:
			return "ethereal";
			default:
			return "common";
		}
	}
}
