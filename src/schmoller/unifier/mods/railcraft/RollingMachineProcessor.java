package schmoller.unifier.mods.railcraft;

import java.util.List;

import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.crafting.IRecipe;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class RollingMachineProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Rolling Machine";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int remapCount = 0;
		
		List<IRecipe> recipeList = RailcraftCraftingManager.rollingMachine.getRecipeList();
		
		for(IRecipe recipe : recipeList)
		{
			if(mappings.applyMapping(recipe.getRecipeOutput()))
				++remapCount;
		}

		return remapCount;
	}

}
