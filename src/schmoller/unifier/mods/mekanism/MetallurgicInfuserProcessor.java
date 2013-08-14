package schmoller.unifier.mods.mekanism;

import mekanism.common.RecipeHandler.Recipe;
import schmoller.unifier.Mappings;

public class MetallurgicInfuserProcessor extends MekanismBaseProcessor
{
	@Override
	public String getName()
	{
		return "Metallurgic Infuser";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		return apply(mappings, Recipe.METALLURGIC_INFUSER);
	}

}
