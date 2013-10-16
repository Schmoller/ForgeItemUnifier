package schmoller.unifier.mods.ic2;

import java.util.Map;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import schmoller.unifier.Mappings;

public class OreWashingPlantProcessor extends BasicIC2MachineProcessor
{
	@Override
	public String getName()
	{
		return "Ore Washing Plant";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		Map<IRecipeInput, RecipeOutput> recipes = Recipes.oreWashing.getRecipes();
		
		return apply(mappings, recipes);
	}

}
