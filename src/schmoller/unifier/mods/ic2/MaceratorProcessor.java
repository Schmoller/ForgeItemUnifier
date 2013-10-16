package schmoller.unifier.mods.ic2;

import java.util.Map;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import schmoller.unifier.Mappings;

public class MaceratorProcessor extends BasicIC2MachineProcessor
{
	@Override
	public String getName()
	{
		return "Macerator";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		Map<IRecipeInput, RecipeOutput> recipes = Recipes.macerator.getRecipes();
		
		return apply(mappings, recipes);
	}

}
