package schmoller.unifier.mods.tinkersConstruct;

import java.util.List;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import tconstruct.TConstruct;
import tconstruct.library.crafting.CastingRecipe;

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
