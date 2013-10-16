package schmoller.unifier.mods.ic2;

import java.util.Map;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import schmoller.unifier.Mappings;

public class ExtractorProcessor extends BasicIC2MachineProcessor 
{
	@Override
	public String getName()
	{
		return "Extractor";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		Map<IRecipeInput, RecipeOutput> recipes = Recipes.extractor.getRecipes();
		
		return apply(mappings, recipes);
	}

}
