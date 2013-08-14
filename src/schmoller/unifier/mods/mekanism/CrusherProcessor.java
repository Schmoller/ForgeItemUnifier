package schmoller.unifier.mods.mekanism;

import mekanism.common.RecipeHandler.Recipe;
import schmoller.unifier.Mappings;

public class CrusherProcessor extends MekanismBaseProcessor
{
	@Override
	public String getName()
	{
		return "Crusher";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		return apply(mappings, Recipe.CRUSHER);
	}

}
