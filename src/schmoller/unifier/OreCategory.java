package schmoller.unifier;

public enum OreCategory
{
	Ingots,
	Dusts,
	Blocks,
	Nuggets,
	Dyes,
	Items,
	Ores,
	Misc;
	
	public static OreCategory getCategory(String oreName)
	{
		if(oreName.startsWith("ore"))
			return Ores; 
		else if(oreName.startsWith("dust"))
			return Dusts;
		else if(oreName.startsWith("ingot"))
			return Ingots;
		else if(oreName.startsWith("block"))
			return Blocks;
		else if(oreName.startsWith("item"))
			return Items;
		else if(oreName.startsWith("dye"))
			return Dyes;
		else if(oreName.startsWith("nugget"))
			return Nuggets;
		
		return Misc;
	}
}
