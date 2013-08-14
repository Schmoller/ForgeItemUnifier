package schmoller.unifier.mods.mekanism;

import mekanism.common.RecipeHandler.Recipe;
import schmoller.unifier.Mappings;

public class EnrichmentChamberProcessor extends MekanismBaseProcessor
{
	@Override
	public String getName()
	{
		return "Enrichment Chamber";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		return apply(mappings, Recipe.ENRICHMENT_CHAMBER);
	}

}
