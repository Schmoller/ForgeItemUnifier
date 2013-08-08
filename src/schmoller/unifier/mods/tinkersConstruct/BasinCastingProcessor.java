package schmoller.unifier.mods.tinkersConstruct;

import java.util.List;

import mods.tinker.tconstruct.TConstruct;
import mods.tinker.tconstruct.library.crafting.CastingRecipe;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class BasinCastingProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Basin Casting";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		List<CastingRecipe> recipes = TConstruct.basinCasting.getCastingRecipes();
		
		int count = 0;
		for(CastingRecipe recipe : recipes)
		{
			if(mappings.applyMapping(recipe.output))
				++count;
		}

		return count;
	}

}
