package schmoller.unifier.mods.thaumcraft;

import java.util.List;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.RecipeCrucible;

public class CrucibleProcessor implements IProcessor
{
	@Override
	public String getName()
	{
		return "Crucible";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int remapCount = 0;
		
		List<RecipeCrucible> recipes = ThaumcraftApi.getCrucibleRecipes();
		
		for(RecipeCrucible recipe : recipes)
		{
			if(mappings.applyMapping(recipe.recipeOutput))
				++remapCount;
		}
		
		return remapCount;
	}

}
