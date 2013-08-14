package schmoller.unifier.mods.mekanism;

import mekanism.common.RecipeHandler.Recipe;
import schmoller.unifier.Mappings;

public class CombinerProcessor extends MekanismBaseProcessor
{
	@Override
	public String getName()
	{
		return "Combiner";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		return apply(mappings, Recipe.COMBINER);
	}

}
