package schmoller.unifier.mods.railcraft;

import java.util.List;

import net.minecraft.item.ItemStack;

import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import schmoller.unifier.Utilities;

public class CokeOvenProcessor implements IProcessor
{
	@Override
	public String getName()
	{
		return "Coke Oven";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int remapCount = 0;
		
		List<ICokeOvenRecipe> recipeList = RailcraftCraftingManager.cokeOven.getRecipes();
		
		for(ICokeOvenRecipe recipe : recipeList)
		{
			ItemStack output = Utilities.getPrivateValue(recipe, "output");

			if(mappings.applyMapping(output))
				++remapCount;
		}

		return remapCount;
	}

}
