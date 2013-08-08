package schmoller.unifier.mods.factorization;

import java.util.List;

import factorization.common.TileEntityCrystallizer;
import factorization.common.TileEntityCrystallizer.CrystalRecipe;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class CrystallizerProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Crystallizer";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int count = 0;
		
		List<CrystalRecipe> recipeList = TileEntityCrystallizer.recipes;
		
		for(CrystalRecipe recipe : recipeList)
		{
			if(mappings.applyMapping(recipe.output))
				++count;
		}
		
		return count;
	}

}
