package schmoller.unifier.mods.mekanism;

import mekanism.common.RecipeHandler.Recipe;
import schmoller.unifier.Mappings;

public class OsmiumCompressorProcessor extends MekanismBaseProcessor
{
	@Override
	public String getName()
	{
		return "Osmium Compressor";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		return apply(mappings, Recipe.OSMIUM_COMPRESSOR);
	}

}
