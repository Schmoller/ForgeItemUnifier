package schmoller.unifier.mods.thaumcraft;

import java.util.List;

import net.minecraft.item.ItemStack;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

public class ThaumcraftCraftingProcessor implements IProcessor
{
	@Override
	public String getName()
	{
		return "Thaumcraft Crafting";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int remapCount = 0;
		
		List allRecipes = ThaumcraftApi.getCraftingRecipes();
		
		for(Object obj : allRecipes)
		{
			if(obj instanceof CrucibleRecipe)
			{
				CrucibleRecipe recipe = (CrucibleRecipe)obj;
				
				if(mappings.applyMapping(recipe.recipeOutput))
					++remapCount;
			}
			else if(obj instanceof IArcaneRecipe)
			{
				if(mappings.applyMapping(((IArcaneRecipe)obj).getRecipeOutput()))
					++remapCount;
			}
			else if(obj instanceof InfusionRecipe)
			{
				InfusionRecipe recipe = (InfusionRecipe)obj;
				if(recipe.recipeOutput instanceof ItemStack)
				{
					if(mappings.applyMapping((ItemStack)recipe.recipeOutput))
						++remapCount;
				}
				
			}
		}

		return remapCount;
	}

}
