package schmoller.unifier;

public enum OreCategory
{
	Ingots,
	Dusts,
	Blocks,
	Nuggets,
	Dyes,
	Ores,
	Gems,
	Misc;
	
	public static final OreCategory[] VALID_CATEGORIES = {Ingots, Dusts, Blocks, Nuggets, Gems, Ores};
	
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
		else if(oreName.startsWith("dye"))
			return Dyes;
		else if(oreName.startsWith("gem"))
			return Gems;
		else if(oreName.startsWith("nugget"))
			return Nuggets;
		
		return Misc;
	}
}
