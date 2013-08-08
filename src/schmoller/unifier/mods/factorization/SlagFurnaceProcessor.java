package schmoller.unifier.mods.factorization;

import java.util.List;

import factorization.common.TileEntitySlagFurnace.SlagRecipes;
import factorization.common.TileEntitySlagFurnace.SmeltingResult;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class SlagFurnaceProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Slag Furnace";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int count = 0;
		
		List<SmeltingResult> recipeList = SlagRecipes.smeltingResults;
		
		for(SmeltingResult recipe : recipeList)
		{
			if(mappings.applyMapping(recipe.output1))
				++count;
			
			if(mappings.applyMapping(recipe.output2))
				++count;
		}
		
		return count;
	}

}
