package schmoller.unifier.mods.railcraft;

import java.util.List;

import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import schmoller.unifier.Utilities;

public class BlastFurnaceProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Blast Furnace";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int remapCount = 0;
		
		List<IBlastFurnaceRecipe> recipeList = RailcraftCraftingManager.blastFurnace.getRecipes();
		
		for(IBlastFurnaceRecipe recipe : recipeList)
		{
			ItemStack output = Utilities.getPrivateValue(recipe, "output");

			if(mappings.applyMapping(output))
				++remapCount;
		}

		return remapCount;
	}

}
