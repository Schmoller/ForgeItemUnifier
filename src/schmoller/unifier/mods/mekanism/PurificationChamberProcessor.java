package schmoller.unifier.mods.mekanism;

import mekanism.common.RecipeHandler.Recipe;
import schmoller.unifier.Mappings;

public class PurificationChamberProcessor extends MekanismBaseProcessor
{
	@Override
	public String getName()
	{
		return "Purification Chamber";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		return apply(mappings, Recipe.PURIFICATION_CHAMBER);
	}

}
