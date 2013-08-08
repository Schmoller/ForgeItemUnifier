package schmoller.unifier.mods.thaumcraft;

import java.util.List;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.IInfusionRecipe;

public class ArcaneCraftingProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Arcane Crafting";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		List<Object> recipes = ThaumcraftApi.getCraftingRecipes();
		
		int count = 0;
		for(Object recipe : recipes)
		{
			if(recipe instanceof IArcaneRecipe)
			{
				if(mappings.applyMapping(((IArcaneRecipe)recipe).getRecipeOutput()))
					++count;
			}
			else if(recipe instanceof IInfusionRecipe)
			{
				if(mappings.applyMapping(((IInfusionRecipe)recipe).getRecipeOutput()))
					++count;
			}
		}

		return count;
	}

}
