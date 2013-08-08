package schmoller.unifier.mods.gregtech;

import gregtechmod.api.util.GT_Recipe;

import java.util.List;

import schmoller.unifier.Mappings;

public class IndustrialSawmillProcessor extends BasicGTProcessor
{
	@Override
	public String getName()
	{
		return "Industrial Sawmill";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		return apply(mappings, (List<GT_Recipe>)GT_Recipe.sSawmillRecipes);
	}

}
