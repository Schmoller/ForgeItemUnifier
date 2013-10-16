package schmoller.unifier.mods.ic2;

import java.util.Map;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import schmoller.unifier.Mappings;

public class MetalFormerProcessor extends BasicIC2MachineProcessor
{
	@Override
	public String getName()
	{
		return "Meta Forming Machine";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int count = 0;
		Map<IRecipeInput, RecipeOutput> recipes = Recipes.metalformerCutting.getRecipes();
		count += apply(mappings, recipes);
		
		recipes = Recipes.metalformerExtruding.getRecipes();
		count += apply(mappings, recipes);
		
		recipes = Recipes.metalformerRolling.getRecipes();
		count += apply(mappings, recipes);
		
		return count;
	}

}
