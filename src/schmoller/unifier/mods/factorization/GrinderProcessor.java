package schmoller.unifier.mods.factorization;

import java.util.List;

import factorization.common.TileEntityGrinder;
import factorization.common.TileEntityGrinder.GrinderRecipe;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class GrinderProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Grinder";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int count = 0;
		
		List<GrinderRecipe> recipeList = TileEntityGrinder.recipes;
		
		for(GrinderRecipe recipe : recipeList)
		{
			if(mappings.applyMapping(recipe.output))
				++count;
		}
		
		return count;
	}

}
